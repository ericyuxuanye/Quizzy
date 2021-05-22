package App;

import com.formdev.flatlaf.FlatLightLaf;

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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (!FlatLightLaf.setup())
                    System.err.println("Error: not able to install look and feel");
                f = new MainFrame();
            }
        });
    }

    public static void home(ActionEvent e) {
        f.home();
    }

    public static void editQuiz(ActionEvent e) {
        f.editQuiz();
    }
}
