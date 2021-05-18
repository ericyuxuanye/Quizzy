package App;

import java.io.Serializable;

import javax.swing.JPanel;

public interface QuizQuestion extends Serializable {
	/**
	 * Gets the panel to show the quiz question
	 */
	public JPanel getPanel();
	/**
	 * Gets the panel with JTextFields so the user can
	 * edit the current question
	 */
	public JPanel getPanelEditable();
	/**
	 * Returns whether the user selected the correct answer
	 */
	public boolean isCorrect();
}
