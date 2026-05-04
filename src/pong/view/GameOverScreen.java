package pong.view;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: The screen displayed when a side reaches 5 points. */
public class GameOverScreen extends JPanel {

    private static final Font  RESULT_FONT = new Font("Monospaced", Font.BOLD,  64);
    private static final Font  PROMPT_FONT = new Font("Monospaced", Font.PLAIN, 22);
    private static final Color BG          = new Color(10, 10, 10);
    private static final Color DIM         = new Color(150, 150, 150);

    private String resultText = "";
    private String winner     = "";

    public GameOverScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(BG);
    }

    /**
     * Sets the winner label shown on screen.
     * @param winner "Player" or "CPU" as returned by GameState.getWinner().
     */
    public void setWinner(String winner) {
        this.winner     = winner;
        this.resultText = "Player".equals(winner) ? "You Win!" : "CPU Wins!";
        repaint();
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

        Color accent = "Player".equals(winner) ? GamePanel.PLAYER_COLOR : GamePanel.AI_COLOR;

        // Subtle full-screen tint in winner's color
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 22));
        g2.fillRect(0, 0, w, h);

        // Decorative concentric rings centered on the screen
        g2.setStroke(new BasicStroke(1.5f));
        for (int r = 80; r <= 360; r += 55) {
            int alpha = Math.max(0, 65 - r / 6);
            g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), alpha));
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        }
        g2.setStroke(new BasicStroke(1));

        // Horizontal accent lines framing the result text
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 130));
        int lineW = 320;
        g2.fillRect(cx - lineW / 2, cy - 78, lineW, 2);
        g2.fillRect(cx - lineW / 2, cy + 8,  lineW, 2);

        // Result text in winner's color
        g2.setFont(RESULT_FONT);
        g2.setColor(accent);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(resultText, cx - fm.stringWidth(resultText) / 2, cy - 18);

        // Restart prompt
        g2.setFont(PROMPT_FONT);
        g2.setColor(DIM);
        String prompt = "Press R to Restart";
        fm = g2.getFontMetrics();
        g2.drawString(prompt, cx - fm.stringWidth(prompt) / 2, cy + 52);
    }
}
