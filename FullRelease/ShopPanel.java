package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShopPanel extends JPanel {
    private final JFrame frame;
    private final JLabel coinsLabel;
    private final JLabel feedbackLabel;
    private final BufferedImage casinoChip;
    private final BufferedImage secondCasinoChip;
    private final BufferedImage thirdCasinoChip;
    private final JLabel coinsPrice1;
    private final int magnifyingGlassPrice = 3;
    private final int freezePrice = 3;
    private final JLabel coinsPrice2;

    public ShopPanel(JFrame frame, JPanel callingPanel, GameGraphics gameGraphics) {
        this.frame = frame;
        setLayout(null);
        this.casinoChip = gameGraphics.getCasinoChip();
        this.secondCasinoChip = gameGraphics.getSecondCasinoChip();
        this.thirdCasinoChip = gameGraphics.getThirdCasinoChip();

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
        magnifyingGlassButton.addActionListener(e -> purchaseItem(magnifyingGlassPrice, (FullRelease.GamePanel) callingPanel, "magnifyingGlass"));
        add(magnifyingGlassButton);

        coinsPrice1 = new JLabel("3");
        coinsPrice1.setFont(new Font("Arial", Font.BOLD, 40));
        coinsPrice1.setForeground(Color.RED);
        coinsPrice1.setBounds(350, 535, 150, 30);
        add(coinsPrice1);

        /*JButton buyHealthPotionButton = new JButton("Buy Health Potion (Free)");
        buyHealthPotionButton.setBounds(50, 150, 200, 30);
        buyHealthPotionButton.addActionListener(e -> purchaseItem(-100, (FullRelease.GamePanel) callingPanel, "healthPotion"));
        add(buyHealthPotionButton);*/

        JButton freezeButton = gameGraphics.getFreezeButton();
        freezeButton.setBounds(600, 275, 230, 250);
        freezeButton.addActionListener(e -> purchaseItem(freezePrice, (FullRelease.GamePanel) callingPanel, "freezeTime"));
        add(freezeButton);

        coinsPrice2 = new JLabel("3");
        coinsPrice2.setFont(new Font("Arial", Font.BOLD, 40));
        coinsPrice2.setForeground(Color.RED);
        coinsPrice2.setBounds(775, 535, 150,30);
        add(coinsPrice2);

        JButton backButton = gameGraphics.getBackButton();
        backButton.setBounds(625, 600, 225, 100);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> switchBack((FullRelease.GamePanel) callingPanel));
        add(backButton);

        updateCoinsPriceColor();
    }

    private void purchaseItem(int cost, FullRelease.GamePanel panel, String item) {
        if (panel != null && FullRelease.GamePanel.CoinManager.getCoins() >= cost) {
            FullRelease.GamePanel.CoinManager.addCoins(-cost);
            updateCoinsDisplay();
            panel.updateCoinsDisplay();
            feedbackLabel.setText("Purchase successful!");
            feedbackLabel.setForeground(Color.GREEN);

            switch (item) {
                case "freezeTime" -> panel.setHasFreezeAbility(true);
                case "healthPotion" -> panel.setHasHealthPotion();
                case "magnifyingGlass" -> panel.setHasMagnifyingGlass(true);
            }
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
    }
}
