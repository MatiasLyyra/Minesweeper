package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

import java.util.Random;

/**
 * MinesweeperGame is the class responsible for holding the reference to the actual {@link Minefield} object
 * and tracking the state of the game and time spent. All interactions with the field should happen through MinesweeperGame.
 */
public class MinesweeperGame {

    /**
     * Represents the state of a {@link MinesweeperGame} object
     */
    public enum GameStatus {
        NOT_STARTED,
        STARTED,
        ENDED_WIN,
        ENDED_LOSS
    }

    /**
     * Default game mode that is used when invalid game mode is provided
     */
    public static final GameMode DEFAULT_GAME_MODE = new GameMode(16, 16, 70);

    /**
     * {@link Minefield} height or width can't exceed this values
     */
    public static final int MAXIMUM_GAME_FIELD_SIDE_LENGTH = 150;

    private static final int ADJACENT_MINE_COUNT = 9;
    private Minefield minefield;
    private GameStatus gameStatus;
    private long startingTime;
    private long endingTime;


    /**
     * Works exactly same as {@link MinesweeperGame#createNewField(GameMode)}
     *
     * @param gameMode GameMode for construction {@link Minefield}
     *
     * @see MinesweeperGame#createNewField(GameMode)
     */
    public MinesweeperGame(GameMode gameMode) {
        createNewField(gameMode);
    }

    /**
     * Constructs {@link Minefield} with given {@link GameMode}. If the {@link GameMode} is invalid
     * {@link MinesweeperGame#DEFAULT_GAME_MODE} is used instead.
     * GameMode is invalid if
     * <ul>
     *     <li>width or height is less or equal to 0</li>
     *     <li>width or height is greater than {@link MinesweeperGame#MAXIMUM_GAME_FIELD_SIDE_LENGTH}</li>
     *     <li>number of mines is greater than width*height-9</li>
     * </ul>
     * @param gameMode GameMode for construction {@link Minefield}
     */
    public final void createNewField(GameMode gameMode) {
        GameMode mode = isGameModeValid(gameMode) ? gameMode : DEFAULT_GAME_MODE;
        gameStatus = GameStatus.NOT_STARTED;
        minefield = new Minefield(mode, new Random());
    }

    private boolean isGameModeValid(GameMode gameMode) {
        if (gameMode.getFieldWidth() <= 0 || gameMode.getFieldHeight() <= 0 ||
                gameMode.getFieldWidth() > MAXIMUM_GAME_FIELD_SIDE_LENGTH || gameMode.getFieldWidth() > MAXIMUM_GAME_FIELD_SIDE_LENGTH) {
            return false;

        }
        //Number of mines cannot exceed the total number of cells. Also any adjacent cell to starting position can't be mine
        if (gameMode.getTotalNumberOfMines() > (gameMode.getFieldWidth() * gameMode.getFieldHeight() - ADJACENT_MINE_COUNT) || gameMode.getTotalNumberOfMines() <= 0) {
            return false;
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
        if (!minefield.isInsideBounds(location) || isGameEnded()) {
            return;
        }
        minefield.flagTile(location);
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

    public GameMode getGameMode() {
        return minefield.getGameMode();
    }

    public int getNumberOfMinesRemaining() {
        return getGameMode().getTotalNumberOfMines() - minefield.getNumberOfTilesFlagged();
    }
}
