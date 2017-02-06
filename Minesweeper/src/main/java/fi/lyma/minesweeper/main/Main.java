package fi.lyma.minesweeper.main;

import fi.lyma.minesweeper.gui.MainWindow;
import fi.lyma.minesweeper.logic.MinesweeperGame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainWindow(MinesweeperGame.DEFAULT_GAME_MODE));
    }
}
