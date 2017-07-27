package main;

import globals.Globals;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by acs on 7/26/17.
 */
public class CmdRegion
    extends Region
{

    // globals
    private Globals glob = Globals.getInstance();

/** members and constructors **/

    private Surface surface = null;
    private Color backgroundColor = new Color(54,119,169);
    private boolean attached = false;
    private LinkedList<Button> buttonsList = new LinkedList<>();
    private int padding = glob.padding;

    /***/

    public CmdRegion(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public CmdRegion(Region r) {
        super(r);
    }

    public CmdRegion() {}



/** methods **/


    private void copy() {
        // need attachment to surface to see selected zone
        if (!attached) {
            Debug.log("attempted to copy from unattached CmdRegion");
            return;
        }

        //Zone zone = surface.get
    }


/** superclass **/

    @Override
    public void touch(Point p) {
        if (!super.contains(p)) return;


    }

    @Override
    public void attach(Surface s) {
        surface = s;
        attached = true;
    }

    @Override
    public void draw(Graphics2D g) {

        Color oldColor = g.getColor();
        g.setColor(backgroundColor);
        super.fill(g);

        int num_buttons = 2; // so we can change this later
        int button_width = ((this.x_max - this.x) / (num_buttons + 1));
        int button_height = this.height / 10;

        // get button centers
        int button_spacing = (this.x_max - this.x) / (num_buttons + 1);
        int[] button_centers = new int[num_buttons];
        for (int i = 1; i <= num_buttons; i++) {
            int x = i * button_spacing + this.x;
            button_centers[i - 1] = x;
        }


        // draw copy button
        Button copyButton = new Button( button_centers[0] - (button_width >> 1),
                this.y_max - padding - button_height, // padding was for avoiding bottom of screen
                button_width,
                button_height,
                "COPY") {
            @Override
            public void click() {
                copy();
            }
        };
        copyButton.draw(g);
        buttonsList.addLast(copyButton);


    }

}
