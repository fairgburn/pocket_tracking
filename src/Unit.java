import java.awt.*;

public class Unit extends Region
{
    private Color backgroundColor = new Color(123, 48, 0);
    private Color txtColor = new Color(194, 108, 52);
    private String text;
    private UnitInfo info;

    public Unit(/*int x, int y, int width, int height,*/ UnitInfo u) {
        //super(x, y, width, height);
        this.info = new UnitInfo(u);
    }

    // drawing info for GUI
    public void setRegion(Region r) {
        // cut down this region to make it smaller than the zone
        int padding_x = this.width / 10;
        int padding_y = this.height / 10;

        super.update(   r.xmin + padding_x,
                        r.ymin + padding_y,
                        r.width - (padding_x >> 1),
                        r.height - (padding_y >> 1));

    }

    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        g.setColor(backgroundColor);
        super.fillRect(g);

        g.setColor(oldColor);

    }

    public UnitInfo getInfo() {
        return info;
    }

}
