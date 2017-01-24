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
    private final int width = 3;
    private final int height = 3;
    private final int numberOfMines = 30;

    @Before
    public void setup() {
        minefield = new Minefield(width, height, numberOfMines, new Random());
    }

    @Test(expected = IllegalArgumentException.class)
    public void placeMinesExceptionLowerBound() {
        minefield.placeMines(-1,-1);
    }

    @Test
    public void firstOpenedTileIsNotMine() {
        RandomMock mock = new RandomMock(1,1,0,0,1,1,0,1,1,0);
        minefield = new Minefield(2, 2, 3, mock);
        minefield.placeMines(1,1);
        assertFalse(minefield.getTile(1, 1).containsBomb());
    }


    @Test
    public void correctNumberOfMines() {
        RandomMock mock = new RandomMock(1,1,0,0,0,0,0,1,0,2,2,3,4,3);
        minefield = new Minefield(5,5,3, mock);
        minefield.placeMines(1,1);
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
        minefield.openTile(0,0);
    }

    @Test
    public void openTileReturnsCorrectValue() {
        RandomMock mock = new RandomMock(1,1,0,0);
        minefield = new Minefield(2,2, 2, mock);
        minefield.placeMines(0,1);
        boolean mineOnTile1 = minefield.openTile(0,1);
        boolean mineOnTile2 = minefield.openTile(1,1);
        assertTrue(mineOnTile1);
        assertFalse(mineOnTile2);
    }

    @Test
    public void cascadeOpenWorks1() {
        int[] mines = new int[48];
        int i = 0;
        for(int j = 0; j < 7; j++) {
            mines[i] = 0;
            mines[i+1] = j;
            mines[i+2] = 6;
            mines[i+3] = j;
            i += 4;
        }
        for(int j = 1; j < 6; j++) {
            mines[i] = j;
            mines[i+1] = 0;
            mines[i+2] = j;
            mines[i+3] = 6;
            i += 4;
        }
        RandomMock mock = new RandomMock(mines);
        minefield = new Minefield(7,7, 24, mock);
        minefield.placeMines(3,3);
        minefield.openTile(3,3);
        checkTiles();
    }

    @Test
    public void cascadeOpenWorks2() {
        RandomMock mock = new RandomMock(2,2,2,3,2,4,2,5,6,0,6,1,6,2,6,3);
        minefield = new Minefield(9,6, 8, mock);
        minefield.placeMines(0,5);
        minefield.openTile(0,5);
        checkTiles();
    }

    private void checkTiles() {
        for (int x = 0; x < minefield.getFieldWidth(); ++x) {
            for (int y = 0; y < minefield.getFieldHeight(); ++y) {
                Tile tile = minefield.getTile(x, y);
                if (tile.containsBomb()) {
                    assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
                } else {
                    assertEquals(Tile.TileStatus.OPEN, tile.getStatus());
                }
            }
        }
    }
}
