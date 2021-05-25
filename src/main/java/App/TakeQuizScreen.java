package App;

import javax.swing.*;
import java.awt.*;

public class TakeQuizScreen extends JPanel {
    private Quiz quiz;
    private JScrollPane sp;
    public TakeQuizScreen() {
        super(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        JButton back = new JButton("Back");
        back.addActionListener(App::home);
        top.add(back, BorderLayout.WEST);
        JLabel title = new JLabel("Take Quiz", JLabel.CENTER);
        top.add(title, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
    }
}
