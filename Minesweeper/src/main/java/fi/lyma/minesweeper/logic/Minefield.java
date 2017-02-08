package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

import java.util.*;
import java.util.stream.Stream;

/**
 * Representation of the minefield. Minefield class is responsible for initializing the field,
 * opening/flagging tiles and getting information about the state of the field
 */
public class Minefield {

    private Tile[][] tiles;
    private final GameMode gameMode;
    private boolean minesPlaced;
    private int tilesRemaining;
    private int tilesFlagged;
    private Random random;

    /**
     * Constructs the Minefield to the settings provided by gameMode
     * @param gameMode Provides settings to the field
     * @param random Random object that is used to decide the locations of the mines.
     *
     * @see GameMode
     */
    public Minefield(GameMode gameMode, Random random) {
        this.tilesFlagged = 0;
        this.gameMode = gameMode;
        this.minesPlaced = false;
        this.random = random;
        this.tilesRemaining = gameMode.getFieldWidth() * gameMode.getFieldHeight() - gameMode.getTotalNumberOfMines();
        this.tiles = new Tile[gameMode.getFieldHeight()][gameMode.getFieldWidth()];
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
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
        markTileOpen(start);
        Queue<Tile> tilesToCheck = new ArrayDeque<>();
        tilesToCheck.add(start);
        while (!tilesToCheck.isEmpty()) {
            Tile tile = tilesToCheck.poll();
            if (tile.getNumberOfSurroundingMines() > 0) {
                continue;
            }
            for (Tile adjacent : getAdjacentTiles(tile.getLocation().getX(), tile.getLocation().getY())) {
                if (adjacent.getStatus() != Tile.TileStatus.OPEN) {
                    tilesToCheck.add(adjacent);
                    markTileOpen(adjacent);
                }
            }
        }
    }

    private void markTileOpen(Tile tile) {
        assert (tile.getStatus() != Tile.TileStatus.OPEN);
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

    public void placeMines(Vector2D<Integer> startingLocation) {
        if (minesPlaced) {
            return;
        }
        minesPlaced = true;
        int minesToPlace = gameMode.getTotalNumberOfMines();
        List<Tile> adjacentToStart = getAdjacentTiles(startingLocation.getX(), startingLocation.getY());
        Tile startingTile = tiles[startingLocation.getY()][startingLocation.getX()];
        while (minesToPlace > 0) {
            int mineX = random.nextInt(gameMode.getFieldWidth());
            int mineY = random.nextInt(gameMode.getFieldHeight());
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
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
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
        return position.getX() >= 0 && position.getX() < gameMode.getFieldWidth() && position.getY() >= 0 && position.getY() < gameMode.getFieldHeight();
    }

    public void revealAllTiles() {
        Arrays.stream(tiles).flatMap(Stream::of).forEach(tile -> tile.open());

    }

    public boolean allEmptyTilesAreOpen() {
        return tilesRemaining == 0;
    }

    public int getNumberOfTilesFlagged() {
        return (int) Arrays.stream(tiles).flatMap(Stream::of).filter(x -> {
            return x.getStatus() == Tile.TileStatus.FLAG;
        }).count();
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
