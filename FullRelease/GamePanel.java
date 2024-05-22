package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GamePanel extends JPanel {
    private final JFrame frame;
    private final MenuPanel menuPanel;
    private JButton playerButton, enemyButton, shopButton;
    private JLabel statusLabel, turnLabel, playerLivesLabel, enemyLivesLabel;
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
    private JButton freezeAbilityButton;
    private boolean hasFreezeAbility = false;
    private final Freeze freeze = new Freeze();
    private int currentRound = 0;
    private boolean secondShot = false;
    private BufferedImage casinoChip;
    private JLabel shootSign;

    public GamePanel(JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
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
    }

    public void applyRoundSettings() {
        System.out.println("DEBUG: Applying round settings. Player Wins: " + Game.playerWins);
        if (Game.playerWins == 0) {
            disableShopAndItems();
        } else if (Game.playerWins == 1) {
            enableShopAndItems();
        } else if (Game.playerWins == 2) {
            playerLives = 1;
            enemyLives = 1;
            playerLivesLabel.setText("Player Lives: " + playerLives);
            enemyLivesLabel.setText("Enemy Lives: " + enemyLives);
            enableShopAndItems();
        }
    }

    private void disableShopAndItems() {
        System.out.println("DEBUG: Disabling shop and items.");
        shopButton.setVisible(true);
        magnifyingGlassButton.setVisible(false);
        healthPotionButton.setVisible(false);
        freezeAbilityButton.setVisible(false);
    }

    private void enableShopAndItems() {
        System.out.println("DEBUG: Enabling shop and items.");
        shopButton.setVisible(true);
        magnifyingGlassButton.setVisible(true);
        healthPotionButton.setVisible(true);
        freezeAbilityButton.setVisible(true);
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
            shopButton.setVisible(isPlayerTurn);
            shootSign.setVisible(isPlayerTurn);
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
        setupBall(-50, Color.BLUE);
        setupBall(75, Color.RED);
        updateHealthPotionButtonState();
        shopButton.setVisible(false);
        loadCasinoChip();
        setupShootLabel();
    }

    private void loadCasinoChip() {
        GameGraphics gameGraphics = menuPanel.getGameGraphics();
        casinoChip = gameGraphics.getCasinoChip();
    }

    private void setupShootLabel() {
        GameGraphics gameGraphics = menuPanel.getGameGraphics();
        BufferedImage shootSignImage = gameGraphics.getShootSignImage();

        shootSign = new JLabel(new ImageIcon(shootSignImage));

        int frameCenterX = frame.getWidth() / 2;

        shootSign.setBounds(frameCenterX - 55, 150, shootSignImage.getWidth(), shootSignImage.getHeight());

        add(shootSign);

    }

    private void setupBall(int x, Color color) {
        JLabel ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(color);

        int adjustedX = frame.getWidth() / 2 + x - 25;
        ball.setBounds(adjustedX, 400, 50, 50);
        this.add(ball);
    }

    private void setupComponents() {
        setLayout(null);
        int frameCenterX = frame.getWidth() / 2;

        statusLabel = new JLabel("Waiting", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBounds(frameCenterX - 75, 100, 150, 30);
        add(statusLabel);

        turnLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setBounds(frameCenterX - 75, 50, 150, 30);
        add(turnLabel);

        playerLivesLabel = new JLabel("Player Lives: " + playerLives);
        playerLivesLabel.setBounds(frameCenterX - 175, 10, 150, 30);
        add(playerLivesLabel);

        enemyLivesLabel = new JLabel("Enemy Lives: " + enemyLives);
        enemyLivesLabel.setBounds(frameCenterX + 25, 10, 150, 30);
        add(enemyLivesLabel);

        coinsLabel = new JLabel("" + FullRelease.GamePanel.CoinManager.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 40));
        coinsLabel.setForeground(Color.YELLOW);
        coinsLabel.setBounds(150, 45, 150, 30);
        add(coinsLabel);

        GameGraphics gameGraphics = menuPanel.getGameGraphics();
        playerButton = gameGraphics.getPlayerButton();
        enemyButton = gameGraphics.getEnemyButton();
        shopButton = gameGraphics.getShopButton();

        playerButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(true);
            }
        });

        enemyButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(false);
                prepareNextTurn(false);
            }
        });

        shopButton.addActionListener(e -> {
            switchToShop();
            shopButton.setIcon(new ImageIcon(getClass().getResource("/SHOP Button.png")));
        });

        playerButton.setBounds(frameCenterX - 87, 300, playerButton.getPreferredSize().width, playerButton.getPreferredSize().height);
        add(playerButton);

        enemyButton.setBounds(frameCenterX + 40, 300, enemyButton.getPreferredSize().width, enemyButton.getPreferredSize().height);
        add(enemyButton);

        shopButton.setBounds(frameCenterX - 75, 500, shopButton.getPreferredSize().width, shopButton.getPreferredSize().height);
        add(shopButton);

        magnifyingGlassButton = new JButton("Use Magnifying Glass");
        magnifyingGlassButton.setBounds(275, 400, 200, 30);
        magnifyingGlassButton.addActionListener(e -> useMagnifyingGlass());
        magnifyingGlassButton.setEnabled(false);
        add(magnifyingGlassButton);

        healthPotionButton = new JButton("Use Health Potion");
        healthPotionButton.setBounds(275, 350, 200, 30);
        healthPotionButton.addActionListener(e -> useHealthPotion());
        healthPotionButton.setEnabled(false);
        add(healthPotionButton);

        freezeAbilityButton = new JButton("Use Freeze");
        freezeAbilityButton.setBounds(275, 300, 200, 30);
        freezeAbilityButton.addActionListener(e -> useFreezeAbility());
        freezeAbilityButton.setEnabled(false);
        add(freezeAbilityButton);
    }

    public void useFreezeAbility() {
        if (hasFreezeAbility && !freeze.isFrozen() && playerStarts) {
            freeze.activateFreeze();
            updateStatus("Freeze activated! Enemy turn skipped.", true);
            prepareNextTurn(true);
        } else {
            updateStatus("Cannot use Freeze Time!", false);
        }
    }

    public void setHasFreezeAbility(boolean has) {
        hasFreezeAbility = has;
        freezeAbilityButton.setEnabled(has);
    }

    public void useMagnifyingGlass() {
        if (hasMagnifyingGlass) {
            String previewMessage = magnifyingGlass.checkRound();
            JOptionPane.showMessageDialog(frame, previewMessage, "Magnifying Glass Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No magnifying glass available!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setHasMagnifyingGlass(boolean has) {
        hasMagnifyingGlass = has;
        magnifyingGlass.acquireMagnifyingGlass();
        magnifyingGlassButton.setEnabled(has);
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

    private void playRussianRoulette(boolean isPlayer) {
        if (gameOver) {
            return;
        }
        boolean shotFired = random.nextInt(7 - shotCount) == 0;
        shotCount++;

        if (shotFired) {
            triggerLiveShot(isPlayer);
            shotCount = 0;
        } else {
            if (shotCount == 6) {
                triggerLiveShot(isPlayer);
                shotCount = 0;
            } else {
                updateStatus(isPlayer ? "Player survives." : "Enemy survives.", true);
                prepareNextTurn(isPlayer || checkPlayerContinuation());
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

        Timer decisionDelay = new Timer(2000, e -> {
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

    private void triggerLiveShot(boolean isPlayer) {
        boolean currentRoundIsLive = roundLive[currentRound];
        System.out.println("DEBUG: Current round " + currentRound + " is " + (currentRoundIsLive ? "LIVE" : "BLANK"));

        if (isPlayer) {
            playerLives--;
            playerLivesLabel.setText("Player Lives: " + playerLives);
            updateCoinsLabel();
            FullRelease.GamePanel.CoinManager.addCoins(-1);
            if (playerLives <= 0) {
                gameOver = true;
                updateStatus("Player died", false);
                handleGameOver(true);
            } else {
                updateStatus("Player was " + (currentRoundIsLive ? "shot" : "safe"), currentRoundIsLive);
                prepareNextTurn(true);
            }
        } else {
            enemyLives--;
            enemyLivesLabel.setText("Enemy Lives: " + enemyLives);
            updateCoinsLabel();
            FullRelease.GamePanel.CoinManager.addCoins(2);
            if (enemyLives <= 0) {
                Game.playerWins++;
                gameOver = true;
                updateStatus("Enemy died", false);
                handleGameOver(false);
            } else {
                updateStatus("Enemy was " + (currentRoundIsLive ? "shot" : "safe"), currentRoundIsLive);
                prepareNextTurn(true);
            }
        }
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
            System.out.println("DEBUG: Player wins updated. Player Wins: " + Game.playerWins);
            applyRoundSettings();
        }
        updateStatusGameOver(gameOverMessage);
        playerButton.setEnabled(false);
        enemyButton.setEnabled(false);

        new Timer(2000, e -> {
            if (!playerDied) {
                if (Game.playerWins < 3) {
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

    public void useHealthPotion() {
        if (playerStarts && healthPotion.getCount() > 0 && playerLives < 3) {
            boolean used = healthPotion.usePotion();
            if (used) {
                playerLives++;
                playerLivesLabel.setText("Player Lives: " + playerLives);
                updateStatus("Health restored by 1!", true);
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

    public void setHasHealthPotion() {
        healthPotion.addPotion();
        healthPotionButton.setEnabled(true);
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
        GameGraphics gameGraphics = menuPanel.getGameGraphics();
        ElevatorPanel elevatorPanel = new ElevatorPanel(gameGraphics, this, frame);
        frame.setContentPane(elevatorPanel);
        frame.revalidate();
        frame.repaint();
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
        GameGraphics gameGraphics = menuPanel.getGameGraphics();
        FullRelease.ShopPanel shopPanel = new FullRelease.ShopPanel(frame, this, gameGraphics);
        frame.setContentPane(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (casinoChip != null) {
            g.drawImage(casinoChip, 10, 10, null);
        }
    }
}
