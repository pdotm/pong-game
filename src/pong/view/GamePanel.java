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

    private static final Color BG_LEFT    = new Color(20, 5,  5);
    private static final Color BG_RIGHT   = new Color(5,  5,  20);
    private static final Color BALL_CLR   = Color.WHITE;
    private static final Color LINE_CLR   = new Color(55, 55, 55);
    private static final Font  LABEL_FONT = new Font("Monospaced", Font.BOLD, 14);
    private static final int   DASH       = 10;

    private Ball   ball;
    private Paddle playerPaddle;
    private Paddle aiPaddle;

    /** 0 = hidden; 1–3 = display that digit during the pre-serve countdown. */
    private int countdownNumber = 0;

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
    }

    /** Pushes the latest model references before each repaint. */
    public void update(Ball ball, Paddle playerPaddle, Paddle aiPaddle) {
        this.ball         = ball;
        this.playerPaddle = playerPaddle;
        this.aiPaddle     = aiPaddle;
    }

    /** Sets the countdown digit to display (0 hides it). */
    public void setCountdown(int n) {
        this.countdownNumber = n;
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

        // Ball — fireball or plain white
        int bx = (int) ball.getX() - Ball.RADIUS;
        int by = (int) ball.getY() - Ball.RADIUS;
        if (ball.isOnFire()) {
            drawFireball(g2, bx, by);
        } else {
            g2.setColor(BALL_CLR);
            g2.fillOval(bx, by, Ball.RADIUS * 2, Ball.RADIUS * 2);
        }

        // Countdown overlay (drawn last so it appears on top)
        if (countdownNumber > 0) {
            drawCountdown(g2, w, h);
        }
    }

    /**
     * Draws the ball as a fireball: layered translucent glow rings in red/orange,
     * an orange body, and a bright yellow-white core.
     */
    private void drawFireball(Graphics2D g2, int bx, int by) {
        int cx = bx + Ball.RADIUS;
        int cy = by + Ball.RADIUS;
        int r  = Ball.RADIUS;

        // Glow layers — outermost to innermost
        int[]   glowRadii  = { 24, 18, 14, 11 };
        int[]   alphas     = {  22, 50, 85, 130 };
        Color[] glowColors = {
            new Color(255,  50,   0),
            new Color(255, 100,   0),
            new Color(255, 155,  20),
            new Color(255, 190,  50)
        };
        for (int i = 0; i < glowRadii.length; i++) {
            int gr = glowRadii[i];
            g2.setColor(new Color(
                    glowColors[i].getRed(),
                    glowColors[i].getGreen(),
                    glowColors[i].getBlue(),
                    alphas[i]));
            g2.fillOval(cx - gr, cy - gr, gr * 2, gr * 2);
        }

        // Ball body — solid orange
        g2.setColor(new Color(255, 115, 0));
        g2.fillOval(bx, by, r * 2, r * 2);

        // Inner core — bright yellow-white
        int coreR = r / 2;
        g2.setColor(new Color(255, 235, 120));
        g2.fillOval(cx - coreR, cy - coreR, coreR * 2, coreR * 2);
    }

    /**
     * Draws a semi-transparent overlay showing the pre-serve countdown digit
     * (3 = red, 2 = yellow, 1 = green) with a shadow and a "GET READY" subtext.
     */
    private void drawCountdown(Graphics2D g2, int w, int h) {
        // Dim the game behind the overlay
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRect(0, 0, w, h);

        Font numFont = new Font("Monospaced", Font.BOLD, 110);
        g2.setFont(numFont);
        FontMetrics fm = g2.getFontMetrics();
        String numStr = String.valueOf(countdownNumber);

        Color numColor = countdownNumber == 3 ? new Color(210, 60,  60)
                       : countdownNumber == 2 ? new Color(210, 170, 30)
                       :                        new Color(60,  190, 80);

        int tx = w / 2 - fm.stringWidth(numStr) / 2;
        int ty = h / 2 + (fm.getAscent() - fm.getDescent()) / 2 - 20;

        // Drop shadow
        g2.setColor(new Color(0, 0, 0, 170));
        g2.drawString(numStr, tx + 4, ty + 4);

        // Number
        g2.setColor(numColor);
        g2.drawString(numStr, tx, ty);

        // "GET READY" subtext
        Font subFont = new Font("Monospaced", Font.BOLD, 18);
        g2.setFont(subFont);
        g2.setColor(new Color(190, 190, 190, 200));
        String sub = "GET READY";
        FontMetrics sfm = g2.getFontMetrics();
        g2.drawString(sub, w / 2 - sfm.stringWidth(sub) / 2, ty + 58);
    }
}
