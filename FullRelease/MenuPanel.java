package FullRelease;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final JFrame frame;
    private GameGraphics gameGraphics;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        gameGraphics = new GameGraphics();
        add(gameGraphics, BorderLayout.CENTER);

        JButton playButton = gameGraphics.getPlayButton();
        if (playButton != null) {
            playButton.addActionListener(e -> switchToElevatorPanel());
        }
    }

    private void switchToElevatorPanel() {
        GamePanel gamePanel = new GamePanel(frame, this);
        ElevatorPanel elevatorPanel = new ElevatorPanel(gameGraphics, gamePanel, frame);
        updateContentPane(elevatorPanel);
    }

    public void updateContentPane(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.setContentPane(newPanel);
        frame.revalidate();
        frame.repaint();
    }

    public GameGraphics getGameGraphics() {
        return gameGraphics;
    }
}
