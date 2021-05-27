package App;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.HashMap;

public class MultipleChoice extends Multiple {
    // A hack so that I can get the underlying vector for ButtonGroup
    // since buttons is labeled as protected. This allows me to access
    // a specific element in constant time
    private static class MyButtonGroup extends ButtonGroup {
        // returns the element that is at the index
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
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void deleteSelectionEdit() {
        buttonIDs.remove(buttons.get(currentID).getModel());
        buttons.remove(currentID);
        selectionChoiceHolder.remove(currentID);
        choicesTF.remove(currentID);
        if (currentID == 1) {
            addDelete.remove(1);
        }
        currentID--;
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        buttons = new MyButtonGroup();
        buttonIDs = new HashMap<>();
        currentID = -1;
    }
}
