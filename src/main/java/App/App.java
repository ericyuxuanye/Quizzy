package App;

// look and feel for this app. Their github page is at https://github.com/JFormDesigner/FlatLaf
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

/*
 * Main class that runs
 */
public class App
{
    private static MainFrame f;
    public static void main(String[] args)
    {
        // swing apps need to have ui change run from the event dispatch thread
        SwingUtilities.invokeLater(() -> {
            // try to install look and feel, and print message if it fails
            if (!FlatMoonlightIJTheme.setup())
                System.err.println("Error: not able to install look and feel");
            // run the main JFrame
            f = new MainFrame();
        });
    }

    // the methods below are triggered by JButtons for switching screens

    public static void home(ActionEvent e) {
        f.home();
    }

    public static void editQuiz(ActionEvent e) {
        f.editQuiz();
    }

    public static void takeQuiz(ActionEvent e) {
        f.takeQuiz();
    }
}
