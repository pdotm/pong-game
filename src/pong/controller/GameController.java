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

    /** Ticks at ~60 fps × 1.5 seconds = 90 ticks per countdown. */
    private static final int COUNTDOWN_TICKS = 90;

    private final MainFrame    frame;
    private final GameState    gameState;
    private final Ball         ball;
    private final Paddle       playerPaddle;
    private final AiPaddle     aiPaddle;
    private final GameLoop     gameLoop;
    private final InputHandler inputHandler;

    private int countdownTicksLeft   = 0;
    private int pendingServeDirection = 1;

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
        // Handle pre-serve countdown — player may still move their paddle
        if (countdownTicksLeft > 0) {
            if (inputHandler.isUpPressed())   playerPaddle.moveUp();
            if (inputHandler.isDownPressed()) playerPaddle.moveDown();

            countdownTicksLeft--;

            if (countdownTicksLeft == 0) {
                // Countdown finished — serve the ball
                ball.serve(pendingServeDirection);
                frame.getGamePanel().setCountdown(0);
            } else {
                // 3 at ticks 89-60, 2 at 59-30, 1 at 29-1
                int displayNumber = (countdownTicksLeft - 1) / 30 + 1;
                frame.getGamePanel().setCountdown(displayNumber);
            }

            frame.getGamePanel().update(ball, playerPaddle, aiPaddle);
            frame.getGamePanel().repaint();
            return;
        }

        // Normal gameplay tick
        if (inputHandler.isUpPressed())   playerPaddle.moveUp();
        if (inputHandler.isDownPressed()) playerPaddle.moveDown();

        ball.move();
        aiPaddle.update(ball);

        // Paddle collisions — 10% chance to ignite on each bounce
        boolean bounced = false;
        if (ball.intersects(playerPaddle.getBounds())) {
            ball.bounceOffPaddle(playerPaddle.getBounds());
            bounced = true;
        } else if (ball.intersects(aiPaddle.getBounds())) {
            ball.bounceOffPaddle(aiPaddle.getBounds());
            bounced = true;
        }
        if (bounced && !ball.isOnFire() && Math.random() < 0.10) {
            ball.ignite();
        }

        // Scoring
        if (ball.isOutLeft()) {
            gameState.incrementCpuScore();
            if (gameState.getPhase() == GameState.Phase.PLAYING) {
                beginCountdown(1);   // serve toward CPU (right)
            }
        } else if (ball.isOutRight()) {
            gameState.incrementPlayerScore();
            if (gameState.getPhase() == GameState.Phase.PLAYING) {
                beginCountdown(-1);  // serve toward player (left)
            }
        }

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

    /**
     * Parks the ball at center, resets both paddles, and starts the 3-second
     * countdown before serving in the given direction.
     */
    private void beginCountdown(int serveDirection) {
        pendingServeDirection = serveDirection;
        countdownTicksLeft    = COUNTDOWN_TICKS;
        ball.parkAtCenter();
        playerPaddle.reset();
        aiPaddle.reset();
        aiPaddle.randomiseError();
        frame.getGamePanel().setCountdown(3);
    }

    private void startGame() {
        gameState.setPhase(GameState.Phase.PLAYING);  // starts the game loop
        beginCountdown(1);
    }

    private void restartGame() {
        gameState.reset();  // clears scores, sets phase → START
        frame.getScorePanel().update(0, 0);
        startGame();
    }
}
