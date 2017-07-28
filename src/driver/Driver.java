// General hardware interface
package driver;

import main.Region;

public interface Driver
{
    // make connections to driver
    void connect(Region[] regs);

    // start communication with an object
    void start(Object o);
}