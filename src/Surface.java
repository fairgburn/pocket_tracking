import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.LinkedList;

public class Surface extends JPanel
{


    private View view;
    private PostgresDB pdb;
    private int x_max, y_max;
    private int x_min, y_min;
    private ResultSet rs;
    private LinkedList<Line> lineList = new LinkedList<>();
    private LinkedList<Button> buttonList = new LinkedList<>();
    private Region linesRegion, scaleRegion, cmdRegion;
    private int padding;
    private Zone selected_zone = null;

    private boolean first_draw = true;


    public Surface(View f) { setView(f); }
    public Surface() { this.view = null; }


    public void setView(View f) {
        this.view = f;
        this.pdb = this.view.getDatabase();
        try {
            this.rs = pdb.executeQuery("SELECT * FROM lines");
        } catch (Exception e) {
            ErrorDlg.showError("error reading from lines table");
        }

        this.linesRegion = new Region();
        this.scaleRegion = new Region();
        this.cmdRegion = new Region();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // handle left mouse clicks
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // only care about left click
                if (mouseEvent.getButton() != 1) return;

                Point p = mouseEvent.getPoint();

                // LINES REGION ////////////////////////////////////////////////////////////////////////////////////////

                if (linesRegion.inRegion(p)) {
                    for (Line l : lineList) {

                        for (Zone z : l.getZoneList()) {
                            if (z.inRegion(p)) { // this zone was clicked
                                // flip selected bit for zone that was clicked
                                z.setSelected(!z.isSelected());
                                Debug.log("user clicked LINE " + l.id);
                                Debug.log(z);

                                // store selected zone
                                selected_zone = (z.isSelected()) ? z : null;
                                repaint();

                            } else { // this zone was not clicked

                                // mark the zone unselected
                                z.setSelected(false);
                                repaint();
                            }
                        }
                    }
                }
                //------------------------------------------------------------------------------------------------------


                // COMMAND REGION //////////////////////////////////////////////////////////////////////////////////////
                else if (cmdRegion.inRegion(p)) {

                    for (Button b : buttonList) {
                        if (b.inRegion(p))
                            b.click();
                    }

                }

                //------------------------------------------------------------------------------------------------------
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    private void setRegion(Region r) {
        this.x_max = r.xmax;
        this.y_max = r.ymax;
        this.x_min = r.xmin;
        this.y_min = r.ymin;
    }

    private void doDrawing(Graphics gg) {
        if (view != null) {
            Graphics2D g = (Graphics2D) gg;
            Rectangle r = view.getBounds();
            x_max = r.width;
            y_max = r.height;
            padding = y_max / 10;

            // divide drawing surface into regions
            linesRegion.update( 0, 0, (x_max * 2/3), y_max);
            scaleRegion.update( linesRegion.xmax, 0, (x_max - linesRegion.xmax), (y_max >> 1) );
            cmdRegion.update( linesRegion.xmax, scaleRegion.ymax, scaleRegion.width, (y_max - scaleRegion.height) );

            // paint the regions
            Color oldColor = g.getColor();
            g.setColor(new Color(169,54,61));
            linesRegion.fillRect(g);

            g.setColor(new Color(180, 124, 20));
            scaleRegion.fillRect(g);

            g.setColor(new Color(54,119,169));
            cmdRegion.fillRect(g);
            g.setColor(oldColor);


            // lines region ////////////////////////////////////////////////////////////////////////////////////////////
            this.setRegion(linesRegion);

            // draw the lines
            try {
                // while loop only runs once (cursor is persistent; remains at end so rs.next() is false
                //  on subsequent passes
                while (this.rs.next()) {
                    Line l = new Line(rs.getInt("id"), rs.getInt("length"), rs.getInt("width"), rs.getInt("num_zones"));
                    //l.setRegion(linesRegion);
                    lineList.addLast(l);
                }

                // move to region for drawing lines
                this.setRegion(linesRegion);

                int num_lines = lineList.size(); // number of lines in DB
                int line_spacing = (x_max - x_min) / (num_lines + 1); // x coordinate to draw lines evenly spaced out
                int[] line_centers = new int[num_lines]; // array of those x coordinates

                // build x coordinate array
                for (int i = 1; i <= num_lines; i++) {
                    int x = i * line_spacing;
                    line_centers[i - 1] = x;
                }

                // here is where the lines actually get drawn // TODO: make use of line width/height from database
                int counter = 0;
                for (Line l : lineList) {
                    // get inventory for line
                    ResultSet inv = pdb.executeQuery("SELECT * FROM inventory WHERE id=" + l.id);

                    // set line region and draw it
                    int width = (x_max - x_min) / 6;
                    int height = (y_max - padding) - (y_min + padding);
                    int x = line_centers[counter] - (width >> 1);
                    int y = y_min + padding;

                    // give line inv info
                    l.setInventory(inv);
                    l.setRegion( new Region(x, y, width, height) );
                    l.draw(g);

                    counter++;
                }

            } catch (Exception e) { e.printStackTrace(); }
            //----------------------------------------------------------------------------------------------------------


            // command region //////////////////////////////////////////////////////////////////////////////////////////
            this.setRegion(cmdRegion);

            ////////////////////////
            // draw buttons ////////
            ////////////////////////
            int num_buttons = 2; // so we can change this later
            int button_width = ((this.x_max - this.x_min) / (num_buttons + 1)) - (padding >> 1);
            int button_height = (this.y_max - this.y_min) / 10;

            // get button centers
            int button_spacing = (this.x_max - this.x_min) / (num_buttons + 1);
            int[] button_centers = new int[num_buttons];
            for (int i = 1; i <= num_buttons; i++) {
                int x = i * button_spacing + this.x_min;
                button_centers[i - 1] = x;
            }

            // draw copy button
            Button buttonCopy = new Button( button_centers[0] - (button_width >> 1),
                                            this.y_max - padding - button_height,
                                            button_width,
                                            button_height,
                                            "COPY") {
                @Override
                public void click() {
                    copyClick(g);
                }
            };
            buttonCopy.draw(g);

            // draw paste button
            Button buttonPaste = new Button(button_centers[1] - (button_width >> 1),
                                            this.y_max - padding - button_height,
                                            button_width,
                                            button_height,
                                            "PASTE") {
                @Override
                public void click() {
                    Debug.log("paste button clicked");
                }
            };
            buttonPaste.draw(g);

            // add buttons to list
            buttonList.clear();
            buttonList.addLast(buttonCopy);
            buttonList.addLast(buttonPaste);





        }
    }

    /////////////////////////////
    // button functions /////////
    /////////////////////////////

    // copy
    void copyClick(Graphics2D g) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}