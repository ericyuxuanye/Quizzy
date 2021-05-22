package App;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Vector;

public class MultipleChoice extends Multiple {
    // A hack so that I can get the underlying vector for ButtonGroup
    // since the buttons field is labeled as protected. This allows me to access
    // a specific element in constant time
    private static class MyButtonGroup extends ButtonGroup {
        public Vector<AbstractButton> getUnderlyingVector() {
            return buttons;
        }
    }
    private int correctAnswer;
    private transient MyButtonGroup buttons = new MyButtonGroup();
    // stores the id of each radio button
    private transient HashMap<ButtonModel, Integer> buttonIDs = new HashMap<>();
    // zero based index of the buttons
    private transient int currentID = -1;

    public MultipleChoice(int number) {
        questionNumber = number;
    }

    // made private so that you have to use the constructor with the number
    private MultipleChoice() { }

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
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void setToCorrectAnswer() {
        buttons.getUnderlyingVector().get(correctAnswer).setSelected(true);
    }

    @Override
    public boolean isCorrect() throws EmptyQuestionException {
        ButtonModel selected = buttons.getSelection();
        if (selected == null) {
            throw new EmptyQuestionException(questionNumber);
        }
        // return whether the selected is the correct answer
        return buttonIDs.get(selected) == correctAnswer;
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        buttons = new MyButtonGroup();
        buttonIDs = new HashMap<>();
    }
}
