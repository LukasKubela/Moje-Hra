import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MyGamePrototype {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My Game Prototype");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.add(new MenuPanel(frame));
            frame.setVisible(true);
        });
    }
}

class MenuPanel extends JPanel {
    private JFrame frame;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());


        JLabel menuLabel = new JLabel("MENU", SwingConstants.CENTER);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 50));
        add(menuLabel, BorderLayout.NORTH);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> switchToGamePanel());
        add(playButton, BorderLayout.CENTER);
    }

    private void switchToGamePanel() {
        GamePanel gamePanel = new GamePanel(frame, this, 0);
        frame.getContentPane().removeAll();
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
    }
}

class GamePanel extends JPanel implements KeyListener {
    private int x = 100, y = 100, size = 50;
    private Rectangle wall = new Rectangle(300, 75, 10, 100);
    private JFrame frame;
    private MenuPanel menuPanel;
    private int playerCoins;

    public GamePanel(JFrame frame, MenuPanel menuPanel, int playerCoins) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        this.playerCoins = playerCoins;  
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(x, y, size, size);

        g.setColor(Color.BLACK);
        g.fillRect(wall.x, wall.y, wall.width, wall.height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int speed = 10;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: x -= speed; break;
            case KeyEvent.VK_RIGHT: x += speed; break;
        }
        if (new Rectangle(x, y, size, size).intersects(wall)) {
            switchToFinalPanel();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void switchToFinalPanel() {
        FinalPanel finalPanel = new FinalPanel(frame, menuPanel);
        frame.getContentPane().removeAll();
        frame.setContentPane(finalPanel);
        frame.revalidate();
        frame.repaint();
    }
}

class FinalPanel extends JPanel {
    private JFrame frame;
    private MenuPanel menuPanel;
    private JButton playerButton, enemyButton;
    private JLabel statusLabel, turnLabel, playerLivesLabel, enemyLivesLabel;
    private Random random = new Random();
    private int playerLives = 3;
    private int enemyLives = 3;
    private boolean gameOver = false;
    private boolean lastShotWasLive = false;
    private boolean playerStarts;
    private int shotCount = 0;
    private int playerCoins = 0;
    private JLabel coinsLabel;

    public FinalPanel(JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        initUI();
        randomStart();
    }

