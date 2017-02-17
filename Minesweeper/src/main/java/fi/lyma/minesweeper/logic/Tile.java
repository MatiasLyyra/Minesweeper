package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

/**
 * Represents single tile within {@link Minefield}.
 */
public class Tile implements ImmutableTile {

    /**
     * Represents the state of a {@link Tile}.
     */
    public enum TileStatus {
        CLOSED,
        OPEN,
        FLAG,
        QUESTION
    }

    private final Vector2D<Integer> location;
    private boolean containsBomb;
    private int surroundingMines;
    private TileStatus status;

    /**
     * Constructs tile with given location. Tile is by default closed and doesn't contain a bomb.
     * @param x location of the bomb in x-axis
     * @param y location of the bomb in y-axis
     */
    public Tile(int x, int y) {
        status = TileStatus.CLOSED;
        containsBomb = false;
        location = new Vector2D<>(x, y);
    }

    @Override
    public boolean containsBomb() {
        return containsBomb;
    }

    /**
     * Places bomb in this tile.
     */
    public void placeBomb() {
        containsBomb = true;
    }

    @Override
    public TileStatus getStatus() {
        return status;
    }

    /**
     * Toggles the FLAG/QUESTION status in following rotation FLAG -> QUESTION -> CLOSED -> FLAG.
     */
    public void flag() {
        switch (status) {
            case CLOSED:
                status = TileStatus.FLAG;
                break;
            case FLAG:
                status = TileStatus.QUESTION;
                break;
            case QUESTION:
                status = TileStatus.CLOSED;
                break;
        }
    }

    @Override
    public boolean canBeOpened() {
        return status == TileStatus.CLOSED || status == TileStatus.QUESTION;
    }

    /**
     * Marks the tile as opened.
     */
    public void open() {
        status = TileStatus.OPEN;
    }

    @Override
    public int getNumberOfSurroundingMines() {
        return surroundingMines;
    }

    /**
     * Sets the amount of surrounding mines.
     * @param surroundingMines number of surrounding mines.
     */
    public void setNumberOfSurroundingMines(int surroundingMines) {
        this.surroundingMines = surroundingMines;
    }

    @Override
    public Vector2D<Integer> getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tile tile = (Tile) o;

        if (containsBomb != tile.containsBomb) {
            return false;
        }
        if (surroundingMines != tile.surroundingMines) {
            return false;
        }
        if (!location.equals(tile.location)) {
            return false;
        }
        return status == tile.status;
    }

}
