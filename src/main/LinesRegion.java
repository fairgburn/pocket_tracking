package main;

import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;

public class LinesRegion
    extends Region
{

/** members and constructors **/

    Surface surface = null;
    Color color = new Color(169,54,61);
    boolean attached = false;
    LinkedList<Line> linesList = new LinkedList<>();

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
    public void setLines(PostgresDB pdb) throws java.sql.SQLException {
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



    }

}
