package fi.lyma.minesweeper.logic;

import fi.lyma.util.Vector2D;

public class Tile implements ImmutableTile {

    public enum TileStatus {
        CLOSED,
        OPEN,
        FLAG,
        QUESTION
    }

    private final Vector2D<Integer> position;
    private boolean containsBomb;
    private int surroundingMines;
    private TileStatus status;

    public Tile(int x, int y) {
        status = TileStatus.CLOSED;
        containsBomb = false;
        position = new Vector2D<>(x, y);
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

    public void open() {
        status = TileStatus.OPEN;
    }

    @Override
    public int getX() {
        return position.getX();
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
        return position.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        if (containsBomb != tile.containsBomb) return false;
        if (surroundingMines != tile.surroundingMines) return false;
        if (!position.equals(tile.position)) return false;
        return status == tile.status;
    }

}
