package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * The class that does a lot of hefty work to manager quizzes.
 * Many of the program's logic is here
 */
public class Quiz {
    /**
     * the file extension for saved quizzes
     */
    public static final String FILE_EXTENSION = "ser";


    // These colors are used to color the background for correct
    // and incorrect answers. It is naturally blended with the background
    // due to an alpha that is below 255.
    // These colors are from
    // https://github.com/atomiks/moonlight-theme/blob/master/styles/colors.less
    // (#4eefcd for green, #ff74a5 for red, I converted hex to rgba 0-255, the transparency is mine)

    /**
     * Red color for incorrect answers
     */
    public static final Color RED = new Color(255, 116, 165, 76);

    /**
     * Green color for correct answers
     */
    public static final Color GREEN = new Color(78, 239, 205, 76);


    // Instance variables

    // the ArrayList to hold the questions
    private ArrayList<QuizQuestion> questions = new ArrayList<>();

    // the panel to hold the quizzes when editing quiz
    private JPanel editQuizPanel;

    // constraints for editQuizPanel
    private GridBagConstraints editQuizConstraints;

    // holds the JPanel for taking/editing quiz
    private JPanel quizScreen;

    // holds JPanel for adding/deleting questions
    private JPanel buttons;

    // button for deleting a question
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
         * All this does is call the {@link JPanel} constructor.
         */
        public WidthPanel(final LayoutManager layout) {
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
            final int h = super.getPreferredSize().height;
            final int w = getParent().getSize().width;
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
    public void loadFromFile(final File f) {
        // create temporary objects, so that if they fail, our
        // original objects would stay intact
        String tempTitle;
        ArrayList<QuizQuestion> tempQuestions;
        final JRootPane jrp =
            quizScreen != null ? quizScreen.getRootPane()
            : null;

        // try reading the file with try-with-resources so that it auto closes no matter what
        try (FileInputStream in = new FileInputStream(f);
                BufferedInputStream buf = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(buf)) {
            tempTitle = (String)ois.readObject();
            tempQuestions = (ArrayList<QuizQuestion>)ois.readObject();
            /*
             This loop will throw a ClassCastException if the object inside the arraylist is
             not a QuizQuestion. The ClassCastException will be caught by the catch block later on.
            */
            for (final Object o : tempQuestions)
                if (!(o instanceof QuizQuestion))
                    throw new ClassCastException("Cannot cast " + o.getClass() +
                            " to QuizQuestion.");
        } catch (final IOException e) {
            // Happens when the file is unreadable or that it is in a wrong format
            JOptionPane.showMessageDialog(jrp, "File was unable to load\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ClassNotFoundException e) {
            // Happens when a serialized class in the file is not in the runtime path.
            // This is almost certain to be the user's mistake
            JOptionPane.showMessageDialog(jrp, "Incorrect File Format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ClassCastException e) {
            // Happens when the class is not the correct class.
            // This is almost certain to be the user's mistake
            JOptionPane.showMessageDialog(jrp, "Incorrect file format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Tell user that the correct answer that was set is invalid. The cause was probably
            // that the user MANUALLY created a serialized object file because this program would
            // not create a file with an invalid correct answer. In fact, I don't think that this
            // program even has to deal with it, but just in case.
            JOptionPane.showMessageDialog(jrp,
                    "Invalid correct answer set for one of the questions", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // set our variables to those temp variables
        title = tempTitle;
        questions = tempQuestions;
        questionNumber = questions.size();
        // set quizScreen to null so that the program will refresh it
        quizScreen = null;
    }

    /**
     * Writes the quiz to a file
     */
    public void saveToFile() {
        // tell the questions to save the correct answer
        try {
            // tell the question to save
            for (final QuizQuestion q : questions) {
                q.save();
            }
        } catch (final EmptyQuestionException e) {
            // we end up here if one of the questions does not have a correct answer set
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Question " + e.getNumber() +
                    " does not have a correct answer set", "Empty Question",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        final JFileChooser jfc = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized class files",
                FILE_EXTENSION);
        jfc.setFileFilter(filter);
        // show file chooser dialog. If the user does not press 'open', return from this function
        if (jfc.showSaveDialog(quizScreen.getRootPane()) != JFileChooser.APPROVE_OPTION)
            return;
        // save title
        title = titleField.getText();
        // set file name to the selected file and add the file extension if there isn't one
        String name = jfc.getSelectedFile().getPath();
        if (!name.endsWith("." + FILE_EXTENSION)) {
            name += "." + FILE_EXTENSION;
        }
        final File file = new File(name);
        // try to write to file (using try with resources so it auto closes no matter what)
        try (FileOutputStream f = new FileOutputStream(file);
                BufferedOutputStream buf = new BufferedOutputStream(f);
                ObjectOutputStream out = new ObjectOutputStream(buf)) {
            // write the title String and the questions ArrayList to file
            out.writeObject(title);
            out.writeObject(questions);
        } catch (final IOException e) {
            // if we were unsuccessful, we inform the user
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Unable to write to file:\n" +
                    e.getMessage(),
                    "IO Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // the file writing is successful if code reaches here
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
        if (quizScreen != null) {
            // Happens if the user loads an unreadable file.
            // The program aborts, so nothing changes, meaning that we do not
            // have to add the components again.
            return quizScreen;
        }

        quizScreen = new JPanel(new GridBagLayout());
        final GridBagConstraints quizScreenConstraints = new GridBagConstraints();
        quizScreenConstraints.gridx = 0;
        quizScreenConstraints.anchor = GridBagConstraints.NORTHWEST;
        quizScreenConstraints.weightx = 1;
        quizScreenConstraints.fill = GridBagConstraints.HORIZONTAL;

        // title
        final JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Title: "));
        titleField = new JTextField(title, 20);
        titlePanel.add(titleField);
        quizScreen.add(titlePanel, quizScreenConstraints);

        // init buttons
        buttons = new JPanel();

        // button that adds a multiple choice question
        final JButton multipleChoice = new JButton("Multiple Choice");
        multipleChoice.addActionListener((e) -> addQuestion(new MultipleChoice(++questionNumber)));
        buttons.add(multipleChoice);

        // button that adds a multiple selection question
        final JButton multipleSelection = new JButton("Multiple Selection");
        multipleSelection.addActionListener((e) ->
                addQuestion(new MultipleSelection(++questionNumber)));
        buttons.add(multipleSelection);

        // button that adds a fill in the blank question
        final JButton fillBlank = new JButton("Fill in the blank");
        fillBlank.addActionListener((e) -> addQuestion(new FillBlank(++questionNumber)));
        buttons.add(fillBlank);

        // delete button
        delete = new JButton("Delete");
        delete.addActionListener(this::deleteQuestion);

        // initialize edit quiz panel and add the existing questions.
        // buttons is added to panel after method call because the delete button gets added
        // if there is more than one question already
        initEditQuizPanel();
        quizScreen.add(editQuizPanel, quizScreenConstraints);
        quizScreen.add(buttons, quizScreenConstraints);

        // save button
        final JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(200, 50));
        save.setMaximumSize(new Dimension(200, 50));
        save.addActionListener((e) -> saveToFile());
        quizScreenConstraints.fill = GridBagConstraints.NONE;
        quizScreenConstraints.anchor = GridBagConstraints.CENTER;
        quizScreen.add(save, quizScreenConstraints);

        // add vertical glue (so elements stick to the top)
        quizScreenConstraints.weighty = 1;
        quizScreen.add(Box.createVerticalGlue(), quizScreenConstraints);

        return quizScreen;
    }

    // initializes editQuizPanel and adds existing QuizQuestions to it
    private void initEditQuizPanel() {
        editQuizPanel = new JPanel(new GridBagLayout());
        editQuizConstraints = new GridBagConstraints();
        editQuizConstraints.gridx = 0;
        editQuizConstraints.weightx = 1;
        editQuizConstraints.insets = new Insets(0, 30, 0, 0);
        editQuizConstraints.anchor = GridBagConstraints.NORTHWEST;
        editQuizConstraints.fill = GridBagConstraints.HORIZONTAL;
        for (final QuizQuestion item : questions) {
            editQuizPanel.add(item.getPanelEditable(), editQuizConstraints);
        }
        // add delete button if there is more than 1 question
        if (questions.size() > 0) {
            buttons.add(delete);
        }
    }

    // method that adds a questions. q is for the specific question type
    private void addQuestion(final QuizQuestion q) {
        questions.add(q);
        editQuizPanel.add(q.getPanelEditable(), editQuizConstraints);
        if (questionNumber == 1) {
            buttons.add(delete);
        }
        quizScreen.revalidate();
        quizScreen.repaint();
    }

    // method that deletes a question
    private void deleteQuestion(final ActionEvent e) {
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
    public JPanel getPanel(final JPanel parent) {
        if (quizScreen != null) {
            // Happens if the user loads an unreadable file.
            // The program aborts, so nothing changes, meaning that we do not
            // have to add the components again.
            return quizScreen;
        }
        quizScreen = new WidthPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 30, 0, 30);
        if (questions.size() == 0) {
            if (title != null) {
                // title will not be null if a quiz is loaded. We warn that the quiz is empty
                // Just so that you know, even if the user did not set a title,
                // the title will be "", not null
                title = null;
                JOptionPane.showMessageDialog(parent.getRootPane(), "Quiz has no body. " +
                        "Add elements to the quiz in the \"Create Quiz\" screen",
                        "Empty Quiz", JOptionPane.WARNING_MESSAGE);
            }
            // Write a message in the center of the screen that tells the user to load the quiz
            final JLabel info = new JLabel("Click the \"Load Quiz\" button to load quiz");
            final Font biggerFont = info.getFont().deriveFont(24f);
            info.setFont(biggerFont);
            quizScreen.add(info);
            return quizScreen;
        }
        gbc.fill = GridBagConstraints.HORIZONTAL;
        final JLabel titleLabel = new JLabel(encloseInHTML(title), JLabel.CENTER);
        final Font biggerFont = titleLabel.getFont().deriveFont(24f);
        titleLabel.setFont(biggerFont);
        quizScreen.add(titleLabel, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.ipady = 20;
        // add questions
        for (final QuizQuestion item : questions) {
            quizScreen.add(item.getPanel(), gbc);
            quizScreen.add(Box.createRigidArea(new Dimension(0, 50)));
        }
        // add submit button
        submit = new JButton("Submit Quiz");
        submit.setFont(submit.getFont().deriveFont(16f));
        submit.setPreferredSize(new Dimension(200, 50));
        submit.setMaximumSize(new Dimension(200, 50));
        submit.addActionListener(this::submit);
        // put submit button in the center, without fill
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        quizScreen.add(submit, gbc);
        // vertical glue for keeping items at top
        gbc.weighty = 1;
        quizScreen.add(Box.createVerticalGlue(), gbc);
        return quizScreen;
    }

    private void submit(final ActionEvent e) {
        int score = 0;
        try {
            for (final QuizQuestion q : questions) {
                // increment score is question is correct
                score += q.check() ? 1 : 0;
            }
        } catch (final EmptyQuestionException ex) {
            // happens when one of the questions is left blank
            JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                    "Question " + ex.getNumber() + " is not answered",
                    "Question not answered", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // calculate percentage user got
        final double percentage = (double) score / questionNumber * 100;
        // disable submit button (user cannot submit answer again after quiz is graded)
        submit.setEnabled(false);
        // tell the user how many they got correct, and the percentage
        // with precision of 2 decimal digits
        JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                String.format("You got %d/%d correct (%.2f%%)", score, questionNumber, percentage),
                "Results", JOptionPane.INFORMATION_MESSAGE);
        // color the answers
        for (final QuizQuestion q : questions) {
            q.colorAnswers();
        }
    }

    /**
     * Encloses the String into html tags. Since the output is html, we have to sanitize the input,
     * so we will escape all '&lt;', '&gt;', '&amp;', and '&quot;'
     * into '&amp;amp;', '&amp;lt;', '&amp;gt;', and '&amp;quot;'
     *
     * @param s the input String
     * @return the string with enclosing html tags and escaped '&lt;', '&gt;', '&amp;', and '&quot;'
     */
    public static String encloseInHTML(final String s) {
        final StringBuilder sb = new StringBuilder("<html>");
        final int n = s.length();
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
        sb.append("</html>");
        return sb.toString();
    }
}
