package main;

import info.Globals;

import java.awt.*;

public class InfoRegion
    extends Region
{

/** members and constructors **/

    Globals glob = Globals.getInstance();

    private Surface surface = null;
    private Color color = glob.countColor;
    private boolean attached = false;
    private Point cursor = new Point();


    /***/

    public InfoRegion(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public InfoRegion(Region r) {
        super(r);
    }

    public InfoRegion() {}



/** methods **/

    private void print(Graphics2D g, String s, int face, int size) {
        Font font = new Font(glob.font, face, size);
        g.setColor(Color.black);
        g.setFont(font);
        g.drawString(s, cursor.x, cursor.y);

        // advance the cursor
        cursor.y += 30;
    }

    private void cursorToTop() {
        cursor.x = this.x + glob.padding;
        cursor.y = this.y + glob.padding;
    }





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

        // show info about selected zone
        if (surface.getSelectedZone() == null) return;

        // get the unit info
        Zone z = surface.getSelectedZone();
        UnitInfo ui;
        try {
            ui = z.getUnit().getInfo();
        } catch (NullPointerException e) {
            ui = null;
        }
        if (ui == null) {
            cursorToTop();
            print(g, "empty zone", Font.BOLD, 50);
            return;
        }

        cursorToTop();
        int size = 24;
        print(g, "Order", Font.BOLD, size);
        print(g, Integer.toString(ui.order), Font.PLAIN, size);

        print(g, "Customer", Font.BOLD, size);
        print(g, ui.customer, Font.PLAIN, size);

        print(g, "Width", Font.BOLD, size);
        print(g, Integer.toString(ui.width), Font.PLAIN, size);

        print(g, "Length", Font.BOLD, size);
        print(g, Integer.toString(ui.length), Font.PLAIN, size);




        g.setColor(oldColor);
    }


}

















