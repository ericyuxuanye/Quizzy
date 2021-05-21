package App;

import javax.swing.*;

public class FillBlank implements QuizQuestion {
    @Override
    public JPanel getPanel() {
        return null;
    }

    @Override
    public JPanel getPanelEditable() {
        return null;
    }

    @Override
    public boolean isCorrect() throws EmptyQuestionException {
        return false;
    }

    @Override
    public void save() throws EmptyQuestionException {

    }
}
