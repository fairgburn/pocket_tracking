package main;

/*****************************************************************
 * main.Surface class, GUI
 *  - extends JPanel
 *
 * Main drawing board for graphics
 * Uses its region to shift focus to subregions for painting
 *
 *
 * Brandon Fairburn 7/17
 *
 * ***************************************************************/

import driver.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private int padding;
    private Zone selected_zone = null;

    // used for copy
    private Zone clipboard = null;

    // regions of the screen
    private LinesRegion linesRegion;
    private CountRegion countRegion;
    private CmdRegion cmdRegion;


    public Surface(View f) { init(f); }
    public Surface() { this.view = null; }


    public void init(View f) {
        view = f;
        pdb = view.getDatabase();

        // initialize regions
        linesRegion = new LinesRegion();
        countRegion = new CountRegion();
        cmdRegion = new CmdRegion();

        // read lines from database
        try {
            linesRegion.setLines(pdb);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorDlg.showError("error reading lines from database");
        }

        // attach regions to the Surface
        linesRegion.attach(this);
        countRegion.attach(this);
        cmdRegion.attach(this);

        // todo next - remove
        try {
            this.rs = pdb.executeQuery("SELECT * FROM lines");
        } catch (Exception e) {
            ErrorDlg.showError("error reading from lines table");
        }



        // connect the touch screen TODO
        /*Driver ts = new TouchScreenDriver();
        ts.connect(null);
        ts.start(this);*/

/**********************************************************************************************************************/
// TODO replace with TouchScreenDriver
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
                                selected_zone = (z.isSelected()) ? z : selected_zone;
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

/**___________________________________________________________________________________________________________________*/

    }

    private void setRegion(Region r) {
        this.x_max = r.x_max;
        this.y_max = r.y_max;
        this.x_min = r.x;
        this.y_min = r.y;
    }

    private void doDrawing(Graphics gg) {
        if (view == null)
            return;

        Graphics2D g = (Graphics2D) gg;
        Rectangle r = view.getBounds();
        x_max = r.width;
        y_max = r.height;
        padding = y_max / 10;

        // divide drawing surface into regions
        linesRegion.update( 0, 0, (x_max * 2/3), y_max);
        countRegion.update( linesRegion.x_max, 0, (x_max - linesRegion.x_max), (y_max >> 1) );
        cmdRegion.update( linesRegion.x_max, countRegion.y_max, countRegion.width, (y_max - countRegion.height) );

/**********************************************************************************************************************/
// TODO replace from here to end of function with region subclasses

        // paint the regions
        Color oldColor = g.getColor();
        g.setColor(new Color(169,54,61));
        linesRegion.fill(g);

        //g.setColor(new Color(180, 124, 20));
        //countRegion.fill(g);
        countRegion.draw(g);

        g.setColor(new Color(54,119,169));
        cmdRegion.fill(g);
        g.setColor(oldColor);


        // lines region ////////////////////////////////////////////////////////////////////////////////////////////
        this.setRegion(linesRegion);

        // draw the lines
        try {
            // while loop only runs once (cursor is persistent; remains at end so rs.next() is false
            //  on subsequent passes
            while (this.rs.next()) { // TODO move this to init()
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

                // set line region and draw it
                int width = (x_max - x_min) / 6;
                int height = (y_max - padding) - (y_min + padding);
                int x = line_centers[counter] - (width >> 1);
                int y = y_min + padding;
                l.setRegion( new Region(x, y, width, height) );
                l.draw(g); // draw zones and units

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
                clickCopy();
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
                clickPaste(g);
            }
        };
        buttonPaste.draw(g);

        // add buttons to list
        buttonList.clear();
        buttonList.addLast(buttonCopy);
        buttonList.addLast(buttonPaste);
/**********************************************************************************************************************/
    }

    /////////////////////////////
    // button functions /////////
    /////////////////////////////

    // copy
    void clickCopy() {
        this.clipboard = this.selected_zone;
        this.view.setStatusText("copied " + this.clipboard);
    }

    // paste
    void clickPaste(Graphics2D g) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}