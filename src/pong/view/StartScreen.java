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

    private static final Font  TITLE_FONT  = new Font("Monospaced", Font.BOLD,  72);
    private static final Font  PROMPT_FONT = new Font("Monospaced", Font.PLAIN, 24);
    private static final Color BG          = Color.BLACK;
    private static final Color FG          = Color.WHITE;
    private static final Color DIM         = new Color(180, 180, 180);

    public StartScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth()  / 2;
        int cy = getHeight() / 2;

        // Title
        g2.setFont(TITLE_FONT);
        g2.setColor(FG);
        String title = "PONG";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, cx - fm.stringWidth(title) / 2, cy - 40);

        // Prompt
        g2.setFont(PROMPT_FONT);
        g2.setColor(DIM);
        String prompt = "Press ENTER to Start";
        fm = g2.getFontMetrics();
        g2.drawString(prompt, cx - fm.stringWidth(prompt) / 2, cy + 30);
    }
}
