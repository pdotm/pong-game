package pong.view;

import pong.model.Ball;
import pong.model.Paddle;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: The main gameplay canvas (extends JPanel). */
public class GamePanel extends JPanel {

    private static final Color BG    = Color.BLACK;
    private static final Color FG    = Color.WHITE;
    private static final int   DASH  = 10;

    private Ball   ball;
    private Paddle playerPaddle;
    private Paddle aiPaddle;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(BG);
    }

    /** Pushes the latest model references before each repaint. */
    public void update(Ball ball, Paddle playerPaddle, Paddle aiPaddle) {
        this.ball         = ball;
        this.playerPaddle = playerPaddle;
        this.aiPaddle     = aiPaddle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ball == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(FG);

        // Center dashed dividing line
        int cx = getWidth() / 2;
        float[] dashPattern = { DASH, DASH };
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2.drawLine(cx, 0, cx, getHeight());
        g2.setStroke(new BasicStroke(1));

        // Paddles
        g2.fillRect(playerPaddle.getX(), playerPaddle.getY(), Paddle.WIDTH, Paddle.HEIGHT);
        g2.fillRect(aiPaddle.getX(),     aiPaddle.getY(),     Paddle.WIDTH, Paddle.HEIGHT);

        // Ball
        int bx = (int) ball.getX() - Ball.RADIUS;
        int by = (int) ball.getY() - Ball.RADIUS;
        g2.fillOval(bx, by, Ball.RADIUS * 2, Ball.RADIUS * 2);
    }
}
