package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.Timer;
import static FullRelease.Game.playerWins;

public class GamePanel extends JPanel {
    private final JFrame frame;
    private JButton playerButton, enemyButton, shopButton;
    private JLabel statusLabel, turnLabel;
    private final Random random = new Random();
    private int playerLives = 3;
    private int enemyLives = 3;
    private boolean gameOver = false;
    private boolean playerStarts = true;
    private int shotCount = 0;
    private JLabel coinsLabel;
    private boolean hasMagnifyingGlass = false;
    private JButton magnifyingGlassButton;
    private boolean[] roundLive;
    private final MagnifyingGlass magnifyingGlass = new MagnifyingGlass();
    private final HealthPotion healthPotion = new HealthPotion();
    private JButton healthPotionButton;
    private boolean hasFreezeAbility = false;
    private final Freeze freeze = new Freeze();
    private JButton freezeAbilityButton;
    private int currentRound = 0;
    private boolean secondShot = false;
    private BufferedImage casinoChip;
    private JLabel shootSign;
    private JLabel playerHealthLabel, enemyHealthLabel;
    private final GameGraphics gameGraphics;
    private JLabel lampLabel;
    private JLabel revolver1Label;
    private JLabel revolver2Label;
    private JLabel sparklingEffect1Label;
    private JLabel sparklingEffect2Label;
    private JLabel playerStandingLabel;
    private JLabel enemyStandingLabel;

    public GamePanel(JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.gameGraphics = menuPanel.getGameGraphics();
        setBackground(Color.BLACK);
        initUI();
        initializeRounds();
        applyRoundSettings();
    }

    public void startGame() {
        gameOver = false;
        playerLives = 3;
        enemyLives = 3;
        shotCount = 0;
        currentRound = 0;
        playerStarts = true;
        updateTurnLabel("Player");
        applyRoundSettings();
        shootSign.setVisible(true);
        updateHealthIcons();
        lampLabel.setVisible(true);
        hideRevolvers();
    }

    public void applyRoundSettings() {
        System.out.println("DEBUG: Applying round settings. Player Wins: " + playerWins);
        if (playerWins == 0) {
            disableShopAndItems();
        } else if (playerWins == 1) {
            enableShopAndItems();
        } else if (playerWins == 2) {
            playerLives = 1;
            enemyLives = 1;
            updateHealthIcons();
            enableShopAndItems();
        }
    }

    public void updateItemVisibility() {
        magnifyingGlassButton.setVisible(hasMagnifyingGlass);
        healthPotionButton.setVisible(healthPotion.getCount() > 0);
        freezeAbilityButton.setVisible(hasFreezeAbility);
    }

    public void setHasMagnifyingGlass(boolean has) {
        hasMagnifyingGlass = has;
        magnifyingGlass.acquireMagnifyingGlass();
        magnifyingGlassButton.setEnabled(has);
        updateItemVisibility();
    }

    public void setHasHealthPotion(boolean has) {
        if (has) {
            healthPotion.addPotion();
        }
        healthPotionButton.setEnabled(healthPotion.getCount() > 0);
        updateItemVisibility();
    }

    public void setHasFreezeAbility(boolean has) {
        hasFreezeAbility = has;
        freezeAbilityButton.setEnabled(has);
        updateItemVisibility();
    }

    private void initializeRounds() {
        roundLive = new boolean[6];
        int liveCount = 0;
        boolean forcedLive = false;

        for (int i = 0; i < roundLive.length; i++) {
            roundLive[i] = random.nextBoolean();

            if (roundLive[i]) {
                liveCount++;
            }
            System.out.println("DEBUG: Round " + i + " initialized as " + (roundLive[i] ? "LIVE" : "BLANK"));
        }
        if (liveCount == 0) {
            int randomRound = random.nextInt(roundLive.length);
            roundLive[randomRound] = true;
            forcedLive = true;
            System.out.println("DEBUG: Forcing round " + randomRound + " to LIVE because all were BLANK.");
        }
        magnifyingGlass.setNextRoundLive(roundLive[0]);
        System.out.println("DEBUG: Magnifying Glass set for Round 0 - " + (roundLive[0] ? "LIVE" : "BLANK") + (forcedLive ? " (Forced)" : ""));
    }

