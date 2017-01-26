package fi.lyma.logic;

import java.util.*;

public class Minefield {

    private Tile[][] mines;
    private final int fieldWidth;
    private final int fieldHeight;
    private final int totalNumberOfMines;
    private boolean minesPlaced;
    private int tilesRemaining;
    private Random random;

    public Minefield(int fieldWidth, int fieldHeight, int totalNumberOfMines, Random random) {
        if (fieldWidth <= 0 || fieldHeight <= 0) {
            throw new IllegalArgumentException("fieldWidth and fieldHeight must be greater than 0");
        }
        if (totalNumberOfMines >= fieldWidth * fieldHeight || totalNumberOfMines <= 0) {
            throw new IllegalArgumentException("totalNumberOfMines must be less than fieldWidth*fieldHeight and greater than 0");
        }
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.totalNumberOfMines = totalNumberOfMines;
        this.minesPlaced = false;
        this.random = random;
        this.tilesRemaining = fieldWidth * fieldHeight - totalNumberOfMines;
        this.mines = new Tile[fieldHeight][fieldWidth];
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                mines[y][x] = new Tile(x, y);
            }
        }
    }

    public boolean openTile(int x, int y) {
        checkMinesArePlaced();
        throwExceptionIfOutOfBounds(x, y);
        Tile openedTile = mines[y][x];
        boolean containsBomb = openedTile.containsBomb();
        if (openedTile.canBeOpened() && !containsBomb) {
            cascadeOpen(openedTile);
        }
        return containsBomb;
    }

    public void flagTile(int x, int y) {
        throwExceptionIfOutOfBounds(x, y);
        mines[y][x].flag();
    }

    private void cascadeOpen(Tile start) {
        boolean checkedTiles[][] = new boolean[fieldHeight][fieldWidth];
        checkedTiles[start.getY()][start.getX()] = true;
        markTileOpen(start);

        Queue<Tile> tilesToCheck = new ArrayDeque<>();
        tilesToCheck.add(start);
        while (!tilesToCheck.isEmpty()) {
            Tile tile = tilesToCheck.poll();
            if (tile.getNumberOfSurroundingMines() > 0) {
                continue;
            }
            for (Tile adjacent : getAdjacentTiles(tile.getX(), tile.getY())) {
                if (!checkedTiles[adjacent.getY()][adjacent.getX()]) {
                    checkedTiles[adjacent.getY()][adjacent.getX()] = true;
                    tilesToCheck.add(adjacent);
                    markTileOpen(adjacent);
                }
            }
        }
    }

    private void markTileOpen(Tile tile) {
        tilesRemaining--;
        tile.open();
    }

    private boolean isInsideBounds(int x, int y) {
        return x >= 0 && x < fieldWidth && y >= 0 && y < fieldHeight;
    }

    private void throwExceptionIfOutOfBounds(int x, int y) {
        if (!isInsideBounds(x, y)) {
            throw new IllegalArgumentException("x and y must be inside the bounds");
        }
    }

    private void checkMinesArePlaced() {
        if (!minesPlaced) {
            throw new IllegalStateException("placeMines has to be called first");
        }
    }

    public ImmutableTile getTile(int x, int y) {
        throwExceptionIfOutOfBounds(x, y);
        return mines[y][x];
    }

    public void placeMines(int x, int y) {
        if (minesPlaced) {
            return;
        }
        throwExceptionIfOutOfBounds(x, y);
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
        calculateNumberOFSurroundingMines();
    }

    private void calculateNumberOFSurroundingMines() {
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                int numberOfMines = 0;
                for (Tile neighbour : getAdjacentTiles(x, y)) {
                    numberOfMines += neighbour.containsBomb() ? 1 : 0;
                }
                mines[y][x].setNumberOfSurroundingMines(numberOfMines);
            }
        }
    }

    private List<Tile> getAdjacentTiles(int x, int y) {
        List<Tile> adjacentTiles = new ArrayList();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int dx = x + i;
                int dy = y + j;
                if (isInsideBounds(dx, dy)) {
                    adjacentTiles.add(mines[dy][dx]);
                }
            }
        }
        return adjacentTiles;
    }

    public boolean allEmptyTilesAreOpen() {
        return tilesRemaining == 0;
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
