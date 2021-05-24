package App;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * class for MultipleChoice and MultipleSelection extend from
 */
public abstract class Multiple implements QuizQuestion {
    // variables to keep track of state.
    // transient = don't write this to file when serializing
    // (who serializes GUI components???)
    protected transient JPanel panel = null;
    protected transient JPanel editPanel = null;
    protected transient JTextArea questionTF = null;
    protected transient JPanel selectionChoiceHolder = null;
    protected transient GridBagConstraints selectionChoiceConstraints = null;
    protected transient ArrayList<JTextField> choicesTF = new ArrayList<>();
    protected transient JButton delete;
    protected transient JPanel buttons;
    protected String question;
    protected int questionNumber;
    // holds the question choice strings
    protected String[] choicesText;

    public JPanel getPanel() {
        // TODO
        return panel = new JPanel();
    }

    public JPanel getPanelEditable() {
        // this method probably would be called multiple times, so we save
        // the JPanel, and construct it only if it is null
        if (editPanel == null) {
            editPanel = new JPanel(new GridBagLayout());

            // jpanel to hold add/delete buttons, located at bottom
            buttons = new JPanel();
            JButton add = new JButton("Add Selection");
            add.addActionListener((e) -> addSelectionEdit());
            buttons.add(add);
            delete = new JButton("Delete Selection");
            delete.addActionListener((e) -> deleteSelectionEdit());

            GridBagConstraints questionConstraints = new GridBagConstraints();
            questionConstraints.gridx = 0;
            questionConstraints.weightx = 1;
            questionConstraints.anchor = GridBagConstraints.LINE_START;
            questionConstraints.insets = new Insets(0, 30, 0, 0);
            JLabel questionLabel = new JLabel("Question " + questionNumber + ":");
            editPanel.add(questionLabel, questionConstraints);
            questionTF = new JTextArea(question, 3, 50);
            // wrap by word
            questionTF.setLineWrap(true);
            questionTF.setWrapStyleWord(true);
            JScrollPane questionTFS = new JScrollPane(questionTF,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            editPanel.add(questionTFS, questionConstraints);

            selectionChoiceHolder = new JPanel(new GridBagLayout());
            selectionChoiceConstraints = new GridBagConstraints();
            selectionChoiceConstraints.gridx = 0;

            // try to load available choices is there is any
            if (choicesText == null) {
                addSelectionEdit();
            } else {
                for (String text : choicesText) {
                    addSelectionEdit(text);
                }
                try {
                    setToCorrectAnswer();
                } catch (ArrayIndexOutOfBoundsException e) {
                    // tell user that the correct answer is invalid
                    JOptionPane.showMessageDialog(editPanel,
                            "Unable to load correct answer for question " + questionNumber + ".",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            editPanel.add(selectionChoiceHolder, questionConstraints);
            editPanel.add(buttons, questionConstraints);
            editPanel.add(Box.createRigidArea(new Dimension(0, 50)), questionConstraints);
        }
        return editPanel;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        choicesTF = new ArrayList<>();
    }

    protected abstract void addSelectionEdit();
    protected abstract void deleteSelectionEdit();
    protected abstract void addSelectionEdit(String text);
    protected abstract void setToCorrectAnswer();
}
