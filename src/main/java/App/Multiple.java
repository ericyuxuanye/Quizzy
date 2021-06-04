package App;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * class for MultipleChoice and MultipleSelection to extend from
 *
 * @see QuizQuestion
 */
public abstract class Multiple implements QuizQuestion {
    // variables to keep track of state.
    // transient = don't write this to file when serializing
    // (who serializes GUI components???)

    // holds the panel when taking the quiz
    protected transient JPanel panel;
    // constraints for panel
    protected transient GridBagConstraints panelConstraints;
    // holds the panel when editing the quiz
    protected transient JPanel editPanel;
    // holds the text area where the user enters the question
    protected transient JTextArea questionTF;
    // holds the selection choices (button + choice text)
    protected transient JPanel selectionChoiceHolder;
    // constraints for selectionChoiceHolder
    protected transient GridBagConstraints selectionChoiceConstraints;
    // holds the answer choice text fields (for edit panel)
    protected transient ArrayList<JTextField> choicesTF = new ArrayList<>();
    // delete button
    protected transient JButton delete;
    // JPanel that holds add and delete buttons
    protected transient JPanel addDelete;


    // variables that will be serialized

    // Holds the question
    protected String question;
    // holds the question number
    protected int questionNumber;
    // holds the question choice strings
    protected String[] choicesText;

    @Override
    public JPanel getPanel() {
        panel = new JPanel(new GridBagLayout());
        panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.anchor = GridBagConstraints.LINE_START;
        panelConstraints.weightx = 1;
        panelConstraints.ipady = 10;
        panelConstraints.fill = GridBagConstraints.HORIZONTAL;
        // question label uses html tags to auto wrap
        JLabel questionLabel = new JLabel(
                Quiz.encloseInHTML(questionNumber + ". " + question));
        questionLabel.setFont(questionLabel.getFont().deriveFont(16f));
        panel.add(questionLabel, panelConstraints);
        addSelections();
        return panel;
    }

    @Override
    public JPanel getPanelEditable() {
        editPanel = new JPanel(new GridBagLayout());

        GridBagConstraints questionConstraints = new GridBagConstraints();
        questionConstraints.gridx = 0;
        questionConstraints.anchor = GridBagConstraints.LINE_START;
        questionConstraints.weightx = 1;
        JLabel questionLabel = new JLabel("Question " + questionNumber + ":");
        questionTF = new JTextArea(question, 3, 50);
        // wrap by word
        questionTF.setLineWrap(true);
        questionTF.setWrapStyleWord(true);
        // place question text field inside scrollpane so that
        // we are able to scroll it, and it also highlights the border
        JScrollPane questionTFS = new JScrollPane(questionTF,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // JPanel to hold the question choices
        selectionChoiceHolder = new JPanel(new GridBagLayout());
        selectionChoiceConstraints = new GridBagConstraints();
        selectionChoiceConstraints.gridx = 0;

        // Jpanel to hold add/delete buttons, located at bottom
        addDelete = new JPanel();
        JButton add = new JButton("Add Selection");
        add.addActionListener((e) -> {
            addSelectionEdit();
            editPanel.revalidate();
            editPanel.repaint();
        });
        // add "Add" button
        addDelete.add(add);
        delete = new JButton("Delete Selection");
        delete.addActionListener((e) -> deleteSelectionEdit());
        // don't add delete button to buttons panel yet!

        // try to load available choices is there is any
        if (choicesText == null) {
            // if there aren't, create one blank selection choice
            addSelectionEdit();
        } else {
            for (String text : choicesText) {
                addSelectionEdit(text);
            }
            setToCorrectAnswer();
        }

        // add those components to JPanel
        editPanel.add(questionLabel, questionConstraints);
        editPanel.add(questionTFS, questionConstraints);
        editPanel.add(selectionChoiceHolder, questionConstraints);
        editPanel.add(addDelete, questionConstraints);
        editPanel.add(Box.createRigidArea(new Dimension(0, 50)), questionConstraints);
        return editPanel;
    }

    // helps with serialization. After the object is read in, we want to initialize arraylist
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        choicesTF = new ArrayList<>();
    }

    // abstract methods that methods in this class call
    // These are implemented in the child classes

    /**
     * Adds a selection when editing
     */
    protected abstract void addSelectionEdit();

    /**
     * Adds a selection when editing
     *
     * @param text the String to add to the TextField
     */
    protected abstract void addSelectionEdit(String text);

    /**
     * Deletes a selection when editing
     */
    protected abstract void deleteSelectionEdit();

    /**
     * Sets the correct answer variable to the current answer
     */
    protected abstract void setToCorrectAnswer();

    /**
     * Add selections that were serialized
     */
    protected abstract void addSelections();
}
