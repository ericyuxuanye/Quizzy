package App;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Quiz {
    // the ArrayList to hold the questions
    private ArrayList<QuizQuestion> questions = new ArrayList<>();
    // the file extension for saved quizzes
    public final static String FILE_EXTENSION = "ser";

    private JPanel editScreen = null;

    private JPanel editQuizPanel = null;

    private GridBagConstraints editQuizConstraints = null;

    private JPanel quizScreen = null;

    private JPanel buttons;

    private JButton delete;

    private String title = null;

    private JTextField titleField = null;

    private int questionNumber = 0;

    /**
     * loads a quiz from a saved file
     * NOTE: needs the annotation SuppressWarnings because we are dealing with raw data
     *
     * @param  f  the file that we are reading from
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile(File f) {
        String tempTitle = null;
        ArrayList<QuizQuestion> tempQuestions = null;
        try (FileInputStream in = new FileInputStream(f);
                BufferedInputStream buf = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(buf)) {
            tempTitle = (String)ois.readObject();
            tempQuestions = (ArrayList<QuizQuestion>)ois.readObject();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(quizScreen, "File was unable to load\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(quizScreen, "Incorrect File Format\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        title = tempTitle;
        questions = tempQuestions;
        questionNumber = questions.size();
        // TODO print success message here
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
            //TODO tell the user that question number e.getNumber() has nothing selected
            System.err.println("Question " + e.getNumber() + " has nothing selected");
            return;
        }
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized class files",
                FILE_EXTENSION);
        jfc.setFileFilter(filter);
        if (jfc.showSaveDialog(editScreen) != JFileChooser.APPROVE_OPTION)
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
            //TODO tell user that we are unable to save to file
            System.err.println("IOException");
            e.printStackTrace();
            return;
        }
        // TODO tell user that we have successfully saved the file
        System.out.println("Success");
    }

    /**
     * returns a JPanel for the user to edit the quiz
     */
    public JPanel getEditPanel() {
        /*
           if (editScreen != null) {
           return editScreen;
           }
           */
        editScreen = new JPanel(new GridBagLayout());
        GridBagConstraints editScreenConstraints = new GridBagConstraints();
        editScreenConstraints.gridx = 0;
        editScreenConstraints.gridy = 0;
        editScreenConstraints.anchor = GridBagConstraints.NORTHWEST;
        editScreenConstraints.weightx = 1;
        editScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Title: "));
        titleField = new JTextField(title, 20);
        titlePanel.add(titleField);
        editScreen.add(titlePanel, editScreenConstraints);
        // init buttons first
        buttons = new JPanel();
        JButton multipleChoice = new JButton("Multiple Choice");

        multipleChoice.addActionListener((e) -> {
            addQuestion(new MultipleChoice(++questionNumber));
        });

        buttons.add(multipleChoice);

        JButton multipleSelection = new JButton("Multiple Selection");

        multipleSelection.addActionListener((e) -> {
            addQuestion(new MultipleSelection(++questionNumber));
        });

        buttons.add(multipleSelection);
        buttons.add(new JButton("Fill in the blank"));
        delete = new JButton("Delete");

        delete.addActionListener(this::deleteQuestion);

        initEditQuizPanel();
        editScreenConstraints.gridy = 1;
        editScreen.add(editQuizPanel, editScreenConstraints);
        editScreenConstraints.gridy = 2;
        editScreen.add(buttons, editScreenConstraints);
        editScreenConstraints.gridy = 3;
        editScreenConstraints.fill = GridBagConstraints.NONE;
        editScreenConstraints.anchor = GridBagConstraints.CENTER;
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(200, 50));
        save.setMaximumSize(new Dimension(200, 50));
        save.addActionListener((e) -> saveToFile());
        editScreen.add(save, editScreenConstraints);
        editScreenConstraints.weighty = 1;
        editScreenConstraints.gridy = 4;
        editScreen.add(Box.createVerticalGlue(), editScreenConstraints);
        return editScreen;
    }

    private void initEditQuizPanel() {
        editQuizPanel = new JPanel(new GridBagLayout());
        editQuizConstraints = new GridBagConstraints();
        editQuizConstraints.gridx = 0;
        editQuizConstraints.gridy = 0;
        editQuizConstraints.weightx = 1;
        editQuizConstraints.anchor = GridBagConstraints.NORTHWEST;
        editQuizConstraints.fill = GridBagConstraints.HORIZONTAL;
        for (QuizQuestion item : questions) {
            editQuizConstraints.gridy++;
            editQuizPanel.add(item.getPanelEditable(), editQuizConstraints);
        }
        if (questions.size() > 0) {
            buttons.add(delete);
        }
    }

    private void addQuestion(QuizQuestion q) {
        questions.add(q);
        editQuizConstraints.gridy++;
        editQuizPanel.add(q.getPanelEditable(), editQuizConstraints);
        if (questionNumber == 1) {
            buttons.add(delete);
        }
        editScreen.revalidate();
        editScreen.repaint();
    }

    private void deleteQuestion(ActionEvent e) {
        questionNumber--;
        questions.remove(questionNumber);
        editQuizConstraints.gridy--;
        editQuizPanel.remove(questionNumber);
        if (questionNumber == 0) {
            buttons.remove(3);
        }
        editScreen.revalidate();
        editScreen.repaint();
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
        quizScreen = new JPanel();
        quizScreen.setLayout(new BoxLayout(quizScreen, BoxLayout.PAGE_AXIS));
        JLabel quizTitle = new JLabel(title, JLabel.CENTER);
        quizScreen.add(quizTitle);
        for (QuizQuestion item : questions) {
            quizScreen.add(item.getPanel());
        }
        return quizScreen;
    }
}
