package App;

import javax.swing.*;

import java.util.HashMap;
// import java.awt.event.ItemListener;
// import java.awt.event.ItemEvent;

public class MultipleChoice extends Multiple {
	private int correctAnswer;
	private transient ButtonGroup buttons = new ButtonGroup();
	// stores the id of each radio button
	private transient HashMap<ButtonModel, Integer> buttonIDs = new HashMap<>();
	private transient int currentID = 0;

	// debugging code
	/*
	private transient ItemListener debugSelection = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			final ButtonModel item = ((JRadioButton) e.getSource()).getModel();
			final int choiceNumber = buttonIDs.get(item);
			System.out.println("Selected item " + choiceNumber + " of question " + questionNumber);
		}
	};
	*/

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
		buttonIDs.put(r.getModel(), currentID++);

		// debugging code
		// r.addItemListener(debugSelection);
		// System.out.println(r.getModel());

		JTextField question = new JTextField(text, 40);
		choicesTF.add(question);
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
