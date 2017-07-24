import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;

public class Line
{
    int id;
    int length;
    int width;
    int num_zones;
    private ResultSet inventory;

    Region region = null;

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
            Zone z = new Zone(region.xmin, ( region.ymin + (i * zone_height) ), region.width, zone_height);
            z.setZoneNum(i+1);

            // zone inventory
            //inventory.get

            if (create_new) {
                zoneList.addLast(z);
            } else {
                zoneList.get(i).update(z);
                zoneList.get(i).setZoneNum(i);
            }
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


    public LinkedList<Zone> getZoneList() {
        return this.zoneList;
    }

    public void setInventory(ResultSet r) {
        this.inventory = r;
    }
























}
