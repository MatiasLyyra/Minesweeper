package fi.lyma.minesweeper.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Panel that shows information about minefield and holds reset button.
 */
public class StatusPanel extends JPanel {

    private static final Dimension STATUS_LABEL_SIZE = new Dimension(50, 25);
    private static final Dimension STATUS_PANEL_SIZE = new Dimension(256, 40);

    private JLabel minesLeftLabel;
    public JLabel timeSpentLabel;
    private ImageIcon bombIcon;
    private ImageIcon clockIcon;
    private JButton resetButton;
    private MainWindow mainWindow;

    public StatusPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(new CompoundBorder(border, BorderFactory.createEmptyBorder(0, 2, 0, 2)));

        timeSpentLabel = new JLabel();
        minesLeftLabel = new JLabel();
        clockIcon = new ImageIcon(ImageResources.CLOCK_ICON);
        bombIcon = new ImageIcon(ImageResources.BOMB_ICON);


        resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setName("resetButton");
        resetButton.addActionListener(listener -> mainWindow.startNewGame());
        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(resetButton);
        this.add(buttonWrapper, BorderLayout.CENTER);

        createStatusComponent(border, bombIcon, minesLeftLabel, BorderLayout.WEST);
        createStatusComponent(border, clockIcon, timeSpentLabel, BorderLayout.EAST);
    }

    private void createStatusComponent(Border border, ImageIcon imageIcon, JLabel statusLabel, String alignment) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setOpaque(false);
        this.add(panel, alignment);
        JLabel stateIconLabel = new JLabel(imageIcon);
        stateIconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(STATUS_LABEL_SIZE);
        statusLabel.setBorder(border);
        statusLabel.setText("0");
        stateIconLabel.setLabelFor(statusLabel);
        panel.add(stateIconLabel);
        panel.add(statusLabel);
    }

    @Override
    public Dimension getPreferredSize() {
        return STATUS_PANEL_SIZE;
    }

    public void setMinesLeft(int numberOfMinesLeft) {
        minesLeftLabel.setText(Integer.toString(numberOfMinesLeft));
    }

    public void setTimeSpent(long timeSpent) {
        timeSpentLabel.setText(mainWindow.formatGameTime(timeSpent));
    }
}
