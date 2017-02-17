package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

/**
 * Immutable interface for accessing {@link Tile}.
 *
 * @see Tile
 */
public interface ImmutableTile {
    /**
     * Returns {@link fi.lyma.minesweeper.logic.Tile.TileStatus} associated to that tile.
     * @return TileStatus of the tile
     * @see fi.lyma.minesweeper.logic.Tile.TileStatus
     */
    Tile.TileStatus getStatus();

    /**
     * Returns true or false if there is a bomb on this tile.
     * @return true if there is a bomb in this tile, otherwise false.
     */
    boolean containsBomb();

    /**
     * Returns number of bombs surrounding this tile.
     * @return number of bombs surrounding this tile
     */
    int getNumberOfSurroundingMines();

    /**
     * Returns whether this tile can be opened. Tile should be openable if it's status is CLOSED or QUESTION.
     * @return true if the status of the tile is CLOSED or QUESTION, otherwise false
     * @see fi.lyma.minesweeper.logic.Tile.TileStatus
     */
    boolean canBeOpened();

    /**
     * Returns location of the tile in the minefield. Top left corner has location (0,0).
     * @return Returns Integer Vector that contains x and y components of the location
     */
    Vector2D<Integer> getLocation();
}
