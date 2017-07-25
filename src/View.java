import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.util.LinkedList;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class View extends JFrame
{

    Surface surface = new Surface();
    PostgresDB pdb = new PostgresDB();
    Arguments arguments = null;
    Settings config = null;

    JPanel statusBar = new JPanel();
    JLabel statusLabel = new JLabel();


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
        surface.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        surface.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));

/*** configure the GUI ***/

        this.setLayout(new BorderLayout());

        // default size if window is moved
        this.setSize(1280, 720);

        // start program maximized
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // put the main surface at the top
        this.add(surface);

        // status bar (bottom of screen)
        statusBar = new JPanel();
        statusBar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(), 20)); // size of panel
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusLabel.setText("testing status bar");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        this.add(statusBar, BorderLayout.SOUTH);

        setTitle(config.get("window_title"));



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

    // update status bar
    public void setStatusText(String s) {
        this.statusLabel.setText(s);
    }

}















