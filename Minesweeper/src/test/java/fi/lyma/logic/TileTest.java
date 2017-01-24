package fi.lyma.logic;

import static org.junit.Assert.*;
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
}
