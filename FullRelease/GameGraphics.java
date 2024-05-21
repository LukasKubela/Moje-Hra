package FullRelease;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;

public class GameGraphics extends JPanel {
    private BufferedImage backgroundImage;
    private JButton playButton;
    private ImageIcon originalIcon;
    private ImageIcon darkerIcon;
    private BufferedImage standingImage1;
    private BufferedImage standingImage2;
    private ImageIcon walkingImageLeft;
    private ImageIcon walkingImageRight;
    private BufferedImage elevatorImage;

    public GameGraphics() {
        setLayout(new GridBagLayout());
        loadImage();
        initializeButton();
        loadPlayerImages();
        loadElevatorImage();
    }

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/table better.png"));
            System.out.println("Background image loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayerImages() {
        try {
            standingImage1 = ImageIO.read(getClass().getResource("/player standing.png"));
            standingImage2 = ImageIO.read(getClass().getResource("/player standing 2.png"));
            walkingImageRight = new ImageIcon(getClass().getResource("/player walking 2.gif"));
            walkingImageLeft = new ImageIcon(getClass().getResource("/player walking.gif"));
            System.out.println("Player images loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeButton() {
        try {
            BufferedImage playButtonImage = ImageIO.read(getClass().getResource("/PLAY button.png"));
            originalIcon = new ImageIcon(playButtonImage);
            darkerIcon = new ImageIcon(createDarkerImage(playButtonImage));

            playButton = new JButton(originalIcon);
            playButton.setPreferredSize(new Dimension(300, 300));
            playButton.setContentAreaFilled(false);
            playButton.setBorderPainted(false);
            playButton.setFocusPainted(false);

            playButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    playButton.setIcon(darkerIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    playButton.setIcon(originalIcon);
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 0, 0, 0);
            gbc.anchor = GridBagConstraints.CENTER;

            add(playButton, gbc);
            System.out.println("Play button initialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage createDarkerImage(BufferedImage original) {
        RescaleOp op = new RescaleOp(0.7f, 0, null);
        return op.filter(original, null);
    }

    private void loadElevatorImage() {
        try {
            elevatorImage = ImageIO.read(getClass().getResource("/Elevator.png"));
            System.out.println("Elevator image loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public BufferedImage getStandingImage1() {
        return standingImage1;
    }

    public BufferedImage getStandingImage2() {
        return standingImage2;
    }

    public ImageIcon getWalkingImageRight() {
        return walkingImageRight;
    }

    public ImageIcon getWalkingImageLeft() {
        return walkingImageLeft;
    }

    public BufferedImage getElevatorImage() {
        return elevatorImage;
    }
}
