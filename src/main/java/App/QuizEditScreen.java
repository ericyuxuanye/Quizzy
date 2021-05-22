package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;

public class QuizEditScreen extends JPanel {
    private Quiz quiz;
    private JScrollPane sp;
    public QuizEditScreen() {
        super(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        JButton back = new JButton("Back");
        // back goes back when clicked
        back.addActionListener(App::home);
        top.add(back, BorderLayout.WEST);
        JLabel titleScreen = new JLabel("Edit Quiz", JLabel.CENTER);
        top.add(titleScreen, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton loadFromFile = new JButton("Load from file");
        loadFromFile.addActionListener(this::loadFromFile);
        buttons.add(loadFromFile);
        JButton clear = new JButton("Clear");
        clear.addActionListener(this::clear);
        buttons.add(clear);
        top.add(buttons, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        quiz = new Quiz();
        sp = new JScrollPane(quiz.getEditPanel());
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    private void loadFromFile(ActionEvent e) {
        JFileChooser op = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Quiz.FILE_EXTENSION
        );
        op.setFileFilter(filter);
        int returnVal = op.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            quiz.loadFromFile(op.getSelectedFile());
            sp.setViewportView(quiz.getEditPanel());
        }
    }

    private void clear(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this, "Warning: This resets the whole screen.\n" +
                    "Continue?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                    == JOptionPane.YES_OPTION) {
            quiz = new Quiz();
            sp.setViewportView(quiz.getEditPanel());
        }
    }
}
