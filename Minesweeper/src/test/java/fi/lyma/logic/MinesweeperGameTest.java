package fi.lyma.logic;

import fi.lyma.util.Vector2D;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MinesweeperGameTest {
    private MinesweeperGame game;

    @Before
    public void setup() {
        game = new MinesweeperGame(10, 10, 35);
    }

    @Test
    public void gameStateIsNotStarted() {
        assertEquals(MinesweeperGame.GameStatus.NOT_STARTED, game.getGameStatus());
    }

    @Test
    public void gameStartsWhenTileIsOpened() {
        game.openTile(new Vector2D(4, 4));
        assertEquals(MinesweeperGame.GameStatus.STARTED, game.getGameStatus());
    }

    @Test
    public void gameEndsWhenMineIsClicked() {
        game.openTile(new Vector2D<>(5, 5));
        ImmutableTile mineTile = null;
        for (int x = 0; x < game.getFieldWidth(); ++x) {
            for (int y = 0; y < game.getFieldHeight(); ++y) {
                ImmutableTile tile = game.getTile(new Vector2D<>(x, y));
                if (tile.containsBomb()) {
                    mineTile = tile;
                    break;
                }
            }
        }
        game.openTile(new Vector2D<>(mineTile.getX(), mineTile.getY()));
        assertEquals(MinesweeperGame.GameStatus.ENDED_LOSS, game.getGameStatus());
    }


}
