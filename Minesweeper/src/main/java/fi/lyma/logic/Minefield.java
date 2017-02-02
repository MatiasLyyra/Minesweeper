package fi.lyma.logic;

import fi.lyma.util.Vector2D;

import java.util.*;

public class Minefield {

    private Tile[][] tiles;
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
        //Number of mines cannot exceed the total number of cells. Also any adjacent cell to starting position can't be mine
        if (totalNumberOfMines >= (fieldWidth * fieldHeight - 8) || totalNumberOfMines <= 0) {
            throw new IllegalArgumentException("totalNumberOfMines must be less than 'fieldWidth*fieldHeight-8' and greater than 0");
        }
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.totalNumberOfMines = totalNumberOfMines;
        this.minesPlaced = false;
        this.random = random;
        this.tilesRemaining = fieldWidth * fieldHeight - totalNumberOfMines;
        this.tiles = new Tile[fieldHeight][fieldWidth];
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                tiles[y][x] = new Tile(x, y);
            }
        }
    }

    public boolean openTile(Vector2D<Integer> location) {
        checkMinesArePlaced();
        Tile openedTile = tiles[location.getY()][location.getX()];
        boolean containsBomb = openedTile.containsBomb();
        if (openedTile.canBeOpened() && !containsBomb) {
            cascadeOpen(openedTile);
        }
        return containsBomb && openedTile.canBeOpened();
    }

    public void flagTile(Vector2D<Integer> location) {
        tiles[location.getY()][location.getX()].flag();
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


    private void checkMinesArePlaced() {
        if (!minesPlaced) {
            throw new IllegalStateException("placeMines has to be called first");
        }
    }

    public ImmutableTile getTile(Vector2D<Integer> location) {
        return tiles[location.getY()][location.getX()];
    }

    public void placeMines(Vector2D<Integer> location) {
        if (minesPlaced) {
            return;
        }
        minesPlaced = true;
        int minesToPlace = totalNumberOfMines;
        List<Tile> adjacentToStart = getAdjacentTiles(location.getX(), location.getY());
        Tile startingTile = tiles[location.getY()][location.getX()];
        while (minesToPlace > 0) {
            int mineX = random.nextInt(fieldWidth);
            int mineY = random.nextInt(fieldHeight);
            Tile mineTile = tiles[mineY][mineX];
            if (!mineTile.containsBomb() && isValidPositionForBomb(startingTile, mineTile, adjacentToStart)) {
                --minesToPlace;
                tiles[mineY][mineX].placeBomb();
            }
        }
        calculateNumberOFSurroundingMines();
    }

    private boolean isValidPositionForBomb(Tile start, Tile other, List<Tile> adjacents) {
        if (start.equals(other)) {
            return false;
        }
        for (Tile adjacent : adjacents) {
            if (other.equals(adjacent)) {
                return false;
            }
        }
        return true;
    }

    private void calculateNumberOFSurroundingMines() {
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                int numberOfMines = 0;
                for (Tile neighbour : getAdjacentTiles(x, y)) {
                    numberOfMines += neighbour.containsBomb() ? 1 : 0;
                }
                tiles[y][x].setNumberOfSurroundingMines(numberOfMines);
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
                Vector2D<Integer> adjPos = new Vector2D(x + i, y + j);
                if (isInsideBounds(adjPos)) {
                    adjacentTiles.add(tiles[adjPos.getY()][adjPos.getX()]);
                }
            }
        }
        return adjacentTiles;
    }

    public boolean isInsideBounds(Vector2D<Integer> position) {
        return position.getX() >= 0 && position.getX() < fieldWidth && position.getY() >= 0 && position.getY() < fieldHeight;
    }

    public void revealAllTiles() {
        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                tiles[y][x].open();
            }
        }
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
