package App;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame {
	public MainFrame() {
		JFrame f = new JFrame("Quiz App");
		f.setMinimumSize(new Dimension(500, 300));
		f.setPreferredSize(new Dimension(1000, 600));
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		HomeScreen hs = new HomeScreen();
		f.setContentPane(hs);
		f.pack();
		f.setVisible(true);
	}
}
