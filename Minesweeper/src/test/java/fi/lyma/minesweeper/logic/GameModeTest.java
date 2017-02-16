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
    public void equalsToimii() {
        assertTrue(gameMode.equals(new GameMode(gameMode.getFieldWidth(), gameMode.getFieldHeight(), gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(0, gameMode.getFieldHeight(), gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(gameMode.getFieldWidth(), 0, gameMode.getTotalNumberOfMines())));
        assertFalse(gameMode.equals(new GameMode(gameMode.getFieldWidth(), gameMode.getFieldHeight(), 0)));
    }

}
