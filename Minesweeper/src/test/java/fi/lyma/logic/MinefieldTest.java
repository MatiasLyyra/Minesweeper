package fi.lyma.logic;

import static org.junit.Assert.*;

import fi.lyma.testutil.RandomMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by lyma on 24.1.2017.
 */
public class MinefieldTest {
    private Minefield minefield;

    @Before
    public void setup() {
        minefield = new Minefield(10, 10, 50, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void placeMinesExceptionLowerBound() {
        minefield.placeMines(-1, -1);
    }

    @Test
    public void firstOpenedTileIsNotMine() {
        RandomMock mock = new RandomMock(1, 1, 0, 0, 1, 1, 0, 1, 1, 0);
        minefield = new Minefield(2, 2, 3, mock);
        minefield.placeMines(1, 1);
        assertFalse(minefield.getTile(1, 1).containsBomb());
    }


    @Test
    public void correctNumberOfMines() {
        RandomMock mock = new RandomMock(1, 1, 0, 0, 0, 0, 0, 1, 0, 2, 2, 3, 4, 3);
        minefield = new Minefield(5, 5, 3, mock);
        minefield.placeMines(1, 1);
        int mines = 0;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                mines += minefield.getTile(x, y).containsBomb() ? 1 : 0;
            }
        }
        assertEquals(3, mines);
    }

    @Test(expected = IllegalStateException.class)
    public void openTileIllegalState() {
        minefield.openTile(0, 0);
    }

    @Test
    public void openTileReturnsCorrectValue() {
        RandomMock mock = new RandomMock(1, 1, 0, 0);
        minefield = new Minefield(2, 2, 2, mock);
        minefield.placeMines(0, 1);
        boolean mineOnTile1 = minefield.openTile(0, 1);
        boolean mineOnTile2 = minefield.openTile(1, 1);
        assertTrue(mineOnTile1);
        assertFalse(mineOnTile2);
    }

    @Test
    public void openTileOpensTile() {
        RandomMock mock = new RandomMock(0, 0, 1, 0, 2, 0, 0, 1, 2, 1, 0, 2, 1, 2, 2);
        minefield = new Minefield(3, 3, 8, mock);
        minefield.placeMines(1, 1);
        minefield.openTile(1, 1);
        ImmutableTile emptyTile = minefield.getTile(1, 1);
        assertEquals(Tile.TileStatus.OPEN, emptyTile.getStatus());
    }

    @Test
    public void cascadeOpenWorks1() {
        int[] mines = new int[48];
        int i = 0;
        for (int j = 0; j < 7; j++) {
            mines[i] = 0;
            mines[i + 1] = j;
            mines[i + 2] = 6;
            mines[i + 3] = j;
            i += 4;
        }
        for (int j = 1; j < 6; j++) {
            mines[i] = j;
            mines[i + 1] = 0;
            mines[i + 2] = j;
            mines[i + 3] = 6;
            i += 4;
        }
        RandomMock mock = new RandomMock(mines);
        minefield = new Minefield(7, 7, 24, mock);
        minefield.placeMines(3, 3);
        minefield.openTile(3, 3);
        checkTiles();
    }

    @Test
    public void cascadeOpenWorks2() {
        RandomMock mock = new RandomMock(2, 2, 2, 3, 2, 4, 2, 5, 6, 0, 6, 1, 6, 2, 6, 3);
        minefield = new Minefield(9, 6, 8, mock);
        minefield.placeMines(0, 5);
        minefield.openTile(0, 5);
        checkTiles();
    }

    @Test
    public void openedTilesCountIsCorrect() {
        RandomMock mock = new RandomMock(3, 0, 3, 1, 3, 2);
        minefield = new Minefield(7, 3, 3, mock);
        minefield.placeMines(1, 1);
        minefield.openTile(1, 1);
        assertFalse(minefield.allEmptyTilesAreOpen());
        minefield.openTile(5, 1);
        assertTrue(minefield.allEmptyTilesAreOpen());
    }

    @Test
    public void minesAreCountedCorrectly() {
        RandomMock mock = new RandomMock(0,0,1,0,2,0,0,1,2,1,0,2,1,2,2,2,6,2,7,0);
        minefield = new Minefield(8, 3, 10, mock);
        minefield.placeMines(1,1);
        assertEquals(8, minefield.getTile(1, 1).getNumberOfSurroundingMines());
        assertEquals(3, minefield.getTile(3, 1).getNumberOfSurroundingMines());
        assertEquals(2, minefield.getTile(3, 2).getNumberOfSurroundingMines());
        assertEquals(0, minefield.getTile(5, 0).getNumberOfSurroundingMines());
        assertEquals(2, minefield.getTile(6, 1).getNumberOfSurroundingMines());
        assertEquals(1, minefield.getTile(7, 2).getNumberOfSurroundingMines());
    }

    private void checkTiles() {
        for (int x = 0; x < minefield.getFieldWidth(); ++x) {
            for (int y = 0; y < minefield.getFieldHeight(); ++y) {
                ImmutableTile tile = minefield.getTile(x, y);
                if (tile.containsBomb()) {
                    assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
                } else {
                    assertEquals(Tile.TileStatus.OPEN, tile.getStatus());
                }
            }
        }
    }
}
