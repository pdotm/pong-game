package pong.view;

import pong.model.Ball;
import pong.model.Paddle;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: The main gameplay canvas (extends JPanel). */
public class GamePanel extends JPanel {

    public static final Color PLAYER_COLOR = new Color(220, 60,  60);
    public static final Color AI_COLOR     = new Color(60,  120, 220);

    private static final Color BG_LEFT   = new Color(20, 5,  5);
    private static final Color BG_RIGHT  = new Color(5,  5,  20);
    private static final Color BALL_CLR  = Color.WHITE;
    private static final Color LINE_CLR  = new Color(55, 55, 55);
    private static final Font  LABEL_FONT = new Font("Monospaced", Font.BOLD, 14);
    private static final int   DASH      = 10;

    private Ball   ball;
    private Paddle playerPaddle;
    private Paddle aiPaddle;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
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

        int w  = getWidth();
        int h  = getHeight();
        int cx = w / 2;

        // Tinted background halves
        g2.setColor(BG_LEFT);
        g2.fillRect(0, 0, cx, h);
        g2.setColor(BG_RIGHT);
        g2.fillRect(cx, 0, cx, h);

        // Center dashed dividing line
        float[] dashPattern = { DASH, DASH };
        g2.setColor(LINE_CLR);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2.drawLine(cx, 0, cx, h);
        g2.setStroke(new BasicStroke(1));

        // Side labels
        g2.setFont(LABEL_FONT);
        FontMetrics fm = g2.getFontMetrics();
        String pLabel = "YOU";
        String cLabel = "CPU";
        g2.setColor(new Color(140, 55, 55));
        g2.drawString(pLabel, cx / 2 - fm.stringWidth(pLabel) / 2, 22);
        g2.setColor(new Color(50, 85, 155));
        g2.drawString(cLabel, cx + cx / 2 - fm.stringWidth(cLabel) / 2, 22);

        // Player paddle — red, rounded
        g2.setColor(PLAYER_COLOR);
        g2.fillRoundRect(playerPaddle.getX(), playerPaddle.getY(), Paddle.WIDTH, Paddle.HEIGHT, 6, 6);

        // AI paddle — blue, rounded
        g2.setColor(AI_COLOR);
        g2.fillRoundRect(aiPaddle.getX(), aiPaddle.getY(), Paddle.WIDTH, Paddle.HEIGHT, 6, 6);

        // Ball — white
        g2.setColor(BALL_CLR);
        int bx = (int) ball.getX() - Ball.RADIUS;
        int by = (int) ball.getY() - Ball.RADIUS;
        g2.fillOval(bx, by, Ball.RADIUS * 2, Ball.RADIUS * 2);
    }
}
