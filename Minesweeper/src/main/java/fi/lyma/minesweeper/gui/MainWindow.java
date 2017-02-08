package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.event.MinefieldMouseListener;
import fi.lyma.minesweeper.logic.GameMode;
import fi.lyma.minesweeper.logic.MinesweeperGame;

import javax.swing.*;
import java.awt.*;

public class MainWindow implements Runnable {

    private MinesweeperGame minesweeperGame;
    private JFrame frame;
    private MinefieldPanel minefieldPanel;
    private StatusPanel statusPanel;
    private GameMode gameMode;
    private Timer timer;

    public MainWindow(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void run() {
        minesweeperGame = new MinesweeperGame(gameMode);
        minefieldPanel = new MinefieldPanel(minesweeperGame);
        frame = new JFrame();
        statusPanel = new StatusPanel(this);
        statusPanel.setMinesLeft(minesweeperGame.getNumberOfMinesRemaining());

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new TopBar(this), BorderLayout.NORTH);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.add(minefieldPanel, BorderLayout.CENTER);
        MinefieldMouseListener mouseListener = new MinefieldMouseListener(minefieldPanel, minesweeperGame, statusPanel);
        minefieldPanel.addMouseListener(mouseListener);
        minefieldPanel.addMouseMotionListener(mouseListener);

        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);
        timer = new Timer(250, listener -> statusPanel.setTimeSpent(minesweeperGame.getTimeSpent()));
        timer.setRepeats(true);
        timer.start();
    }

    public void startNewGame() {
        minesweeperGame.createNewField(gameMode);
        frame.pack();
        frame.repaint();
        statusPanel.setMinesLeft(minesweeperGame.getNumberOfMinesRemaining());
    }

    public void startNewGame(GameMode gameMode) {
        this.gameMode = gameMode;
        startNewGame();
    }

    public void showGameModeChooser() {
        JTextField fieldWidthField = new JTextField(Integer.toString(gameMode.getFieldWidth()));
        JTextField fieldHeightField = new JTextField(Integer.toString(gameMode.getFieldHeight()));
        JTextField minesField = new JTextField(Integer.toString(gameMode.getTotalNumberOfMines()));
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Field width:"),
            fieldWidthField,
            new JLabel("Field height:"),
            fieldHeightField,
            new JLabel("Number of mines:"),
            minesField
        };
        int result = JOptionPane.showConfirmDialog(frame, inputs, "Game Settings", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            setGameMode(fieldWidthField.getText(), fieldHeightField.getText(), minesField.getText());
        }
    }

    private void setGameMode(String widthString, String heightString, String minesString) {
        int height, width, mines;
        try {
            width = Integer.parseInt(widthString);
            height = Integer.parseInt(heightString);
            mines = Integer.parseInt(minesString);
            startNewGame(new GameMode(width, height, mines));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid values in game mode fields!");
        }
    }
}
