package main;

import java.awt.*;

/**
 * Created by acs on 7/26/17.
 */
public class CmdRegion
    extends Region
{


    Surface surface = null;
    Color color = new Color(54,119,169);
    boolean attached = false;






/** superclass **/

    @Override
    public void touch(Point p) {

    }

    @Override
    public void attach(Surface s) {
        surface = s;
        attached = true;
    }

    @Override
    public void draw(Graphics2D g) {

    }

}
