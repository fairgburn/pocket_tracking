/**
 * TouchScreenDriver class
 *
 * Handles touch screen interactions and signals the regions which are touched
 *
 * Brandon Fairburn 7/2017
 */

package driver;

import main.Region;
import main.Surface;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class TouchScreenDriver
        extends MouseAdapter
        implements driver.Driver
{

    private Surface surface;
    private LinkedList<Region> regions;


    public TouchScreenDriver() {
        surface = null;
        regions = new LinkedList<>();
    }



/** Driver **/

    // connect to the regions of the screen so we can signal them when they are touched
    @Override
    public void connect(List<Object> objects) {
        // need a list of regions to signal
        for (Object o : objects) {
            regions.addLast( (Region)o );
        }
    }

    // make the Surface listen to the TouchScreen
    @Override
    public void start(Object o) {
        Surface s = (Surface)o;
        s.addMouseListener(this);
    }


/** MouseAdapter **/

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

        // looking for left click
        if (mouseEvent.getButton() != 1) return;

        // figure out where screen was clicked
        Point p = mouseEvent.getPoint();

        // signal the clicked region
        for (Region r : regions) {
            if (r.contains(p)) {
                r.touch(p);
                break;
            }
        }

        /*// only care about left click
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
        else if (cmdRegion.contains(p)) {

            for (Button b : buttonList) {
                if (b.contains(p))
                    b.click();
            }

        }

        *///------------------------------------------------------------------------------------------------------
    }






}
