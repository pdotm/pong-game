package pong.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: The splash screen shown before the game begins. */
public class StartScreen extends JPanel {

    private static final Font  TITLE_FONT  = new Font("Monospaced", Font.BOLD,  80);
    private static final Font  SUB_FONT    = new Font("Monospaced", Font.PLAIN, 16);
    private static final Font  INFO_FONT   = new Font("Monospaced", Font.BOLD,  14);
    private static final Font  PROMPT_FONT = new Font("Monospaced", Font.PLAIN, 24);
    private static final Color BG          = new Color(10, 10, 10);
    private static final Color DIM         = new Color(150, 150, 150);

    public StartScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w  = getWidth();
        int h  = getHeight();
        int cx = w / 2;
        int cy = h / 2;

        // Decorative large circles — player side (red) and CPU side (blue)
        g2.setColor(new Color(180, 30, 30, 30));
        g2.fillOval(-80, cy - 260, 520, 520);
        g2.setColor(new Color(30, 80, 200, 30));
        g2.fillOval(w - 440, cy - 260, 520, 520);

        // Smaller corner accent circles
        g2.setColor(new Color(220, 60, 60, 50));
        g2.fillOval(40, h - 160, 120, 120);
        g2.setColor(new Color(60, 120, 220, 50));
        g2.fillOval(w - 160, 40, 120, 120);

        // Title: "PO" in red, "NG" in blue
        g2.setFont(TITLE_FONT);
        FontMetrics fm   = g2.getFontMetrics();
        String      p1   = "PO";
        String      p2   = "NG";
        int         tx   = cx - fm.stringWidth(p1 + p2) / 2;
        int         ty   = cy - 50;
        g2.setColor(GamePanel.PLAYER_COLOR);
        g2.drawString(p1, tx, ty);
        g2.setColor(GamePanel.AI_COLOR);
        g2.drawString(p2, tx + fm.stringWidth(p1), ty);

        // Subtitle
        g2.setFont(SUB_FONT);
        g2.setColor(DIM);
        String sub = "1 Player  vs  CPU";
        fm = g2.getFontMetrics();
        g2.drawString(sub, cx - fm.stringWidth(sub) / 2, ty + 36);

        // Side labels with controls hint
        g2.setFont(INFO_FONT);
        fm = g2.getFontMetrics();
        String playerLabel = "[ YOU ]  ↑ / ↓  Arrow Keys";
        String cpuLabel    = "[ CPU ]";
        g2.setColor(new Color(180, 80, 80));
        g2.drawString(playerLabel, cx / 2 - fm.stringWidth(playerLabel) / 2, cy + 24);
        g2.setColor(new Color(80, 130, 200));
        g2.drawString(cpuLabel, cx + cx / 2 - fm.stringWidth(cpuLabel) / 2, cy + 24);

        // Start prompt
        g2.setFont(PROMPT_FONT);
        g2.setColor(DIM);
        String prompt = "Press ENTER to Start";
        fm = g2.getFontMetrics();
        g2.drawString(prompt, cx - fm.stringWidth(prompt) / 2, cy + 82);
    }
}
