package fi.lyma.main;

import fi.lyma.gui.MainWindow;
import fi.lyma.logic.Minefield;
import fi.lyma.logic.MinesweeperGame;

import javax.swing.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainWindow(new MinesweeperGame(18, 16, 40)));
    }
}
