package pong.model;

import java.util.Random;

/** Purpose: Represents the CPU-controlled paddle (extends Paddle). */
public class AiPaddle extends Paddle {

    private static final int AI_SPEED = 5;

    /**
     * Maximum random error (in pixels) applied to the AI's committed target Y.
     * Because the target is fixed at the moment the ball turns toward the AI,
     * a faster paddle simply reaches the wrong position sooner — speed does not
     * cancel the error.
     */
    private static final int ERROR_RANGE = 120;

    private final Random random = new Random();

    private int     errorOffset       = 0;
    private int     committedTargetY;
    private boolean ballHeadingToAi   = false;

    public AiPaddle(int x, int canvasHeight) {
        super(x, canvasHeight);
        committedTargetY = canvasHeight / 2 - HEIGHT / 2;
    }

    /**
     * Each tick:
     *  - Ball turning toward AI  → commit once to a predicted (possibly wrong) landing Y.
     *  - Ball heading away       → drift back to center so the paddle is ready for the next rally.
     */
    public void update(Ball ball) {
        boolean headingToAi = ball.getDx() > 0;

        if (headingToAi && !ballHeadingToAi) {
            // Ball just turned toward the AI — lock in a (possibly wrong) destination
            randomiseError();
            committedTargetY = predictTargetY(ball);
            ballHeadingToAi = true;
        } else if (!headingToAi) {
            // Ball heading away — return to center as the neutral ready position
            committedTargetY = canvasHeight / 2 - HEIGHT / 2;
            ballHeadingToAi = false;
        }

        if (y < committedTargetY) {
            y = Math.min(y + AI_SPEED, committedTargetY);
        } else if (y > committedTargetY) {
            y = Math.max(y - AI_SPEED, committedTargetY);
        }

        y = Math.max(0, Math.min(canvasHeight - HEIGHT, y));
    }

    /**
     * Projects where the ball will be (in Y) when it reaches the AI paddle's X.
     * Simulates wall bounces using modular folding so the result is always within
     * the canvas — preventing the paddle from being committed to an out-of-bounds target.
     * Applies the random error offset after the bounce simulation.
     */
    private int predictTargetY(Ball ball) {
        double timeToReach = (x - ball.getX()) / ball.getDx();
        double rawY = ball.getY() + ball.getDy() * timeToReach;

        // Fold rawY into [RADIUS, canvasHeight - RADIUS] to simulate wall bounces
        double lo    = Ball.RADIUS;
        double hi    = canvasHeight - Ball.RADIUS;
        double range = hi - lo;

        double normalized = rawY - lo;
        double mod = normalized % (2 * range);
        if (mod < 0) mod += 2 * range;

        double bouncedY = (mod <= range) ? lo + mod : hi - (mod - range);

        return (int) bouncedY - HEIGHT / 2 + errorOffset;
    }

    /** Picks a new random error for the next approach. */
    public void randomiseError() {
        errorOffset = random.nextInt(ERROR_RANGE * 2 + 1) - ERROR_RANGE;
    }
}
