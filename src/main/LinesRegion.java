package main;

import database.PostgresDB;
import info.Debug;
import info.Globals;

import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;

// TODO Decorator class for labelling lines/zones and drawing misc. objects
public class LinesRegion
    extends Region
{

/** members and constructors **/

    Globals glob = Globals.getInstance();

    private Surface surface = null;
    private Color backgroundColor = glob.linesColor;
    private boolean attached = false;
    private LinkedList<Line> linesList = new LinkedList<>();

    /***/

    public LinesRegion(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public LinesRegion(Region r) {
        super(r);
    }

    public LinesRegion() {}


/** methods **/

    // pull the lines from the database
    public void init(PostgresDB pdb) throws java.sql.SQLException {
        linesList.clear();
        ResultSet rs = pdb.executeQuery("SELECT * FROM lines");

        // iterate through the results and store them
        while (rs.next()) {
            Line l = new Line(rs.getInt("id"), rs.getInt("length"), rs.getInt("width"), rs.getInt("num_zones"));
            linesList.addLast(l);
        }
    }

    public void updateInventory(PostgresDB pdb) {
        for (Line l : linesList) {
            l.updateInventory(pdb);
        }
    }

    // hand out references to the lines
    public LinkedList<Line> getLinesList() {
        return linesList;
    }


/** superclass **/

    @Override
    public void touch(Point p) {
        // ignore touch if it was in another region
        if (!super.contains(p)) return;

        // extend touch to the lines
        for (Line l : linesList) {
            l.touch(p);
        }

    }

    @Override
    public void attach(Surface s) {
        surface = s;
        attached = true;

        // also attach all the lines
        for (Line l : linesList) l.attach(surface);
    }

    @Override
    public void draw(Graphics2D g) {
        // fill in the background
        Color oldColor = g.getColor();
        g.setColor(backgroundColor);
        super.fill(g);

        // calculate where to draw the lines
        int num_lines = linesList.size(); // number of lines
        int line_spacing = (x_max - x) / (num_lines + 1); // draw lines evenly spaced out
        int[] line_centers = new int[num_lines]; // array of those x coordinates

        // build x coordinate array
        for (int i = 1; i <= num_lines; i++) {
            int x = i * line_spacing;
            line_centers[i - 1] = x;
        }

        // draw the lines
        // TODO: use line width/height from database
        int counter = 0;
        for (Line l : linesList) {

            // calculate the line's region and draw it
            int lw = (this.width) / 6;
            int lh = this.height - glob.padding;
            int lx = line_centers[counter] - (lw >> 1);
            int ly = this.y + 25;
            l.setRegion( new Region(lx, ly, lw, lh) );
            l.draw(g); // draw zones and units

            counter++;
        }

        // restore color
        g.setColor(oldColor);
    }

}
