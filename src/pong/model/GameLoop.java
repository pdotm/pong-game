package pong.model;

import javax.swing.Timer;

/** Purpose: Drives the fixed-timestep game loop at ~60 fps. */
public class GameLoop {

    private static final int TICK_MS = 1000 / 60;

    /** Called once per tick so the Controller can update the model and repaint. */
    public interface TickListener {
        void onTick();
    }

    private final Timer timer;

    public GameLoop(TickListener listener) {
        timer = new Timer(TICK_MS, e -> listener.onTick());
        timer.setCoalesce(true);
    }

    /** Starts the game loop. */
    public void start() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    /** Stops the game loop. */
    public void stop() {
        timer.stop();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }
}
