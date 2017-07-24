public class UnitInfo 
{
    public int order;
    public String customer;
    public int width;
    public int length;
    
    public UnitInfo(int order, String customer, int width, int length) {
        update(order, customer, width, length);
    }

    public UnitInfo(UnitInfo u) {
        update(u.order, u.customer, u.width, u.length);
    }

    public void update(int order, String customer, int width, int length) {
        this.order = order;
        this.customer = customer;
        this.width = width;
        this.length = length;
    }

    public void update(UnitInfo u) {
        update(u.order, u.customer, u.width, u.length);
    }


}
