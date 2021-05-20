package App;

import javax.swing.*;
import java.awt.*;

public class QuizEditScreen extends JPanel {
	private Quiz quiz;
	public QuizEditScreen() {
		super(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		JButton back = new JButton("Back");
		// back goes back when clicked
		back.addActionListener(App::editQuiz);
		top.add(back, BorderLayout.WEST);
		JLabel titleScreen = new JLabel("Edit Quiz", JLabel.CENTER);
		top.add(titleScreen, BorderLayout.CENTER);
		top.add(new JButton("Load from file"), BorderLayout.EAST);
		add(top, BorderLayout.NORTH);
		quiz = new Quiz();
		JScrollPane sp = new JScrollPane(quiz.getEditPanel());
		sp.setBorder(BorderFactory.createEmptyBorder());
		add(sp, BorderLayout.CENTER);
	}
}
