package App;

import javax.swing.*;

import java.util.ArrayList;

public class MultipleSelection extends Multiple {
	private boolean[] correctAnswer;
	private transient ArrayList<JCheckBox> choices = new ArrayList<>();

	public MultipleSelection(int number) {
		questionNumber = number;
	}

	// made private so that you have to use the constructor with the number
	private MultipleSelection() { }

	@Override
	protected void addSelectionEdit() {
		addSelectionEdit(null);
	}

	@Override
	protected void addSelectionEdit(String text) {
		JPanel selection = new JPanel();
		JCheckBox c = new JCheckBox();
		choices.add(c);
		JTextField question = new JTextField(40);
		question.setText(text);
		selection.add(c);
		selection.add(question);
		selectionChoiceHolder.add(selection, selectionChoiceConstraints);
		editPanel.revalidate();
		editPanel.repaint();
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
			} else if (correctAnswer[i]) {
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
		correctAnswer = new boolean[choices.size()];
		for (int i = 0; i < correctAnswer.length; i++) {
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
		int n = choicesTF.size();
		choicesTextStrings = new String[choicesTF.size()];
		for (int i = 0; i < n; i++) {
			choicesTextStrings[i] = choicesTF.get(i).getText();
		}
	}
}