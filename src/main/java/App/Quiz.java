package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * The class that does a lot of hefty work.
 * Many of the program's logic is here
 */
public class Quiz {
    // the file extension for saved quizzes
    public final static String FILE_EXTENSION = "ser";

    // these colors are used to color the background for correct
    // and incorrect answers. It is naturally blended with the background

    /**
     * Red color for incorrect answers
     */
    public final static Color RED = new Color(255, 116, 165, 76);

    /**
     * Green color for correct answers
     */
    public final static Color GREEN = new Color(78, 239, 205, 76);

    // Instance variables

    // the ArrayList to hold the questions
    private ArrayList<QuizQuestion> questions = new ArrayList<>();

    private JPanel editQuizPanel;

    private GridBagConstraints editQuizConstraints;

    private JPanel quizScreen;

    private JPanel buttons;

    private JButton delete;

    // submit button
    private JButton submit;

    // title of Quiz represented as String
    private String title;

    // JTextField for the title of Quiz
    private JTextField titleField;

    // number of last question (0 if there are no questions)
    private int questionNumber = 0;

    /**
     * Used for the quizScreen JPanel so that it sizes the same
     * as the width of the JScrollPane
    */
    private static class WidthPanel extends JPanel {
        /**
         * Constructs a new {@link WidthPanel} object.
         * All this does is call the {@link JPanel} constructor
         */
        public WidthPanel(LayoutManager layout) {
            super(layout);
        }
        /**
         * Returns a new {@link Dimension} with the height set as the preferred height,
         * and the width set as the same width as the parent
         *
         * @return a {@link Dimension} object
         */
        @Override
        public Dimension getPreferredSize() {
            int h = super.getPreferredSize().height;
            int w = getParent().getSize().width;
            return new Dimension(w, h);
        }
    }

    /**
     * loads a quiz from a saved file.
     * It needs the annotation SuppressWarnings because java compiler cannot
     * guarantee that the cast will be successful
     * (Don't worry, we have plenty of error checking code here)
     *
     * @param  f  the file that we are reading from
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile(File f) {
        // create temporary objects, so that if they fail, our
        // original objects would stay intact
        String tempTitle;
        ArrayList<QuizQuestion> tempQuestions;
        JRootPane jrp =
            quizScreen != null ? quizScreen.getRootPane()
            : null;

        try (FileInputStream in = new FileInputStream(f);
                BufferedInputStream buf = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(buf)) {
            tempTitle = (String)ois.readObject();
            tempQuestions = (ArrayList<QuizQuestion>)ois.readObject();
            /*
             This loop will throw a ClassCastException if the object inside the arraylist is not a QuizQuestion.
             The ClassCastException will be caught by the catch block later on.
            */
            for (Object o : tempQuestions)
                if (!(o instanceof QuizQuestion))
                    throw new ClassCastException("Cannot cast " + o.getClass() +
                            " to QuizQuestion.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(jrp, "File was unable to load\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(jrp, "Incorrect File Format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(jrp, "Incorrect file format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Tell user that the correct answer that was set is invalid. The cause was probably
            // that the user MANUALLY created a serialized object file because this program would
            // not create a file with an invalid correct answer. In fact, I don't think that this
            // program even has to deal with it, but just in case.
            JOptionPane.showMessageDialog(jrp,
                    "Invalid correct answer set for one of the questions", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        title = tempTitle;
        questions = tempQuestions;
        questionNumber = questions.size();
        editQuizPanel = null;
        quizScreen = null;
    }

    /**
     * Writes the quiz to a file
     */
    public void saveToFile() {
        // tell the questions to save the correct answer
        try {
            // tell the question to save
            for (QuizQuestion q : questions) {
                q.save();
            }
        } catch (EmptyQuestionException e) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Question " + e.getNumber() +
                    " does not have a correct answer set", "Empty Question",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized class files",
                FILE_EXTENSION);
        jfc.setFileFilter(filter);
        if (jfc.showSaveDialog(quizScreen.getRootPane()) != JFileChooser.APPROVE_OPTION)
            return;
        // save title
        title = titleField.getText();
        String name = jfc.getSelectedFile().getPath();
        if (!name.endsWith("." + FILE_EXTENSION)) {
            name += "." + FILE_EXTENSION;
        }
        File file = new File(name);
        try (FileOutputStream f = new FileOutputStream(file);
                BufferedOutputStream buf = new BufferedOutputStream(f);
                ObjectOutputStream out = new ObjectOutputStream(buf)) {
            out.writeObject(title);
            out.writeObject(questions);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Unable to write to file:\n" +
                    e.getMessage(),
                    "IO Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Successfully wrote to file",
                "Success!", JOptionPane.INFORMATION_MESSAGE);
    }

    public int numQuestions() {
        return questionNumber;
    }

    /**
     * returns a JPanel for the user to edit the quiz
     */
    public JPanel getEditPanel() {
        quizScreen = new JPanel(new GridBagLayout());
        GridBagConstraints quizScreenConstraints = new GridBagConstraints();
        quizScreenConstraints.gridx = 0;
        quizScreenConstraints.anchor = GridBagConstraints.NORTHWEST;
        quizScreenConstraints.weightx = 1;
        quizScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Title: "));
        titleField = new JTextField(title, 20);
        titlePanel.add(titleField);
        quizScreen.add(titlePanel, quizScreenConstraints);

        // init buttons first
        buttons = new JPanel();

        JButton multipleChoice = new JButton("Multiple Choice");
        multipleChoice.addActionListener((e) -> addQuestion(new MultipleChoice(++questionNumber)));
        buttons.add(multipleChoice);

        JButton multipleSelection = new JButton("Multiple Selection");
        multipleSelection.addActionListener((e) ->
                addQuestion(new MultipleSelection(++questionNumber)));
        buttons.add(multipleSelection);

        JButton fillBlank = new JButton("Fill in the blank");
        fillBlank.addActionListener((e) -> addQuestion(new FillBlank(++questionNumber)));
        buttons.add(fillBlank);

        // delete button
        delete = new JButton("Delete");
        delete.addActionListener(this::deleteQuestion);

        initEditQuizPanel();
        quizScreen.add(editQuizPanel, quizScreenConstraints);
        quizScreen.add(buttons, quizScreenConstraints);
        quizScreenConstraints.fill = GridBagConstraints.NONE;
        quizScreenConstraints.anchor = GridBagConstraints.CENTER;
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(200, 50));
        save.setMaximumSize(new Dimension(200, 50));
        save.addActionListener((e) -> saveToFile());
        quizScreen.add(save, quizScreenConstraints);
        quizScreenConstraints.weighty = 1;
        quizScreen.add(Box.createVerticalGlue(), quizScreenConstraints);
        return quizScreen;
    }

