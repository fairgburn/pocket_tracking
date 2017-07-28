package main;

import java.awt.*;

public class Unit extends Region
{
    private Color backgroundColor = new Color(123, 48, 0);
    private Color txtColor = new Color(194, 108, 52);
    private String text;
    private UnitInfo info;

    // know the parent zone - for drawing size
    private Zone zone;

    public Unit(UnitInfo u) {
        //super(x, y, width, height);
        this.info = new UnitInfo(u);
    }

/** methods **/

    // drawing info for GUI
    public void setRegion(Region r) {
        if (r == null) return;

        // cut down this region to make it smaller than the zone
        int padding_x = this.width / 10;
        int padding_y = this.height / 10;

        super.update(   r.x + padding_x,
                        r.y + padding_y,
                        r.width - (padding_x >> 1),
                        r.height - (padding_y >> 1));

    }

    public void setZone(Zone z) {
        zone = z;
        int unit_padding = 25;

        this.x = z.x + unit_padding;
        this.y = z.y + unit_padding;
        this.width = z.width - (unit_padding << 1);
        this.height = z.height - (unit_padding << 1);
    }

    public UnitInfo getInfo() {
        return info;
    }

/** superclass **/

    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        g.setColor(backgroundColor);
        super.fill(g);

        // write the order number on the unit
        Font font = new Font(glob.font, Font.BOLD, 20);
        g.setFont(font);
        g.setColor(txtColor);
        g.drawString(Integer.toString(info.order), x + 20, y + 50);
        g.setColor(oldColor);

    }


}
