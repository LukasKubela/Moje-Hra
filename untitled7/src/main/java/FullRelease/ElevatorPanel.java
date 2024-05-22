package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class ElevatorPanel extends JPanel implements KeyListener, ComponentListener {
    private final BufferedImage standingImage1;
    private final BufferedImage standingImage2;
    private final ImageIcon walkingImageRight;
    private final ImageIcon walkingImageLeft;
    private final BufferedImage elevatorImage;
    private boolean isWalking = false;
    private final Timer walkingTimer;
    private final JLabel playerLabel;
    private String direction = "standing";
    private int playerX = 50;
    private int playerY = 50;
    private final Rectangle wall;
    private final Rectangle wall2;
    private final Rectangle wall3;
    private final GamePanel gamePanel;
    private final JFrame frame;

    public ElevatorPanel(GameGraphics gameGraphics, GamePanel gamePanel, JFrame frame) {
        this.gamePanel = gamePanel;
        this.frame = frame;

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        this.standingImage1 = gameGraphics.getStandingImage1();
        this.standingImage2 = gameGraphics.getStandingImage2();
        this.walkingImageRight = gameGraphics.getWalkingImageRight();
        this.walkingImageLeft = gameGraphics.getWalkingImageLeft();
        this.elevatorImage = gameGraphics.getElevatorImage();

        setLayout(null);

        playerLabel = new JLabel(new ImageIcon(standingImage1));
        playerLabel.setBounds(playerX, playerY, standingImage1.getWidth(), standingImage1.getHeight());
        add(playerLabel);

        playerY = (getHeight() - standingImage1.getHeight()) - 150;
        playerLabel.setBounds(playerX, playerY, playerLabel.getIcon().getIconWidth(), playerLabel.getIcon().getIconHeight());

        int wallWidth = 20;
        int wallHeight = getHeight();
        int wallX = 0;
        int wallY = 0;
        wall = new Rectangle(wallX, wallY, wallWidth, wallHeight);
        int wall2X = 925;
        int wall2Y = 0;
        int wall2Width = 20;
        int wall2Height = 1000;
        wall2 = new Rectangle(wall2X, wall2Y, wall2Width, wall2Height);
        int wall3X = 1050;
        int wall3Y = 0;
        int wall3Width = 0;
        int wall3Height = getHeight();
        wall3 = new Rectangle(wall3X, wall3Y, wall3Width, wall3Height);

        walkingTimer = new Timer(0, e -> updateWalkingAnimation());

        addComponentListener(this);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int wallHeight = getHeight();
        wall.setBounds(0, 0, 20, wallHeight);

        int wall3Height = getHeight();
        wall3.setBounds(1150, 0, 1, wall3Height);

        playerY = (getHeight() - standingImage1.getHeight());
        playerLabel.setBounds(playerX, playerY, playerLabel.getIcon().getIconWidth(), playerLabel.getIcon().getIconHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private void updateWalkingAnimation() {
        if (isWalking) {
            switch (direction) {
                case "left":
                    if (!wall.intersects(playerLabel.getBounds())) {
                        playerX -= 15;
                        playerLabel.setIcon(walkingImageRight);
                    }
                    break;
                case "right":
                    playerX += 15;
                    playerLabel.setIcon(walkingImageLeft);
                    break;
            }
            playerLabel.setBounds(playerX, playerY, playerLabel.getIcon().getIconWidth(), playerLabel.getIcon().getIconHeight());

            if (playerLabel.getBounds().intersects(wall3)) {
                switchToGamePanel();
            }

        } else {
            if (direction.equals("left")) {
                playerLabel.setIcon(new ImageIcon(standingImage2));
            } else if (direction.equals("right")) {
                playerLabel.setIcon(new ImageIcon(standingImage1));
            } else {
                playerLabel.setIcon(new ImageIcon(standingImage1));
            }
            playerLabel.setBounds(playerX, playerY, playerLabel.getIcon().getIconWidth(), playerLabel.getIcon().getIconHeight());
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                direction = "left";
                startWalking();
                break;
            case KeyEvent.VK_RIGHT:
                direction = "right";
                startWalking();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                stopWalking();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void startWalking() {
        if (!isWalking) {
            isWalking = true;
            walkingTimer.start();
        }
    }

    private void stopWalking() {
        if (isWalking) {
            isWalking = false;
            walkingTimer.stop();
            if (direction.equals("left")) {
                playerLabel.setIcon(new ImageIcon(standingImage2));
            } else if (direction.equals("right")) {
                playerLabel.setIcon(new ImageIcon(standingImage1));
            } else {
                playerLabel.setIcon(new ImageIcon(standingImage1));
            }
            playerLabel.setBounds(playerX, playerY, playerLabel.getIcon().getIconWidth(), playerLabel.getIcon().getIconHeight());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (elevatorImage != null) {
            g.drawImage(elevatorImage, getWidth() - elevatorImage.getWidth() + 100, getHeight() - elevatorImage.getHeight() +40, this);
        }

        g.setColor(Color.BLACK);
        g.fillRect(wall.x, wall.y, wall.width, wall.height);

        g.setColor(Color.BLACK);
        g.fillRect(wall2.x, wall2.y, wall2.width, wall2.height);
    }

    private void switchToGamePanel() {
        walkingTimer.stop();
        gamePanel.startGame();
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
    }
}
