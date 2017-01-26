package fi.lyma.logic;

public interface ImmutableTile {
    public Tile.TileStatus getStatus();

    public boolean containsBomb();

    public int getNumberOfSurroundingMines();

    public int getX();

    public int getY();
}