    private void randomStart() {
        playerStarts = random.nextBoolean();
        updateTurnLabel(playerStarts ? "Player" : "Enemy");
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
        });
    }

    private void prepareNextTurn(boolean isPlayer) {
        playerStarts = isPlayer;
        updateTurnLabel(isPlayer ? "Player" : "Enemy");
        if (!isPlayer) {
            playerButton.setEnabled(false);
            enemyButton.setEnabled(false);
            delayEnemyTurn();
        } else {
            playerButton.setEnabled(true);
            enemyButton.setEnabled(true);
        }
    }

    private void initUI() {
        setLayout(null);
        setupComponents();
        setupBall(300, 250, Color.RED);
        setupBall(150, 250, Color.BLUE);
    }

    private JLabel setupBall(int x, int y, Color color) {
        JLabel ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(color);
        ball.setBounds(x, y, 50, 50);
        this.add(ball);
        return ball;
    }


    private void setupComponents() {
        setLayout(null);


        statusLabel = new JLabel("Waiting for player...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBounds(175, 100, 150, 30);
        add(statusLabel);

        turnLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setBounds(175, 50, 150, 30);
        add(turnLabel);

        playerLivesLabel = new JLabel("Player Lives: " + playerLives);
        playerLivesLabel.setBounds(50, 10, 200, 30);
        add(playerLivesLabel);

        // Enemy lives label
        enemyLivesLabel = new JLabel("Enemy Lives: " + enemyLives);
        enemyLivesLabel.setBounds(300, 10, 200, 30);
        add(enemyLivesLabel);


        coinsLabel = new JLabel("Coins: " + playerCoins);
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinsLabel.setBounds(175, 400, 150, 30);
        add(coinsLabel);


        playerButton = new JButton("YOU");
        playerButton.setFont(new Font("Arial", Font.BOLD, 12));
        playerButton.setBounds(125, 200, 100, 30);
        playerButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(true);
            }
        });
        add(playerButton);


        enemyButton = new JButton("ENEMY");
        enemyButton.setFont(new Font("Arial", Font.BOLD, 12));
        enemyButton.setBounds(275, 200, 100, 30);
        enemyButton.addActionListener(e -> {
            if (!gameOver && !playerStarts) {
                playRussianRoulette(true);
            }
        });
        add(enemyButton);

        playerButton.setEnabled(playerStarts);
        enemyButton.setEnabled(playerStarts);
    }

    private void updateStatus(String text, boolean isSurvived) {
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



    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBounds(x, y, 100, 30);
        button.addActionListener(e -> {
            System.out.println(text + " button clicked.");
            if (gameOver) {
                System.out.println("Game over. No action taken.");
                return; // Don't allow actions if the game is over
            }
            if ("YOU".equals(text)) {
                playRussianRoulette(true);
            } else if ("ENEMY".equals(text)) {
                playRussianRoulette(false);
            }
        });
        add(button);
        return button;
    }

    private void playRussianRoulette(boolean isPlayer) {
        if (gameOver) {
            return;
        }

        boolean shotFired = random.nextInt(7 - shotCount++) == 0;
        lastShotWasLive = shotFired;

        if (shotFired) {
            triggerLiveShot(isPlayer);
            shotCount = 0;
        } else {
            if (shotCount >= 6) {
                triggerLiveShot(isPlayer);
                shotCount = 0;
            } else {
                String survivalText = isPlayer ? "Player survives." : "Enemy survives.";
                updateStatus(survivalText, true);
                if (isPlayer) {
                    prepareNextTurn(true);
                } else {
                    prepareNextTurn(true);
                }
            }
        }
    }

    private void enemyTakeTurn() {
        if (gameOver) return;

        boolean decidesToShootPlayer = lastShotWasLive || random.nextInt(2) == 0;
        System.out.println("Enemy's turn: Deciding...");

        Timer decisionDelay = new Timer(2000, e -> {
            playRussianRoulette(!decidesToShootPlayer);
            ((Timer)e.getSource()).stop();

            prepareNextTurn(true);
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
        if (isPlayer) {
            playerLives--;
            playerLivesLabel.setText("Player Lives: " + playerLives);
            playerCoins -= 1;
            updateCoinsLabel();
            if (playerLives <= 0) {
                gameOver = true;
                playerCoins = 0;
                updateCoinsLabel();
                updateStatus("Player died", false);
                handleGameOver(true);
            } else {
                updateStatus("Player was shot", true);
            }
        } else {
            enemyLives--;
            enemyLivesLabel.setText("Enemy Lives: " + enemyLives);
            playerCoins += 2;
            updateCoinsLabel();
            if (enemyLives <= 0) {
                gameOver = true;
                updateStatus("Enemy died", false);
                handleGameOver(false);
            } else {
                updateStatus("Enemy was shot", true);
            }
        }

        if (!gameOver) {
            prepareNextTurn(!isPlayer);
        }
    }

    private void updateCoinsLabel() {
        SwingUtilities.invokeLater(() -> {
            coinsLabel.setText("Coins: " + playerCoins);
        });
    }

    private void handleGameOver(boolean playerDied) {
        String gameOverMessage = playerDied ? "YOU DIED" : "ENEMY DIED";
        updateStatusGameOver(gameOverMessage);
        playerButton.setEnabled(false);
        enemyButton.setEnabled(false);

        new Timer(2000, e -> {
            if (!playerDied) {
                switchToGamePanel();
            } else {
                playerCoins = 0;
                switchToMenu();
            }
            ((Timer)e.getSource()).stop();
        }).start();
    }

    private void updateStatusGameOver(String gameOverMessage) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(gameOverMessage));
    }

    private void switchToMenu() {
        menuPanel = new MenuPanel(frame);
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void switchToGamePanel() {
        GamePanel gamePanel = new GamePanel(frame, menuPanel, playerCoins);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
// FIXME: the enemy button doesn't work, when the enemy shoot itself it is apparently players turn, enemy doesn't want to shoot the player, saving coins to another level
// TODO: shop, random events, settings