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

import database.PostgresDB;

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
    private Zone selectedZone = null;

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
            linesRegion.init(pdb);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorDlg.showError("error reading lines from database");
        }

        // attach regions to the Surface
        linesRegion.attach(this);
        countRegion.attach(this);
        cmdRegion.attach(this);



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

                if (linesRegion.contains(p)) {
                    for (Line l : lineList) {

                        for (Zone z : l.getZoneList()) {
                            if (z.contains(p)) { // this zone was clicked
                                // flip selected bit for zone that was clicked
                                z.setSelected(!z.isSelected());
                                Debug.log("user clicked LINE " + l.id);
                                Debug.log(z);

                                // store selected zone
                                selectedZone = (z.isSelected()) ? z : selectedZone;
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
                else if (cmdRegion.contains(p)) {

                    for (Button b : buttonList) {
                        if (b.contains(p))
                            b.click();
                    }

                }

                //------------------------------------------------------------------------------------------------------
            }
        });
    }

    private void compose(Graphics gg) {
        if (view == null)
            return;

        Graphics2D g = (Graphics2D) gg;
        Rectangle r = view.getBounds();
        this.x_max = r.width;
        this.y_max = r.height;
        this.padding = 100;

        // calculate bounds of regions based on window size
        linesRegion.update( 0, 0, (x_max * 2/3), y_max);
        countRegion.update( linesRegion.x_max, 0, (x_max - linesRegion.x_max), (y_max >> 1) );
        cmdRegion.update( linesRegion.x_max, countRegion.y_max, countRegion.width, (y_max - countRegion.height) );

        // draw the regions individually
        linesRegion.draw(g);
        countRegion.draw(g);
        cmdRegion.draw(g);

    }

    /////////////////////////////
    // button functions /////////
    /////////////////////////////

    // copy
    void clickCopy() {
        this.clipboard = this.selectedZone;
        this.view.setStatusText("copied " + this.clipboard);
    }

    // paste
    void clickPaste(Graphics2D g) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        compose(g);
    }
}