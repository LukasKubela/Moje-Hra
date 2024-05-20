package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel {
    private final JFrame frame;
    private final FullRelease.MenuPanel menuPanel;
    private JButton playerButton, enemyButton, shopButton;
    private JLabel statusLabel, turnLabel, playerLivesLabel, enemyLivesLabel;
    private final Random random = new Random();
    private int playerLives = 3;
    private int enemyLives = 3;
    private boolean gameOver = false;
    private boolean playerStarts;
    private int shotCount = 0;
    private JLabel coinsLabel;
    private final boolean hasMagnifyingGlass = false;
    private JButton magnifyingGlassButton;
    private boolean[] roundLive;
    private final MagnifyingGlass magnifyingGlass = new MagnifyingGlass();
    private final HealthPotion healthPotion = new HealthPotion();
    private JButton healthPotionButton;
    private JButton freezeAbilityButton;
    private boolean hasFreezeAbility = false;
    private boolean freezeAbilityUsed = false;
    private int currentRound = 0;

    public GamePanel(JFrame frame, FullRelease.MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        initUI();
        randomStart();
        initializeRounds();
        applySettings();
    }

    public void applySettings() {
        boolean isEnabled = FullRelease.SettingsPanel.isShowShopAndUseItemsEnabled();
        
        shopButton.setVisible(isEnabled);
        magnifyingGlassButton.setVisible(isEnabled);
        healthPotionButton.setVisible(isEnabled);
        freezeAbilityButton.setVisible(isEnabled);

        magnifyingGlassButton.setEnabled(isEnabled && hasMagnifyingGlass);
        healthPotionButton.setEnabled(isEnabled && healthPotion.getCount() > 0 && canUseHealthPotion());
        freezeAbilityButton.setEnabled(isEnabled && hasFreezeAbility);
    }

    private boolean canUseHealthPotion() {
        return playerStarts && healthPotion.getCount() > 0 && playerLives < 3;
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

            // Debug: Print each round's status as it's initialized
            System.out.println("DEBUG: Round " + i + " initialized as " + (roundLive[i] ? "LIVE" : "BLANK"));
        }

        // Ensure at least one live round
        if (liveCount == 0) {
            int randomRound = random.nextInt(roundLive.length);
            roundLive[randomRound] = true;
            forcedLive = true;
            System.out.println("DEBUG: Forcing round " + randomRound + " to LIVE because all were BLANK.");
        }

        // Set the initial round status in the magnifying glass
        magnifyingGlass.setNextRoundLive(roundLive[0]);
        System.out.println("DEBUG: Magnifying Glass set for Round 0 - " + (roundLive[0] ? "LIVE" : "BLANK") + (forcedLive ? " (Forced)" : ""));
    }



    private void randomStart() {
        playerStarts = random.nextBoolean();
        if (playerStarts) {
            updateTurnLabel("Player");
        } else {
            updateTurnLabel("Enemy");
        }
        if (!playerStarts) {
            delayEnemyTurn();
        }
    }

    private void updateTurnLabel(String text) {
        SwingUtilities.invokeLater(() -> {
            turnLabel.setText("Turn: " + text);
            boolean isPlayerTurn = text.equals("Player");
            playerButton.setEnabled(isPlayerTurn);
            enemyButton.setEnabled(isPlayerTurn);
            shopButton.setEnabled(isPlayerTurn);
            magnifyingGlassButton.setEnabled(isPlayerTurn && hasMagnifyingGlass);
            healthPotionButton.setEnabled(isPlayerTurn && healthPotion.getCount() > 0 && playerLives < 3);
        });
    }

    private void prepareNextTurn(boolean isPlayer) {
        if (freezeAbilityUsed) {
            freezeAbilityUsed = false;
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
    }

    private void setupBall(int x, Color color) {
        JLabel ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(color);

        int adjustedX = frame.getWidth() / 2 + x - 25;
        ball.setBounds(adjustedX, 250, 50, 50);
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


        coinsLabel = new JLabel("Coins: " + FullRelease.GamePanel.CoinManager.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinsLabel.setBounds(frameCenterX - 75, 400, 150, 30);
        add(coinsLabel);


        playerButton = new JButton("YOU");
        playerButton.setBounds(frameCenterX - 100, 200, 100, 30);
        playerButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(true);
            }
        });
        add(playerButton);


        enemyButton = new JButton("ENEMY");
        enemyButton.setBounds(frameCenterX + 25, 200, 100, 30);
        enemyButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(false);
                prepareNextTurn(false);
            }
        });
        add(enemyButton);

        playerButton.setEnabled(playerStarts);
        enemyButton.setEnabled(playerStarts);

        shopButton = new JButton("Shop");
        shopButton.setBounds(frameCenterX - 75, 450, 150, 30);
        shopButton.addActionListener(e -> switchToShop());
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
        if (hasFreezeAbility && !freezeAbilityUsed && playerStarts) {
            freezeAbilityUsed = true;
            updateStatus("Freeze activated! Extra turn granted.", true);
        } else {
            updateStatus("Cannot use Freeze Time!", false);
        }
    }

    public void setHasFreezeAbility(boolean has) {
        hasFreezeAbility = has;
        freezeAbilityButton.setEnabled(has);  // Enable button if ability is acquired
    }

    private void useMagnifyingGlass() {
        if (magnifyingGlass.hasMagnifyingGlass()) {
            // Fetch and display the debug information before showing it to the player
            boolean currentStatus = magnifyingGlass.getNextRoundLive();
            System.out.println("DEBUG: Before checking - Magnifying Glass was told the next round is " + (currentStatus ? "LIVE" : "BLANK"));

            String previewMessage = magnifyingGlass.checkRound();
            JOptionPane.showMessageDialog(frame, previewMessage, "Magnifying Glass Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No magnifying glass available!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setHasMagnifyingGlass(boolean has) {
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
            currentRound = 0; // Loop rounds or handle as game design requires
        }
        magnifyingGlass.setNextRoundLive(roundLive[currentRound]);
        System.out.println("DEBUG: Magnifying Glass updated - Next Round " + currentRound + " is " + (roundLive[currentRound] ? "LIVE" : "BLANK"));
    }



    private void enemyTakeTurn() {
        if (gameOver){
            return;
        }

        boolean decidesToShootPlayer = random.nextInt(2) == 0;

        Timer decisionDelay = new Timer(2000, e -> {
            playRussianRoulette(!decidesToShootPlayer); // pokud decidesToShootPlayer je true, nepřítel střílí na sebe, jinak na hráče
            ((Timer)e.getSource()).stop(); // zastavení časovače po jednom spuštění
        });
        decisionDelay.setRepeats(false); // nastaví časovač, aby se spustil pouze jednou, tedy neopakoval svůj běh
        decisionDelay.start(); // spustí časovač
    }

    private void delayEnemyTurn() {
        Timer delayTimer = new Timer(1000, e -> {
            enemyTakeTurn(); // Spustí tah nepřítele po uplynutí zpoždění
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void triggerLiveShot(boolean isPlayer) {
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
                updateStatus("Player was shot", false);
                prepareNextTurn(true);
            }
        } else {
            enemyLives--;
            enemyLivesLabel.setText("Enemy Lives: " + enemyLives);
            updateCoinsLabel();
            FullRelease.GamePanel.CoinManager.addCoins(2);
            if (enemyLives <= 0) {
                gameOver = true;
                updateStatus("Enemy died", false);
                handleGameOver(false);
            } else {
                updateStatus("Enemy was shot", true);
                prepareNextTurn(true);
            }
        }
    }

    private boolean checkPlayerContinuation() { // Kontroluje, zda je další tah pro hráče
        return playerStarts;
    }

    private void updateCoinsLabel() {
        SwingUtilities.invokeLater(() -> coinsLabel.setText("Coins: " + FullRelease.GamePanel.CoinManager.getCoins())); // Aktualizuje zobrazení počtu mincí v rozhraní
    }

    private void handleGameOver(boolean playerDied) {
        String gameOverMessage;
        if (playerDied) {
            gameOverMessage = "YOU DIED";
        } else {
            gameOverMessage = "YOU WIN!";
        }
        updateStatusGameOver(gameOverMessage);
        playerButton.setEnabled(false);
        enemyButton.setEnabled(false);

        new Timer(2000, e -> {
            if (!playerDied) {
                switchToElevatorPanel();
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
        updateHealthPotionButtonState(); // Call this method to ensure button state is updated
    }
    private void updateHealthPotionButtonState() {
        boolean isEnabled = FullRelease.SettingsPanel.isShowShopAndUseItemsEnabled();
        healthPotionButton.setVisible(isEnabled);
        healthPotionButton.setEnabled(isEnabled && canUseHealthPotion());
    }

    public void setHasHealthPotion() {
        healthPotion.addPotion();  // Assume addPotion handles both adding and setting has condition
        updateHealthPotionButtonState();
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
        FullRelease.ElevatorPanel gamePanel = new FullRelease.ElevatorPanel(frame, menuPanel);
        frame.getContentPane().removeAll();
        frame.setContentPane(gamePanel);
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
        SwingUtilities.invokeLater(() -> coinsLabel.setText("Coins: " + FullRelease.GamePanel.CoinManager.getCoins()));
    }

    private void switchToShop() {
        FullRelease.ShopPanel shopPanel = new FullRelease.ShopPanel(frame, this);
        frame.setContentPane(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
