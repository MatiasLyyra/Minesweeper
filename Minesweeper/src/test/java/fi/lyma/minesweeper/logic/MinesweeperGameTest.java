package fi.lyma.minesweeper.logic;

import fi.lyma.minesweeper.logic.event.GameStateListener;
import fi.lyma.util.Vector2D;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

public class MinesweeperGameTest {
    private MinesweeperGame game;

    @Before
    public void setup() {
        game = new MinesweeperGame(GameMode.MEDIUM);
    }

    @Test
    public void gameStateIsNotStarted() {
        assertEquals(MinesweeperGame.GameStatus.NOT_STARTED, game.getGameStatus());
    }

    @Test
    public void gameStartsWhenTileIsOpened() {
        game.openTile(new Vector2D(4, 4), false);
        assertEquals(MinesweeperGame.GameStatus.STARTED, game.getGameStatus());
    }

    @Test
    public void gameEndsWhenMineIsClicked() {
        game.openTile(new Vector2D<>(5, 5), false);
        ImmutableTile mineTile = null;
        mineTile = getTileWithProperty(immutableTile -> immutableTile.containsBomb());
        game.openTile(mineTile.getLocation(), false);
        assertEquals(MinesweeperGame.GameStatus.ENDED_LOSS, game.getGameStatus());
    }

    private ImmutableTile getTileWithProperty(Function<ImmutableTile, Boolean> tileFinder) {
        GameMode gameMode = game.getGameMode();
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
                ImmutableTile tile = game.getTile(new Vector2D<>(x, y));
                if (tileFinder.apply(tile)) {
                    return tile;
                }
            }
        }
        return null;
    }

    @Test
    public void invalidGameModeLoadsDefaultGameMode() {
        game.createNewField(new GameMode(0,0,10));
        assertEquals(GameMode.MEDIUM, game.getGameMode());
        game.createNewField(new GameMode(5,5,17));
        assertEquals(GameMode.MEDIUM, game.getGameMode());
    }

    @Test
    public void openingMineRevealsAllTiles() {
        game.openTile(new Vector2D<>(2,2), false);
        ImmutableTile tile = getTileWithProperty(immutableTile -> immutableTile.containsBomb());
        game.openTile(tile.getLocation(), false);
        checkTiles(Tile.TileStatus.OPEN);
    }

    private void checkTiles(Tile.TileStatus status) {
        GameMode gameMode = game.getGameMode();
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
                assertEquals(status, game.getTile(new Vector2D<>(x, y)).getStatus());
            }
        }
    }

    @Test
    public void openingAllEmptyTilesCausesWin() {
        game.openTile(new Vector2D<>(0,0), false);
        openAllEmptyTiles();
        assertEquals(MinesweeperGame.GameStatus.ENDED_WIN, game.getGameStatus());
    }

    private void openAllEmptyTiles() {
        GameMode gameMode = game.getGameMode();
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
                ImmutableTile tile = game.getTile(new Vector2D<>(x, y));
                if (!tile.containsBomb()) {
                    game.openTile(tile.getLocation(), false);
                }
            }
        }
    }

    @Test
    public void flaggingTileWorks() {
        ImmutableTile tile = game.getTile(new Vector2D<>(0,0));
        game.flagTile(tile.getLocation());
        assertEquals(Tile.TileStatus.FLAG, tile.getStatus());
    }

    @Test
    public void createNewFieldCreatesNewField() {

        GameMode gameMode = new GameMode(5, 5, 16);
        game.createNewField(gameMode);
        assertEquals(gameMode, game.getGameMode());
    }

    @Test
    public void flaggingDoesNotChangeStateWhenGameIsOver() {
        game.openTile(new Vector2D<>(0,0), false);
        ImmutableTile tile = getTileWithProperty(immutableTile -> immutableTile.containsBomb());
        game.openTile(tile.getLocation(), false);
        ImmutableTile openTile = getTileWithProperty(immutableTile -> immutableTile.getStatus() == Tile.TileStatus.OPEN);
        game.flagTile(openTile.getLocation());
        assertEquals(Tile.TileStatus.OPEN, openTile.getStatus());
    }

    @Test
    public void getTimeSpentReturnsSomewhatCorrectResult() throws InterruptedException {
        assertEquals(0, game.getTimeSpent());
        game.openTile(new Vector2D<>(0,0), false);
        Thread.sleep(500);
        assertTrue(500 <= game.getTimeSpent());
        Thread.sleep(500);
        ImmutableTile tile = getTileWithProperty(immutableTile -> immutableTile.containsBomb());
        game.openTile(tile.getLocation(), false);
        assertTrue(1000 <= game.getTimeSpent());
    }

    @Test
    public void getTimeSpentStaysSameWhenWon() throws InterruptedException {
        openAllEmptyTiles();
        long timeNow = game.getTimeSpent();
        Thread.sleep(1000);
        assertEquals(timeNow, game.getTimeSpent());
    }

    @Test
    public void notificationStartedWorks() {
        final boolean[] notificationCalled = {false};
        game.addGameStateListener(gameStatus -> {
            if (gameStatus == MinesweeperGame.GameStatus.STARTED) {
                notificationCalled[0] = true;
            }
        });
        game.openTile(new Vector2D<>(0,0), false);
        assertTrue(notificationCalled[0]);
    }

    @Test
    public void notificationEndedLossWorks() {
        final boolean[] notificationCalled = {false};
        game.addGameStateListener(gameStatus -> {
            if (gameStatus == MinesweeperGame.GameStatus.ENDED_LOSS) {
                notificationCalled[0] = true;
            }
        });
        game.openTile(new Vector2D<>(0,0), false);
        ImmutableTile bombTile = getTileWithProperty(tile -> tile.containsBomb());
        game.openTile(bombTile.getLocation(), false);
        assertTrue(notificationCalled[0]);
    }

    @Test
    public void notificationEndedWinWorks() {
        final boolean[] notificationCalled = {false};
        game.openTile(new Vector2D<>(0,0), false);
        game.addGameStateListener(gameStatus -> {
            if (gameStatus == MinesweeperGame.GameStatus.ENDED_WIN) {
                notificationCalled[0] = true;
            }
        });
        openAllEmptyTiles();
        assertTrue(notificationCalled[0]);
    }

    @Test
    public void removingNotificationsWorks() {
        final boolean[] called = {false};
        GameStateListener listener = new GameStateListener() {
            @Override
            public void gameStatusChanged(MinesweeperGame.GameStatus gameStatus) {
                called[0] = true;
            }
        };
        game.addGameStateListener(listener);
        game.removeGameStateListener(listener);
        game.openTile(new Vector2D<>(0,0), false);
        ImmutableTile bombTile = getTileWithProperty(immutableTile -> immutableTile.containsBomb());
        game.openTile(bombTile.getLocation(), false);
        assertFalse(called[0]);
    }

    @Test
    public void remainingMinesAreCountedCorrectly() {
        GameMode mode = game.getGameMode();
        assertEquals(mode.getTotalNumberOfMines(), game.getNumberOfMinesRemaining());
        game.flagTile(new Vector2D<>(0,0));
        assertEquals(mode.getTotalNumberOfMines() - 1, game.getNumberOfMinesRemaining());
        game.flagTile(new Vector2D<>(0,0));
        assertEquals(mode.getTotalNumberOfMines(), game.getNumberOfMinesRemaining());
    }

    @Test
    public void adjacentClosedTilesIsNotNull() {
        List<ImmutableTile> tiles = game.getAdjacentClosedNonFlaggedTiles(new Vector2D<>(0, 0));
        assertFalse(null == tiles);
    }
}
