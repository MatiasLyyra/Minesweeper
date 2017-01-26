package fi.lyma.logic;

import java.util.Random;

public class MinesweeperGame {
    public enum GameStatus {
        NOT_STARTED,
        STARTED,
        ENDED_WIN,
        ENDED_LOSS
    }

    private Minefield minefield;
    private GameStatus status;
    private long startingTime;
    private long endingTime;

    public MinesweeperGame(int fieldWidth, int fieldHeight, int numberOfMines) {
        createNewField(fieldWidth, fieldHeight, numberOfMines);
    }

    public final void createNewField(int fieldWidth, int fieldHeight, int numberOfMines) {
        status = GameStatus.NOT_STARTED;
        minefield = new Minefield(fieldWidth, fieldHeight, numberOfMines, new Random());
    }

    private void startGame(int x, int y) {
        status = GameStatus.STARTED;
        startingTime = System.currentTimeMillis();
        minefield.placeMines(x, y);
    }

    public ImmutableTile getTile(int x, int y) {
        return minefield.getTile(x, y);
    }

    public void openTile(int x, int y) {
        if (isGameEnded()) {
            return;
        }
        if (GameStatus.NOT_STARTED == status) {
            startGame(x, y);
        }
        boolean wasMine = minefield.openTile(x, y);
        checkForGameEnd(wasMine);
    }

    private void checkForGameEnd(boolean wasMine) {
        if (wasMine) {
            status = GameStatus.ENDED_LOSS;
        } else if (minefield.allEmptyTilesAreOpen()) {
            status = GameStatus.ENDED_WIN;
        }
        endingTime = System.currentTimeMillis();
    }

    public void flagTile(int x, int y) {
        if (isGameStarted()) {
            minefield.flagTile(x, y);
        }
    }

    public boolean isGameStarted() {
        return status == GameStatus.STARTED;
    }

    public boolean isGameEnded() {
        return status != GameStatus.ENDED_LOSS || status != GameStatus.ENDED_WIN;
    }

    public long getTimeSpent() {
        long timeSpent = 0;
        switch (status) {
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
}
