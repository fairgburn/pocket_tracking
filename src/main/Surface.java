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
package main;


import database.PostgresDB;
import driver.*;
import info.FailureDlg;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.LinkedList;

public class Surface extends JPanel
{

/** members and constructors **/

    private View view;
    private PostgresDB pdb;
    private int x_max, y_max;
    private LinkedList<Line> linesList = new LinkedList<>();
    private LinkedList<Button> buttonList = new LinkedList<>();

    // used for copying
    private Zone selectedZone = null;
    private Zone clipboard = null;

    // regions of the screen
    private LinesRegion linesRegion;
    private CountRegion countRegion;
    private CmdRegion cmdRegion;

    public Surface(View f) { init(f); }



/** methods **/


    public void init(View f) {
        view = f;
        pdb = view.getDatabase();

        // initialize regions
        linesRegion = new LinesRegion();
        countRegion = new CountRegion();
        cmdRegion = new CmdRegion();

        // read lines from database
        // get reference to all the lines
        try {
            linesRegion.init(pdb);
            this.linesList = linesRegion.getLinesList();
        } catch (SQLException e) {
            e.printStackTrace();
            FailureDlg.showError("error reading lines from database");
        }

        // attach regions to the Surface
        linesRegion.attach(this);
        countRegion.attach(this);
        cmdRegion.attach(this);



        // connect the touch screen to all the regions
        Driver ts = new TouchScreenDriver();
        Region[] ra = {
                linesRegion,
                countRegion,
                cmdRegion
        };
        ts.connect(ra);
        ts.start(this);

/**********************************************************************************************************************/
// TODO replace with TouchScreenDriver
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // handle left mouse clicks
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /*addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // only care about left click
                if (mouseEvent.getButton() != 1) return;

                Point p = mouseEvent.getPoint();

                // LINES REGION ////////////////////////////////////////////////////////////////////////////////////////

                if (linesRegion.contains(p)) {
                    for (Line l : linesList) {

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
        });*/
    }

    // draw the interface
    private void compose(Graphics gg) {
        if (view == null)
            return;

        Graphics2D g = (Graphics2D) gg;
        Rectangle r = view.getBounds();
        this.x_max = r.width;
        this.y_max = r.height;

        // calculate bounds of regions based on window size
        linesRegion.update( 0, 0, (x_max * 2/3), y_max);
        countRegion.update( linesRegion.x_max, 0, (x_max - linesRegion.x_max), (y_max >> 1) );
        cmdRegion.update( linesRegion.x_max, countRegion.y_max, countRegion.width, (y_max - countRegion.height) );

        // draw the regions individually
        linesRegion.draw(g);
        countRegion.draw(g);
        cmdRegion.draw(g);

    }

/** Superclass **/

    // this function is triggered when the screen needs to be redrawn
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        compose(g);
    }
}