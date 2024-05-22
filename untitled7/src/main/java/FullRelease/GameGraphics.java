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
    private JButton playerButton;
    private JButton enemyButton;
    private JButton shopButton;
    private BufferedImage casinoChip;
    private BufferedImage secondCasinoChip;
    private BufferedImage thirdCasinoChip;
    private BufferedImage backButtonImage;
    private JButton backButton;
    private BufferedImage magnifyingGlassImage;
    private JButton magnifyingGlassButton;
    private BufferedImage freezeImage;
    private JButton freezeButton;
    private BufferedImage shootSignImage;

    public GameGraphics() {
        setLayout(new GridBagLayout());
        loadImage();
        initializeButtons();
        loadPlayerImages();
        loadElevatorImage();
        loadCasinoChip();
        loadSecondCasinoChip();
        loadThirdCasinoChip();
        loadBackButtonImage();
        loadMagnifyingGlassImage();
        loadFreezeImage();
        initializeBackButton();
        initializeMagnifyingGlassButton();
        initializeFreezeButton();
        loadShootSignImage();
    }

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/table better.png"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCasinoChip() {
        try {
            casinoChip = ImageIO.read(getClass().getResource("/casino chip.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSecondCasinoChip() {
        try {
            secondCasinoChip = ImageIO.read(getClass().getResource("/casino chip.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadThirdCasinoChip() {
        try {
            thirdCasinoChip = ImageIO.read(getClass().getResource("/casino chip.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadShootSignImage() {
        try {
            shootSignImage = ImageIO.read(getClass().getResource("/SHOOT sign.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBackButtonImage() {
        try {
            backButtonImage = ImageIO.read(getClass().getResource("/Back.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMagnifyingGlassImage() {
        try {
            magnifyingGlassImage = ImageIO.read(getClass().getResource("/Magnifying glass.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFreezeImage() {
        try {
            freezeImage = ImageIO.read(getClass().getResource("/Freeze.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeButtons() {
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

            BufferedImage playerButtonImage = ImageIO.read(getClass().getResource("/YOU button.png"));
            BufferedImage enemyButtonImage = ImageIO.read(getClass().getResource("/ENEMY button.png"));
            BufferedImage shopButtonImage = ImageIO.read(getClass().getResource("/SHOP Button.png"));
            playerButton = createHoverButton(playerButtonImage);
            enemyButton = createHoverButton(enemyButtonImage);
            shopButton = createHoverButton(shopButtonImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBackButton() {
        backButton = createHoverButton(backButtonImage);
    }

    private void initializeMagnifyingGlassButton() {
        magnifyingGlassButton = createHoverButton(magnifyingGlassImage);
    }

    private void initializeFreezeButton() {
        freezeButton = createHoverButton(freezeImage);
    }

    private JButton createHoverButton(BufferedImage buttonImage) {
        ImageIcon originalIcon = new ImageIcon(buttonImage);
        ImageIcon darkerIcon = new ImageIcon(createDarkerImage(buttonImage));

        JButton button = new JButton(originalIcon);
        button.setPreferredSize(new Dimension(originalIcon.getIconWidth(), originalIcon.getIconHeight()));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(darkerIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(originalIcon);
            }
        });

        return button;
    }

    private BufferedImage createDarkerImage(BufferedImage original) {
        RescaleOp op = new RescaleOp(0.7f, 0, null);
        return op.filter(original, null);
    }

    private void loadElevatorImage() {
        try {
            elevatorImage = ImageIO.read(getClass().getResource("/Elevator.png"));
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

    public JButton getPlayerButton() {
        return playerButton;
    }

    public JButton getEnemyButton() {
        return enemyButton;
    }

    public JButton getShopButton() {
        return shopButton;
    }

    public BufferedImage getCasinoChip() {
        return casinoChip;
    }

    public BufferedImage getSecondCasinoChip() {
        return secondCasinoChip;
    }

    public BufferedImage getThirdCasinoChip() {
        return thirdCasinoChip;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getMagnifyingGlassButton() {
        return magnifyingGlassButton;
    }

    public JButton getFreezeButton() {
        return freezeButton;
    }

    public BufferedImage getShootSignImage() {
        return shootSignImage;
    }
}
