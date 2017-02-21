package fi.lyma.minesweeper.logic.event;

import fi.lyma.minesweeper.logic.MinesweeperGame;

public interface GameStateListener {
    void gameStatusChanged(MinesweeperGame.GameStatus gameStatus);
}
