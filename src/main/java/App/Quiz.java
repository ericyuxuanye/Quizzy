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
    // the ArrayList to hold the questions
    private ArrayList<QuizQuestion> questions = new ArrayList<>();
    // the file extension for saved quizzes
    public final static String FILE_EXTENSION = "ser";

    // these colors are used to color the background for correct
    // and incorrect answers. It is naturally blended with the background
    public final static Color red = new Color(255, 116, 165, 76);
    public final static Color green = new Color(78, 239, 205, 76);

    private JPanel editQuizPanel = null;

    private GridBagConstraints editQuizConstraints = null;

    private JPanel quizScreen = null;

    private JPanel buttons;

    private JButton delete;

    private JButton submit;

    private String title = null;

    private JTextField titleField = null;

    private int questionNumber = 0;

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
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "File was unable to load\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Incorrect File Format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(), "Incorrect file format\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        title = tempTitle;
        questions = tempQuestions;
        questionNumber = questions.size();
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
        if (jfc.showSaveDialog(quizScreen) != JFileChooser.APPROVE_OPTION)
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
        /*
           if (quizScreen != null) {
           return quizScreen;
           }
           */
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
     */
    public JPanel getPanel() {
        /*
           if (quizScreen != null) {
           return quizScreen;
           }
           */
        quizScreen = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 30, 0, 0);
        if (questions.size() == 0) {
            JLabel info = new JLabel("Click the \"Load Quiz\" button to load quiz");
            final Font biggerFont = info.getFont().deriveFont(24f);
            info.setFont(biggerFont);
            quizScreen.add(info);
            return quizScreen;
        }
        JLabel titleLabel = new JLabel(title);
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
        submit.setPreferredSize(new Dimension(200, 50));
        submit.setMaximumSize(new Dimension(200, 50));
        submit.addActionListener(this::submit);
        gbc.anchor = GridBagConstraints.CENTER;
        quizScreen.add(submit, gbc);
        gbc.weighty = 1;
        quizScreen.add(Box.createVerticalGlue(), gbc);
        return quizScreen;
    }

    private void submit(ActionEvent e) {
        int score = 0;
        try {
            for (QuizQuestion q : questions) {
                score += q.check() ? 1 : 0;
            }
        } catch (EmptyQuestionException ex) {
            JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                    "Question " + ex.getNumber() + " is not answered",
                    "Question not answered", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double percentage = (double) score / questionNumber * 100;
        submit.setEnabled(false);
        JOptionPane.showMessageDialog(quizScreen.getRootPane(),
                String.format("You got %d/%d correct (%.2f%%)", score, questionNumber, percentage),
                "Results", JOptionPane.INFORMATION_MESSAGE);
        // color the answers
        for (QuizQuestion q : questions) {
            q.colorAnswers();
        }
    }
}
