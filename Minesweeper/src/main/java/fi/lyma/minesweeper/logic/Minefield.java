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
     * Constructs the Minefield to the settings provided by gameMode.
     *
     * @param gameMode Provides settings to the field
     * @param random   Random object that is used to decide the locations of the mines
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
     * Tries to reveal a tile in the minefield in specified location.
     * Flagged and open tiles are ignored. Locations outside the bounds are ignored.
     *
     * @param location Location of the tile that is tried to be opened
     * @return True if tile was a mine and not flagged (i.e. game should end), otherwise false
     * @throws IllegalStateException if {@link Minefield#placeMines(Vector2D)} has not been called first
     */
    public boolean tryOpeningTile(Vector2D<Integer> location) {
        if (!minesPlaced) {
            throw new IllegalStateException("Minefield#placeMines(Vector2D) has to be called first");
        }

        if (!isInsideBounds(location)) {
            return false;
        }

        Tile openedTile = tiles[location.getY()][location.getX()];
        boolean containsBomb = openedTile.containsBomb();
        if (openedTile.canBeOpened() && !containsBomb) {
            cascadeOpen(openedTile);
        }
        return containsBomb && openedTile.canBeOpened();
    }

    /**
     * Tries to toggle flag/question tile in specified location. Tile can be only flagged if it's closed.
     * Toggle rotation follows FLAG -> QUESTION -> CLOSED -> FLAG. Number of flags cannot exceed the number of bombs in the minefield.
     *
     * @param location Location of the tile that is tried to be flagged.
     */
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

    /**
     * Returns {@link Tile} in specified location as {@link ImmutableTile}.
     *
     * @param location of the tile
     * @return Tile as {@link ImmutableTile} or null if called outside the bounds
     */
    public ImmutableTile getTile(Vector2D<Integer> location) {
        if (!isInsideBounds(location)) {
            return null;
        }
        return tiles[location.getY()][location.getX()];
    }

    /**
     * Places the mines to the minefield so that there are no mines in startingLocation or it's adjacent squares.
     *
     * @param startingLocation Starting location i.e. first opened tile
     */
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

    /**
     * Returns list of the adjacent squares around specified location that are closed and not flagged.
     *
     * @param location Location that specifies the adjacent tiles
     * @return List of the adjacent tiles
     */
    public List<ImmutableTile> getAdjacentClosedNonFlaggedTiles(Vector2D<Integer> location) {
        return getAdjacentTiles(location)
                .stream().filter(tile -> tile.getStatus() == Tile.TileStatus.CLOSED && tile.getStatus() != Tile.TileStatus.FLAG)
                .collect(Collectors.toList());
    }

    private boolean isInsideBounds(Vector2D<Integer> position) {
        return position.getX() >= 0 && position.getX() < gameMode.getFieldWidth() && position.getY() >= 0 && position.getY() < gameMode.getFieldHeight();
    }

    /**
     * Reveals all the tiles in the minefield.
     */
    public void revealAllTiles() {
        Arrays.stream(tiles).flatMap(Stream::of).forEach(tile -> tile.open());
    }

    /**
     * Returns whether all the tiles in the minefield that don't contain a bomb are opened.
     *
     * @return True if all non-mine tiles are open, otherwise false
     */
    public boolean allEmptyTilesAreOpen() {
        return tilesRemaining == 0;
    }

    /**
     * Returns number of flagged tiles in the minefield.
     *
     * @return Number of flags in the minefield
     */
    public int getNumberOfTilesFlagged() {
        return (int) Arrays.stream(tiles).flatMap(Stream::of).filter(x -> x.getStatus() == Tile.TileStatus.FLAG).count();
    }

    /**
     * Alternative way for opening tiles that can be only used on already opened tile.
     * Opens all the adjacent tiles if number of flags surrounding the tile equals the number of bombs around that tile.
     * Does nothing when called on a tile location outside the bounds.
     *
     * @param location that's adjacent squares are tried to be opened
     * @return True if any of the adjacent tiles was a mine and not flagged (i.e. game should end), otherwise false
     */
    public boolean tryQuickOpening(Vector2D<Integer> location) {
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

    /**
     * Returns the {@link GameMode} that is used currently.
     *
     * @return Currently used {@link GameMode}
     */
    public GameMode getGameMode() {
        return gameMode;
    }
}