// General hardware interface
package driver;

import java.util.List;

public interface Driver
{
    // make connections to driver
    void connect(List<Object> objects);

    // start communication with an object
    void start(Object o);
}