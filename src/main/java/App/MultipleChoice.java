package App;

import javax.swing.*;

import java.util.Enumeration;
import java.util.HashMap;

public class MultipleChoice extends Multiple {
	private int correctAnswer;
	private transient ButtonGroup buttons = new ButtonGroup();
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
		final Enumeration<AbstractButton> bEnum = buttons.getElements();
		for (int i = 0; i < currentID; i++) {
			bEnum.nextElement();
		}
		((JRadioButton)bEnum.nextElement()).setSelected(true);
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
}
