package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
    private Properties p = new Properties();
    private static String config_file_path = "config/config.ini";

    // Singleton instance
    private static Settings self = new Settings(config_file_path);
    public static Settings getInstance() {
        return self;
    }


    private Settings(String s) {
        try {
            FileInputStream fis = new FileInputStream(s);
            p.load(fis);
        } catch (IOException e) {
            ErrorDlg.showError("couldn't load " + config_file_path);
        }
    }

    public String get(String s) {
        return p.getProperty(s);
    }
}
