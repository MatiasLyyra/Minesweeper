package fi.lyma.minesweeper.gui;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JMenuBar{
    private final MainWindow mainWindow;

    public TopBar(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        JMenu file = new JMenu("File");
        JMenuItem gameSettings = new JMenuItem("Change game settings");
        gameSettings.addActionListener(listener -> mainWindow.showGameModeChooser());
        file.add(gameSettings);
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(listener -> System.exit(0));
        file.add(gameSettings);
        file.add(exit);
        this.add(file);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(288, 30);
    }
}
