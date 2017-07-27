/**
 * Global values for program-scope access
 */

package globals;

import database.PostgresConnectionInfo;
import main.SettingsReader;

public class Globals
{

/** members **/
    // singleton
    private static Globals self = new Globals();

    // database connection
    public PostgresConnectionInfo connectionInfo;

    // window title
    public String windowTitle;

    // master font name
    public String font;

    // enable debugging
    public boolean debugEnable;

    /** advanced options (mainly used for testing) **/
    // status bar stuff
    public int statusbarHeight;
    public int statusbarTextSize;
    public boolean startMaximized;
    public java.awt.Point resolution;


/** access **/
    // only one Globals object
    public static Globals getInstance() { return self; }
    private Globals() {
        // get globals from config.ini
        SettingsReader config = SettingsReader.getInstance();
        connectionInfo = new PostgresConnectionInfo(
                config.get("database"),
                config.get("host"),
                config.get("port"),
                config.get("user"),
                config.get("password"));

        // window title
        windowTitle = config.get("window_title");

        // master font name
        font = config.get("font");

        // debugging - turn on if setting is "on", "true", or "yes"
        debugEnable = config.get("debug_mode").equals("on");

        /** advanced **/
        boolean use_advanced_defaults = true;
        if (config.get("advanced_mode").equals("on")) {
            use_advanced_defaults = false;
            try {
                statusbarHeight = Integer.parseInt(config.get("statusbar_height"));
                statusbarTextSize = Integer.parseInt(config.get("statusbar_text_size"));
                startMaximized = config.get("start_maximized").equals("yes");
                String[] _res = config.get("window_size").split("x");
                resolution = new java.awt.Point(Integer.parseInt(_res[0]), Integer.parseInt(_res[1]));

            } catch (NumberFormatException e) {
                use_advanced_defaults = true; // use defaults if any error occurs
            }

        }

        // use default settings if advanced config is off or if an error occurred just now
        // skip this if advanced config is enabled
        if (use_advanced_defaults) {
            statusbarHeight = 25;
            statusbarTextSize = 18;
            startMaximized = true;
            resolution = new java.awt.Point(1280, 720);
        }

    }
}
