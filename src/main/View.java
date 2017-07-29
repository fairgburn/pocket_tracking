package main;

import database.PostgresDB;
import info.Arguments;
import info.Debug;
import info.Globals;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class View extends JFrame
{

    private Surface surface;
    private PostgresDB pdb = new PostgresDB();
    private Arguments arguments = null;

    private JPanel statusBar = new JPanel();
    private JLabel statusLabel = new JLabel();


    public View(String[] args) {

        // get global objects
        Globals glob = Globals.getInstance();

        // enable debugging info
        if (glob.debugEnable) Debug.enable();

        // parse command line arguments
        arguments = new Arguments(args);

        pdb.setConnectionInfo(glob.connectionInfo);

        pdb.connect();


        /*** configure the GUI ***/

        this.setLayout(new BorderLayout());

        // default size if reduced
        this.setSize(glob.resolution.x, glob.resolution.y);

        // start program maximized depending on config
        if (glob.startMaximized) this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // put the main surface at the top
        surface = new Surface(this);
        surface.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        surface.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        add(surface);

        // status bar (bottom of screen)
        statusBar = new JPanel();
        statusBar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(this.getWidth(), glob.statusbarHeight)); // size of panel
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));

        statusLabel.setFont(new Font(glob.font, Font.PLAIN, glob.statusbarTextSize));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        // set the window title
        setTitle(glob.windowTitle);

        // set the program icon
        try {
            setIconImage(ImageIO.read(new File("res/icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // just exit the program when user clicks on the x
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // timer for inventory updates
        // runs every half second
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                surface.updateInventory();
                surface.repaint();
            }
        }, 0, 500);

    }



    public PostgresDB getDatabase() {
        return this.pdb;
    }




    // update status bar
    public void setStatusText(String s) {
        statusLabel.setText(s);
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
