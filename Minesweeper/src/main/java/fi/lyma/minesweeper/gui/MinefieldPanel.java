package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.logic.ImmutableTile;
import fi.lyma.minesweeper.logic.MinesweeperGame;
import fi.lyma.minesweeper.logic.Tile;
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
        for (int x = 0; x < game.getGameMode().getFieldWidth(); ++x) {
            for (int y = 0; y < game.getGameMode().getFieldHeight(); ++y) {
                ImmutableTile tile = game.getTile(new Vector2D<>(x, y));
                graphics2D.drawImage(ImageResources.getImageWithTile(tile), x * tileSideLength, y * tileSideLength, null);
            }
        }
        if (highlightedTile != null) {
            graphics2D.drawImage(ImageResources.TILE_OPEN, highlightedTile.getX() * tileSideLength, highlightedTile.getY() * tileSideLength, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(tileSideLength * game.getGameMode().getFieldWidth(), tileSideLength * game.getGameMode().getFieldHeight());
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
