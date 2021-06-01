package App;

import javax.swing.*;

import java.awt.*;

/**
 * Represents a question that requires the test taker to Fill in the blank.
 *
 * @see QuizQuestion
 */
public class FillBlank implements QuizQuestion {
    // so that even if I update this class,
    // quizzes created on older versions will still work.
    // However, I will have to make sure that the non transient fields
    // of this class will stay the same
    private static final long serialVersionUID = -8375462480244920151L;
    private transient JTextArea questionTA;
    private transient JTextField answerTF;
    private transient JLabel correctAnswer;
    private String questionText;
    private String answerText;
    private final int questionNumber;

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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // holds the question. Using html tags to wrap text
        JLabel title = new JLabel(Quiz.encloseInHTML(questionNumber + ". " + questionText));
        title.setFont(title.getFont().deriveFont(16f));
        panel.add(title, gbc);

        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerTF = new JTextField(20);
        correctAnswer = new JLabel("Correct Answer: " + answerText);
        correctAnswer.setVisible(false);
        answerPanel.add(answerTF);
        answerPanel.add(correctAnswer);
        panel.add(answerPanel, gbc);
        return panel;
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
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel questionLabel = new JLabel("Question " + questionNumber + ":");

        questionTA = new JTextArea(questionText, 3, 50);
        questionTA.setLineWrap(true);
        questionTA.setWrapStyleWord(true);
        JScrollPane questionTFS = new JScrollPane(questionTA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel bottom = new JPanel();
        JLabel label = new JLabel("answer: ");
        answerTF = new JTextField(answerText, 20);
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
     * @return true if answer is correct
     */
    @Override
    public boolean check() throws EmptyQuestionException {
        String answer = answerTF.getText().trim();
        if (answer.length() == 0) {
            throw new EmptyQuestionException(questionNumber);
        }
        return answer.equalsIgnoreCase(answerText);
    }

    @Override
    public void colorAnswers() {
        String answer = answerTF.getText().trim();
        // disable editing
        answerTF.setEnabled(false);
        if (answer.equalsIgnoreCase(answerText)) {
            answerTF.setBackground(Quiz.GREEN);
            return;
        }
        correctAnswer.setVisible(true);
        answerTF.setBackground(Quiz.RED);
    }

    /**
     * saves the question and the correct answer to serialized variables
     */
    @Override
    public void save() throws EmptyQuestionException {
        String answer = answerTF.getText().trim();
        if (answer.length() == 0) {
            throw new EmptyQuestionException(questionNumber);
        }
        questionText = questionTA.getText();
        answerText = answer;
    }
}
