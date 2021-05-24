package App;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        super();
        // set layout to a boxlayout
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        // init buttons
        JButton createQuiz = new JButton("Create Quiz");
        createQuiz.setBackground (Color.decode("#274472"));
        createQuiz.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        createQuiz.setMaximumSize(new Dimension(300, 100));
        createQuiz.setPreferredSize(new Dimension(300, 100));

        JButton takeQuiz = new JButton("Take Quiz");
        takeQuiz.setBackground (Color.decode("#274472"));
        takeQuiz.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        takeQuiz.setMaximumSize(new Dimension(300, 100));
        takeQuiz.setPreferredSize(new Dimension(300, 100));
        // create quiz button action listener
        createQuiz.addActionListener(App::editQuiz);
        // init title
        JLabel title = new JLabel("<html><h1>Welcome!</h1></html>", JLabel.CENTER);
        // align buttons and title in the middle horizontally
        createQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        takeQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        // add title
        add(title);
        // add buttons
        add(Box.createVerticalGlue());
        add(createQuiz);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(takeQuiz);
        add(Box.createVerticalGlue());
        setBackground (Color.decode("#a9a9a9"));
    }
}
