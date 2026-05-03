package pong;

import pong.controller.GameController;

import javax.swing.SwingUtilities;

/** Purpose: Application entry point. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameController::new);
    }
}
