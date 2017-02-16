package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

import java.util.*;
import java.util.stream.Collectors;
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
     *
     * @param gameMode Provides settings to the field
     * @param random   Random object that is used to decide the locations of the mines.
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

    /**
     * Reveals a tile in the minefield. Flagged tiles are ignored. Locations outside the bounds are ignored.
     *
     * @param location
     * @return true if tile was a mine and not flagged (i.e. game should end), otherwise false.
     * @throws IllegalStateException if {@link Minefield#placeMines(Vector2D)} has not been called first
     */
    public boolean tryOpeningTile(Vector2D<Integer> location) {
        if (!minesPlaced) {
            throw new IllegalStateException("Minefield#placeMines(Vector2D) has to be called first");
        }
        if(!isInsideBounds(location)) {
            return false;
        }

        Tile openedTile = tiles[location.getY()][location.getX()];
        boolean containsBomb = openedTile.containsBomb();
        if (openedTile.canBeOpened() && !containsBomb) {
            cascadeOpen(openedTile);
        }
        return containsBomb && openedTile.canBeOpened();
    }

    public void tryFlaggingTile(Vector2D<Integer> location) {
        if (!isInsideBounds(location)) {
            return;
        }
        Tile tile = tiles[location.getY()][location.getX()];
        if (getNumberOfTilesFlagged() < gameMode.getTotalNumberOfMines() || tile.getStatus() != Tile.TileStatus.CLOSED) {
            tiles[location.getY()][location.getX()].flag();
        }
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
            for (Tile adjacent : getAdjacentTiles(tile.getLocation())) {
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

    public ImmutableTile getTile(Vector2D<Integer> location) {
        return tiles[location.getY()][location.getX()];
    }

    public void placeMines(Vector2D<Integer> startingLocation) {
        if (minesPlaced) {
            throw new IllegalStateException("Minefield#placeMines(Vector2D) has already been called.");
        }
        minesPlaced = true;
        int minesToPlace = gameMode.getTotalNumberOfMines();
        Tile startingTile = tiles[startingLocation.getY()][startingLocation.getX()];
        while (minesToPlace > 0) {
            int mineX = random.nextInt(gameMode.getFieldWidth());
            int mineY = random.nextInt(gameMode.getFieldHeight());
            Tile mineTile = tiles[mineY][mineX];
            if (!mineTile.containsBomb() && isValidPositionForBomb(startingTile, mineTile)) {
                --minesToPlace;
                tiles[mineY][mineX].placeBomb();
            }
        }
        calculateNumberOfSurroundingMines();
    }

    private boolean isValidPositionForBomb(Tile start, Tile other) {
        for (Tile adjacent : getAdjacentTiles(start.getLocation())) {
            if (other.equals(adjacent)) {
                return false;
            }
        }
        return !start.equals(other);
    }

    private void calculateNumberOfSurroundingMines() {
        for (int x = 0; x < gameMode.getFieldWidth(); ++x) {
            for (int y = 0; y < gameMode.getFieldHeight(); ++y) {
                int numberOfMines = 0;
                for (Tile neighbour : getAdjacentTiles(new Vector2D<Integer>(x, y))) {
                    numberOfMines += neighbour.containsBomb() ? 1 : 0;
                }
                tiles[y][x].setNumberOfSurroundingMines(numberOfMines);
            }
        }
    }

    private List<Tile> getAdjacentTiles(Vector2D<Integer> location) {
        List<Tile> adjacentTiles = new ArrayList();
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                if (x == 0 && y == 0) {
                    continue;
                }
                Vector2D<Integer> adjacentLocation = new Vector2D(location.getX() + x, location.getY() + y);
                if (isInsideBounds(adjacentLocation)) {
                    adjacentTiles.add(tiles[adjacentLocation.getY()][adjacentLocation.getX()]);
                }
            }
        }
        return adjacentTiles;
    }

    public List<ImmutableTile> getAdjacentClosedNonFlaggedTiles(Vector2D<Integer> location) {
        return getAdjacentTiles(location)
                .stream().filter(tile -> tile.getStatus() == Tile.TileStatus.CLOSED && tile.getStatus() != Tile.TileStatus.FLAG)
                .collect(Collectors.toList());
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
        return (int) Arrays.stream(tiles).flatMap(Stream::of).filter(x -> x.getStatus() == Tile.TileStatus.FLAG).count();
    }

    public boolean quickOpen(Vector2D<Integer> location) {
        if (!isInsideBounds(location)) {
            return false;
        }
        Tile selectedTile = tiles[location.getY()][location.getX()];
        List<Tile> adjacentTiles = getAdjacentTiles(location);
        int surroundingFlaggedTiles = (int) adjacentTiles.stream().filter(tile -> tile.getStatus() == Tile.TileStatus.FLAG).count();
        //Adjacent tiles can be quick opened only when the number of flagged tile equals number of surrounding mines
        if (selectedTile.getStatus() != Tile.TileStatus.OPEN || surroundingFlaggedTiles != selectedTile.getNumberOfSurroundingMines()) {
            return false;
        }
        boolean gameEnded = false;
        for (Tile adjacent : adjacentTiles) {
            gameEnded = gameEnded || tryOpeningTile(adjacent.getLocation());
        }
        return gameEnded;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}