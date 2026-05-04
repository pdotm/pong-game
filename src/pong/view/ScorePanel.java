package pong.view;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: Displays the current scores for both sides. */
public class ScorePanel extends JPanel {

    private static final int   BASE_SIZE  = 48;
    private static final int   POP_BONUS  = 26;   // extra font pts at peak of pop
    private static final int   POP_FRAMES = 20;   // frames the pop lasts
    private static final Color BG         = new Color(10, 10, 10);
    private static final int   HEIGHT     = 70;

    private int playerScore     = 0;
    private int cpuScore        = 0;
    private int playerPopFrames = 0;
    private int cpuPopFrames    = 0;

    private final Timer popTimer;

    public ScorePanel(int width) {
        setPreferredSize(new Dimension(width, HEIGHT));
        setBackground(BG);

        // Drives the pop animation at ~60 fps; stops itself when both counters hit 0
        popTimer = new Timer(16, e -> {
            boolean active = false;
            if (playerPopFrames > 0) { playerPopFrames--; active = true; }
            if (cpuPopFrames    > 0) { cpuPopFrames--;    active = true; }
            repaint();
            if (!active) ((Timer) e.getSource()).stop();
        });
    }

    /** Refreshes the displayed scores and triggers a pop on whichever side changed. */
    public void update(int playerScore, int cpuScore) {
        if (playerScore != this.playerScore) playerPopFrames = POP_FRAMES;
        if (cpuScore    != this.cpuScore)    cpuPopFrames    = POP_FRAMES;
        this.playerScore = playerScore;
        this.cpuScore    = cpuScore;
        if (playerPopFrames > 0 || cpuPopFrames > 0) popTimer.restart();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int mid = getWidth() / 2;
        int gap = 30;

        drawScore(g2, String.valueOf(playerScore), playerPopFrames,
                  GamePanel.PLAYER_COLOR, mid - gap, true);
        drawScore(g2, String.valueOf(cpuScore),    cpuPopFrames,
                  GamePanel.AI_COLOR,     mid + gap, false);
    }

    /**
     * Draws a single score string, scaling its font and brightening its color
     * proportionally to the remaining pop frames.
     */
    private void drawScore(Graphics2D g2, String text, int popFrames,
                           Color base, int anchorX, boolean rightAlign) {
        float t    = popFrames / (float) POP_FRAMES;          // 1.0 at peak, 0.0 at rest
        int   size = BASE_SIZE + (int) (POP_BONUS * t);
        g2.setFont(new Font("Monospaced", Font.BOLD, size));

        // Interpolate from base color toward white at peak
        float r = base.getRed()   / 255f;
        float gr = base.getGreen() / 255f;
        float b = base.getBlue()  / 255f;
        g2.setColor(new Color(
            Math.min(1f, r  + (1f - r)  * t),
            Math.min(1f, gr + (1f - gr) * t),
            Math.min(1f, b  + (1f - b)  * t)
        ));

        FontMetrics fm = g2.getFontMetrics();
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        int x = rightAlign ? anchorX - fm.stringWidth(text) : anchorX;
        g2.drawString(text, x, y);
    }
}
