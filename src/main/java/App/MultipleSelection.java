package App;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MultipleSelection extends Multiple {
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
            buttons.add(delete);
        }
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void deleteSelectionEdit() {
        int index = choices.size() - 1;
        choices.remove(index);
        selectionChoiceHolder.remove(index);
        if (choices.size() == 1) 
            buttons.remove(1);
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void setToCorrectAnswer() {
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            choices.get(i).setSelected(correctAnswer[i]);
        }
    }

    @Override
    public boolean isCorrect() throws EmptyQuestionException {
        // whether at least one is selected
        boolean oneIsSelected = false;
        int numCorrect = 0;
        int n = correctAnswer.length;
        // loop through and talley up number that is correct
        for (int i = 0; i < n; i++) {
            if (choices.get(i).isSelected()) {
                oneIsSelected = true;
                if (correctAnswer[i]) {
                    numCorrect++;
                }
            } else if (!correctAnswer[i]) {
                numCorrect++;
            }
        }
        // if none is selected, throw an EmptyQuestionException
        if (!oneIsSelected) {
            throw new EmptyQuestionException(questionNumber);
        }
        // true if all answers are correct
        return numCorrect == n;
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
        choices = new ArrayList<>();
    }
}
