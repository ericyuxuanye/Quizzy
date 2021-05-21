package App;

import javax.swing.*;

import java.util.HashMap;

public class MultipleChoice extends Multiple {
	private int correctAnswer;
	private transient ButtonGroup buttons = new ButtonGroup();
	// stores the id of each radio button
	private transient HashMap<JRadioButton, Integer> buttonIDs = new HashMap<>();
	private transient int currentID = 0;

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
		buttonIDs.put(r, currentID++);
		JTextField question = new JTextField(40);
		question.setText(text);
		selection.add(r);
		selection.add(question);
		selectionChoiceHolder.add(selection, selectionChoiceConstraints);
		editPanel.revalidate();
		editPanel.repaint();
	}

	@Override
	public boolean isCorrect() throws EmptyQuestionException {
		JRadioButton selected = (JRadioButton) buttons.getSelection();
		if (selected == null) {
			throw new EmptyQuestionException(questionNumber);
		}
		// return whether the selected is the correct answer
		return buttonIDs.get(selected) == correctAnswer;
	}

	@Override
	public void save() throws EmptyQuestionException {
		JRadioButton selected = (JRadioButton) buttons.getSelection();
		if (selected == null) {
			throw new EmptyQuestionException(questionNumber);
		}
		question = questionTF.getText();
		correctAnswer = buttonIDs.get(selected);
		int n = choicesTF.size();
		choicesTextStrings = new String[choicesTF.size()];
		for (int i = 0; i < n; i++) {
			choicesTextStrings[i] = choicesTF.get(i).getText();
		}
	}
}
