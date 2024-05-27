package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Game {

    public static int playerWins = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("6 Shot Roulette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Load the icon image using ImageIO
            URL iconURL = Game.class.getResource("/Revolver 1(48 x 48).png");
            if (iconURL != null) {
                System.out.println("Image loaded");
                try {
                    Image iconImage = ImageIO.read(iconURL);
                    frame.setIconImage(iconImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to load icon image");
                }
            } else {
                System.out.println("Icon image not found at: " + iconURL);
            }

            MenuPanel menuPanel = new MenuPanel(frame);
            frame.setContentPane(menuPanel);

            frame.setVisible(true);
        });
    }
}
