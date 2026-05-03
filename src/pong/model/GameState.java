package pong.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Purpose: Holds all authoritative game state. */
public class GameState {

    public static final int WINNING_SCORE = 5;

    public enum Phase { START, PLAYING, GAME_OVER }

    public static final String PROP_PHASE        = "phase";
    public static final String PROP_PLAYER_SCORE = "playerScore";
    public static final String PROP_CPU_SCORE    = "cpuScore";

    private Phase phase = Phase.START;
    private int playerScore = 0;
    private int cpuScore    = 0;
    private String winner   = null;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** Increments the player's score; transitions to GAME_OVER if winning score is reached. */
    public void incrementPlayerScore() {
        int old = playerScore;
        playerScore++;
        pcs.firePropertyChange(PROP_PLAYER_SCORE, old, playerScore);
        if (playerScore >= WINNING_SCORE) {
            winner = "Player";
            setPhase(Phase.GAME_OVER);
        }
    }

    /** Increments the CPU's score; transitions to GAME_OVER if winning score is reached. */
    public void incrementCpuScore() {
        int old = cpuScore;
        cpuScore++;
        pcs.firePropertyChange(PROP_CPU_SCORE, old, cpuScore);
        if (cpuScore >= WINNING_SCORE) {
            winner = "CPU";
            setPhase(Phase.GAME_OVER);
        }
    }

    /** Resets scores, winner, and phase back to START. */
    public void reset() {
        playerScore = 0;
        cpuScore    = 0;
        winner      = null;
        setPhase(Phase.START);
    }

    public void setPhase(Phase newPhase) {
        Phase old = this.phase;
        this.phase = newPhase;
        pcs.firePropertyChange(PROP_PHASE, old, newPhase);
    }

    public Phase  getPhase()       { return phase; }
    public int    getPlayerScore() { return playerScore; }
    public int    getCpuScore()    { return cpuScore; }
    public String getWinner()      { return winner; }

    public void addPropertyChangeListener(PropertyChangeListener l)    { pcs.addPropertyChangeListener(l); }
    public void removePropertyChangeListener(PropertyChangeListener l) { pcs.removePropertyChangeListener(l); }
}
