package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static FullRelease.Game.playerWins;

public class GamePanel extends JPanel {
    private final JFrame frame;
    private final GameLogic gameLogic;
    private JButton playerButton, enemyButton, shopButton;
    private JLabel statusLabel, turnLabel;
    private JLabel coinsLabel;
    private JButton magnifyingGlassButton;
    private JButton healthPotionButton;
    private JButton freezeAbilityButton;
    private BufferedImage casinoChip;
    private JLabel shootSign;
    private JLabel playerHealthLabel, enemyHealthLabel;
    private final GameGraphics gameGraphics;
    private JLabel lampLabel;
    private JLabel revolver1Label;
    private JLabel revolver2Label;
    private JLabel sparklingEffect1Label;
    private JLabel sparklingEffect2Label;

    public GamePanel(JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.gameGraphics = menuPanel.getGameGraphics();
        setBackground(Color.BLACK);
        initUI();
        this.gameLogic = new GameLogic(this);
        setupHealthIcons();
    }

    public JFrame getFrame() {
        return frame;
    }

    public JLabel getShootSign() {
        return shootSign;
    }

    public JLabel getLampLabel() {
        return lampLabel;
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

    public JButton getMagnifyingGlassButton() {
        return magnifyingGlassButton;
    }

    public JButton getHealthPotionButton() {
        return healthPotionButton;
    }

    public JButton getFreezeAbilityButton() {
        return freezeAbilityButton;
    }

    public void startGame() {
        gameLogic.startGame();
    }

    public void updateItemVisibility() {
        magnifyingGlassButton.setVisible(gameLogic.hasMagnifyingGlass());
        healthPotionButton.setVisible(gameLogic.getHealthPotionCount() > 0);
        freezeAbilityButton.setVisible(gameLogic.hasFreezeAbility());
    }

    public void setHasMagnifyingGlass(boolean has) {
        gameLogic.setHasMagnifyingGlass(has);
    }

    public void setHasHealthPotion(boolean has) {
        gameLogic.setHasHealthPotion(has);
    }

    public void setHasFreezeAbility(boolean has) {
        gameLogic.setHasFreezeAbility(has);
    }

    void updateTurnLabel(String text) {
        SwingUtilities.invokeLater(() -> {
            turnLabel.setText("Turn: " + text);
            boolean isPlayerTurn = text.equals("Player");
            playerButton.setEnabled(isPlayerTurn);
            enemyButton.setEnabled(isPlayerTurn);
            playerButton.setVisible(isPlayerTurn);
            enemyButton.setVisible(isPlayerTurn);
            magnifyingGlassButton.setEnabled(isPlayerTurn && gameLogic.hasMagnifyingGlass());
            healthPotionButton.setEnabled(isPlayerTurn && gameLogic.getHealthPotionCount() > 0 && gameLogic.getPlayerLives() < 3);
            freezeAbilityButton.setEnabled(isPlayerTurn && gameLogic.hasFreezeAbility());
            shopButton.setEnabled(isPlayerTurn);
            shopButton.setVisible(isPlayerTurn && playerWins > 0);
        });
    }

    private void initUI() {
        setLayout(null);
        setupComponents();
        setupPlayerStanding();
        loadCasinoChip();
        setupShootLabel();
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

        JLabel playerStandingLabel = new JLabel(new ImageIcon(playerImage));
        JLabel enemyStandingLabel = new JLabel(new ImageIcon(enemyImage));

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
            if (gameLogic.isGameOver() && gameLogic.isPlayerStarts()) {
                hideButtons();
                gameLogic.playRussianRoulette(true);
            }
        });

