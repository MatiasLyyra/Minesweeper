package fi.lyma.event;

import fi.lyma.gui.MinefieldPanel;
import fi.lyma.logic.MinesweeperGame;
import fi.lyma.util.Vector2D;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MinefieldMouseListener implements MouseListener, MouseMotionListener {
    private final MinefieldPanel minefieldPanel;
    private final MinesweeperGame minesweeperGame;

    public MinefieldMouseListener(MinefieldPanel minefieldPanel, MinesweeperGame minesweeperGame) {
        this.minefieldPanel = minefieldPanel;
        this.minesweeperGame = minesweeperGame;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        handleHighlight(new Vector2D<>(mouseEvent.getX(), mouseEvent.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Vector2D<Integer> worldCoords = minefieldPanel.convertScreenToWorldCoordinates(new Vector2D(mouseEvent.getX(), mouseEvent.getY()));
        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
            minesweeperGame.openTile(worldCoords);
        } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
            minesweeperGame.flagTile(worldCoords);
        }
        minefieldPanel.clearHighlightedTile();
        minefieldPanel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        handleHighlight(new Vector2D<>(mouseEvent.getX(), mouseEvent.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    private void handleHighlight(Vector2D<Integer> screenCoords) {
        minefieldPanel.setHighlightedTile(screenCoords);
        minefieldPanel.repaint();
    }
}
