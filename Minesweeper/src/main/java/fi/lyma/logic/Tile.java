package fi.lyma.logic;


public class Tile implements ImmutableTile {

    public enum TileStatus {
        CLOSED,
        OPEN,
        FLAG
    }

    private final int x;
    private final int y;
    private boolean containsBomb;
    private int surroundingMines;
    private TileStatus status;
    public Tile(int x, int y) {
        status = TileStatus.CLOSED;
        containsBomb = false;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean containsBomb() {
        return containsBomb;
    }

    public void placeBomb() {
        containsBomb = true;
    }

    @Override
    public TileStatus getStatus() {
        return status;
    }

    public void flag() {
        status = TileStatus.FLAG;
    }

    public void open() {
        status = TileStatus.OPEN;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getNumberOfSurroundingMines() {
        return surroundingMines;
    }

    public void setNumberOfSurroundingMines(int surroundingMines) {
        this.surroundingMines = surroundingMines;
    }

    @Override
    public int getY() {
        return y;
    }

}
