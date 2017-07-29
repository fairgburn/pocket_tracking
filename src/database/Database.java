package database;

/**
 * Created by acs on 7/7/17.
 */
public interface Database
{
    void setConnectionInfo(ConnectionInfo ci);
    int executeUpdate(String sql); // modify the database
    java.sql.ResultSet executeQuery(String sql); // query the database
}
