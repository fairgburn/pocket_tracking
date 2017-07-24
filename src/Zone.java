import java.awt.*;
import java.awt.event.MouseAdapter;

public class Zone extends Region
{

    Color backgroundColor = new Color(200, 200, 200);
    Color borderColor = Color.black;
    int zone_num = 0;

    boolean selected = false;

    public Zone(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setSelected(boolean b) { this.selected = b; }

    public void setZoneNum(int num) { this.zone_num = num; }

    public boolean isSelected() { return selected; }

    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        g.setColor( (selected) ? Color.yellow : backgroundColor);
        super.fillRect(g);

        g.setColor(borderColor);
        super.drawRect(g);

        g.setColor(oldColor);
    }

    @Override
    public String toString() {
        return "((ZONE " + zone_num + "))\n" +
                "x: " + this.xmin + "\n" +
                "y: " + this.ymin + "\n" +
                "width: " + this.width + "\n" +
                "height: " + this.height + "\n";
    }



}