    private void updateTurnLabel(String text) {
        SwingUtilities.invokeLater(() -> {
            turnLabel.setText("Turn: " + text);
            boolean isPlayerTurn = text.equals("Player");
            playerButton.setEnabled(isPlayerTurn);
            enemyButton.setEnabled(isPlayerTurn);
            playerButton.setVisible(isPlayerTurn);
            enemyButton.setVisible(isPlayerTurn);
            magnifyingGlassButton.setEnabled(isPlayerTurn && hasMagnifyingGlass);
            healthPotionButton.setEnabled(isPlayerTurn && healthPotion.getCount() > 0 && playerLives < 3);
            freezeAbilityButton.setEnabled(isPlayerTurn && hasFreezeAbility);
            shopButton.setEnabled(isPlayerTurn);
            shopButton.setVisible(isPlayerTurn && playerWins > 0);
        });
    }

    private void prepareNextTurn(boolean isPlayer) {
        if (secondShot) {
            secondShot = false;
            updateTurnLabel("Player");
        } else if (freeze.isFrozen() && isPlayer) {
            secondShot = true;
            updateTurnLabel("Player");
        } else {
            playerStarts = isPlayer;
            if (isPlayer) {
                updateTurnLabel("Player");
            } else {
                updateTurnLabel("Enemy");
                playerButton.setEnabled(false);
                enemyButton.setEnabled(false);
                delayEnemyTurn();
            }
        }
        updateHealthPotionButtonState();
    }

    private void initUI() {
        setLayout(null);
        setupComponents();
        setupPlayerStanding();
        updateHealthPotionButtonState();
        shopButton.setVisible(false);
        loadCasinoChip();
        setupShootLabel();
        setupHealthIcons();
        updateHealthIcons();
        setupLamp();
        setupRevolver1();
        setupRevolver2();
        setupSparklingEffects();
    }

    private void loadCasinoChip() {
        casinoChip = gameGraphics.getCasinoChip();
    }

    private void setupShootLabel() {
        BufferedImage shootSignImage = gameGraphics.getShootSignImage();

        shootSign = new JLabel(new ImageIcon(shootSignImage));

        int frameCenterX = frame.getWidth() / 2;

        shootSign.setBounds(frameCenterX - 55, 150, shootSignImage.getWidth(), shootSignImage.getHeight());

        add(shootSign);
    }

    private void setupLamp() {
        BufferedImage lampImage = gameGraphics.getLamp();
        lampLabel = new JLabel(new ImageIcon(lampImage));
        int frameCenterX = frame.getWidth() / 2;
        lampLabel.setBounds(frameCenterX - 90, 0, lampImage.getWidth(), lampImage.getHeight());
        add(lampLabel);
    }

    private void setupRevolver1() {
        BufferedImage revolverImage = gameGraphics.getRevolver1Image();
        revolver1Label = new JLabel(new ImageIcon(revolverImage));
        revolver1Label.setBounds(668, 375, revolverImage.getWidth(), revolverImage.getHeight());
        revolver1Label.setVisible(false);
        add(revolver1Label);
    }

    private void setupRevolver2() {
        BufferedImage revolverImage = gameGraphics.getRevolver2Image();
        revolver2Label = new JLabel(new ImageIcon(revolverImage));
        revolver2Label.setBounds(668, 375, revolverImage.getWidth(), revolverImage.getHeight());
        revolver2Label.setVisible(false);
        add(revolver2Label);
    }

    private void setupPlayerStanding() {
        BufferedImage playerImage = gameGraphics.getStandingImage1();
        BufferedImage enemyImage = gameGraphics.getStandingImage2();

        playerStandingLabel = new JLabel(new ImageIcon(playerImage));
        enemyStandingLabel = new JLabel(new ImageIcon(enemyImage));

        playerStandingLabel.setBounds(470, 300, playerImage.getWidth(), playerImage.getHeight());
        enemyStandingLabel.setBounds(775, 300, enemyImage.getWidth(), enemyImage.getHeight());

        add(playerStandingLabel);
        add(enemyStandingLabel);
    }


