package FullRelease;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final JFrame frame;
    private final FullRelease.MenuPanel menuPanel;
    private static boolean showShopAndUseItems = true;

    public SettingsPanel(JFrame frame, FullRelease.MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        setLayout(new BorderLayout());

        JLabel settingsLabel = new JLabel("Settings", SwingConstants.CENTER);
        settingsLabel.setFont(new Font("Arial", Font.BOLD, 50));
        add(settingsLabel, BorderLayout.NORTH);

        JCheckBox showShopAndItemsCheckbox = new JCheckBox("Enable Shop and Use Items", showShopAndUseItems);
        showShopAndItemsCheckbox.addActionListener(e -> {
            showShopAndUseItems = showShopAndItemsCheckbox.isSelected();
            Component currentPanel = frame.getContentPane();
            if (currentPanel instanceof GamePanel) { // operátor instanceof kontroluje, zda daný objekt je instancí specifické třídy nebo její podtřídy, kontroluje také hierarchii tříd v rozhraních
                ((GamePanel) currentPanel).applySettings();
            }
        });
        add(showShopAndItemsCheckbox, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> switchBackToMenu());
        add(backButton, BorderLayout.SOUTH);
    }

    private void switchBackToMenu() {
        MenuPanel newMenuPanel = new MenuPanel(frame);
        menuPanel.updateContentPane(newMenuPanel);
    }

    public static boolean isShowShopAndUseItemsEnabled() {
        return showShopAndUseItems;
    }
}
