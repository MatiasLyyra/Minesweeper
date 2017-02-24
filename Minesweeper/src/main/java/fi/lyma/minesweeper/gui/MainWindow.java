package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.gui.event.MinefieldMouseListener;
import fi.lyma.minesweeper.logic.GameMode;
import fi.lyma.minesweeper.logic.MinesweeperGame;
import fi.lyma.minesweeper.logic.event.GameStateListener;
import fi.lyma.minesweeper.score.ScoreRecord;
import fi.lyma.minesweeper.score.ScoreRecordKeeper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for holding reference to the {@link MinesweeperGame} and starting the gui.
 */
public class MainWindow implements Runnable, GameStateListener {

    private static final String DEFAULT_NAME = "No Name";

    private MinesweeperGame minesweeperGame;
    private JFrame frame;
    private StatusPanel statusPanel;
    private GameMode gameMode;
    private ScoreRecordKeeper scoreRecordKeeper;

    public MainWindow(GameMode gameMode) {
        scoreRecordKeeper = ScoreRecordKeeper.loadFromFile();
        this.gameMode = gameMode;
    }

    @Override
    public void run() {
        minesweeperGame = new MinesweeperGame(gameMode);
        minesweeperGame.addGameStateListener(this);
        MinefieldPanel minefieldPanel = new MinefieldPanel(minesweeperGame);
        frame = new JFrame();
        statusPanel = new StatusPanel(this);
        statusPanel.setMinesLeft(minesweeperGame.getNumberOfMinesRemaining());
        frame.setIconImage(ImageResources.BOMB_ICON);
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
        Timer timer = new Timer(250, listener -> statusPanel.setTimeSpent(minesweeperGame.getTimeSpent()));
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

    @Override
    public void gameStatusChanged(MinesweeperGame.GameStatus gameStatus) {
        if (gameStatus == MinesweeperGame.GameStatus.ENDED_WIN) {
            String name = showWinDialog();
            scoreRecordKeeper.storeNewRecord(gameMode, new ScoreRecord(name, minesweeperGame.getTimeSpent()));
            scoreRecordKeeper.saveToFile();
        }
    }

    private String showWinDialog() {
        JTextField nameField = new JTextField();
        final JComponent[] input = new JComponent[]{
                new JLabel("Enter your name: "),
                nameField,
        };
        JOptionPane.showConfirmDialog(frame, input, "You won!", JOptionPane.PLAIN_MESSAGE);
        return nameField.getText().isEmpty() ? DEFAULT_NAME : nameField.getText();
    }

    public void showHighScores() {
        List<ScoreRecord> scoreRecords = scoreRecordKeeper.getScoresByGameMode(gameMode);
        StringBuilder sb = new StringBuilder();
        sb.append("Scores for " + gameMode + "\n");
        for (ScoreRecord scoreRecord : scoreRecords) {
            sb.append(String.format("%s : %s\n", scoreRecord.playerName, formatGameTime(scoreRecord.timeSpent)));
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.PLAIN_MESSAGE);
    }

    public void clearScores() {
        scoreRecordKeeper.clearRecords();
    }

    public String formatGameTime(long time) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
    }
}
