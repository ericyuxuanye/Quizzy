package App;

import javax.swing.*;

import java.awt.*;

public class FillBlank implements QuizQuestion {
    private transient JTextArea questionTA;
    private transient JFormattedTextField answerTF;
    private String questionText;
    private String answerText;
    private int questionNumber;

    public FillBlank(int questionNumber) {
        this.questionNumber = questionNumber;
    }

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
        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 30, 0, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel questionLabel = new JLabel("Question " + questionNumber + ":");

        questionTA = new JTextArea(questionText, 3, 50);
        JScrollPane questionTFS = new JScrollPane(questionTA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel bottom = new JPanel();
        JLabel label = new JLabel("answer: ");
        answerTF = new JFormattedTextField(answerText);
        answerTF.setColumns(20);
        bottom.add(label);
        bottom.add(answerTF);

        editPanel.add(questionLabel, gbc);
        editPanel.add(questionTFS, gbc);
        editPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        editPanel.add(bottom, gbc);
        // create vertical spacing
        editPanel.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
        return editPanel;
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
        questionText = questionTA.getText();
        answerText = answerTF.getText();
    }
}
