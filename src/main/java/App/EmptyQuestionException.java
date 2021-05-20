package App;

/**
 * exception that gets thrown when a specific question is empty
 */
public class EmptyQuestionException extends Exception {
	private int questionNumber;
	public EmptyQuestionException(int number) {
		questionNumber = number;
	}
	public int getNumber() {
		return questionNumber;
	}
}
