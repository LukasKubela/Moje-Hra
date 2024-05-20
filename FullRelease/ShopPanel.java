package FullRelease;

import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {
    private final JFrame frame;
    private final JLabel coinsLabel;
    private final JLabel feedbackLabel;

    public ShopPanel(JFrame frame, JPanel callingPanel) {
        this.frame = frame;
        setLayout(new GridLayout(6, 1));

        coinsLabel = new JLabel("Coins: " + FullRelease.GamePanel.CoinManager.getCoins());
        add(coinsLabel);

        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(feedbackLabel);

        JButton magnifyingGlassButton = new JButton("Buy Magnifying Glass (Free)");
        magnifyingGlassButton.addActionListener(e -> purchaseItem(-100, (FullRelease.GamePanel) callingPanel, "magnifyingGlass"));
        add(magnifyingGlassButton);

        JButton buyHealthPotionButton = new JButton("Buy Health Potion (Free)");
        buyHealthPotionButton.addActionListener(e -> purchaseItem(0, (FullRelease.GamePanel) callingPanel, "healthPotion"));
        add(buyHealthPotionButton);

        JButton freezeTimeButton = new JButton("Buy Freeze Time (Free)");
        freezeTimeButton.addActionListener(e -> purchaseItem(-100, (FullRelease.GamePanel) callingPanel, "freezeTime"));
        add(freezeTimeButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchBack((FullRelease.GamePanel) callingPanel));
        add(backButton);
    }

    private void purchaseItem(int cost, FullRelease.GamePanel panel, String item) {
        if (panel != null && FullRelease.GamePanel.CoinManager.getCoins() >= cost) {
            FullRelease.GamePanel.CoinManager.addCoins(-cost);
            updateCoinsDisplay();
            panel.updateCoinsDisplay();
            feedbackLabel.setText("Purchase successful!");
            feedbackLabel.setForeground(Color.GREEN);

            if (item.equals("freezeTime")) {
                panel.setHasFreezeAbility(true);  // Enable the freeze ability in the game panel
            } else if (item.equals("healthPotion")) {
                panel.setHasHealthPotion();
            } else if (item.equals("magnifyingGlass")) {
                panel.setHasMagnifyingGlass(true);
            }
        } else {
            feedbackLabel.setText("Not enough coins!");
            feedbackLabel.setForeground(Color.RED);
        }
    }

    private void updateCoinsDisplay() {
        coinsLabel.setText("Coins: " + FullRelease.GamePanel.CoinManager.getCoins());
    }

    private void switchBack(FullRelease.GamePanel finalPanel) {
        finalPanel.updateCoinsDisplay();
        frame.setContentPane(finalPanel);
        frame.revalidate();
        frame.repaint();
    }
}