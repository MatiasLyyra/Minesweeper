package fi.lyma.minesweeper.logic;

import fi.lyma.minesweeper.logic.event.GameStateListener;
import fi.lyma.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MinesweeperGame is the class responsible for holding the reference to the actual {@link Minefield} object
 * and tracking the state of the game and time spent. All interactions with the field should happen through MinesweeperGame.
 */
public class MinesweeperGame {

    /**
     * Represents the state of a {@link MinesweeperGame} object.
     */
    public enum GameStatus {
        NOT_STARTED,
        STARTED,
        ENDED_WIN,
        ENDED_LOSS
    }

    /**
     * {@link Minefield} height or width can't exceed this values.
     */
    public static final int MAXIMUM_GAME_FIELD_SIDE_LENGTH = 150;

    private static final int ADJACENT_MINE_COUNT = 9;
    private Minefield minefield;
    private GameStatus gameStatus;
    private long startingTime;
    private long endingTime;
    private List<GameStateListener> listeners;

    /**
     * Works exactly same as {@link MinesweeperGame#createNewField(GameMode)}.
     * @param gameMode GameMode for construction {@link Minefield}
     * @see MinesweeperGame#createNewField(GameMode)
     */
    public MinesweeperGame(GameMode gameMode) {
        createNewField(gameMode);
        listeners = new ArrayList<>();
    }

    /**
     * Constructs {@link Minefield} with given {@link GameMode}. If the {@link GameMode} is invalid.
     * {@link GameMode#MEDIUM} is used instead.
     * GameMode is invalid if
     * <ul>
     *     <li>width or height is less or equal to 0</li>
     *     <li>width or height is greater than {@link MinesweeperGame#MAXIMUM_GAME_FIELD_SIDE_LENGTH}</li>
     *     <li>number of mines is greater than width*height-9</li>
     * </ul>
     * @param gameMode GameMode for construction {@link Minefield}
     */
    public final void createNewField(GameMode gameMode) {
        GameMode mode = isGameModeValid(gameMode) ? gameMode : GameMode.MEDIUM;
        gameStatus = GameStatus.NOT_STARTED;
        minefield = new Minefield(mode, new Random());
    }

    private boolean isGameModeValid(GameMode gameMode) {
        if (gameMode.getFieldWidth() <= 0 || gameMode.getFieldHeight() <= 0 ||
                gameMode.getFieldWidth() > MAXIMUM_GAME_FIELD_SIDE_LENGTH || gameMode.getFieldHeight() > MAXIMUM_GAME_FIELD_SIDE_LENGTH) {
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
        notifyListeners();
    }

    /**
     * Returns {@link ImmutableTile} in the specified location.
     * @param location of the tile
     * @return ImmutableTile or null if the location was outside the bounds.
     */
    public ImmutableTile getTile(Vector2D<Integer> location) {
        return minefield.getTile(location);
    }

    /**
     * Opens tile in the minefield in the specified location. Starts the game if it's not already started.
     * @param location of the tile that is tried to be opened
     * @param quickOpen false to use normal open and true to use quick open
     * @see Minefield#tryOpeningTile(Vector2D)
     * @see Minefield#tryQuickOpening(Vector2D)
     */
    public void openTile(Vector2D<Integer> location, boolean quickOpen) {
        if (isGameEnded()) {
            return;
        }
        if (GameStatus.NOT_STARTED == gameStatus) {
            startGame(location);
        }
        boolean wasMine = quickOpen ? minefield.tryQuickOpening(location) : minefield.tryOpeningTile(location);
        if (wasMine) {
            endGameLose();
        } else if (minefield.allEmptyTilesAreOpen()) {
            endGameWin();
        }
    }

    private void endGameWin() {
        gameStatus = GameStatus.ENDED_WIN;
        endGame();
    }

    private void endGameLose() {
        minefield.revealAllTiles();
        gameStatus = GameStatus.ENDED_LOSS;
        endGame();
    }

    private void endGame() {
        endingTime = System.currentTimeMillis();
        notifyListeners();
    }

    /**
     * Flags tile in specified location.
     * @param location of the tile that is tried to be flagged
     * @see Minefield#tryFlaggingTile(Vector2D)
     */
    public void flagTile(Vector2D<Integer> location) {
        minefield.tryFlaggingTile(location);
    }

    /**
     * Returns whether game has ended.
     * @return true if game status is ENDED_LOSS or ENDED_WIN, otherwise false.
     */
    public boolean isGameEnded() {
        return gameStatus == GameStatus.ENDED_LOSS || gameStatus == GameStatus.ENDED_WIN;
    }

    /**
     * Return time spent since the start of the game.
     * @return time in milliseconds since tha game started
     */
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
            default:
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

    /**
     * Return the number of potential non-flagged mines. Even if flag is placed in wrong location, it is counted.
     * @return number of non-flagged mines
     */
    public int getNumberOfMinesRemaining() {
        return getGameMode().getTotalNumberOfMines() - minefield.getNumberOfTilesFlagged();
    }

    /**
     * Returns list of the adjacent squares around specified location that are closed and not flagged.
     * @param location that specifies the adjacent tiles.
     * @return List of the adjacent tiles
     */
    public List<ImmutableTile> getAdjacentClosedNonFlaggedTiles(Vector2D<Integer> location) {
        return minefield.getAdjacentClosedNonFlaggedTiles(location);
    }

    private void notifyListeners() {
        for (GameStateListener listener : listeners) {
            listener.gameStatusChanged(gameStatus);
        }
    }

    /**
     * Adds listener to be notified for change on {@link GameStatus}.
     * @param gameStateListener listener to add
     */
    public void addGameStateListener(GameStateListener gameStateListener) {
        listeners.add(gameStateListener);
    }

    /**
     * Removes listener
     * @param gameStateListener listener to remove
     */
    public void removeGameStateListener(GameStateListener gameStateListener) {
        listeners.remove(gameStateListener);
    }
}
