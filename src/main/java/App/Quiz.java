package App;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Quiz {
	// the ArrayList to hold the questions
	private ArrayList<QuizQuestion> questions = new ArrayList<>();
	// the file extension for saved quizzes
	private final static String FILE_EXTENSION = ".ser";

	private JPanel editScreen = null;

	private JPanel editQuizPanel = null;

	private GridBagConstraints editQuizConstraints = null;

	private JPanel quizScreen = null;

	private String title = "";

	private JTextField titleField = null;

	/**
	 * loads a quiz from a saved file
	 * NOTE: needs the annotation SuppressWarnings because we are dealing with raw data
	 *
	 * @param  f  the file that we are reading from
	 */
	@SuppressWarnings("unchecked")
	public void loadFromFile(File f) {
		try (FileInputStream in = new FileInputStream(f);
				BufferedInputStream buf = new BufferedInputStream(in);
				ObjectInputStream ois = new ObjectInputStream(buf)) {
			title = (String)ois.readObject();
			questions = (ArrayList<QuizQuestion>)ois.readObject();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(quizScreen, "File was unable to load");
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(quizScreen, "File was unable to load");
 
		}
	}

	/**
	* Writes the quiz to a file
	*
	* @param  name  the name of the file without the extension
	* @param  title  the title of the quiz
	*/
	public void saveToFile(String name) {
		try {
			// tell the questions to save the correct answer
			for (QuizQuestion q : questions) {
				q.save();
			}
		} catch (EmptyQuestionException e) {
			// TODO do stuff if one of the questions is empty
		}
		File filename = new File(name + FILE_EXTENSION);
		try (FileOutputStream f = new FileOutputStream(filename);
				BufferedOutputStream buf = new BufferedOutputStream(f);
				ObjectOutputStream out = new ObjectOutputStream(buf)) {
			out.writeObject(title);
			out.writeObject(questions);
		} catch (IOException e) {
			//TODO handle exceptions here
		}
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
		initEditQuizPanel();
		editScreenConstraints.gridy = 1;
		editScreen.add(editQuizPanel, editScreenConstraints);
		JPanel buttons = new JPanel();
		JButton multipleChoice = new JButton("Multiple Choice");

		multipleChoice.addActionListener((e) -> {
			editQuizConstraints.gridy++;
			MultipleChoice mc = new MultipleChoice(questions.size() + 1);
			questions.add(mc);
			editQuizPanel.add(mc.getPanelEditable(), editQuizConstraints);
			editScreen.revalidate();
			editScreen.repaint();
		});

		buttons.add(multipleChoice);
		buttons.add(new JButton("Multiple Selection"));
		buttons.add(new JButton("Fill in the blank"));
		editScreenConstraints.gridy = 2;
		editScreen.add(buttons, editScreenConstraints);
		editScreenConstraints.gridy = 3;
		editScreenConstraints.fill = GridBagConstraints.NONE;
		editScreenConstraints.anchor = GridBagConstraints.CENTER;
		JButton save = new JButton("Save");
		save.setPreferredSize(new Dimension(200, 50));
		save.setMaximumSize(new Dimension(200, 50));
		editScreen.add(save, editScreenConstraints);
		editScreenConstraints.gridy = 4;
		editScreenConstraints.weighty = 1;
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
			editQuizPanel.add(item.getPanelEditable(), editQuizConstraints);
			editQuizConstraints.gridy++;
		}
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
