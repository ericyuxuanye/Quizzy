package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

/**
 * panel for taking quizzez
 */
public class TakeQuizScreen extends JPanel {
    // the Quiz object (for doing all of the logic and rendering)
    private Quiz quiz;
    // scrollpane that holds the Quiz
    private JScrollPane sp;

    public TakeQuizScreen() {
        // uses BorderLayout
        super(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        JButton back = new JButton("Back");
        back.addActionListener(App::home);
        top.add(back, BorderLayout.WEST);
        JLabel title = new JLabel("Take Quiz", JLabel.CENTER);
        top.add(title, BorderLayout.CENTER);
        JButton loadFromFile = new JButton("Load Quiz");
        loadFromFile.addActionListener(this::loadFromFile);
        top.add(loadFromFile, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        quiz = new Quiz();
        sp = new JScrollPane(quiz.getPanel(this));
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    // Called by JButton. Opens a file chooser and loads that quiz
    private void loadFromFile(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Quiz.FILE_EXTENSION);
        jfc.setFileFilter(filter);
        int returnVal = jfc.showOpenDialog(getRootPane());
        // check if user clicked approve option, and if there are more than one question,
        // show another conferm dialog that informs user that he will lose his answer.
        // If user clicks OK, then finally show the new quiz
        if ((returnVal == JFileChooser.APPROVE_OPTION) &&
                (quiz.numQuestions() == 0 ||
                        (JOptionPane.showConfirmDialog(getRootPane(), "Warning: You will lose your answers in your current quiz",
                                "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)
                                == JOptionPane.OK_OPTION
                        )
                )) {
            quiz.loadFromFile(jfc.getSelectedFile());
            sp.setViewportView(quiz.getPanel(this));
        }
    }
}
