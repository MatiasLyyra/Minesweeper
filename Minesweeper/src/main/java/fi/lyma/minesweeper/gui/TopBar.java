package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.logic.GameMode;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JMenuBar {
    private static final Dimension TOP_BAR_SIZE = new Dimension(256, 30);
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

        JMenu gameModes = new JMenu("Game Modes");
        JMenuItem easy = new JMenuItem("Easy");
        easy.addActionListener(listener -> mainWindow.startNewGame(GameMode.EASY));
        JMenuItem medium = new JMenuItem("Medium");
        medium.addActionListener(listener -> mainWindow.startNewGame(GameMode.MEDIUM));
        JMenuItem hard = new JMenuItem("Hard");
        hard.addActionListener(listener -> mainWindow.startNewGame(GameMode.HARD));
        gameModes.add(easy);
        gameModes.add(medium);
        gameModes.add(hard);

        JMenu scores = new JMenu("Score");
        JMenuItem highscores = new JMenuItem("High Scores");
        highscores.addActionListener(listener -> mainWindow.showHighScores());
        JMenuItem clearScore = new JMenuItem("Clear scores");
        clearScore.addActionListener(listener -> mainWindow.clearScores());
        scores.add(highscores);
        scores.add(clearScore);

        this.add(file);
        this.add(gameModes);
        this.add(scores);
    }

    @Override
    public Dimension getPreferredSize() {
        return TOP_BAR_SIZE;
    }
}
