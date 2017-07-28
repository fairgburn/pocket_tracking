package main;

import info.Globals;

import java.awt.*;

public class CountRegion
    extends Region
{

/** members and constructors **/

    Globals glob = Globals.getInstance();

    Surface surface = null;
    Color color = glob.countColor;
    boolean attached = false;

    /***/

    public CountRegion(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public CountRegion(Region r) {
        super(r);
    }

    public CountRegion() {}



/** methods **/







/** Superclass **/
    @Override
    public void touch(Point p) {

    }

    @Override
    public void attach(Surface s) {
        surface = s;
        attached = true;
    }

    // todo
    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();
        g.setColor(color);
        super.fill(g);
        g.setColor(oldColor);
    }


}
