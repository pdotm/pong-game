package pong.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** Purpose: Translates keyboard input into paddle movement commands (extends KeyAdapter). */
public class InputHandler extends KeyAdapter {

    private boolean upPressed   = false;
    private boolean downPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:   upPressed   = true;  break;
            case KeyEvent.VK_DOWN: downPressed = true;  break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:   upPressed   = false; break;
            case KeyEvent.VK_DOWN: downPressed = false; break;
        }
    }

    public boolean isUpPressed()   { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
}
