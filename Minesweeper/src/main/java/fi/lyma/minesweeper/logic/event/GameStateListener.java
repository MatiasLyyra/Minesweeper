package fi.lyma.minesweeper.logic.event;

import fi.lyma.minesweeper.logic.MinesweeperGame;

/**
 * Listener interface for {@link MinesweeperGame} that is used to signal changes in {@link fi.lyma.minesweeper.logic.MinesweeperGame.GameStatus}.
 */
public interface GameStateListener {
    /**
     * Called when game's status changes with new {@link fi.lyma.minesweeper.logic.MinesweeperGame.GameStatus} as argument.
     *
     * @param gameStatus New changed status
     */
    void gameStatusChanged(MinesweeperGame.GameStatus gameStatus);
}
