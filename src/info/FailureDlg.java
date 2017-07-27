package info;

import javax.swing.*;
import java.awt.*;


// Total program failure, show a message and exit
public class FailureDlg
{
    public static void showError(String errmsg) {
        try {
            JLabel label = new JLabel(errmsg);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

            JOptionPane.showMessageDialog(null, label, errmsg, JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println(errmsg);
            e.printStackTrace();
        }
        System.exit(1);
    }
}
