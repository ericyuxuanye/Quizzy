package App;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * class for MultipleChoice and MultipleSelection extend from
 */
public abstract class Multiple implements QuizQuestion {
	// variables to keep track of state.
	// transient = don't write this to file when serializing
	// (who serializes GUI components???)
	protected transient JPanel panel = null;
	protected transient JPanel editPanel = null;
	protected transient JTextArea questionTF = null;
	protected transient JPanel selectionChoiceHolder = null;
	protected transient GridBagConstraints selectionChoiceConstraints = null;
	protected ArrayList<JTextField> choicesTF = new ArrayList<>();
	protected String question = "";
	protected int questionNumber;
	protected String[] choicesTextStrings;

	public JPanel getPanel() {
		// TODO
		return panel = new JPanel();
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

			if (choicesTextStrings != null) {
				for (String choice : choicesTextStrings) {
					addSelectionEdit(choice);
				}
			} else {
				addSelectionEdit();
			}

			editPanel.add(selectionChoiceHolder, questionEditConstraints);
			JButton add = new JButton("Add Selection");
			add.addActionListener((e) -> addSelectionEdit());
			editPanel.add(add, questionEditConstraints);
			editPanel.add(Box.createRigidArea(new Dimension(0, 50)), questionEditConstraints);
		}
		return editPanel;
	}

	protected abstract void addSelectionEdit();
	protected abstract void addSelectionEdit(String text);
	public abstract boolean isCorrect() throws EmptyQuestionException;
}
