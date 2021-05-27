package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        super();


        ImageIcon logo = new ImageIcon ("quiz.png");
        JLabel picture = new JLabel ();
        picture.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("<html><h1>Welcome!</h1></html>", JLabel.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        picture.setIcon (logo);
        add (picture);
        // set layout to a boxlayout
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        // init buttons
        JButton createQuiz = new JButton("Create Quiz");
        createQuiz.setMaximumSize(new Dimension(300, 100));
        createQuiz.setPreferredSize(new Dimension(300, 100));
        createQuiz.setFont(new Font("Calibri", Font.PLAIN, 20));
        createQuiz.setFocusable(true);


        JButton takeQuiz = new JButton("Take Quiz");
        takeQuiz.setMaximumSize(new Dimension(300, 100));
        takeQuiz.setPreferredSize(new Dimension(300, 100));
        takeQuiz.setFont(new Font("Calibri", Font.PLAIN, 20));
        takeQuiz.setFocusable(true);

        JButton quit = new JButton("Quit");
        quit.setMaximumSize(new Dimension(300, 70));
        quit.setPreferredSize(new Dimension(300, 70));
        quit.setFont(new Font("Calibri", Font.PLAIN, 20));
        quit.setFocusable(true);
        quit.addActionListener(e -> System.exit(0));

        // create quiz button action listener
        createQuiz.addActionListener(App::editQuiz);
        // take quiz button actionlistener
        takeQuiz.addActionListener(App::takeQuiz);
        // init title
        // align buttons and title in the middle horizontally
        createQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        takeQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add title
        add(title);
        // add buttons
        add(Box.createVerticalGlue());
        add(createQuiz);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(takeQuiz);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(quit);
        add(Box.createVerticalGlue());


    }
}
