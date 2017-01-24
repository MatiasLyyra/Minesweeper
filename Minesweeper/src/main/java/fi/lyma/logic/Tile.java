package fi.lyma.logic;

/**
 * Created by lyma on 23.1.2017.
 */
public class Tile {
    public enum TileStatus {
        CLOSED,
        OPEN,
        FLAG
    }

    private boolean containsBomb;
    private TileStatus status;
    public final int x,y;

    public Tile(int x, int y) {
        status = TileStatus.CLOSED;
        containsBomb = false;
        this.x = x;
        this.y = y;
    }

    public boolean containsBomb() {
        return containsBomb;
    }

    public void placeBomb() {
        containsBomb = true;
    }

    public TileStatus getStatus() {
        return status;
    }

    public void flag() {
        status = TileStatus.FLAG;
    }

    public void open() {
        status = TileStatus.OPEN;
    }
}
