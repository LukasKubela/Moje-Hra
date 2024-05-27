package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Game {

    public static int playerWins = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("6 Shot Roulette");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Load the icon image
            try (InputStream is = Game.class.getResourceAsStream("/Revolver 1.png")) {
                if (is != null) {
                    Image iconImage = ImageIO.read(is);
                    frame.setIconImage(iconImage);
                } else {
                    System.err.println("Icon image not found");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            MenuPanel menuPanel = new MenuPanel(frame);
            frame.setContentPane(menuPanel);

            frame.setVisible(true);
        });
    }
}
