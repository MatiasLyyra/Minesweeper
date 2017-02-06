package fi.lyma.minesweeper.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class StatusPanel extends JPanel {

    private JLabel minesLeftLabel;
    public JLabel timeSpentLabel;
    private JButton resetButton;

    public StatusPanel(MainWindow mainWindow) {
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(new CompoundBorder(border, BorderFactory.createEmptyBorder(0,5,0,5)));

        timeSpentLabel = new JLabel();
        minesLeftLabel = new JLabel();

        resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setName("resetButton");
        resetButton.addActionListener(listener -> mainWindow.startNewGame());
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(resetButton);
        this.add(buttonWrapper, BorderLayout.CENTER);

        createStatusComponent(border, "Mines left:", minesLeftLabel, BorderLayout.WEST);
        createStatusComponent(border, "Time spent: ", timeSpentLabel, BorderLayout.EAST);
    }

    private void createStatusComponent(Border border, String labelText, JLabel statusLabel, String alignment) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setOpaque(false);
        this.add(panel, alignment);
        JLabel minesLeftLabel = new JLabel(labelText);
        minesLeftLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(25,25));
        statusLabel.setBorder(border);
        statusLabel.setText("0");
        minesLeftLabel.setLabelFor(statusLabel);
        panel.add(minesLeftLabel);
        panel.add(statusLabel);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(288, 40);
    }

    public void setMinesLeft(int numberOfMinesLeft) {
        minesLeftLabel.setText(Integer.toString(numberOfMinesLeft));
    }

    public void setTimeSpent(long timeSpent) {
        timeSpentLabel.setText(Long.toString(timeSpent/1000));
    }
}
