package fi.lyma.minesweeper.logic;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Created by Matias-PC on 17.2.2017.
 */
public class GameModeTest {

    private GameMode gameMode;
    @Before
    public void setup() {
        gameMode = new GameMode(10,15,5);
    }

    @Test
    public void equalsWorks() {
        assertTrue(gameMode.equals(new GameMode(gameMode.getFieldWidth(), gameMode.getFieldHeight(), gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(0, gameMode.getFieldHeight(), gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(gameMode.getFieldWidth(), 0, gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(gameMode.getFieldWidth(), gameMode.getFieldHeight(), 0)));
        assertEquals(gameMode.hashCode(), new GameMode(10, 15, 5).hashCode());
    }

    @Test
    public void toStringWorks() {
        assertEquals("Width: 10, Height: 15, Mines: 5", gameMode.toString());
        assertEquals("Easy", GameMode.EASY.toString());
        assertEquals("Medium", GameMode.MEDIUM.toString());
        assertEquals("Hard", GameMode.HARD.toString());
    }

}
