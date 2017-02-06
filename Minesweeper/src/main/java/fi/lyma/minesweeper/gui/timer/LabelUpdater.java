package fi.lyma.minesweeper.gui.timer;

import fi.lyma.minesweeper.gui.StatusPanel;
import fi.lyma.minesweeper.logic.MinesweeperGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabelUpdater implements ActionListener {
    private final MinesweeperGame minesweeperGame;
    private final StatusPanel statusPanel;

    public LabelUpdater(MinesweeperGame minesweeperGame, StatusPanel statusPanel) {
        this.minesweeperGame = minesweeperGame;
        this.statusPanel = statusPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        statusPanel.setTimeSpent(minesweeperGame.getTimeSpent());
    }
}
