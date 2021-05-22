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
        String tempTitle;
        ArrayList<QuizQuestion> tempQuestions;
        try (FileInputStream in = new FileInputStream(f);
                BufferedInputStream buf = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(buf)) {
            tempTitle = (String)ois.readObject();
            tempQuestions = (ArrayList<QuizQuestion>)ois.readObject();
            // if list has elements, try to get the first element,
            // if it fails, then the class is not right
            if (tempQuestions.size() > 0 && !(tempQuestions.get(0) instanceof QuizQuestion)) {
                JOptionPane.showMessageDialog(quizScreen, "Incorrect file format",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(quizScreen, "File was unable to load\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(quizScreen, "Incorrect File Format\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(quizScreen, "Incorrect file format\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(quizScreen, "Question " + e.getNumber() + " has nothing selected",
                    "Empty Question", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(quizScreen, "Unable to write to file:\n" + e.getMessage(),
                    "IO Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(quizScreen, "Successfully wrote to file", "Success!",
                JOptionPane.INFORMATION_MESSAGE);
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
        quizScreenConstraints.gridy = 0;
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
        quizScreenConstraints.gridy = 1;
        quizScreen.add(editQuizPanel, quizScreenConstraints);
        quizScreenConstraints.gridy = 2;
        quizScreen.add(buttons, quizScreenConstraints);
        quizScreenConstraints.gridy = 3;
        quizScreenConstraints.fill = GridBagConstraints.NONE;
        quizScreenConstraints.anchor = GridBagConstraints.CENTER;
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(200, 50));
        save.setMaximumSize(new Dimension(200, 50));
        save.addActionListener((e) -> saveToFile());
        quizScreen.add(save, quizScreenConstraints);
        quizScreenConstraints.weighty = 1;
        quizScreenConstraints.gridy = 4;
        quizScreen.add(Box.createVerticalGlue(), quizScreenConstraints);
        return quizScreen;
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
        quizScreen.revalidate();
        quizScreen.repaint();
    }

    private void deleteQuestion(ActionEvent e) {
        questionNumber--;
        questions.remove(questionNumber);
        editQuizConstraints.gridy--;
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
