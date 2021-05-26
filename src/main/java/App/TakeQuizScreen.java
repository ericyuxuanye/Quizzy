package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

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
        JButton loadFromFile = new JButton("Load Quiz");
        loadFromFile.addActionListener(this::loadFromFile);
        top.add(loadFromFile, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        quiz = new Quiz();
        sp = new JScrollPane(quiz.getPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    private void loadFromFile(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Quiz.FILE_EXTENSION);
        jfc.setFileFilter(filter);
        int returnVal = jfc.showOpenDialog(getRootPane());
        if ((returnVal == JFileChooser.APPROVE_OPTION) &&
                (quiz.numQuestions() == 0 ||
                        (JOptionPane.showConfirmDialog(getRootPane(), "Warning: You will lose your progress in your current quiz",
                                "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)
                                == JOptionPane.OK_OPTION
                        )
                )) {
            quiz.loadFromFile(jfc.getSelectedFile());
            sp.setViewportView(quiz.getPanel());
        }
    }
}
