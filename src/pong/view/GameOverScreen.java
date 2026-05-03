package pong.view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Purpose: The screen displayed when a side reaches 5 points. */
public class GameOverScreen extends JPanel {

    private static final Font  RESULT_FONT = new Font("Monospaced", Font.BOLD,  60);
    private static final Font  PROMPT_FONT = new Font("Monospaced", Font.PLAIN, 24);
    private static final Color BG          = Color.BLACK;
    private static final Color FG          = Color.WHITE;
    private static final Color DIM         = new Color(180, 180, 180);

    private String resultText = "";

    public GameOverScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(BG);
    }

    /**
     * Sets the winner label shown on screen.
     * @param winner "Player" or "CPU" as returned by GameState.getWinner().
     */
    public void setWinner(String winner) {
        resultText = "Player".equals(winner) ? "You Win!" : "CPU Wins!";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth()  / 2;
        int cy = getHeight() / 2;

        // Result
        g2.setFont(RESULT_FONT);
        g2.setColor(FG);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(resultText, cx - fm.stringWidth(resultText) / 2, cy - 40);

        // Restart prompt
        g2.setFont(PROMPT_FONT);
        g2.setColor(DIM);
        String prompt = "Press R to Restart";
        fm = g2.getFontMetrics();
        g2.drawString(prompt, cx - fm.stringWidth(prompt) / 2, cy + 30);
    }
}
