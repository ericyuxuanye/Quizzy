package App;

import javax.swing.*;
import java.awt.Component;

public class HomeScreen extends JPanel {
	private JButton createQuiz;
	private JButton takeQuiz;
	public HomeScreen() {
		super();
		// set layout to a boxlayout
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// init buttons
		createQuiz = new JButton("Create Quiz");
		takeQuiz = new JButton("Take Quiz");
		// init title
		JLabel title = new JLabel("<html><h1>Welcome!</h1></html>", JLabel.CENTER);
		// align buttons and title in the middle horizontally
		createQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
		takeQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		// add title
		add(title);
		// add buttons
		add(createQuiz);
		add(takeQuiz);
	}
}
