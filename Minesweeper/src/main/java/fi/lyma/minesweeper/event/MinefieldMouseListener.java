package fi.lyma.minesweeper.event;

import fi.lyma.minesweeper.gui.MinefieldPanel;
import fi.lyma.minesweeper.gui.StatusPanel;
import fi.lyma.minesweeper.logic.MinesweeperGame;
import fi.lyma.util.Vector2D;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MinefieldMouseListener implements MouseListener, MouseMotionListener {
    private final MinefieldPanel minefieldPanel;
    private final MinesweeperGame minesweeperGame;
    private final StatusPanel statusPanel;

    public MinefieldMouseListener(MinefieldPanel minefieldPanel, MinesweeperGame minesweeperGame, StatusPanel statusPanel) {
        this.minefieldPanel = minefieldPanel;
        this.minesweeperGame = minesweeperGame;
        this.statusPanel = statusPanel;
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
        statusPanel.setMinesLeft(minesweeperGame.getNumberOfMinesRemaining());
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
        if (!minesweeperGame.isGameEnded()) {
            minefieldPanel.setHighlightedTile(screenCoords);
            minefieldPanel.repaint();
        }
    }
}
