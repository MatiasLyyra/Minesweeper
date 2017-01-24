package fi.lyma.logic;

import java.util.*;

public class Minefield {

    private Tile[][] mines;
    private final int fieldWidth;
    private final int fieldHeight;
    private final int totalNumberOfMines;
    private boolean minesPlaced;
    private Random random;

    public Minefield(int fieldWidth, int fieldHeight, int totalNumberOfMines, Random random) {
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.totalNumberOfMines = totalNumberOfMines;
        this.minesPlaced = false;
        this.random = random;
        mines = new Tile[fieldHeight][fieldWidth];
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                mines[y][x] = new Tile(x, y);
            }
        }
    }

    public boolean openTile(int x, int y) {
        if(!minesPlaced) {
            throw new IllegalStateException("placeMines has to be called first");
        }
        checkForOutOfBounds(x, y);
        Tile openedTile = mines[y][x];
        if(openedTile.containsBomb()) {
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
        Queue<Tile> tilesToCheck = new ArrayDeque<>();
        tilesToCheck.add(start);
        while (!tilesToCheck.isEmpty()) {
            Tile tile = tilesToCheck.poll();
            if (numberOfSurroundingMines(tile) != 0) {
                continue;
            }
            for (Tile adjacent : getAdjacentTiles(tile)) {
                if (!checkedTiles[adjacent.y][adjacent.x]) {
                    checkedTiles[adjacent.y][adjacent.x] = true;
                    tilesToCheck.add(adjacent);
                    adjacent.open();
                }
            }
        }
    }

    private boolean isInsideBounds(int x, int y) {
        return x >= 0 && x < fieldWidth && y >= 0 && y < fieldHeight;
    }

    public Tile getTile(int x, int y) {
        checkForOutOfBounds(x, y);
        return mines[y][x];
    }

    public void placeMines(int x, int y) {
        if (minesPlaced) {
            return;
        }
        checkForOutOfBounds(x, y);
        minesPlaced = true;
        int minesToPlace = totalNumberOfMines;
        while (minesToPlace > 0) {
            int mineX = random.nextInt(fieldWidth);
            int mineY = random.nextInt(fieldHeight);
            if (!mines[mineY][mineX].containsBomb() && !(x == mineX && y == mineY)) {
                --minesToPlace;
                mines[mineY][mineX].placeBomb();
            }
        }
    }

    private int numberOfSurroundingMines(Tile center) {
        int numberOfMines = 0;
        List<Tile> tiles = getAdjacentTiles(center);
        for(Tile tile : tiles) {
            numberOfMines += tile.containsBomb() ? 1 : 0;
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
                int dx = tile.x + i;
                int dy = tile.y + j;
                if(isInsideBounds(dx, dy)) {
                    adjacentTiles.add(mines[dy][dx]);
                }
            }
        }
        return adjacentTiles;
    }
    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getTotalNumberOfMines() {
        return totalNumberOfMines;
    }
}
