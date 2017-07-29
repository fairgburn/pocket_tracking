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
import driver.Driver;
import driver.TouchScreenDriver;
import info.FailureDlg;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Surface extends JPanel
{

/** members and constructors **/

    private View view;
    private PostgresDB pdb;
    private int x_max, y_max;

    // used for copying
    private Zone selectedZone = null;
    private UnitInfo clipboard = null;

    // regions of the screen
    private LinesRegion linesRegion;
    private InfoRegion infoRegion;
    private CmdRegion cmdRegion;

    public Surface(View f) { init(f); }



/** methods **/


    public void init(View f) {
        view = f;
        pdb = view.getDatabase();

        // initialize regions
        linesRegion = new LinesRegion();
        infoRegion = new InfoRegion();
        cmdRegion = new CmdRegion();

        // read lines from database
        // get reference to all the lines
        try {
            linesRegion.init(pdb);
        } catch (SQLException e) {
            e.printStackTrace();
            FailureDlg.showError("error reading lines from database");
        }

        // attach regions to the Surface
        linesRegion.attach(this);
        infoRegion.attach(this);
        cmdRegion.attach(this);

        // connect a touch screen driver for input
        Driver ts = new TouchScreenDriver();
        Region[] ra = {
                linesRegion,
                infoRegion,
                cmdRegion
        };
        ts.connect(ra);
        ts.start(this);

    }

    // update the statusbar message
    public void setStatusText(String s) {
        view.setStatusText(s);
    }

    /** copying/pasting zones **/

    // change the selected zone
    public void setSelectedZone(Zone z) {
        selectedZone = z;
    }

    // get the selected zone
    public Zone getSelectedZone() {
        return selectedZone;
    }

    public void setClipboard(UnitInfo ui) {
        this.clipboard = ui;
    }

    public UnitInfo getClipboard() {
        return clipboard;
    }

    public PostgresDB getDB() {
        return pdb;
    }

    /** inventory tracking functions **/

    // check for inventory updates
    // this is executed on a timer by the View class
    public void updateInventory() {
        linesRegion.updateInventory(pdb);
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
        infoRegion.update( linesRegion.x_max, 0, (x_max - linesRegion.x_max), (y_max >> 1) );
        cmdRegion.update( linesRegion.x_max, infoRegion.y_max, infoRegion.width, (y_max - infoRegion.height) );

        // draw the regions individually
        linesRegion.draw(g);
        infoRegion.draw(g);
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