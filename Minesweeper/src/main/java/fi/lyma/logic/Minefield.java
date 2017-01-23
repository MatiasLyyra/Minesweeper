package fi.lyma.logic;

import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Minefield {

    private Tile[][] mines;
    private final int fieldWidth;
    private final int fieldHeight;
    private final int totalNumberOfMines;
    private boolean minesPlaced;

    public Minefield(int fieldWidth, int fieldHeight, int totalNumberOfMines) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.totalNumberOfMines = totalNumberOfMines;
        this.minesPlaced = false;
    }

    public boolean openTile(int x, int y) {
        checkForOutOfBounds(x, y);
        Tile openedTile = mines[y][x];
        if (!minesPlaced) {
            minesPlaced = true;
            placeMines(x, y);
        }
        if(openedTile.hasBomb()) {
            return false;
        }
        cascadeOpen(openedTile);
        return true;
    }

    private void checkForOutOfBounds(int x, int y) {
        if (x < 0 || x >= fieldWidth) {
            throw new IllegalArgumentException("x must be within 0-fieldWidth (upper bound exclusive)");
        }
        if (y < 0 || y >= fieldHeight) {
            throw new IllegalArgumentException("y must be within 0-fieldHeight (upper bound exclusive)");
        }
    }

    private void cascadeOpen(Tile start) {
        boolean checkedTiles[][] = new boolean[fieldHeight][fieldWidth];
        Stack<Tile> tilesToCheck = new Stack<>();
        tilesToCheck.push(start);
        while (!tilesToCheck.empty()) {
            Tile tile = tilesToCheck.pop();
            if (numberOfSurroundingMines(tile) != 0) {
                continue;
            }
            for (Tile adjacent : getAdjacentTiles(tile)) {
                if (!checkedTiles[adjacent.y][adjacent.x]) {
                    checkedTiles[adjacent.y][adjacent.x] = true;
                    tilesToCheck.push(adjacent);
                    adjacent.open();
                }
            }
        }
    }

    private boolean isInsideBounds(int x, int y) {
        return x > 0 && x < fieldWidth && y > 0 && y < fieldHeight;
    }

    public Tile getTile(int x, int y) {
        checkForOutOfBounds(x, y);
        return mines[y][x];
    }

    private void placeMines(int x, int y) {
        Random random = new Random();
        int minesToPlace = totalNumberOfMines;
        while (minesToPlace > 0) {
            int mineX = random.nextInt(fieldWidth);
            int mineY = random.nextInt(fieldHeight);
            if (!mines[y][x].hasBomb() && x != mineX && y != mineY) {
                --minesToPlace;
                mines[y][x].placeBomb();
            }
        }
    }

    private int numberOfSurroundingMines(Tile tile) {
        int numberOfMines = 0;
        List<Tile> tiles = getAdjacentTiles(tile);
        for(Tile i : tiles) {
            numberOfMines += i.hasBomb() ? 1 : 0;
        }
        return numberOfMines;
    }
    private List<Tile> getAdjacentTiles(Tile tile) {
        List<Tile> adjacentTiles = new ArrayList();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int dx = tile.x - i;
                int dy = tile.y - j;
                if(isInsideBounds(dx, dy)) {
                    adjacentTiles.add(mines[dy][dx]);
                }
            }
        }
        return adjacentTiles;
    }
}
