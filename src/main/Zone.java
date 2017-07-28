package main;

import java.awt.*;

public class Zone extends Region
{


/** members **/

    private  Surface surface = null;
    private Color backgroundColor = new Color(200, 200, 200);
    private Color borderColor = Color.black;
    private int zone_num = 0;
    private int line_num;

    // inventory held by this zone
    private Unit unit = null;

    boolean selected = false;

    public Zone(int x, int y, int width, int height, int zone_num) {
        super(x, y, width, height);
        this.zone_num = zone_num;
    }


/** methods **/

    public boolean isSelected() { return selected; }

    public void setSelected(boolean b) { this.selected = b; }

    public void setZoneNum(int num) { this.zone_num = num; }

    public void setLineNum(int num) { line_num = num; }

    public int getZoneNum() {
        return this.zone_num;
    }





    public void setUnit(Unit u) {
        this.unit = u;
    }


/** Superclass **/

    @Override
    public void touch(Point p) {
        // not me that was clicked, make sure I'm unselected
        if (!super.contains(p)) {
            if (surface.getSelectedZone() == this) {
                surface.setSelectedZone(null);
            }
            selected = false;
        }

        // I was clicked, handle that
        else {
            // flip the selection bit
            selected = !selected;

            // set the selected zone
            if (!selected) surface.setSelectedZone(null); // I was unselected
            else surface.setSelectedZone(this); // I was selected
        }
    }

    @Override
    public void attach(Surface s) {
        this.surface = s;
    }

    @Override
    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        g.setColor( (selected) ? Color.yellow : backgroundColor);
        super.fill(g);

        g.setColor(borderColor);
        super.drawBorder(g);

        g.setColor(oldColor);
    }

    @Override
    public String toString() {
        return "((ZONE " + zone_num + "))\n" +
                "x: " + this.x + "\n" +
                "y: " + this.y + "\n" +
                "width: " + this.width + "\n" +
                "height: " + this.height + "\n";
    }

}