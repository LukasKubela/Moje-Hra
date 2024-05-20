package FullRelease;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final JFrame frame;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        JLabel menuLabel = new JLabel("MENU", SwingConstants.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 50));
        add(menuLabel, BorderLayout.NORTH);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> switchToElevatorPanel());
        add(playButton, BorderLayout.CENTER);

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> switchToSettingsPanel());
        add(settingsButton, BorderLayout.SOUTH);
    }

    private void switchToElevatorPanel() {
        ElevatorPanel elevatorPanel = new FullRelease.ElevatorPanel(frame, this);
        updateContentPane(elevatorPanel);
    }

    private void switchToSettingsPanel() {
        SettingsPanel settingsPanel = new SettingsPanel(frame, this);
        updateContentPane(settingsPanel);
    }

    public void updateContentPane(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.setContentPane(newPanel);
        frame.revalidate();
        frame.repaint();
    }
}
