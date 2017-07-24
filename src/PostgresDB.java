import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;

public class PostgresDB implements Database
{

    PostgresConnectionInfo _ci;
    Connection _conn;

    public PostgresDB(ConnectionInfo ci) {
        setConnectionInfo(ci);
    }

    public PostgresDB() {}

    public void setConnectionInfo(ConnectionInfo ci) {
        try {
            Class.forName("org.postgresql.Driver");
            _ci = new PostgresConnectionInfo(ci);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        HashMap<String, String> h = _ci.getConnectionInfo();
        try {
            _conn = DriverManager.getConnection(_ci.getConnectionString(), h.get("username"), h.get("password"));
        } catch (Exception e) {
            ErrorDlg.showError("Error connecting to database!");
            return false;
        }

        return true;
    }

    public int executeUpdate(String sql) {
        try {
            return _conn.createStatement().executeUpdate(sql);
        } catch (Exception e) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return 0;

        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            return _conn.createStatement().executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }






}

