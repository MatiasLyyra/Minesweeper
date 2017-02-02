package fi.lyma.logic;

import static org.junit.Assert.*;

import fi.lyma.testutil.RandomMock;
import fi.lyma.util.Vector2D;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class MinefieldTest {
    private Minefield minefield;

    @Before
    public void setup() {
        minefield = new Minefield(10, 10, 50, new Random());
    }


    @Test
    public void firstOpenedTileIsNotMine() {
        RandomMock mock = new RandomMock(0, 0, 2, 2, 0, 1, 0, 2, 0, 3, 3, 1, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4, 4, 3, 4, 2, 4, 1, 4, 0, 3, 0, 2, 0, 1, 2, 1, 0);
        minefield = new Minefield(5, 5, 16, mock);
        minefield.placeMines(new Vector2D(2, 2));
        assertFalse(minefield.getTile(new Vector2D(2, 2)).containsBomb());
        assertFalse(minefield.getTile(new Vector2D(3, 1)).containsBomb());
        assertFalse(minefield.getTile(new Vector2D(1, 2)).containsBomb());
    }


    @Test
    public void correctNumberOfMines() {
        RandomMock mock = new RandomMock(2, 1, 3, 1, 4, 1, 4, 2, 4, 3, 3, 3, 2, 3, 2, 2, 0, 0, 1, 2, 2, 4, 5, 4, 5, 1, 5, 4);
        minefield = new Minefield(6, 6, 5, mock);
        minefield.placeMines(new Vector2D(2, 2));
        int mines = 0;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                mines += minefield.getTile(new Vector2D(x, y)).containsBomb() ? 1 : 0;
            }
        }
        assertEquals(5, mines);
    }

    @Test
    public void openTileReturnsCorrectValue() {
        RandomMock mock = new RandomMock(0, 0, 4, 3);
        minefield = new Minefield(5, 5, 2, mock);
        minefield.placeMines(new Vector2D(2, 2));
        boolean mineOnTile1 = minefield.openTile(new Vector2D(2, 2));
        boolean mineOnTile2 = minefield.openTile(new Vector2D(0, 0));
        boolean mineOnTile3 = minefield.openTile(new Vector2D(4, 3));
        assertFalse(mineOnTile1);
        assertTrue(mineOnTile2);
        assertTrue(mineOnTile3);
    }

    @Test
    public void openTileOpensTile() {
        RandomMock mock = new RandomMock(3, 0, 4, 0, 5, 0, 5, 1, 5, 2, 4, 2, 3, 2, 3, 1);
        minefield = new Minefield(6, 3, 8, mock);
        minefield.placeMines(new Vector2D(0, 1));
        minefield.openTile(new Vector2D(0, 1));
        ImmutableTile emptyTile = minefield.getTile(new Vector2D(0, 1));
        assertEquals(Tile.TileStatus.OPEN, emptyTile.getStatus());

        minefield.openTile(new Vector2D(4, 1));
        ImmutableTile emptyTile2 = minefield.getTile(new Vector2D(4, 1));
        assertEquals(Tile.TileStatus.OPEN, emptyTile2.getStatus());

        minefield.openTile(new Vector2D(5, 2));
        ImmutableTile mineTile = minefield.getTile(new Vector2D(5, 2));
        assertEquals(Tile.TileStatus.OPEN, emptyTile2.getStatus());


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
        minefield.placeMines(new Vector2D(3, 3));
        minefield.openTile(new Vector2D(3, 3));
        checkTiles();
    }

    @Test
    public void cascadeOpenWorks2() {
        RandomMock mock = new RandomMock(2, 2, 2, 3, 2, 4, 2, 5, 6, 0, 6, 1, 6, 2, 6, 3);
        minefield = new Minefield(9, 6, 8, mock);
        minefield.placeMines(new Vector2D(0, 5));
        minefield.openTile(new Vector2D(0, 5));
        checkTiles();
    }

    @Test
    public void openedTilesCountIsCorrect() {
        RandomMock mock = new RandomMock(3, 0, 3, 1, 3, 2);
        minefield = new Minefield(7, 3, 3, mock);
        minefield.placeMines(new Vector2D(1, 1));
        minefield.openTile(new Vector2D(1, 1));
        assertFalse(minefield.allEmptyTilesAreOpen());
        minefield.openTile(new Vector2D(5, 1));
        assertTrue(minefield.allEmptyTilesAreOpen());
    }

    @Test
    public void minesAreCountedCorrectly() {
        RandomMock mock = new RandomMock(0, 0, 1, 0, 2, 0, 0, 1, 2, 1, 0, 2, 1, 2, 2, 2, 6, 2, 7, 0);
        minefield = new Minefield(8, 3, 10, mock);
        minefield.placeMines(new Vector2D(4, 2));
        assertEquals(8, minefield.getTile(new Vector2D(1, 1)).getNumberOfSurroundingMines());
        assertEquals(3, minefield.getTile(new Vector2D(3, 1)).getNumberOfSurroundingMines());
        assertEquals(2, minefield.getTile(new Vector2D(3, 2)).getNumberOfSurroundingMines());
        assertEquals(0, minefield.getTile(new Vector2D(5, 0)).getNumberOfSurroundingMines());
        assertEquals(2, minefield.getTile(new Vector2D(6, 1)).getNumberOfSurroundingMines());
        assertEquals(1, minefield.getTile(new Vector2D(7, 2)).getNumberOfSurroundingMines());
    }

    private void checkTiles() {
        for (int x = 0; x < minefield.getFieldWidth(); ++x) {
            for (int y = 0; y < minefield.getFieldHeight(); ++y) {
                ImmutableTile tile = minefield.getTile(new Vector2D(x, y));
                if (tile.containsBomb()) {
                    assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
                } else {
                    assertEquals(Tile.TileStatus.OPEN, tile.getStatus());
                }
            }
        }
    }

    @Test
    public void flaggingTileWorks() {
        Vector2D<Integer> pos = new Vector2D<>(0, 0);
        minefield.placeMines(pos);
        minefield.flagTile(pos);
        assertEquals(Tile.TileStatus.FLAG, minefield.getTile(pos).getStatus());
        minefield.flagTile(pos);
        assertEquals(Tile.TileStatus.QUESTION, minefield.getTile(pos).getStatus());
        minefield.flagTile(pos);
        assertEquals(Tile.TileStatus.CLOSED, minefield.getTile(pos).getStatus());
    }

    @Test
    public void cannotFlagOpenTile() {
        Vector2D<Integer> pos = new Vector2D<>(0, 0);
        minefield.placeMines(pos);
        minefield.openTile(pos);
        minefield.flagTile(pos);
        assertEquals(Tile.TileStatus.OPEN, minefield.getTile(pos).getStatus());
    }

    //Tests for invalid input

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentZeroWidth() {
        minefield = new Minefield(0, 10, 8, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentZeroHeight() {
        minefield = new Minefield(10, 0, 8, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentNegativeNumberOfMines() {
        minefield = new Minefield(10, 0, -1, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentEqualNumberOfTilesAndMines() {
        minefield = new Minefield(10, 10, 100, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIllegalArgumentZeroMines() {
        minefield = new Minefield(10, 10, 0, new Random());
    }

    @Test(expected = IllegalStateException.class)
    public void openTileIllegalState() {
        minefield.openTile(new Vector2D<Integer>(0, 0));
    }

}
