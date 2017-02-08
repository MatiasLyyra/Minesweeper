package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

/**
 * Immutable interface for accessing {@link Tile}
 *
 * @see Tile
 */
public interface ImmutableTile {
    Tile.TileStatus getStatus();

    boolean containsBomb();

    int getNumberOfSurroundingMines();

    boolean canBeOpened();

    Vector2D<Integer> getLocation();
}
