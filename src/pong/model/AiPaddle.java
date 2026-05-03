package pong.model;

import java.util.Random;

/** Purpose: Represents the CPU-controlled paddle (extends Paddle). */
public class AiPaddle extends Paddle {

    /** Maximum pixels the AI paddle moves per tick. */
    private static final int AI_SPEED = 4;

    /**
     * Maximum random offset applied to the AI's target y each reset.
     * Introduces imperfection so the AI is beatable.
     */
    private static final int ERROR_RANGE = 60;

    private final Random random = new Random();

    /** Current random error offset applied to the target position. */
    private int errorOffset = 0;

    public AiPaddle(int x, int canvasHeight) {
        super(x, canvasHeight);
    }

    /**
     * Moves the paddle toward the ball's y-position (with a random error offset)
     * at a capped speed so the AI can be outplayed.
     */
    public void update(Ball ball) {
        int target = (int) ball.getY() - HEIGHT / 2 + errorOffset;

        if (y < target) {
            y = Math.min(y + AI_SPEED, target);
        } else if (y > target) {
            y = Math.max(y - AI_SPEED, target);
        }

        y = Math.max(0, Math.min(canvasHeight - HEIGHT, y));
    }

    /** Randomises the error offset. Call once per rally (e.g., after each serve). */
    public void randomiseError() {
        errorOffset = random.nextInt(ERROR_RANGE * 2 + 1) - ERROR_RANGE;
    }
}
