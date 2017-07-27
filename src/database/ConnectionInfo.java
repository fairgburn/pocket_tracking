package database; /**
 * Marker interface for data structure containing connection parameters
 */


import java.util.HashMap;


public interface ConnectionInfo
{
    String getType();
    String getConnectionString();
    HashMap getConnectionInfo();

}
