package App;

import java.util.ArrayList;
import java.io.*;

public class Quiz {
	// the ArrayList to hold the questions
	private ArrayList<QuizQuestion> questions;
	// the file extension for saved quizzes
	private final static String FILE_EXTENSION = ".ser";

	/**
	 * The default constructor. Begins empty
	 */
	public Quiz() {
		questions = new ArrayList<>();
	}

	/**
	 * Creates a new Quiz object from a stored file.  
	 * NOTE: needs the annotation SuppressWarnings because we are dealing with raw data
	 *
	 * @param  f  the file that we are reading from
	 */
	@SuppressWarnings("unchecked")
	public Quiz(File f) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream in = new FileInputStream(f);
		BufferedInputStream buf = new BufferedInputStream(in);
		ObjectInputStream ois = new ObjectInputStream(buf);
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
		out.writeObject(questions);
		out.close();
	}
}
