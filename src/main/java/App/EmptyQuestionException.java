package App;

/**
 * exception that gets thrown when a specific question is empty
 */
public class EmptyQuestionException extends Exception {
    private final int questionNumber;
    /**
     * Initializes this exception
     *
     * @param number the question number that threw this exception
     */
    public EmptyQuestionException(int number) {
        questionNumber = number;
    }
    /**
     * Returns the question number of the offending question
     *
     * @return the question number
     */
    public int getNumber() {
        return questionNumber;
    }
}
