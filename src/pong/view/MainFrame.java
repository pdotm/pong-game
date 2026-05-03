package pong.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

/** Purpose: The top-level application window (extends JFrame). */
public class MainFrame extends JFrame {

    public static final String CARD_START    = "START";
    public static final String CARD_GAME     = "GAME";
    public static final String CARD_GAMEOVER = "GAMEOVER";

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 600;

    private final CardLayout   cardLayout = new CardLayout();
    private final JPanel       cardPanel  = new JPanel(cardLayout);

    private final StartScreen    startScreen;
    private final ScorePanel     scorePanel;
    private final GamePanel      gamePanel;
    private final GameOverScreen gameOverScreen;

    public MainFrame() {
        setTitle("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        startScreen    = new StartScreen(WIDTH, HEIGHT);
        scorePanel     = new ScorePanel(WIDTH);
        gamePanel      = new GamePanel(WIDTH, HEIGHT - scorePanel.getPreferredSize().height);
        gameOverScreen = new GameOverScreen(WIDTH, HEIGHT);

        // Gameplay card: score on top, canvas below
        JPanel gameCard = new JPanel(new BorderLayout());
        gameCard.add(scorePanel, BorderLayout.NORTH);
        gameCard.add(gamePanel,  BorderLayout.CENTER);

        cardPanel.add(startScreen,    CARD_START);
        cardPanel.add(gameCard,       CARD_GAME);
        cardPanel.add(gameOverScreen, CARD_GAMEOVER);

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);
    }

    /** Switches the visible card by name (use the CARD_* constants). */
    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }

    public StartScreen    getStartScreen()    { return startScreen; }
    public ScorePanel     getScorePanel()     { return scorePanel; }
    public GamePanel      getGamePanel()      { return gamePanel; }
    public GameOverScreen getGameOverScreen() { return gameOverScreen; }
}
