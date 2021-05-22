package App;

import javax.swing.*;

public class FillBlank implements QuizQuestion {
    /**
     * get question panel when taking quiz
     *
     * @return the panel to show
     */
    @Override
    public JPanel getPanel() {
        return null;
    }

    /**
     * get question panel when editing quiz
     *
     * @return the panel to show
     */
    @Override
    public JPanel getPanelEditable() {
        return null;
    }

    /**
     * whether the answer filled in is the correct answer
     *
     * @return true is answer is correct
     */
    @Override
    public boolean isCorrect() throws EmptyQuestionException {
        return false;
    }

    /**
     * saves the question and the correct answer to serialized variables
     */
    @Override
    public void save() throws EmptyQuestionException {

    }
}
