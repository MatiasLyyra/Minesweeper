package fi.lyma.gui;

import fi.lyma.event.MinefieldMouseListener;
import fi.lyma.logic.MinesweeperGame;

import javax.swing.*;

public class MainWindow implements Runnable {

    private MinesweeperGame minesweeperGame;
    private JFrame frame;
    private MinefieldPanel minefieldPanel;

    public MainWindow(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
        minefieldPanel = new MinefieldPanel(minesweeperGame);

    }

    @Override
    public void run() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(true);
        frame.add(minefieldPanel);
        MinefieldMouseListener mouseListener = new MinefieldMouseListener(minefieldPanel, minesweeperGame);
        minefieldPanel.addMouseListener(mouseListener);
        minefieldPanel.addMouseMotionListener(mouseListener);
        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);
    }
}
