package main;

import database.PostgresDB;
import info.FailureDlg;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

// TODO clean up and extend Region
public class Line
{
    int id;
    int length;
    int width;
    int num_zones;
    private Unit inventory;

    private Region region = null;
    private Surface surface;
    private LinkedList<Zone> zoneList = new LinkedList<>();
    private boolean zones_added = false;

    public Line(int id, int length, int width, int num_zones) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.num_zones = num_zones;
    }

    public Region getRegion() { return this.region; }

    public void setRegion(Region r) {
        boolean create_new = false;

        if (this.region == null) {
            this.region = r;

            // create a new zone list if this is the first time we draw this line
            create_new = true;
        } else {
            this.region.update(r);
        }

        // create zones
        for (int i = 0 ; i < num_zones ; i++) {
            int zone_height = region.height / num_zones;
            Zone z = new Zone(region.x, ( region.y + (i * zone_height) ), region.width, zone_height, i + 1);
            z.setLineNum(this.id);
            //z.setZoneNum(i+1);

            // zone inventory
            //inventory.get

            if (create_new) {
                zoneList.addLast(z);
                z.attach(surface);
            } else {
                zoneList.get(i).update(z);
                zoneList.get(i).setLineNum(this.id);
                zoneList.get(i).setZoneNum(i+1);
            }
        }
    }

    // check the database for inventory
    // runs on a timer in the View class
    public void updateInventory(PostgresDB pdb) {
        ResultSet rs = pdb.executeQuery("SELECT * FROM inventory WHERE id=" + this.id);

        // check the results
        try {
            while (rs.next()) {
                for (Zone z : zoneList) {

                    // only interested in current zone
                    if (rs.getInt("zone") != z.getZoneNum()) continue;

                    // order_num > 0 indicates there is a unit in this zone
                    if (rs.getInt("order_num") > 0) {
                        // zone has unit
                        UnitInfo ui = new UnitInfo(
                                rs.getInt("order_num"),
                                rs.getString("customer"),
                                rs.getInt("width"),
                                rs.getInt("length"));
                        z.setUnit(new Unit(ui));
                    }

                    else {
                        // nothing in zone
                        z.setUnit(null);
                    }

                } // for
            } // while
        } catch (SQLException e) {
            FailureDlg.showError("program failed while updating line inventory");
        }
    }

    public void draw(Graphics2D g) {
        if (region != null) {
            Color oldColor = g.getColor();

            for (Zone z : zoneList) {
                z.draw(g);
            }

            // restore color
            g.setColor(oldColor);
        }
    }

    public void touch(Point p) {
        for (Zone z : zoneList) {
            z.touch(p);
        }
        surface.repaint();
    }

    public void attach(Surface s) {
        this.surface = s;
    }
























}
