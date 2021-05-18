package App;

/*
 * Main class that runs
 */
public class App 
{
	private static MainFrame f;
	public static void main(String[] args)
	{
		f = new MainFrame();
	}

	public static void home() {
		f.home();
	}

	public static void editQuiz() {
		f.editQuiz();
	}
}
