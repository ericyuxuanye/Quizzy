package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

public class TakeQuizScreen extends JPanel {
    private final Quiz quiz;
    private final JScrollPane sp;

    public TakeQuizScreen() {
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
