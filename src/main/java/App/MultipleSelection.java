package App;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MultipleSelection extends Multiple {
    // so that even if I update this class,
    // quizzes created on older versions will still work.
    // However, I will have to make sure that the non transient fields
    // of this class will stay the same
    private static final long serialVersionUID = -4843741680223517983L;
    private boolean[] correctAnswer;
    private transient ArrayList<JCheckBox> choices = new ArrayList<>();

    public MultipleSelection(int number) {
        questionNumber = number;
    }

    @Override
    protected void addSelectionEdit() {
        addSelectionEdit(null);
    }

    @Override
    protected void addSelectionEdit(String text) {
        JPanel selection = new JPanel();
        JCheckBox c = new JCheckBox();
        choices.add(c);
        JTextField question = new JTextField(text, 40);
        selection.add(c);
        selection.add(question);
        choicesTF.add(question);
        selectionChoiceHolder.add(selection, selectionChoiceConstraints);
        if (choices.size() == 2) {
            addDelete.add(delete);
        }
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void deleteSelectionEdit() {
        int index = choices.size() - 1;
        choices.remove(index);
        choicesTF.remove(index);
        selectionChoiceHolder.remove(index);
        if (choices.size() == 1)
            addDelete.remove(1);
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void addSelections() {
        for (String choice : choicesText) {
            JCheckBox button = new JCheckBox(Quiz.encloseInHTML(choice));
            choices.add(button);
            panel.add(button, panelConstraints);
        }
    }

    @Override
    protected void setToCorrectAnswer() {
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            choices.get(i).setSelected(correctAnswer[i]);
        }
    }

    @Override
    public boolean check() throws EmptyQuestionException {
        // whether at least one is selected
scope: {
        for (JCheckBox button : choices) {
            if (button.isSelected()) {
                break scope;
            }
        }
        // if none is selected, throw an EmptyQuestionException
        throw new EmptyQuestionException(questionNumber);
}
        // loop through and talley up number that is correct
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            JCheckBox currentButton = choices.get(i);
            if (currentButton.isSelected() != correctAnswer[i]) {
                return false;
            }
        }
        // true if all answers are correct
        return true;
    }

    @Override
    public void colorAnswers() {
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            JCheckBox currentButton = choices.get(i);
            if (correctAnswer[i]) {
                currentButton.setBackground(Quiz.GREEN);
            } else if (currentButton.isSelected()) {
                currentButton.setBackground(Quiz.RED);
            }
            // disable button
            currentButton.setEnabled(false);
        }
    }

    @Override
    public void save() throws EmptyQuestionException {
        // whether at least one is selected
        boolean oneIsSelected = false;
        int numButtons = choices.size();
        correctAnswer = new boolean[numButtons];
        choicesText = new String[numButtons];
        for (int i = 0; i < numButtons; i++) {
            choicesText[i] = choicesTF.get(i).getText();
            if (choices.get(i).isSelected()) {
                oneIsSelected = true;
                correctAnswer[i] = true;
            } else {
                correctAnswer[i] = false;
            }
        }
        if (!oneIsSelected) {
            throw new EmptyQuestionException(questionNumber);
        }
        question = questionTF.getText();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // range check to see if serialization is valid
        if (choicesText.length != correctAnswer.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        choices = new ArrayList<>();
    }
}
