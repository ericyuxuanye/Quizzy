package App;

import javax.swing.*;

public class MultipleChoice extends Multiple {
	private ButtonModel correctAnswer;
	private transient ButtonGroup buttons = new ButtonGroup();

	public MultipleChoice(int number) {
		questionNumber = number;
	}

	// made private so that you have to use the constructor with the number
	private MultipleChoice() { }

	@Override
	protected void addSelectionEdit() {
		JPanel selection = new JPanel();
		JRadioButton r = new JRadioButton();
		buttons.add(r);
		JTextField question = new JTextField(40);
		selection.add(r);
		selection.add(question);
		selectionChoiceHolder.add(selection, selectionChoiceConstraints);
		editPanel.revalidate();
		editPanel.repaint();
	}

	@Override
	public boolean isCorrect() throws EmptyQuestionException {
		ButtonModel selected = buttons.getSelection();
		if (selected == null) {
			throw new EmptyQuestionException(questionNumber);
		}
		return selected == correctAnswer;
	}

	@Override
	public void save() throws EmptyQuestionException {
		correctAnswer = buttons.getSelection();
		if (correctAnswer == null) {
			throw new EmptyQuestionException(questionNumber);
		}
	}
}
