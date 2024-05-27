package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShopPanel extends JPanel {
    private final JFrame frame;
    private final GamePanel gamePanel;
    private final JLabel coinsLabel;
    private final JLabel feedbackLabel;
    private final BufferedImage casinoChip;
    private final BufferedImage secondCasinoChip;
    private final BufferedImage thirdCasinoChip;
    private final BufferedImage fourthCasinoChip;
    private final JLabel coinsPrice1;
    private final int magnifyingGlassPrice = 10;
    private final int freezePrice = 10;
    private final JLabel coinsPrice2;
    private final JLabel coinsPrice3;
    private final int HealthPotionPrice = 5;

    public ShopPanel(JFrame frame, GamePanel callingPanel, GameGraphics gameGraphics) {
        this.frame = frame;
        this.gamePanel = callingPanel;
        setLayout(null);
        this.casinoChip = gameGraphics.getCasinoChip();
        this.secondCasinoChip = gameGraphics.getSecondCasinoChip();
        this.thirdCasinoChip = gameGraphics.getThirdCasinoChip();
        this.fourthCasinoChip = gameGraphics.getFourthCasinoChip();

        coinsLabel = new JLabel("" + FullRelease.GamePanel.CoinManager.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 40));
        coinsLabel.setForeground(Color.YELLOW);
        coinsLabel.setBounds(150, 45, 150, 30);
        add(coinsLabel);

        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackLabel.setBounds(50, 70, 400, 30);
        add(feedbackLabel);

        JButton magnifyingGlassButton = gameGraphics.getMagnifyingGlassButton();
        magnifyingGlassButton.setBounds(200, 300, 200, 200);
        magnifyingGlassButton.addActionListener(e -> purchaseItem(magnifyingGlassPrice, "magnifyingGlass"));
        add(magnifyingGlassButton);

        coinsPrice1 = new JLabel("10");
        coinsPrice1.setFont(new Font("Arial", Font.BOLD, 40));
        coinsPrice1.setForeground(Color.RED);
        coinsPrice1.setBounds(350, 535, 150, 30);
        add(coinsPrice1);

        JButton buyHealthPotionButton = gameGraphics.getHealthPotion();
        buyHealthPotionButton.setBounds(1000, 300, 100, 200);
        buyHealthPotionButton.addActionListener(e -> purchaseItem(HealthPotionPrice, "healthPotion"));
        add(buyHealthPotionButton);

        coinsPrice3 = new JLabel("5");
        coinsPrice3.setFont(new Font("Arial", Font.BOLD, 40));
        coinsPrice3.setForeground(Color.RED);
        coinsPrice3.setBounds(1100, 535,150,30);
        add(coinsPrice3);

        JButton freezeButton = gameGraphics.getFreezeButton();
        freezeButton.setBounds(600, 275, 230, 250);
        freezeButton.addActionListener(e -> purchaseItem(freezePrice, "freezeTime"));
        add(freezeButton);

        coinsPrice2 = new JLabel("10");
        coinsPrice2.setFont(new Font("Arial", Font.BOLD, 40));
        coinsPrice2.setForeground(Color.RED);
        coinsPrice2.setBounds(775, 535, 150,30);
        add(coinsPrice2);

        JButton backButton = gameGraphics.getBackButton();
        backButton.setBounds(625, 600, 225, 100);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> switchBack(gamePanel));
        add(backButton);

        updateCoinsPriceColor();
    }

    private void purchaseItem(int cost, String item) {
        if (FullRelease.GamePanel.CoinManager.getCoins() >= cost) {
            FullRelease.GamePanel.CoinManager.addCoins(-cost);
            updateCoinsDisplay();
            gamePanel.updateCoinsDisplay();
            feedbackLabel.setText("Purchase successful!");
            feedbackLabel.setForeground(Color.GREEN);

            switch (item) {
                case "magnifyingGlass":
                    gamePanel.setHasMagnifyingGlass(true);
                    break;
                case "healthPotion":
                    gamePanel.setHasHealthPotion(true);
                    break;
                case "freezeTime":
                    gamePanel.setHasFreezeAbility(true);
                    break;
            }

            gamePanel.updateItemVisibility();
        } else {
            feedbackLabel.setText("Not enough coins!");
            feedbackLabel.setForeground(Color.RED);
        }
    }

    private void updateCoinsDisplay() {
        coinsLabel.setText("" + FullRelease.GamePanel.CoinManager.getCoins());
        updateCoinsPriceColor();
    }

    private void updateCoinsPriceColor() {
        int playerCoins = FullRelease.GamePanel.CoinManager.getCoins();

        if (playerCoins >= magnifyingGlassPrice) {
            coinsPrice1.setForeground(Color.GREEN);
        } else {
            coinsPrice1.setForeground(Color.RED);
        }

        if (playerCoins >= freezePrice) {
            coinsPrice2.setForeground(Color.GREEN);
        } else {
            coinsPrice2.setForeground(Color.RED);
        }

        if (playerCoins >= HealthPotionPrice) {
            coinsPrice3.setForeground(Color.GREEN);
        } else {
            coinsPrice3.setForeground(Color.RED);
        }
    }

    private void switchBack(FullRelease.GamePanel finalPanel) {
        finalPanel.updateCoinsDisplay();
        frame.setContentPane(finalPanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (casinoChip != null) {
            g.drawImage(casinoChip, 10, 10, null);
        }

        if (secondCasinoChip != null) {
            g.drawImage(secondCasinoChip, 200, 500, null);
        }

        if (thirdCasinoChip != null) {
            g.drawImage(thirdCasinoChip, 600, 500, null);
        }

        if (fourthCasinoChip != null) {
            g.drawImage(fourthCasinoChip,950, 500, null);
        }
    }
}
