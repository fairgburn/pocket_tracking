package main;

import java.awt.*;

// container class - used to split surface into regions
public class Region
{
    public int x;
    public int x_max;
    public int y;
    public int y_max;
    public int width;
    public int height;

    public Region(int x, int y, int width, int height) { update(x, y, width, height); }

    public Region(Region r) { this(r.x, r.y, r.width, r.height); }

    public Region() {}

    public void update(int x, int y, int width, int height) {
        this.x = x;
        this.x_max = x + width;
        this.y = y;
        this.y_max = y + height;
        this.width = width;
        this.height = height;
    }

    public void update(Region r) {
        update(r.x, r.y, r.width, r.height);
    }

    // check if point is in region
    public boolean inRegion(Point p) {
        int x = p.x;
        int y = p.y;

        return ( x >= this.x && x <= this.x_max &&
                 y >= this.y && y <= this.y_max);
    }

    public void drawBorder(Graphics2D g) { drawBorder(g, false); }

    private void drawBorder(Graphics2D g, boolean fill) {
        if (fill) {
            g.fillRect(x, y, width, height);
        } else {
            g.drawRect(x, y, width, height);

        }
    }

    public void fill(Graphics2D g) { drawBorder(g, true); }

}