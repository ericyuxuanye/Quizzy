package App;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.net.URL;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        super();

        // get image location (which is in classpath)
        URL url = getClass().getResource("/quiz.png");
        ImageIcon logo = new ImageIcon (url);
        JLabel picture = new JLabel ();
        picture.setAlignmentX(Component.CENTER_ALIGNMENT);
        picture.setIcon (logo);

        // set layout to a boxlayout
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // init buttons
        JButton createQuiz = new JButton("Create Quiz");
        createQuiz.setMaximumSize(new Dimension(300, 100));
        createQuiz.setPreferredSize(new Dimension(300, 100));
        createQuiz.setFont(createQuiz.getFont().deriveFont(20f));
        createQuiz.setFocusable(true);
        // create quiz button action listener
        createQuiz.addActionListener(App::editQuiz);

        JButton takeQuiz = new JButton("Take Quiz");
        takeQuiz.setMaximumSize(new Dimension(300, 100));
        takeQuiz.setPreferredSize(new Dimension(300, 100));
        takeQuiz.setFont(takeQuiz.getFont().deriveFont(20f));
        takeQuiz.setFocusable(true);
        // take quiz button actionlistener
        takeQuiz.addActionListener(App::takeQuiz);

        JButton quit = new JButton("Quit");
        quit.setMaximumSize(new Dimension(300, 70));
        quit.setPreferredSize(new Dimension(300, 70));
        quit.setFont(quit.getFont().deriveFont(20f));
        quit.setFocusable(true);
        quit.addActionListener(e -> System.exit(0));

        // align buttons and title in the middle horizontally
        createQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        takeQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add elements to this JPanel
        add(Box.createVerticalGlue());
        add(picture);
        add(Box.createVerticalGlue());
        add(createQuiz);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(takeQuiz);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(quit);
        add(Box.createVerticalGlue());
    }
}
