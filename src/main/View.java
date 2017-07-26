package main;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class View extends JFrame
{

    Surface surface;
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


        /*** configure the GUI ***/

        this.setLayout(new BorderLayout());

        // default size if window is moved
        this.setSize(1280, 720);

        // start program maximized
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // put the main surface at the top
        surface = new Surface(this);
        surface.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        surface.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        add(surface);

        // status bar (bottom of screen)
        statusBar = new JPanel();
        statusBar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(), 20)); // size of panel
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusLabel.setText("testing status bar");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        // set the window title
        setTitle(config.get("window_title"));

        // can't remember what this does
        //setLocationRelativeTo(null);

        // just exit the program when user clicks on the x
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }



    public PostgresDB getDatabase() {
        return this.pdb;
    }




    // update status bar
    public void setStatusText(String s) {
        this.statusLabel.setText(s);
    }


/** main **/


    public static void main(String[] args) {
        // set native OS look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create a main.View object in the event queue to be run later
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                View v = new View(args);
                v.setVisible(true);
            }
        });
    }

}















