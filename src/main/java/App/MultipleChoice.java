package App;

import javax.swing.*;
import java.awt.*;

public class MultipleChoice implements QuizQuestion {
	private transient JPanel panel = null;
	private transient JPanel editPanel = null;
	private String question = "";
	private int questionNumber;

	public MultipleChoice(int number) {
		questionNumber = number;
	}

	private ButtonGroup buttons = new ButtonGroup();
	private ButtonModel correctAnswer;
	public JPanel getPanel() {
		// TODO
		return new JPanel();
	}
	public JPanel getPanelEditable() {
		// TODO
		return new JPanel();
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
