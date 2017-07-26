// General hardware interface
package driver;

public interface Driver
{
    // make connections to driver
    void connect(Object[] objects);

    // start communication with hardware
    void start();
}