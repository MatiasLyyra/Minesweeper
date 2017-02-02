package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.logic.ImmutableTile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileImages {
    public static final BufferedImage TILE_CLOSED = loadImage("images/tile_closed.png");
    public static final BufferedImage TILE_OPEN = loadImage("images/tile_open.png");
    public static final BufferedImage TILE_OPEN_1 = loadImage("images/tile_open_1.png");
    public static final BufferedImage TILE_OPEN_2 = loadImage("images/tile_open_2.png");
    public static final BufferedImage TILE_OPEN_3 = loadImage("images/tile_open_3.png");
    public static final BufferedImage TILE_OPEN_4 = loadImage("images/tile_open_4.png");
    public static final BufferedImage TILE_OPEN_5 = loadImage("images/tile_open_5.png");
    public static final BufferedImage TILE_OPEN_6 = loadImage("images/tile_open_6.png");
    public static final BufferedImage TILE_OPEN_7 = loadImage("images/tile_open_7.png");
    public static final BufferedImage TILE_OPEN_8 = loadImage("images/tile_open_8.png");
    public static final BufferedImage TILE_QUESTION = loadImage("images/tile_question.png");
    public static final BufferedImage TILE_BOMB = loadImage("images/tile_bomb.png");
    public static final BufferedImage TILE_FLAG = loadImage("images/tile_flag.png");
    private static final BufferedImage[] OPEN_TILES = {TILE_OPEN, TILE_OPEN_1, TILE_OPEN_2, TILE_OPEN_3, TILE_OPEN_4, TILE_OPEN_5, TILE_OPEN_6, TILE_OPEN_7, TILE_OPEN_8};

    public static final BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println(e);
            System.err.println("Couldn't load image from path: " + path);
        }
        return image;
    }

    public static final BufferedImage getImageWithTile(ImmutableTile tile) {
        switch (tile.getStatus()) {
            case CLOSED:
                return TILE_CLOSED;
            case FLAG:
                return TILE_FLAG;
            case QUESTION:
                return TILE_QUESTION;
            case OPEN:
                if (tile.containsBomb()) {
                    return TILE_BOMB;
                }
                return OPEN_TILES[tile.getNumberOfSurroundingMines()];
        }
        return null;
    }
}
