package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

import java.util.Random;

public class MinesweeperGame {

    public enum GameStatus {
        NOT_STARTED,
        STARTED,
        ENDED_WIN,
        ENDED_LOSS
    }

    private Minefield minefield;
    private GameStatus gameStatus;
    private long startingTime;
    private long endingTime;

    public MinesweeperGame(int fieldWidth, int fieldHeight, int numberOfMines) {
        createNewField(fieldWidth, fieldHeight, numberOfMines);
    }

    public final void createNewField(int fieldWidth, int fieldHeight, int numberOfMines) {
        gameStatus = GameStatus.NOT_STARTED;
        minefield = new Minefield(fieldWidth, fieldHeight, numberOfMines, new Random());
    }

    private void startGame(Vector2D<Integer> location) {
        gameStatus = GameStatus.STARTED;
        startingTime = System.currentTimeMillis();
        minefield.placeMines(location);
    }

    public ImmutableTile getTile(Vector2D<Integer> location) {
        ImmutableTile tile = null;
        if (minefield.isInsideBounds(location)) {
            tile = minefield.getTile(location);
        }
        return tile;
    }

    public void openTile(Vector2D<Integer> location) {
        if (isGameEnded() || !minefield.isInsideBounds(location)) {
            return;
        }
        if (GameStatus.NOT_STARTED == gameStatus) {
            startGame(location);
        }
        boolean wasMine = minefield.openTile(location);
        checkForGameEnd(wasMine);
    }

    private void checkForGameEnd(boolean wasMine) {
        if (wasMine) {
            minefield.revealAllTiles();
            gameStatus = GameStatus.ENDED_LOSS;
        } else if (minefield.allEmptyTilesAreOpen()) {
            gameStatus = GameStatus.ENDED_WIN;
        }
        endingTime = System.currentTimeMillis();
    }

    public void flagTile(Vector2D<Integer> location) {
        if (!minefield.isInsideBounds(location)) {
            return;
        }
        if (!isGameEnded()) {
            minefield.flagTile(location);
        }
    }

    public boolean isGameStarted() {
        return gameStatus == GameStatus.STARTED;
    }

    public boolean isGameEnded() {
        return gameStatus == GameStatus.ENDED_LOSS || gameStatus == GameStatus.ENDED_WIN;
    }

    public long getTimeSpent() {
        long timeSpent = 0;
        switch (gameStatus) {
            case ENDED_LOSS:
            case ENDED_WIN:
                timeSpent = endingTime - startingTime;
                break;
            case STARTED:
                timeSpent = System.currentTimeMillis() - startingTime;
                break;
        }
        return timeSpent;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getFieldWidth() {
        return minefield.getFieldWidth();
    }

    public int getFieldHeight() {
        return minefield.getFieldHeight();
    }
}
