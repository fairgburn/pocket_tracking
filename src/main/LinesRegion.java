package main;

import database.PostgresDB;
import globals.Globals;

import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;

public class LinesRegion
    extends Region
{

/** members and constructors **/

    private Surface surface = null;
    private Color backgroundColor = new Color(169,54,61);
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
        ResultSet rs = pdb.executeQuery("SELECT * FROM lines");

        // iterate through the results
        while (rs.next()) {
            Line l = new Line(rs.getInt("id"), rs.getInt("length"), rs.getInt("width"), rs.getInt("num_zones"));
            linesList.addLast(l);
        }
    }




/** superclass **/

    @Override
    public void touch(Point p) {

    }

    @Override
    public void attach(Surface s) {
        surface = s;
        attached = true;
    }

    @Override
    public void draw(Graphics2D g) {
        // fill in the background
        Color oldColor = g.getColor();
        g.setColor(backgroundColor);
        super.fill(g);

        int num_lines = linesList.size(); // number of lines
        int line_spacing = (x_max - x) / (num_lines + 1); // draw lines evenly spaced out
        int[] line_centers = new int[num_lines]; // array of those x coordinates

        // build x coordinate array
        for (int i = 1; i <= num_lines; i++) {
            int x = i * line_spacing;
            line_centers[i - 1] = x;
        }

        // here is where the lines actually get drawn // TODO: make use of line width/height from database
        int counter = 0;
        for (Line l : linesList) {

            // set line region and draw it
            int w = (this.width) / 6;
            int h = (int) (this.height * 0.9); // lines are drawn to 90% of window height
            int xx = line_centers[counter] - (w >> 1);
            int yy = this.y + 25;
            l.setRegion( new Region(xx, yy, w, h) );
            l.draw(g); // draw zones and units

            counter++;
        }

        // restore color
        g.setColor(oldColor);
    }

}

