    private void initEditQuizPanel() {
        editQuizPanel = new JPanel(new GridBagLayout());
        editQuizConstraints = new GridBagConstraints();
        editQuizConstraints.gridx = 0;
        editQuizConstraints.weightx = 1;
        editQuizConstraints.insets = new Insets(0, 30, 0, 0);
        editQuizConstraints.anchor = GridBagConstraints.NORTHWEST;
        editQuizConstraints.fill = GridBagConstraints.HORIZONTAL;
        for (QuizQuestion item : questions) {
            editQuizPanel.add(item.getPanelEditable(), editQuizConstraints);
        }
        if (questions.size() > 0) {
            buttons.add(delete);
        }
    }

    private void addQuestion(QuizQuestion q) {
        questions.add(q);
        editQuizPanel.add(q.getPanelEditable(), editQuizConstraints);
        if (questionNumber == 1) {
            buttons.add(delete);
        }
        quizScreen.revalidate();
        quizScreen.repaint();
    }

    private void deleteQuestion(ActionEvent e) {
        questionNumber--;
        questions.remove(questionNumber);
        editQuizPanel.remove(questionNumber);
        if (questionNumber == 0) {
            buttons.remove(3);
        }
        quizScreen.revalidate();
        quizScreen.repaint();
    }

    /**
     * returns a JPanel for the user to take the quiz
     *
     * @param  parent  the parent JPanel (Used for JOptionPane)
     */
    public JPanel getPanel(JPanel parent) {
        if (quizScreen != null) {
            return quizScreen;
        }
        quizScreen = new WidthPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 30, 0, 30);
        if (questions.size() == 0) {
            if (title != null) {
                title = null;
                JOptionPane.showMessageDialog(parent.getRootPane(), "Quiz has no body. " +
                        "Add elements to the quiz in the \"Create Quiz\" screen",
                        "Empty Quiz", JOptionPane.WARNING_MESSAGE);
            }
            JLabel info = new JLabel("Click the \"Load Quiz\" button to load quiz");
            final Font biggerFont = info.getFont().deriveFont(24f);
            info.setFont(biggerFont);
            quizScreen.add(info);
            return quizScreen;
        }
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel(encloseInHTML(title), JLabel.CENTER);
        final Font biggerFont = titleLabel.getFont().deriveFont(24f);
        titleLabel.setFont(biggerFont);
        quizScreen.add(titleLabel, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.ipady = 20;
        for (QuizQuestion item : questions) {
            quizScreen.add(item.getPanel(), gbc);
            quizScreen.add(Box.createRigidArea(new Dimension(0, 50)));
        }
        submit = new JButton("Submit Quiz");
        submit.setFont(submit.getFont().deriveFont(16f));
        submit.setPreferredSize(new Dimension(200, 50));
        submit.setMaximumSize(new Dimension(200, 50));
        submit.addActionListener(this::submit);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        quizScreen.add(submit, gbc);
        gbc.weighty = 1;
        quizScreen.add(Box.createVerticalGlue(), gbc);
        return quizScreen;
    }

    private void submit(ActionEvent e) {
        int score = 0;
        try {
            for (QuizQuestion q : questions) {
                // increment score is question is correct
                score += q.check() ? 1 : 0;
            }
        } catch (EmptyQuestionException ex) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                    "Question " + ex.getNumber() + " is not answered",
                    "Question not answered", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // calculate percentage user got
        double percentage = (double) score / questionNumber * 100;
        // disable submit button (user cannot submit answer again after quiz is graded)
        submit.setEnabled(false);
        // tell the user how many they got correct, and the percentage
        // with precision of 2 decimal digits
        JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                String.format("You got %d/%d correct (%.2f%%)", score, questionNumber, percentage),
                "Results", JOptionPane.INFORMATION_MESSAGE);
        // color the answers
        for (QuizQuestion q : questions) {
            q.colorAnswers();
        }
    }

    /**
     * Encloses the String into html tags. Since the output is html, we have to sanitize the input,
     * so we will escape all '&lt;', '&gt;', '&amp;', and '&quot;'
     * into '&amp;amp;', '&amp;lt;', '&amp;gt;', and '&amp;quot;'
     *
     * @param s the input String
     * @return the string with enclosing html tags and escaped '<', '>', and '&amp;'
     */
    public static String encloseInHTML(String s) {
        StringBuilder sb = new StringBuilder("<html>");
        int n = s.length();
        for (int i = 0; i < n; i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
