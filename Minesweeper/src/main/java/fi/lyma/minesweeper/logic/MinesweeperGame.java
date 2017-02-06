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

    public static final GameMode DEFAULT_GAME_MODE = new GameMode(16,16, 70);
    private Minefield minefield;
    private GameStatus gameStatus;
    private long startingTime;
    private long endingTime;

    public MinesweeperGame(GameMode gameMode) {
        createNewField(gameMode);
    }

    public final void createNewField(GameMode gameMode) {
        GameMode mode = isGameModeValid(gameMode) ? gameMode : DEFAULT_GAME_MODE;
        gameStatus = GameStatus.NOT_STARTED;
        minefield = new Minefield(mode, new Random());
    }

    private boolean isGameModeValid(GameMode gameMode) {
        if (gameMode.getFieldWidth() <= 0 || gameMode.getFieldHeight() <= 0) {
            return false;
            //throw new IllegalArgumentException("fieldWidth and fieldHeight must be greater than 0");
        }
        //Number of mines cannot exceed the total number of cells. Also any adjacent cell to starting position can't be mine
        if (gameMode.getTotalNumberOfMines() >= (gameMode.getFieldWidth() * gameMode.getFieldHeight() - 8) || gameMode.getTotalNumberOfMines() <= 0) {
            return false;
            //throw new IllegalArgumentException("totalNumberOfMines must be less than 'fieldWidth*fieldHeight-8' and greater than 0");
        }
        return true;
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

    public int getNumberOfMinesRemaining() {
        return minefield.getTotalNumberOfMines() - minefield.getNumberOfTilesFlagged();
    }
}
