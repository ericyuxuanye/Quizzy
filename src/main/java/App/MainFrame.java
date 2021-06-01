package App;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    private final HomeScreen hs = new HomeScreen();
    private final QuizEditScreen quizEdit = new QuizEditScreen();
    private final TakeQuizScreen takeQuiz = new TakeQuizScreen();

    public MainFrame() {
        super("Quizzy");
        setMinimumSize(new Dimension(500, 300));
        setPreferredSize(new Dimension(1000, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

    public void takeQuiz() {
        setContentPane(takeQuiz);
        revalidate();
        repaint();
    }
}
