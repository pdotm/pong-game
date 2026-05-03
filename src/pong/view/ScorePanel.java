package pong.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: Displays the current scores for both sides. */
public class ScorePanel extends JPanel {

    private static final Font  SCORE_FONT = new Font("Monospaced", Font.BOLD, 48);
    private static final Color BG         = Color.BLACK;
    private static final Color FG         = Color.WHITE;
    private static final int   HEIGHT     = 70;

    private int playerScore = 0;
    private int cpuScore    = 0;

    public ScorePanel(int width) {
        setPreferredSize(new Dimension(width, HEIGHT));
        setBackground(BG);
    }

    /** Refreshes the displayed scores. Call before repaint(). */
    public void update(int playerScore, int cpuScore) {
        this.playerScore = playerScore;
        this.cpuScore    = cpuScore;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(SCORE_FONT);
        g2.setColor(FG);

        FontMetrics fm  = g2.getFontMetrics();
        int         mid = getWidth() / 2;
        int         y   = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

        String pStr = String.valueOf(playerScore);
        String cStr = String.valueOf(cpuScore);

        // Player score — right-aligned to center, with a gap
        int gap = 30;
        g2.drawString(pStr, mid - gap - fm.stringWidth(pStr), y);
        // CPU score — left-aligned from center
        g2.drawString(cStr, mid + gap, y);
    }
}
