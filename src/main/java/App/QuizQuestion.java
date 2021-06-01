package App;

import java.io.Serializable;

import javax.swing.JPanel;

/**
 * Represents a single quiz question.
 */
public interface QuizQuestion extends Serializable {
    /**
     * Gets the panel to show the quiz question
     */
    JPanel getPanel();

    /**
     * Gets the panel with JTextFields so the user can
     * edit the current question
     */
    JPanel getPanelEditable();

    /**
     * Returns whether the user selected the correct answer.
     */
    boolean check() throws EmptyQuestionException;

    /**
     * colors the wrong and correct answers
     */
    void colorAnswers();

    /**
     * Saves the correct answer in edit pane to the corresponding variables.
     * Prepares the object for serialization
     *
     * @throws EmptyQuestionException if no answer is set by user
     */
    void save() throws EmptyQuestionException;
}
