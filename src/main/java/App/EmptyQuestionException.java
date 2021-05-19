package App;

public class EmptyQuestionException extends Exception {
	private int questionNumber;
	public EmptyQuestionException(int number) {
		super();
		questionNumber = number;
	}
	int getNumber() {
		return questionNumber;
	}
}
