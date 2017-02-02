package fi.lyma.minesweeper.logic;

import static org.junit.Assert.*;

import fi.lyma.minesweeper.logic.Tile;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by lyma on 24.1.2017.
 */
public class TileTest {
    Tile tile;

    @Before
    public void setup() {
        tile = new Tile(0, 0);
    }

    @Test
    public void defaultValuesAreSet() {
        assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
        assertFalse(tile.containsBomb());
    }

    @Test
    public void settersWork() {
        tile.flag();
        assertEquals(Tile.TileStatus.FLAG, tile.getStatus());
        tile.open();
        assertEquals(Tile.TileStatus.OPEN, tile.getStatus());
        tile.placeBomb();
        assertTrue(tile.containsBomb());
    }

    @Test
    public void flaggingWorks() {
        assertTrue(tile.canBeOpened());
        assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
        tile.flag();
        assertFalse(tile.canBeOpened());
        assertEquals(Tile.TileStatus.FLAG, tile.getStatus());
        tile.flag();
        assertTrue(tile.canBeOpened());
        assertEquals(Tile.TileStatus.QUESTION, tile.getStatus());
        tile.flag();
        assertEquals(Tile.TileStatus.CLOSED, tile.getStatus());
    }
}
