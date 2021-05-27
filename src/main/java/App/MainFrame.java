package App;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    private final HomeScreen hs;
    private final QuizEditScreen quizEdit;
    private final TakeQuizScreen takeQuiz;

    public MainFrame() {
        super("Quizzy");
        setMinimumSize(new Dimension(500, 300));
        setPreferredSize(new Dimension(1000, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hs = new HomeScreen();
        quizEdit = new QuizEditScreen();
        takeQuiz = new TakeQuizScreen();
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
