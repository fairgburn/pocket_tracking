import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class View extends JFrame
{

    Surface surface = new Surface();
    PostgresDB pdb = new PostgresDB();
    Arguments arguments = null;
    Settings config = null;     // class Settings is a factory:
                                // get instance by calling static getSettings() method, not constructor


    public View(String[] args) {

        // enable debugging info
        Debug.enable();

        // parse command line arguments
        arguments = new Arguments(args);

        // load configurations file
        config = Settings.getInstance();

        pdb.setConnectionInfo(new PostgresConnectionInfo(
                config.get("database"),       // database name
                config.get("host"),           // host
                config.get("port"),           // port
                config.get("user"),       // username
                config.get("password")        // password
        ));

        pdb.connect();
        surface.setView(this);
        add(surface);
        setTitle("ACS Pocket Tracking");

        // default size if window is moved
        setSize(1280, 720);

        // start program maximized
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // can't remember what this does
        setLocationRelativeTo(null);

        // just exit the program when user clicks on the x
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public PostgresDB getDatabase() {
        return this.pdb;
    }


    public static void main(String[] args) {
        // set native look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create a View object in the event queue to be run later
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                View v = new View(args);
                v.setVisible(true);
            }
        });
    }

}















