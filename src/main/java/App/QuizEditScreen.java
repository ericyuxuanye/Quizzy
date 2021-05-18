package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuizEditScreen extends JPanel {
	private JPanel body;
	private Quiz quiz;
	public QuizEditScreen() {
		super(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.home();
			}
		});
		top.add(back, BorderLayout.WEST);
		JPanel centerComponents = new JPanel();
		JLabel titleLabel = new JLabel("Quiz name: ");
		centerComponents.add(titleLabel);
		JTextField titleField = new JTextField(20);
		centerComponents.add(titleField);
		top.add(centerComponents, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		body = new JPanel();
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
		quiz = new Quiz();
		body.add(quiz.getPanel());
		JPanel buttons = new JPanel();
		buttons.add(new JButton("Multiple Choice"));
		buttons.add(new JButton("Multiple Selection"));
		buttons.add(new JButton("Fill in the blank"));
		body.add(buttons);
		JScrollPane sp = new JScrollPane(body);
		add(sp, BorderLayout.CENTER);
	}
}
