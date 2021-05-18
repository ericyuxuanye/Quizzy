package App;

import javax.swing.*;
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

public class Quiz {
	// the ArrayList to hold the questions
	private ArrayList<QuizQuestion> questions = new ArrayList<>();
	// the file extension for saved quizzes
	private final static String FILE_EXTENSION = ".ser";

	private String title = "";

	private JPanel editScreen = null;

	private JPanel quizScreen = null;

	/**
	 * loads a quiz from a saved file
	 * NOTE: needs the annotation SuppressWarnings because we are dealing with raw data
	 *
	 * @param  f  the file that we are reading from
	 */
	@SuppressWarnings("unchecked")
	public void loadFromFile(File f) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream in = new FileInputStream(f);
		BufferedInputStream buf = new BufferedInputStream(in);
		ObjectInputStream ois = new ObjectInputStream(buf);
		title = (String)ois.readObject();
		questions = (ArrayList<QuizQuestion>)ois.readObject();
		ois.close();
	}

	/**
	* Writes the quiz to a file
	*
	* @param  name  the name of the file without the extension
	*/
	public void saveToFile(String name) throws IOException, FileNotFoundException {
		File filename = new File(name + FILE_EXTENSION);
		FileOutputStream f = new FileOutputStream(filename);
		BufferedOutputStream buf = new BufferedOutputStream(f);
		ObjectOutputStream out = new ObjectOutputStream(buf);
		out.writeObject(title);
		out.writeObject(questions);
		out.close();
	}

	/**
	 * returns a JPanel for the user to edit the quiz
	 */
	public JPanel getEditPanel() {
		if (editScreen != null) {
			return editScreen;
		}
		editScreen = new JPanel();
		editScreen.setLayout(new BoxLayout(editScreen, BoxLayout.PAGE_AXIS));
		for (QuizQuestion item : questions) {
			editScreen.add(item.getPanelEditable());
		}
		return editScreen;
	}

	/**
	 * returns a JPanel for the user to take the quiz
	 */
	public JPanel getPanel() {
		if (quizScreen != null) {
			return quizScreen;
		}
		quizScreen = new JPanel();
		quizScreen.setLayout(new BoxLayout(quizScreen, BoxLayout.PAGE_AXIS));
		for (QuizQuestion item : questions) {
			quizScreen.add(item.getPanel());
		}
		return quizScreen;
	}
}
