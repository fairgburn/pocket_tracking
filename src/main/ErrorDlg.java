package main;

import javax.swing.*;
import java.awt.*;


// contains one method - pop up an error dialog (or print error to standard error
// if there is any issue with the dialog) and kill the program unconditionally
public class ErrorDlg
{
    public static void showError(String errmsg) {
        try {
            JLabel label = new JLabel(errmsg);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            JOptionPane.showMessageDialog(null, label, errmsg, JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println(errmsg);
        }
        System.exit(1);
    }
}
