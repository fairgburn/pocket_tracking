import java.awt.*;

// container class - used to split surface into regions
public class Region
{
    public int xmin;
    public int xmax;
    public int ymin;
    public int ymax;
    public int height;
    public int width;

    public Region(int x, int y, int width, int height) { update(x, y, width, height); }

    public Region(Region r) { this(r.xmin, r.ymin, r.width, r.height); }

    public Region() {}

    public void update(int x, int y, int width, int height) {
        this.xmin = x;
        this.xmax = x + width;
        this.ymin = y;
        this.ymax = y + height;
        this.width = width;
        this.height = height;
    }

    public void update(Region r) {
        update(r.xmin, r.ymin, r.width, r.height);
    }

    // check if point is in region
    public boolean inRegion(Point p) {
        int x = p.x;
        int y = p.y;

        return ( x >= this.xmin && x <= this.xmax &&
                 y >= this.ymin && y <= this.ymax );
    }

    public void drawRect(Graphics2D g) { drawRect(g, false); }

    public void fillRect(Graphics2D g) { drawRect(g, true); }

    private void drawRect(Graphics2D g, boolean fill) {
        if (fill) {
            g.fillRect(xmin, ymin, width, height);
        } else {
            g.drawRect(xmin, ymin, width, height);

        }
    }

}