package fi.lyma.minesweeper.logic.event;

import fi.lyma.minesweeper.logic.MinesweeperGame;

/**
 * Listener interface for {@link MinesweeperGame} that is used to signal changes in {@link fi.lyma.minesweeper.logic.MinesweeperGame.GameStatus}
 */
public interface GameStateListener {
    void gameStatusChanged(MinesweeperGame.GameStatus gameStatus);
}
