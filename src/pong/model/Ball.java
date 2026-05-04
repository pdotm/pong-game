package pong.model;

import java.awt.Rectangle;

/** Purpose: Represents the ball in the game. */
public class Ball {

    public static final int    RADIUS     = 8;
    public static final double BASE_SPEED = 7.0;

    private double  x;
    private double  y;
    private double  dx;
    private double  dy;
    private boolean onFire = false;

    private final int canvasWidth;
    private final int canvasHeight;

    public Ball(int canvasWidth, int canvasHeight) {
        this.canvasWidth  = canvasWidth;
        this.canvasHeight = canvasHeight;
        reset(1);
    }

    /** Moves the ball by its current velocity and bounces off top/bottom walls. */
    public void move() {
        x += dx;
        y += dy;

        if (y - RADIUS <= 0) {
            y  = RADIUS;
            dy = Math.abs(dy);
        } else if (y + RADIUS >= canvasHeight) {
            y  = canvasHeight - RADIUS;
            dy = -Math.abs(dy);
        }
    }

    /** Bounces the ball off a paddle by reversing dx and nudging it outside the paddle. */
    public void bounceOffPaddle(Rectangle paddleBounds) {
        dx = -dx;
        if (dx > 0) {
            x = paddleBounds.getMaxX() + RADIUS;
        } else {
            x = paddleBounds.getMinX() - RADIUS;
        }
    }

    /** Returns true if the ball's bounding circle overlaps the given rectangle. */
    public boolean intersects(Rectangle rect) {
        double nearestX = Math.max(rect.x, Math.min(x, rect.x + rect.width));
        double nearestY = Math.max(rect.y, Math.min(y, rect.y + rect.height));
        double diffX = x - nearestX;
        double diffY = y - nearestY;
        return (diffX * diffX + diffY * diffY) <= (RADIUS * RADIUS);
    }

    public boolean isOutLeft()  { return x + RADIUS < 0; }
    public boolean isOutRight() { return x - RADIUS > canvasWidth; }

    /** Parks the ball at center with zero velocity and clears fire state. */
    public void parkAtCenter() {
        x      = canvasWidth  / 2.0;
        y      = canvasHeight / 2.0;
        dx     = 0;
        dy     = 0;
        onFire = false;
    }

    /** Gives the ball its serve velocity. Call after parkAtCenter(). */
    public void serve(int serveDirection) {
        dx = BASE_SPEED * serveDirection;
        dy = BASE_SPEED * (Math.random() < 0.5 ? 1 : -1);
    }

    /** Convenience: parks the ball then immediately serves it. */
    public void reset(int serveDirection) {
        parkAtCenter();
        serve(serveDirection);
    }

    /**
     * Sets the ball on fire, raising its speed to 200% of BASE_SPEED for this round.
     * No-op if already on fire.
     */
    public void ignite() {
        if (!onFire) {
            onFire = true;
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            if (magnitude > 0) {
                double scale = (BASE_SPEED * 2) / magnitude;
                dx *= scale;
                dy *= scale;
            }
        }
    }

    public boolean isOnFire() { return onFire; }
    public double  getX()     { return x; }
    public double  getY()     { return y; }
    public double  getDx()    { return dx; }
    public double  getDy()    { return dy; }
}
