package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

public interface ImmutableTile {
    Tile.TileStatus getStatus();

    boolean containsBomb();

    int getNumberOfSurroundingMines();

    boolean canBeOpened();

    Vector2D<Integer> getLocation();
}
