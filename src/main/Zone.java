package main;

import java.awt.*;

public class Zone extends Region
{

    Color backgroundColor = new Color(200, 200, 200);
    Color borderColor = Color.black;
    int zone_num = 0;

    private Unit unit = null;

    boolean selected = false;

    public Zone(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setSelected(boolean b) { this.selected = b; }

    public void setZoneNum(int num) { this.zone_num = num; }

    public int getZoneNum() {
        return this.zone_num;
    }

    public boolean isSelected() { return selected; }

    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        g.setColor( (selected) ? Color.yellow : backgroundColor);
        super.fill(g);

        g.setColor(borderColor);
        super.drawBorder(g);

        // draw unit if zone has one
        if (unit != null) {
            g.fillRect(this.x + 20, this.y + 20, 30, 30);
        }

        g.setColor(oldColor);
    }

    public void setUnit(Unit u) {
        this.unit = u;
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