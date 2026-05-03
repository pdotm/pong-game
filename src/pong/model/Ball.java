package pong.model;

import java.awt.Rectangle;

/** Purpose: Represents the ball in the game. */
public class Ball {

    public static final int RADIUS = 8;
    public static final double BASE_SPEED = 4.0;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private final int canvasWidth;
    private final int canvasHeight;

    public Ball(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        reset(1);
    }

    /** Moves the ball by its current velocity and bounces off top/bottom walls. */
    public void move() {
        x += dx;
        y += dy;

        if (y - RADIUS <= 0) {
            y = RADIUS;
            dy = Math.abs(dy);
        } else if (y + RADIUS >= canvasHeight) {
            y = canvasHeight - RADIUS;
            dy = -Math.abs(dy);
        }
    }

    /**
     * Bounces the ball off a paddle by reversing dx and nudging it outside
     * the paddle rectangle to prevent repeated collision triggers.
     */
    public void bounceOffPaddle(Rectangle paddleBounds) {
        dx = -dx;
        if (dx > 0) {
            x = paddleBounds.getMaxX() + RADIUS;
        } else {
            x = paddleBounds.getMinX() - RADIUS;
        }
    }

    /**
     * Returns true if the ball's bounding circle overlaps the given rectangle.
     * Uses a simple AABB-vs-circle test.
     */
    public boolean intersects(Rectangle rect) {
        double nearestX = Math.max(rect.x, Math.min(x, rect.x + rect.width));
        double nearestY = Math.max(rect.y, Math.min(y, rect.y + rect.height));
        double dx = x - nearestX;
        double dy = y - nearestY;
        return (dx * dx + dy * dy) <= (RADIUS * RADIUS);
    }

    /** Returns true if the ball has exited past the left edge of the canvas. */
    public boolean isOutLeft() {
        return x + RADIUS < 0;
    }

    /** Returns true if the ball has exited past the right edge of the canvas. */
    public boolean isOutRight() {
        return x - RADIUS > canvasWidth;
    }

    /**
     * Resets the ball to the center of the canvas.
     * @param serveDirection 1 to serve rightward (toward AI), -1 to serve leftward (toward player).
     */
    public void reset(int serveDirection) {
        x = canvasWidth / 2.0;
        y = canvasHeight / 2.0;
        dx = BASE_SPEED * serveDirection;
        dy = BASE_SPEED * (Math.random() < 0.5 ? 1 : -1);
    }

    public double getX()  { return x; }
    public double getY()  { return y; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }
}
