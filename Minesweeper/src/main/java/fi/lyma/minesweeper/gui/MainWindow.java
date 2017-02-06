package fi.lyma.minesweeper.gui;

import fi.lyma.minesweeper.event.MinefieldMouseListener;
import fi.lyma.minesweeper.gui.timer.LabelUpdater;
import fi.lyma.minesweeper.logic.MinesweeperGame;

import javax.swing.*;
import java.awt.*;

public class MainWindow implements Runnable {

    private MinesweeperGame minesweeperGame;
    private JFrame frame;
    private MinefieldPanel minefieldPanel;
    private StatusPanel statusPanel;
    private Timer timer;

    public MainWindow(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
        minefieldPanel = new MinefieldPanel(minesweeperGame);
        statusPanel = new StatusPanel();
        frame = new JFrame();
    }

    @Override
    public void run() {
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(statusPanel, BorderLayout.NORTH);
        frame.add(minefieldPanel, BorderLayout.CENTER);
        MinefieldMouseListener mouseListener = new MinefieldMouseListener(minefieldPanel, minesweeperGame);
        minefieldPanel.addMouseListener(mouseListener);
        minefieldPanel.addMouseMotionListener(mouseListener);

        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);
        timer = new Timer(500,  new LabelUpdater(minesweeperGame, statusPanel));
        timer.setRepeats(true);
        timer.start();
    }
}
