package main;

import java.util.HashMap;

class PostgresConnectionInfo implements ConnectionInfo
{
    String _db;
    String _host;
    String _port;
    String _username;
    String _password;


    /***************
     * Constructors
     **************/
    private PostgresConnectionInfo(){}

    /**
     * explicit constructor
     * @param db        name of database
     * @param host      host name or IP address
     * @param port      port (usually 5432)
     * @param username  database username
     * @param password  database password
     */
    public PostgresConnectionInfo(String db, String host, String port, String username, String password) {
        _db = db;
        _host = host;
        _port = port;
        _username = username;
        _password = password;
    }

    /**
     * copy constructor
     * @param ci            a main.ConnectionInfo interface
     * @throws Exception    if ci.getType() != "postgresql"
     */
    public PostgresConnectionInfo(ConnectionInfo ci) throws Exception {
        if ( !ci.getType().equals("postgresql") ) {
            throw new Exception("main.PostgresConnectionInfo: copy constructor: wrong type!");
        }

        HashMap<String, String> h = ci.getConnectionInfo();
        _db = h.get("db");
        _host = h.get("host");
        _port = h.get("port");
        _username = h.get("username");
        _password = h.get("password");

    }


    /********************
     * interface methods
     *******************/


    @Override
    public String getType() {
        return "postgresql";
    }

    @Override
    public String getConnectionString() {
        return "jdbc:postgresql://"+_host+":"+_port+"/"+_db;
    }

    @Override
    public HashMap<String, String> getConnectionInfo() {
        HashMap h = new HashMap<String, String>();

        h.put("db", _db);
        h.put("host", _host);
        h.put("port", _port);
        h.put("username", _username);
        h.put("password", _password);

        return h;
    }
}