        enemyButton.addActionListener(e -> {
            if (gameLogic.isGameOver() && gameLogic.isPlayerStarts()) {
                hideButtons();
                gameLogic.playRussianRoulette(false);
                gameLogic.prepareNextTurn(false);
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
        magnifyingGlassButton.addActionListener(e -> gameLogic.useMagnifyingGlass());
        magnifyingGlassButton.setVisible(false);
        add(magnifyingGlassButton);

        healthPotionButton = createItemButton(gameGraphics.getHealthPotionImage(), 1075);
        healthPotionButton.addActionListener(e -> gameLogic.useHealthPotion());
        healthPotionButton.setVisible(false);
        add(healthPotionButton);

        freezeAbilityButton = createItemButton(gameGraphics.getFreezeImage(), 675);
        freezeAbilityButton.addActionListener(e -> gameLogic.useFreezeAbility());
        freezeAbilityButton.setVisible(false);
        add(freezeAbilityButton);
    }

    private void setupSparklingEffects() {
        BufferedImage sparklingEffectImage1 = gameGraphics.getSparklingEffect1();
        sparklingEffect1Label = new JLabel(new ImageIcon(sparklingEffectImage1));
        sparklingEffect1Label.setBounds(revolver1Label.getX(), revolver1Label.getY(), sparklingEffectImage1.getWidth(), sparklingEffectImage1.getHeight());
        sparklingEffect1Label.setVisible(false);
        add(sparklingEffect1Label);

        BufferedImage sparklingEffectImage2 = gameGraphics.getSparklingEffect2();
        sparklingEffect2Label = new JLabel(new ImageIcon(sparklingEffectImage2));
        sparklingEffect2Label.setBounds(revolver2Label.getX(), revolver2Label.getY(), sparklingEffectImage2.getWidth(), sparklingEffectImage2.getHeight());
        sparklingEffect2Label.setVisible(false);
        add(sparklingEffect2Label);
    }

    public void updateSparklingEffect1Coordinates(int x, int y) {
        sparklingEffect1Label.setBounds(x, y, sparklingEffect1Label.getWidth(), sparklingEffect1Label.getHeight());
        sparklingEffect1Label.setVisible(true);
    }

    public void updateSparklingEffect2Coordinates(int x, int y) {
        sparklingEffect2Label.setBounds(x, y, sparklingEffect2Label.getWidth(), sparklingEffect2Label.getHeight());
        sparklingEffect2Label.setVisible(true);
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
        updateSparklingEffect1Coordinates(x, y);
        revolver1Label.setVisible(true);
    }

    private void showRevolver2WithSparkle() {
        int x = 625;
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

    public void updateHealthIcons() {
        playerHealthLabel.setIcon(getHealthIcon(gameLogic.getPlayerLives()));
        enemyHealthLabel.setIcon(getHealthIcon(gameLogic.getEnemyLives()));
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

    public void updateStatus(String text, boolean isSurvived) {
        Color textColor;
        if (text.contains("was shot")) {
            textColor = Color.RED;
        } else {
            textColor = isSurvived ? Color.GREEN : Color.RED;
        }

        SwingUtilities.invokeLater(() -> {
            statusLabel.setForeground(textColor);
            statusLabel.setText(text);
        });
    }

    void showRevolver1() {
        revolver1Label.setVisible(true);
        sparklingEffect1Label.setVisible(false);
    }

    void showRevolver2() {
        revolver2Label.setVisible(true);
        sparklingEffect2Label.setVisible(false);
    }

    public void hideRevolvers() {
        revolver1Label.setVisible(false);
        sparklingEffect1Label.setVisible(false);
        revolver2Label.setVisible(false);
        sparklingEffect2Label.setVisible(false);
    }

    public void hideSparklingEffects() {
        sparklingEffect1Label.setVisible(false);
        sparklingEffect2Label.setVisible(false);
    }

    public void showRevolverWithSparkle(boolean isPlayer, boolean currentRoundIsLive) {
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

    void hideButtonsWithDelay() {
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
        magnifyingGlassButton.setVisible(gameLogic.hasMagnifyingGlass());
        healthPotionButton.setVisible(gameLogic.getHealthPotionCount() > 0);
        freezeAbilityButton.setVisible(gameLogic.hasFreezeAbility());
    }

    public void updateCoinsLabel() {
        SwingUtilities.invokeLater(() -> coinsLabel.setText("" + FullRelease.GamePanel.CoinManager.getCoins()));
    }

    public void updateStatusGameOver(String gameOverMessage) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(gameOverMessage));
    }

    public void switchToMenu() {
        FullRelease.GamePanel.CoinManager.resetCoins();
        FullRelease.MenuPanel menuPanel = new FullRelease.MenuPanel(frame);
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void switchToElevatorPanel() {
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