    private void setupComponents() {
        setLayout(null);
        int frameCenterX = frame.getWidth() / 2;

        statusLabel = new JLabel("Waiting", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBounds(frameCenterX + 150, 100, 150, 30);
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel);

        turnLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setBounds(frameCenterX + 150, 50, 150, 30);
        turnLabel.setForeground(Color.WHITE);
        add(turnLabel);

        coinsLabel = new JLabel("" + FullRelease.GamePanel.CoinManager.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 40));
        coinsLabel.setForeground(Color.YELLOW);
        coinsLabel.setBounds(150, 45, 150, 30);
        add(coinsLabel);

        playerButton = gameGraphics.getPlayerButton();
        enemyButton = gameGraphics.getEnemyButton();
        shopButton = gameGraphics.getShopButton();

        playerButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                hideButtons(); // Hide all buttons when player button is pressed
                playRussianRoulette(true);
            }
        });

        enemyButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                hideButtons(); // Hide all buttons when enemy button is pressed
                playRussianRoulette(false);
                prepareNextTurn(false);
            }
        });

        shopButton.addActionListener(e -> {
            switchToShop();
            shopButton.setIcon(new ImageIcon(getClass().getResource("/SHOP Button.png")));
        });

        playerButton.setBounds(533, 300, playerButton.getPreferredSize().width, playerButton.getPreferredSize().height);
        add(playerButton);

        enemyButton.setBounds(860, 300, enemyButton.getPreferredSize().width, enemyButton.getPreferredSize().height);
        add(enemyButton);

        shopButton.setBounds(frameCenterX - 75, 500, shopButton.getPreferredSize().width, shopButton.getPreferredSize().height);
        add(shopButton);

        magnifyingGlassButton = createItemButton(gameGraphics.getMagnifyingGlassImage(), 275);
        magnifyingGlassButton.addActionListener(e -> useMagnifyingGlass());
        magnifyingGlassButton.setVisible(false);
        add(magnifyingGlassButton);

        healthPotionButton = createItemButton(gameGraphics.getHealthPotionImage(), 1075);
        healthPotionButton.addActionListener(e -> useHealthPotion());
        healthPotionButton.setVisible(false);
        add(healthPotionButton);

        freezeAbilityButton = createItemButton(gameGraphics.getFreezeImage(), 675);
        freezeAbilityButton.addActionListener(e -> useFreezeAbility());
        freezeAbilityButton.setVisible(false);
        add(freezeAbilityButton);
    }

    private void setupSparklingEffects() {
        BufferedImage sparklingEffectImage1 = gameGraphics.getSparklingEffect1();
        sparklingEffect1Label = new JLabel(new ImageIcon(sparklingEffectImage1));
        sparklingEffect1Label.setBounds(revolver1Label.getX(), revolver1Label.getY(), sparklingEffectImage1.getWidth(), sparklingEffectImage1.getHeight());
        sparklingEffect1Label.setVisible(false); // Initially hidden
        add(sparklingEffect1Label);

        BufferedImage sparklingEffectImage2 = gameGraphics.getSparklingEffect2();
        sparklingEffect2Label = new JLabel(new ImageIcon(sparklingEffectImage2));
        sparklingEffect2Label.setBounds(revolver2Label.getX(), revolver2Label.getY(), sparklingEffectImage2.getWidth(), sparklingEffectImage2.getHeight());
        sparklingEffect2Label.setVisible(false); // Initially hidden
        add(sparklingEffect2Label);

        // Debug information
        System.out.println("Initial setup for sparkling effects completed.");
    }

    public void updateSparklingEffect1Coordinates(int x, int y) {
        sparklingEffect1Label.setBounds(x, y, sparklingEffect1Label.getWidth(), sparklingEffect1Label.getHeight());
        sparklingEffect1Label.setVisible(true);  // Make sure it's visible
        System.out.println("DEBUG: Updated SparklingEffect1 coordinates to x=" + x + ", y=" + y);
    }

    public void updateSparklingEffect2Coordinates(int x, int y) {
        sparklingEffect2Label.setBounds(x, y, sparklingEffect2Label.getWidth(), sparklingEffect2Label.getHeight());
        sparklingEffect2Label.setVisible(true);  // Make sure it's visible
        System.out.println("DEBUG: Updated SparklingEffect2 coordinates to x=" + x + ", y=" + y);
    }

    private boolean isWithinBounds(int x, int y, JLabel label) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int labelWidth = label.getWidth();
        int labelHeight = label.getHeight();
        return (x >= 0 && x + labelWidth <= panelWidth) && (y >= 0 && y + labelHeight <= panelHeight);
    }

    private void showRevolver1WithSparkle() {
        int x = 785;
        int y = 360;
        if (isWithinBounds(x, y, sparklingEffect1Label)) {
            updateSparklingEffect1Coordinates(x, y);
        } else {
            System.out.println("Coordinates out of bounds for SparklingEffect1.");
        }
        revolver1Label.setVisible(true);
    }

    private void showRevolver2WithSparkle() {
        int x = 625; // Example coordinates
        int y = 360;
        if (isWithinBounds(x, y, sparklingEffect2Label)) {
            updateSparklingEffect2Coordinates(x, y);
        } else {
            System.out.println("Coordinates out of bounds for SparklingEffect2.");
        }
        revolver2Label.setVisible(true);
    }

    private void setupHealthIcons() {
        playerHealthLabel = new JLabel();
        playerHealthLabel.setBounds(400, 375, 50, 75);
        add(playerHealthLabel);

        enemyHealthLabel = new JLabel();
        enemyHealthLabel.setBounds(1020, 375, 50, 75);
        add(enemyHealthLabel);

        updateHealthIcons();
    }

    private void updateHealthIcons() {
        playerHealthLabel.setIcon(getHealthIcon(playerLives));
        enemyHealthLabel.setIcon(getHealthIcon(enemyLives));
    }

    private Icon getHealthIcon(int lives) {
        return switch (lives) {
            case 1 -> new ImageIcon(gameGraphics.getHealthClock1());
            case 2 -> new ImageIcon(gameGraphics.getHealthClock2());
            case 3 -> new ImageIcon(gameGraphics.getHealthClock3());
            default -> null;
        };
    }

    private JButton createItemButton(BufferedImage image, int x) {
        JButton button = new JButton(new ImageIcon(image));
        button.setBounds(x, 700, image.getWidth(), image.getHeight());
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    public void useFreezeAbility() {
        if (hasFreezeAbility && !freeze.isFrozen() && playerStarts) {
            freeze.activateFreeze();
            updateStatus("Freeze activated! Enemy turn skipped.", true);
            freezeAbilityButton.setVisible(false);
            hasFreezeAbility = false;
            updateItemVisibility();
            prepareNextTurn(true);
        } else {
            updateStatus("Cannot use Freeze Time!", false);
        }
    }

    public void useMagnifyingGlass() {
        if (hasMagnifyingGlass) {
            String previewMessage = magnifyingGlass.checkRound();
            JOptionPane.showMessageDialog(frame, previewMessage, "Magnifying Glass Result", JOptionPane.INFORMATION_MESSAGE);
            magnifyingGlassButton.setVisible(false);
            hasMagnifyingGlass = false;
            updateItemVisibility();
        } else {
            JOptionPane.showMessageDialog(frame, "No magnifying glass available!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void useHealthPotion() {
        if (playerStarts && healthPotion.getCount() > 0 && playerLives < 3) {
            boolean used = healthPotion.usePotion();
            if (used) {
                playerLives++;
                updateStatus("Health restored by 1!", true);
                healthPotionButton.setVisible(false);
                updateHealthIcons(); // Update the health icons
            }
        } else {
            String message = "It's not your turn!" + (healthPotion.getCount() > 0 ? "" : " No health potions available!");
            updateStatus(message, false);
        }
        updateHealthPotionButtonState();
    }

    private void updateHealthPotionButtonState() {
        healthPotionButton.setEnabled(canUseHealthPotion());
    }

    private boolean canUseHealthPotion() {
        return playerStarts && healthPotion.getCount() > 0 && playerLives < 3;
    }

    private void updateStatus(String text, boolean isSurvived) {
        Color textColor;
        if (text.contains("was shot")) {
            textColor = Color.RED;
        } else {
            if (isSurvived) {
                textColor = Color.GREEN;
            } else {
                textColor = Color.RED;
            }
        }

        SwingUtilities.invokeLater(() -> {
            statusLabel.setForeground(textColor);
            statusLabel.setText(text);
        });
    }

    private void showRevolver1() {
        revolver1Label.setVisible(true);
        sparklingEffect1Label.setVisible(false);
        System.out.println("DEBUG: Showing revolver 1 without sparkle");
    }

    private void showRevolver2() {
        revolver2Label.setVisible(true);
        sparklingEffect2Label.setVisible(false);
        System.out.println("DEBUG: Showing revolver 2 without sparkle");
    }

    private void hideRevolvers() {
        revolver1Label.setVisible(false);
        sparklingEffect1Label.setVisible(false);
        revolver2Label.setVisible(false);
        sparklingEffect2Label.setVisible(false);
        System.out.println("DEBUG: Hiding all revolvers");
    }

    private void hideSparklingEffects() {
        sparklingEffect1Label.setVisible(false);
        sparklingEffect2Label.setVisible(false);
        System.out.println("DEBUG: Hiding sparkling effects");
    }

    private void showRevolverWithSparkle(boolean isPlayer, boolean currentRoundIsLive) {
        if (currentRoundIsLive) {
            if (isPlayer) {
                showRevolver2WithSparkle();
            } else {
                showRevolver1WithSparkle();
            }
        } else {
            if (isPlayer) {
                showRevolver2();
            } else {
                showRevolver1();
            }
        }
    }

    private void hideButtonsWithDelay() {
        hideButtons();

        Timer showButtonsTimer = new Timer(3000, e -> {
            showButtons();
            ((Timer) e.getSource()).stop();
        });
        showButtonsTimer.setRepeats(false);
        showButtonsTimer.start();
    }

    private void hideButtons() {
        playerButton.setVisible(false);
        enemyButton.setVisible(false);
        shopButton.setVisible(false);
        magnifyingGlassButton.setVisible(false);
        healthPotionButton.setVisible(false);
        freezeAbilityButton.setVisible(false);
    }

    private void showButtons() {
        playerButton.setVisible(true);
        enemyButton.setVisible(true);
        shopButton.setVisible(playerWins > 0);
        magnifyingGlassButton.setVisible(hasMagnifyingGlass);
        healthPotionButton.setVisible(healthPotion.getCount() > 0);
        freezeAbilityButton.setVisible(hasFreezeAbility);
    }

    private void playRussianRoulette(boolean isPlayer) {
        if (gameOver) {
            return;
        }
        hideRevolvers();
        boolean shotFired = random.nextInt(7 - shotCount) == 0;
        shotCount++;

        System.out.println("DEBUG: Shot fired: " + shotFired);
        System.out.println("DEBUG: Shot count: " + shotCount);

        boolean currentRoundIsLive = roundLive[currentRound];
        System.out.println("DEBUG: Current round " + currentRound + " is " + (currentRoundIsLive ? "LIVE" : "BLANK"));

        if (shotFired) {
            if (isPlayer) {
                playerLives--;
                updateHealthIcons();
                updateCoinsLabel();
                FullRelease.GamePanel.CoinManager.addCoins(-1);
            } else {
                enemyLives--;
                updateHealthIcons();
                updateCoinsLabel();
                FullRelease.GamePanel.CoinManager.addCoins(2);
            }

            // Check for game over
            if (isPlayer && playerLives <= 0) {
                gameOver = true;
                updateStatus("Player died", false);
                handleGameOver(true);
            } else if (!isPlayer && enemyLives <= 0) {
                gameOver = true;
                updateStatus("Enemy died", false);
                handleGameOver(false);
            } else {
                hideButtonsWithDelay();
                updateStatus((isPlayer ? "Player" : "Enemy") + " was shot", currentRoundIsLive);
                showRevolverWithSparkle(isPlayer, currentRoundIsLive);
                Timer timer = new Timer(3000, e -> {
                    hideRevolvers();
                    hideSparklingEffects();
                    prepareNextTurn(isPlayer);
                });
                timer.setRepeats(false);
                timer.start();
            }
            shotCount = 0;
        } else {
            if (shotCount == 6) {
                playRussianRoulette(isPlayer);
            } else {
                updateStatus(isPlayer ? "Player survives." : "Enemy survives.", true);
                prepareNextTurn(isPlayer || checkPlayerContinuation());
                if (isPlayer) {
                    showRevolver2();
                } else {
                    showRevolver1();
                }
            }
        }

        advanceRound();
    }

    private void advanceRound() {
        currentRound++;
        if (currentRound >= roundLive.length) {
            currentRound = 0;
        }
        magnifyingGlass.setNextRoundLive(roundLive[currentRound]);
        System.out.println("DEBUG: Magnifying Glass updated - Next Round " + currentRound + " is " + (roundLive[currentRound] ? "LIVE" : "BLANK"));
        applyRoundSettings();
    }

    private void enemyTakeTurn() {
        if (gameOver) {
            return;
        }

        boolean decidesToShootPlayer = random.nextInt(2) == 0;

        Timer decisionDelay = new Timer(1000, e -> {
            playRussianRoulette(!decidesToShootPlayer);
            ((Timer) e.getSource()).stop();
        });
        decisionDelay.setRepeats(false);
        decisionDelay.start();
    }

    private void delayEnemyTurn() {
        Timer delayTimer = new Timer(1000, e -> {
            enemyTakeTurn();
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private boolean checkPlayerContinuation() {
        return playerStarts;
    }

    private void updateCoinsLabel() {
        SwingUtilities.invokeLater(() -> coinsLabel.setText("" + FullRelease.GamePanel.CoinManager.getCoins()));
    }

    private void handleGameOver(boolean playerDied) {
        String gameOverMessage;
        if (playerDied) {
            gameOverMessage = "YOU DIED";
        } else {
            gameOverMessage = "YOU WIN!";
            System.out.println("DEBUG: Player wins updated. Player Wins: " + playerWins);
            applyRoundSettings();
        }
        updateStatusGameOver(gameOverMessage);
        playerButton.setEnabled(false);
        enemyButton.setEnabled(false);

        new Timer(2000, e -> {
            if (!playerDied) {
                if (playerWins < 3) {
                    switchToElevatorPanel();
                } else {
                    switchToMenu();
                }
            } else {
                FullRelease.GamePanel.CoinManager.resetCoins();
                switchToMenu();
            }
            ((Timer) e.getSource()).stop();
        }).start();
    }

    private void disableShopAndItems() {
        System.out.println("DEBUG: Disabling shop and items.");
        shopButton.setVisible(false);
        magnifyingGlassButton.setVisible(false);
        healthPotionButton.setVisible(false);
        freezeAbilityButton.setVisible(false);
    }

    private void enableShopAndItems() {
        System.out.println("DEBUG: Enabling shop and items.");
        shopButton.setVisible(true);
        magnifyingGlassButton.setVisible(hasMagnifyingGlass);
        healthPotionButton.setVisible(healthPotion.getCount() > 0);
        freezeAbilityButton.setVisible(hasFreezeAbility);
    }

    private void updateStatusGameOver(String gameOverMessage) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(gameOverMessage));
    }

    private void switchToMenu() {
        FullRelease.GamePanel.CoinManager.resetCoins();
        FullRelease.MenuPanel menuPanel = new FullRelease.MenuPanel(frame);
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void switchToElevatorPanel() {
        ElevatorPanel elevatorPanel = new ElevatorPanel(gameGraphics, this, frame);
        frame.setContentPane(elevatorPanel);
        frame.revalidate();
        frame.repaint();
        playerWins++;
    }

    public static class CoinManager {
        private static int coins = 0;

        public static void addCoins(int amount) {
            coins += amount;
        }

        public static void resetCoins() {
            coins = 0;
        }

        public static int getCoins() {
            return coins;
        }
    }

    public void updateCoinsDisplay() {
        SwingUtilities.invokeLater(() -> coinsLabel.setText("" + FullRelease.GamePanel.CoinManager.getCoins()));
    }

    private void switchToShop() {
        ShopPanel shopPanel = new ShopPanel(frame, this, gameGraphics);
        frame.setContentPane(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (casinoChip != null) {
            g.drawImage(casinoChip, 10, 10, null);
        }
    }
}