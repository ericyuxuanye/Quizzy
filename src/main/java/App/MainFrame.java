package App;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    private HomeScreen hs;
    private QuizEditScreen quizEdit;

    public MainFrame() {
        super("Quiz App");
        setMinimumSize(new Dimension(500, 300));
        setPreferredSize(new Dimension(1000, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hs = new HomeScreen();
        quizEdit = new QuizEditScreen();
        setContentPane(hs);
        pack();
        setVisible(true);
    }

    public void home() {
        setContentPane(hs);
        revalidate();
        repaint();
    }
    public void editQuiz() {
        setContentPane(quizEdit);
        revalidate();
        repaint();
    }
}
