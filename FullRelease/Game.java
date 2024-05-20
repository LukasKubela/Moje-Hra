package FullRelease;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("6 Shot Roulette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            frame.add(new FullRelease.MenuPanel(frame));
            frame.validate();
        });
    }
}
// FIXME: Magnifying Glass doesn't work properly, and fix the Freeze