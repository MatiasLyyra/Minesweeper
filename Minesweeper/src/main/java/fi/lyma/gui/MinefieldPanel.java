package fi.lyma.gui;

import fi.lyma.logic.ImmutableTile;
import fi.lyma.logic.MinesweeperGame;
import fi.lyma.logic.Tile;
import fi.lyma.util.Vector2D;

import javax.swing.*;
import java.awt.*;

public class MinefieldPanel extends JPanel {
    private final MinesweeperGame game;
    private Vector2D<Integer> highlightedTile;
    private final int tileSideLength = 32;

    public MinefieldPanel(MinesweeperGame game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        for (int x = 0; x < game.getFieldWidth(); ++x) {
            for (int y = 0; y < game.getFieldHeight(); ++y) {
                ImmutableTile tile = game.getTile(new Vector2D<>(x, y));
                graphics2D.drawImage(TileImages.getImageWithTile(tile), x * tileSideLength, y * tileSideLength, null);
            }
        }
        if (highlightedTile != null) {
            graphics2D.drawImage(TileImages.TILE_OPEN, highlightedTile.getX() * tileSideLength, highlightedTile.getY() * tileSideLength, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(tileSideLength * game.getFieldWidth(), tileSideLength * game.getFieldHeight());
    }

    public Vector2D<Integer> convertScreenToWorldCoordinates(Vector2D<Integer> screenCoords) {
        return new Vector2D<Integer>(screenCoords.getX() / tileSideLength, screenCoords.getY() / tileSideLength);
    }

    public void clearHighlightedTile() {
        highlightedTile = null;
    }

    public void setHighlightedTile(Vector2D<Integer> location) {
        location = convertScreenToWorldCoordinates(location);
        ImmutableTile tile = game.getTile(location);
        if (tile == null || tile.getStatus() == Tile.TileStatus.OPEN) {
            highlightedTile = null;
        } else {
            highlightedTile = location;
        }
    }
}
