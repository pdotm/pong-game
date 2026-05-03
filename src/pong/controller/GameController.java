package pong.controller;

import pong.model.AiPaddle;
import pong.model.Ball;
import pong.model.GameLoop;
import pong.model.GameState;
import pong.model.Paddle;
import pong.view.MainFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** Purpose: Central coordinator that wires the Model, View, and input together. */
public class GameController implements GameLoop.TickListener {

    private final MainFrame    frame;
    private final GameState    gameState;
    private final Ball         ball;
    private final Paddle       playerPaddle;
    private final AiPaddle     aiPaddle;
    private final GameLoop     gameLoop;
    private final InputHandler inputHandler;

    public GameController() {
        frame = new MainFrame();

        int canvasW = MainFrame.WIDTH;
        int canvasH = frame.getGamePanel().getPreferredSize().height;

        gameState    = new GameState();
        ball         = new Ball(canvasW, canvasH);
        playerPaddle = new Paddle(20, canvasH);
        aiPaddle     = new AiPaddle(canvasW - 20 - Paddle.WIDTH, canvasH);
        gameLoop     = new GameLoop(this);
        inputHandler = new InputHandler();

        // Movement keys (held-state polling)
        frame.addKeyListener(inputHandler);

        // Navigation keys (Enter to start, R to restart)
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        if (gameState.getPhase() == GameState.Phase.START) startGame();
                        break;
                    case KeyEvent.VK_R:
                        if (gameState.getPhase() == GameState.Phase.GAME_OVER) restartGame();
                        break;
                }
            }
        });

        // React to phase transitions
        gameState.addPropertyChangeListener(evt -> {
            if (!GameState.PROP_PHASE.equals(evt.getPropertyName())) return;
            onPhaseChanged((GameState.Phase) evt.getNewValue());
        });

        // Keep score panel in sync with model
        gameState.addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();
            if (GameState.PROP_PLAYER_SCORE.equals(prop) || GameState.PROP_CPU_SCORE.equals(prop)) {
                frame.getScorePanel().update(gameState.getPlayerScore(), gameState.getCpuScore());
            }
        });

        frame.getGamePanel().update(ball, playerPaddle, aiPaddle);
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }

    @Override
    public void onTick() {
        // Apply player input
        if (inputHandler.isUpPressed())   playerPaddle.moveUp();
        if (inputHandler.isDownPressed()) playerPaddle.moveDown();

        // Advance ball
        ball.move();

        // Advance AI
        aiPaddle.update(ball);

        // Paddle collision — check player first, then AI
        if (ball.intersects(playerPaddle.getBounds())) {
            ball.bounceOffPaddle(playerPaddle.getBounds());
        } else if (ball.intersects(aiPaddle.getBounds())) {
            ball.bounceOffPaddle(aiPaddle.getBounds());
        }

        // Scoring — ball exits left: CPU scores; exits right: player scores
        if (ball.isOutLeft()) {
            gameState.incrementCpuScore();
            if (gameState.getPhase() == GameState.Phase.PLAYING) {
                ball.reset(1);  // serve toward CPU (right)
                aiPaddle.randomiseError();
            }
        } else if (ball.isOutRight()) {
            gameState.incrementPlayerScore();
            if (gameState.getPhase() == GameState.Phase.PLAYING) {
                ball.reset(-1); // serve toward player (left)
                aiPaddle.randomiseError();
            }
        }

        // Push latest state to canvas and repaint
        frame.getGamePanel().update(ball, playerPaddle, aiPaddle);
        frame.getGamePanel().repaint();
    }

    private void onPhaseChanged(GameState.Phase phase) {
        switch (phase) {
            case PLAYING:
                frame.showCard(MainFrame.CARD_GAME);
                gameLoop.start();
                break;
            case GAME_OVER:
                gameLoop.stop();
                frame.getGameOverScreen().setWinner(gameState.getWinner());
                frame.showCard(MainFrame.CARD_GAMEOVER);
                break;
            default:
                break;
        }
    }

    private void startGame() {
        ball.reset(1);
        playerPaddle.reset();
        aiPaddle.reset();
        aiPaddle.randomiseError();
        gameState.setPhase(GameState.Phase.PLAYING);
    }

    private void restartGame() {
        gameState.reset();  // clears scores, sets phase → START
        frame.getScorePanel().update(0, 0);
        startGame();        // immediately transitions to PLAYING
    }
}
