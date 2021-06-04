package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;

/**
 * JPanel that shows the edit screen
 */
public class QuizEditScreen extends JPanel {
    private Quiz quiz;
    private final JScrollPane sp;
    public QuizEditScreen() {
        super(new BorderLayout());

        // JPanel at the top
        final JPanel top = new JPanel(new BorderLayout());

        // JPanel at top left, which contains back button
        final JPanel left = new JPanel();
        final JButton back = new JButton("Back");

        // back goes to home screen when clicked
        back.addActionListener(App::home);
        left.add(back);
        top.add(left, BorderLayout.WEST);

        // title
        final JLabel titleScreen = new JLabel("Edit Quiz", JLabel.CENTER);
        top.add(titleScreen, BorderLayout.CENTER);

        // JPanel at top right
        final JPanel buttons = new JPanel();
        final JButton loadFromFile = new JButton("Load from file");
        loadFromFile.addActionListener(this::loadFromFile);
        buttons.add(loadFromFile);
        final JButton clear = new JButton("Clear");
        clear.addActionListener(this::clear);
        buttons.add(clear);
        top.add(buttons, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // show quiz in scrollPane
        quiz = new Quiz();
        sp = new JScrollPane(quiz.getEditPanel());
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    // methods below are triggered by the JButtons

    // shows a JOptionPane that asks for a file, then loads the quiz from the file
    private void loadFromFile(final ActionEvent e) {
        final JFileChooser op = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Quiz.FILE_EXTENSION);
        op.setFileFilter(filter);
        final int returnVal = op.showOpenDialog(getRootPane());
        // make sure user really wants to load and overwrite file
        if ((returnVal == JFileChooser.APPROVE_OPTION) &&
                (quiz.numQuestions() == 0 ||
                        (JOptionPane.showConfirmDialog(getRootPane(), "Overwrite current contents?",
                                "Confirmation",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)

                                == JOptionPane.OK_OPTION
                        )
                )) {
            quiz.loadFromFile(op.getSelectedFile());
            sp.setViewportView(quiz.getEditPanel());
        }
    }

    // clears the screen
    private void clear(final ActionEvent e) {
        if (JOptionPane.showConfirmDialog(getRootPane(),
                    "Warning: This resets the whole screen.\n" +
                    "Continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                    == JOptionPane.YES_OPTION) {
            quiz = new Quiz();
            sp.setViewportView(quiz.getEditPanel());
        }
    }
}
