package FullRelease;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;

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
    private BufferedImage fourthCasinoChip;
    private BufferedImage backButtonImage;
    private JButton backButton;
    private BufferedImage magnifyingGlassImage;
    private JButton magnifyingGlassButton;
    private BufferedImage freezeImage;
    private JButton freezeButton;
    private BufferedImage shootSignImage;
    private BufferedImage healthPotionImage;
    private JButton healthPotion;
    private BufferedImage healthClock1;
    private BufferedImage healthClock2;
    private BufferedImage healthClock3;
    private BufferedImage Lamp;
    private BufferedImage revolver1Image;
    private BufferedImage revolver2Image;
    private BufferedImage sparklingEffect1;
    private BufferedImage sparklingEffect2;
    private BufferedImage playerButtonImage;
    private BufferedImage enemyButtonImage;
    private BufferedImage shopButtonImage;
    private BufferedImage playButtonImage; // Declare playButtonImage here

    public GameGraphics() {
        setLayout(new GridBagLayout());
        loadImage();
        initializeButtons();
        loadPlayerImages();
        loadElevatorImage();
        loadCasinoChip();
        loadSecondCasinoChip();
        loadThirdCasinoChip();
        loadFourthCasinoChip();
        loadBackButtonImage();
        loadMagnifyingGlassImage();
        loadFreezeImage();
        initializeBackButton();
        initializeMagnifyingGlassButton();
        initializeFreezeButton();
        loadShootSignImage();
        loadHealthPotionImage();
        initializeHealthPotionButton();
        loadHealthClock1();
        loadHealthClock2();
        loadHealthClock3();
        loadLampImage();
        loadRevolver1Image();
        loadRevolver2Image();
        loadSparklingEffectImage();
        loadSparklingEffect2Image();
    }

    private void loadImage() {
        backgroundImage = loadImageResource("/table better.png");
    }

    private void loadPlayerImages() {
        standingImage1 = loadImageResource("/player standing.png");
        standingImage2 = loadImageResource("/player standing 2.png");
        walkingImageRight = new ImageIcon(getClass().getResource("/player walking 2.gif"));
        walkingImageLeft = new ImageIcon(getClass().getResource("/player walking.gif"));
    }

    private void loadCasinoChip() {
        casinoChip = loadImageResource("/Casino Chip.png");
    }

    private void loadSecondCasinoChip() {
        secondCasinoChip = loadImageResource("/Casino Chip.png");
    }

    private void loadThirdCasinoChip() {
        thirdCasinoChip = loadImageResource("/Casino Chip.png");
    }

    private void loadFourthCasinoChip() {
        fourthCasinoChip = loadImageResource("/Casino Chip.png");
    }

    private void loadShootSignImage() {
        shootSignImage = loadImageResource("/SHOOT sign.png");
    }

    private void loadLampImage() {
        Lamp = loadImageResource("/Lamp.png");
    }

    private void loadHealthClock1() {
        healthClock1 = loadImageResource("/health clock 1.png");
    }

    private void loadHealthClock2() {
        healthClock2 = loadImageResource("/health clock 2.png");
    }

    private void loadHealthClock3() {
        healthClock3 = loadImageResource("/health clock 3.png");
    }

    private void loadBackButtonImage() {
        backButtonImage = loadImageResource("/Back.png");
    }

    private void loadRevolver1Image() {
        revolver1Image = loadImageResource("/Revolver 1.png");
    }

    private void loadRevolver2Image() {
        revolver2Image = loadImageResource("/Revolver 2.png");
    }

    private void loadSparklingEffectImage() {
        sparklingEffect1 = loadImageResource("/sparkling effect revolver 1.png");
    }

    private void loadSparklingEffect2Image() {
        sparklingEffect2 = loadImageResource("/sparkling effect revolver 2.png");
    }

    private void loadMagnifyingGlassImage() {
        magnifyingGlassImage = loadImageResource("/Magnifying glass.png");
    }

    private void loadFreezeImage() {
        freezeImage = loadImageResource("/Freeze.png");
    }

    private void loadHealthPotionImage() {
        healthPotionImage = loadImageResource("/Health Potion.png");
    }

    private void initializeButtons() {
        playButtonImage = loadImageResource("/PLAY button.png"); // Load the play button image here
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

        playerButtonImage = loadImageResource("/YOU button.png");
        enemyButtonImage = loadImageResource("/ENEMY Button.png");
        shopButtonImage = loadImageResource("/SHOP Button.png");
        playerButton = createHoverButton(playerButtonImage);
        enemyButton = createHoverButton(enemyButtonImage);
        shopButton = createHoverButton(shopButtonImage);
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

    private void initializeHealthPotionButton() {
        healthPotion = createHoverButton(healthPotionImage);
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
        elevatorImage = loadImageResource("/Elevator.png");
    }

    private BufferedImage loadImageResource(String path) {
        BufferedImage image = null;
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                image = ImageIO.read(is);
            } else {
                System.err.println("Resource not found: " + path);
                image = createPlaceholderImage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage placeholder = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 200, 200);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Image not found", 50, 100);
        g2d.dispose();
        return placeholder;
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

    public BufferedImage getFourthCasinoChip() {
        return fourthCasinoChip;
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

    public JButton getHealthPotion() {
        return healthPotion;
    }

    public BufferedImage getMagnifyingGlassImage() {
        return magnifyingGlassImage;
    }

    public BufferedImage getFreezeImage() {
        return freezeImage;
    }

    public BufferedImage getHealthPotionImage() {
        return healthPotionImage;
    }

    public BufferedImage getHealthClock1() {
        return healthClock1;
    }

    public BufferedImage getHealthClock2() {
        return healthClock2;
    }

    public BufferedImage getHealthClock3() {
        return healthClock3;
    }

    public BufferedImage getLamp() {
        return Lamp;
    }

    public BufferedImage getRevolver1Image() {
        return revolver1Image;
    }

    public BufferedImage getRevolver2Image() {
        return revolver2Image;
    }

    public BufferedImage getSparklingEffect1() {
        return sparklingEffect1;
    }

    public BufferedImage getSparklingEffect2() {
        return sparklingEffect2;
    }
}
