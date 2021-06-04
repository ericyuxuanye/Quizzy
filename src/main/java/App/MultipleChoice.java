package App;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Class that represents a Multiple Choice Question.
 *
 * @see Multiple
 */
public class MultipleChoice extends Multiple {
    /**
     * I added functionality to {@link ButtonGroup}
     * <P>
     * This includes:
     * <ul>
     *  <li>Ability to get button element using index</li>
     *  <li>Ability to remove button by index</li>
     * </ul>
     */
    private static class MyButtonGroup extends ButtonGroup {
        // returns the element that is at the index
        // (faster than the original method that has to
        // loop through every element)
        public AbstractButton get(int index) {
            return buttons.get(index);
        }
        // Method to efficiently remove elements
        public void remove(int index) {
            AbstractButton b = buttons.get(index);
            buttons.remove(index);
            if(isSelected(b.getModel())) {
                clearSelection();
            }
            b.getModel().setGroup(null);
        }
    }
    // so that even if I update this class,
    // quizzes created on older versions will still work.
    // However, I will have to make sure that the non transient fields
    // of this class will stay the same
    private static final long serialVersionUID = -3564040796584989713L;
    // zero based index for the correct answer
    private int correctAnswer;
    private transient MyButtonGroup buttons = new MyButtonGroup();
    // stores the id of each radio button
    private transient HashMap<ButtonModel, Integer> buttonIDs = new HashMap<>();
    // zero based index of the buttons
    private transient int currentID = -1;

    public MultipleChoice(int number) {
        questionNumber = number;
    }

    @Override
    protected void addSelectionEdit() {
        addSelectionEdit(null);
    }

    @Override
    protected void addSelectionEdit(String text) {
        JPanel selection = new JPanel();
        JRadioButton r = new JRadioButton();
        buttons.add(r);
        buttonIDs.put(r.getModel(), ++currentID);
        JTextField question = new JTextField(text, 40);
        choicesTF.add(question);
        selection.add(r);
        selection.add(question);
        selectionChoiceHolder.add(selection, selectionChoiceConstraints);
        if (currentID == 1) {
            addDelete.add(delete);
        }
    }

    @Override
    protected void deleteSelectionEdit() {
        // remove from HashMap
        buttonIDs.remove(buttons.get(currentID).getModel());
        // remove from ButtonGroup
        buttons.remove(currentID);
        // remove the JPanel that holds the selection
        selectionChoiceHolder.remove(currentID);
        // remove the TextField
        choicesTF.remove(currentID);
        if (currentID == 1) {
            // if there is only 1 selection,
            // remove the delete button
            addDelete.remove(1);
        }
        // decrement currentID
        currentID--;
        // refresh screen
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void setToCorrectAnswer() {
        buttons.get(correctAnswer).setSelected(true);
    }

    @Override
    protected void addSelections() {
        for (String s : choicesText) {
            JRadioButton button = new JRadioButton(Quiz.encloseInHTML(s));
            buttons.add(button);
            buttonIDs.put(button.getModel(), ++currentID);
            panel.add(button, panelConstraints);
        }
    }

    @Override
    public boolean check() throws EmptyQuestionException {
        ButtonModel selected = buttons.getSelection();
        if (selected == null) {
            throw new EmptyQuestionException(questionNumber);
        }
        int selectedIndex = buttonIDs.get(selected);
        return selectedIndex == correctAnswer;
    }

    @Override
    public void colorAnswers() {
        ButtonModel selected = buttons.getSelection();
        int selectedIndex = buttonIDs.get(selected);
        // color correct and wrong buttons
        buttons.get(correctAnswer).setBackground(Quiz.GREEN);
        if (selectedIndex != correctAnswer) {
            // if a wrong answer exists, set the wrong answer to red
            buttons.get(selectedIndex).setBackground(Quiz.RED);
        }
        // disable buttons
        Enumeration<AbstractButton> buttonEnum = buttons.getElements();
        while (buttonEnum.hasMoreElements()) {
            buttonEnum.nextElement().setEnabled(false);
        }
    }

    @Override
    public void save() throws EmptyQuestionException {
        ButtonModel selected = buttons.getSelection();
        if (selected == null) {
            throw new EmptyQuestionException(questionNumber);
        }
        question = questionTF.getText();
        correctAnswer = buttonIDs.get(selected);
        int numChoices = choicesTF.size();
        choicesText = new String[numChoices];
        for (int i = 0; i < numChoices; i++) {
            choicesText[i] = choicesTF.get(i).getText();
        }
    }

    // initialize objects when deserializing
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // default deserialize
        in.defaultReadObject();
        // check range to see if deserialized object is valid
        if (correctAnswer < 0 || correctAnswer >= choicesText.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        // initialize transient fields
        buttons = new MyButtonGroup();
        buttonIDs = new HashMap<>();
        currentID = -1;
    }
}
