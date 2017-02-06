package fi.lyma.minesweeper.logic;

/**
 * Created by Matias-PC on 6.2.2017.
 */
public class GameMode {
    private final int fieldWidth, fieldHeight, totalNumberOfMines;

    public GameMode(int fieldWidth, int fieldHeight, int totalNumberOfMines) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.totalNumberOfMines = totalNumberOfMines;
    }

    public int getTotalNumberOfMines() {
        return totalNumberOfMines;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }
}
