package App;

import javax.swing.*;
import java.awt.*;

public class MultipleChoice implements QuizQuestion {
	// variables to keep track of state.
	// transient = don't write this to file when serializing
	// (who serializes GUI components???)
	private transient JPanel panel = null;
	private transient JPanel editPanel = null;
	private transient JTextArea questionTF = null;
	private transient JPanel selectionChoiceHolder = null;
	private transient GridBagConstraints selectionChoiceConstraints = null;
	private String question = "";
	private int questionNumber;
	private ButtonGroup buttons = new ButtonGroup();
	private ButtonModel correctAnswer;

	public MultipleChoice(int number) {
		questionNumber = number;
	}

	// made private so that you have to use the constructor with the number
	private MultipleChoice() { }

	public JPanel getPanel() {
		// TODO
		return new JPanel();
	}
	public JPanel getPanelEditable() {
		// this method probably would be called multiple times, so we save
		// the JPanel, and construct it only if it is null
		if (editPanel == null) {
			editPanel = new JPanel(new GridBagLayout());
			GridBagConstraints questionEditConstraints = new GridBagConstraints();
			questionEditConstraints.gridx = 0;
			questionEditConstraints.weightx = 1;
			questionEditConstraints.anchor = GridBagConstraints.LINE_START;
			questionEditConstraints.insets = new Insets(0, 30, 0, 0);
			JLabel questionLabel = new JLabel("Question " + questionNumber + ":");
			editPanel.add(questionLabel, questionEditConstraints);
			questionTF = new JTextArea(3, 50);
			JScrollPane questionTFS = new JScrollPane(questionTF,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			editPanel.add(questionTFS, questionEditConstraints);

			selectionChoiceHolder = new JPanel(new GridBagLayout());
			selectionChoiceConstraints = new GridBagConstraints();
			selectionChoiceConstraints.gridx = 0;
			addSelection();

			editPanel.add(selectionChoiceHolder, questionEditConstraints);
			JButton add = new JButton("Add Selection");
			add.addActionListener((e) -> addSelection());
			editPanel.add(add, questionEditConstraints);
			editPanel.add(Box.createRigidArea(new Dimension(0, 50)), questionEditConstraints);
		}
		return editPanel;
	}

	private void addSelection() {
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
	public boolean isCorrect() throws EmptyQuestionException {
		ButtonModel selected = buttons.getSelection();
		if (selected == null) {
			throw new EmptyQuestionException(questionNumber);
		}
		return selected == correctAnswer;
	}
	public void save() throws EmptyQuestionException {
		correctAnswer = buttons.getSelection();
		if (correctAnswer == null) {
			throw new EmptyQuestionException(questionNumber);
		}
	}
}
