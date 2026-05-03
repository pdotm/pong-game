package pong.model;

import java.awt.Rectangle;

/** Purpose: Represents a player-controlled paddle. */
public class Paddle {

    public static final int WIDTH  = 12;
    public static final int HEIGHT = 100;
    public static final int SPEED  = 5;

    protected int x;
    protected int y;
    protected final int canvasHeight;

    public Paddle(int x, int canvasHeight) {
        this.x = x;
        this.y = canvasHeight / 2 - HEIGHT / 2;
        this.canvasHeight = canvasHeight;
    }

    /** Moves the paddle up by SPEED pixels, clamped to the top wall. */
    public void moveUp() {
        y = Math.max(0, y - SPEED);
    }

    /** Moves the paddle down by SPEED pixels, clamped to the bottom wall. */
    public void moveDown() {
        y = Math.min(canvasHeight - HEIGHT, y + SPEED);
    }

    /** Returns the axis-aligned bounding rectangle used for collision detection. */
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /** Resets the paddle to the vertical center of the canvas. */
    public void reset() {
        y = canvasHeight / 2 - HEIGHT / 2;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
