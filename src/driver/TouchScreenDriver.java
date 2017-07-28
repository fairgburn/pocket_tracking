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

public class TouchScreenDriver
        extends MouseAdapter
        implements driver.Driver
{

    private LinkedList<Region> regions;


    public TouchScreenDriver() {
        regions = new LinkedList<>();
    }



/** Driver **/

    @Override
    public void connect(Region[] regs) {
        for (Region r : regs) {
            regions.addLast(r);
        }
    }

    // make the Surface listen to the TouchScreen
    @Override
    public void start(Object o) {
        Surface s = (Surface)o;
        s.addMouseListener(this);
    }


/** MouseAdapter **/

    // triggered when screen is touched
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

        // looking for left click signal
        if (mouseEvent.getButton() != 1) return;

        // figure out where screen was clicked
        Point p = mouseEvent.getPoint();

        // send a touch signal to all connected regions
        for (Region r : regions) {
            r.touch(p);
        }
    }






}
