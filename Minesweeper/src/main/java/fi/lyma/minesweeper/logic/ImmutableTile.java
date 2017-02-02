package fi.lyma.minesweeper.logic;

public interface ImmutableTile {
    public Tile.TileStatus getStatus();

    public boolean containsBomb();

    public int getNumberOfSurroundingMines();

    public boolean canBeOpened();

    public int getX();

    public int getY();
}
