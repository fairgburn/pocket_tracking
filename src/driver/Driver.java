// General hardware interface
package driver;

import main.Region;

import java.util.List;

public interface Driver
{
    // make connections to driver
    void connect(Region[] regs);

    // start communication with an object
    void start(Object o);
}