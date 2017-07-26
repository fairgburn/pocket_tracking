package driver;

import main.Surface;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public class TouchScreen
        extends MouseAdapter
        implements driver.Driver
{

    Surface surface;



    /** Driver **/
    @Override
    public void connect(Object[] objects) {

    }

    @Override
    public void start() {

    }


    /** MouseAdapter **/

    /*@Override
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
    }*/






}
