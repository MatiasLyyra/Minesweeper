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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameMode gameMode = (GameMode) o;

        if (fieldWidth != gameMode.fieldWidth) {
            return false;
        }
        if (fieldHeight != gameMode.fieldHeight) {
            return false;
        }
        return totalNumberOfMines == gameMode.totalNumberOfMines;
    }

    @Override
    public int hashCode() {
        int result = fieldWidth;
        result = 31 * result + fieldHeight;
        result = 31 * result + totalNumberOfMines;
        return result;
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
