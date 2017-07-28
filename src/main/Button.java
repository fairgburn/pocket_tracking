package main;

import info.Globals;
import info.SettingsReader;

import java.awt.*;

public abstract class Button extends Region
{

    Globals glob = Globals.getInstance();

    private Color backgroundColor = new Color(130, 185, 70);
    private Color borderColor = Color.black;
    private String text = "button";

    public Button(int x, int y, int width, int height, String t, Color bg) {
        super(x, y, width, height);
        this.backgroundColor = bg;
        this.text = t;
    }

    public Button(int x, int y, int width, int height, String t) {
        this(x, y, width, height, t,  new Color(130, 185, 70));
    }

    // handle a click on this button
    // to use:
    //  - add an input driver to the Surface and pass down screen touches to this button
    //  - override this function at button creation
    public abstract void click();


    public void draw(Graphics2D g) {
        Color oldColor = g.getColor();

        // draw button
        g.setColor(backgroundColor);
        super.fill(g);

        // draw border
        g.setColor(borderColor);
        super.drawBorder(g);

        //// write text ////
        int font_size = (int)(this.height * 0.75); // pixels x 0.75 = font size (pt)
        Font font = new Font(glob.font, Font.BOLD, 35);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        // measure text and determine where to draw it
        int txt_width = fm.stringWidth(this.text);
        Point txt_point = new Point();
        txt_point.x = this.x + ( (this.width - txt_width) >> 1 ); // center of button
        txt_point.y = this.y_max - (this.height >> 2);

        // set up text anti aliasing
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // actually write the text
        g.drawString(this.text, txt_point.x, txt_point.y);

        // restore color
        g.setColor(oldColor);
    }

}
