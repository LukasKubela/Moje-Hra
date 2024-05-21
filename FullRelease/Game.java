package FullRelease;

import javax.swing.*;

public class Game {

    public static int playerWins = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("6 Shot Roulette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            MenuPanel menuPanel = new MenuPanel(frame);
            frame.setContentPane(menuPanel);

            frame.setVisible(true);
        });
    }
}
