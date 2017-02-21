package fi.lyma.minesweeper.score;

import java.io.Serializable;

/**
 * Record for high scores. Stores player's name and time.
 */
public class ScoreRecord implements Serializable, Comparable<ScoreRecord> {
    public final String playerName;
    public final long timeSpent;

    /**
     * Construncts new ScoreRecord with give parameters.
     * @param playerName name of the player
     * @param timeSpent time spent on solving the minefield
     */
    public ScoreRecord(String playerName, long timeSpent) {
        this.playerName = playerName;
        this.timeSpent = timeSpent;
    }

    @Override
    public int compareTo(ScoreRecord o) {
        return (int) (timeSpent - o.timeSpent);
    }

}
