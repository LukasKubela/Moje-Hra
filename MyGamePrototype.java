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
    private final JFrame frame;

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
        FinalPanel.CoinManager.resetCoins(); // resetují se hráčovi mince na nulu
        GamePanel gamePanel = new GamePanel(frame, this, FinalPanel.CoinManager.getCoins()); // nejdřív nastaví počet mincí na nulu, pak vyčistí okno a nahradí obsah novou hrou

        frame.getContentPane().removeAll(); // odstraní se všechny součásti z hlavního obsahu okna, aby mohla být nastavena nová obrazovka hry.
        frame.setContentPane(gamePanel); // nastaví se hlavní obsah okna na nový GamePanel
        frame.revalidate(); // provede se validace, aby se ujistilo že všechno je správně zobrazený
        frame.repaint(); // překreslí se okno, aby se zobrazily všechny změny
    }
}


class GamePanel extends JPanel implements KeyListener {
    private int x = 100;
    private final int y = 100;
    private final int size = 50;
    private final Rectangle wall = new Rectangle(300, 75, 10, 100);
    private final JFrame frame;
    private final MenuPanel menuPanel;

    public GamePanel(JFrame frame, MenuPanel menuPanel, int playerCoins) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        setFocusable(true); // nastaví panel jako schopný získat focus, což je nutné pro zachytávání vstupů z klávesnice
        requestFocusInWindow(); // žádá o focus okna pro tento komponent, aby mohl přijímat vstupy z klávesnice
        addKeyListener(this); // přidává tento GamePanel jako posluchače událostí klávesnice
    }

    public void addNotify() {
        super.addNotify(); // volá stejnou metodu nadřazené třídy (JPanel)
        requestFocusInWindow(); // žádá o focus pro toto okno, což znamená, že panel bude moci reagovat na vstupy z klávesnice
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
    private final JFrame frame;
    private final MenuPanel menuPanel;
    private JButton playerButton, enemyButton;
    private JLabel statusLabel, turnLabel, playerLivesLabel, enemyLivesLabel;
    private final Random random = new Random();
    private int playerLives = 3;
    private int enemyLives = 3;
    private boolean gameOver = false;
    private boolean playerStarts;
    private int shotCount = 0;
    private JLabel coinsLabel;

    public FinalPanel(JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        initUI();
        randomStart();
    }

    private void randomStart() {
        playerStarts = random.nextBoolean(); // nastaví, kdo zahájí hru – hráč nebo nepřítel. Výběr je náhodný
        if (playerStarts) {
            updateTurnLabel("Player");
        } else {
            updateTurnLabel("Enemy");
        }
        if (!playerStarts) {
            delayEnemyTurn();  // pokud na začátku není na tahu hráč, zpozdí se tah nepřítele, aby hráč měl čas se připravit
        }
    }

    private void updateTurnLabel(String text) {
        SwingUtilities.invokeLater(() -> { // použití SwingUtilities se ujistím, že změny provedou dprávně
            turnLabel.setText("Turn: " + text);  // Nastaví text popisku tahu

            boolean isPlayerTurn = text.equals("Player");  // Zjistí, zda je na tahu hráč
            playerButton.setEnabled(isPlayerTurn);
            enemyButton.setEnabled(isPlayerTurn);
        });
    }

    private void prepareNextTurn(boolean isPlayer) {
        playerStarts = isPlayer;
        if (isPlayer) {
            updateTurnLabel("Player");
        } else {
            updateTurnLabel("Enemy");
        }
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
        setupBall(300, Color.RED);
        setupBall(150, Color.BLUE);
    }

    private void setupBall(int x, Color color) {
        JLabel ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(color);
        ball.setBounds(x, 250, 50, 50);
        this.add(ball);
    }


    private void setupComponents() {
        setLayout(null);


        statusLabel = new JLabel("Waiting", SwingConstants.CENTER);
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


        coinsLabel = new JLabel("Coins: " + CoinManager.getCoins());
        coinsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinsLabel.setBounds(175, 400, 150, 30);
        add(coinsLabel);


        playerButton = new JButton("YOU");
        playerButton.setBounds(125, 200, 100, 30);
        playerButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(true);
            }
        });
        add(playerButton);


        enemyButton = new JButton("ENEMY");
        enemyButton.setBounds(275, 200, 100, 30);
        enemyButton.addActionListener(e -> {
            if (!gameOver && playerStarts) {
                playRussianRoulette(false);
                prepareNextTurn(false);
            }
        });
        add(enemyButton);

        playerButton.setEnabled(playerStarts);
        enemyButton.setEnabled(playerStarts);

        JButton shopButton = new JButton("Go to Shop");
        shopButton.setBounds(50, 450, 150, 30);
        shopButton.addActionListener(e -> switchToShop());
        add(shopButton);
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
                String survivalText;
                if (isPlayer) {
                    survivalText = "Player survives.";
                } else {
                    survivalText = "Enemy survives.";
                }
                updateStatus(survivalText, true); // aktualizuje statusový label s informací o přežití
                prepareNextTurn(isPlayer || checkPlayerContinuation()); // připraví další tah na základě toho, zda pokračuje hráč nebo ne
            }
        }
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
            CoinManager.addCoins(-1);
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
            CoinManager.addCoins(2);
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
        SwingUtilities.invokeLater(() -> coinsLabel.setText("Coins: " + CoinManager.getCoins())); // Aktualizuje zobrazení počtu mincí v rozhraní
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
                switchToGamePanel(CoinManager.getCoins());
            } else {
                CoinManager.resetCoins();
                switchToMenu();
            }
            ((Timer) e.getSource()).stop();
        }).start();
    }



    private void updateStatusGameOver(String gameOverMessage) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(gameOverMessage));
    }

    private void switchToMenu() {
        CoinManager.resetCoins();
        MenuPanel menuPanel = new MenuPanel(frame);
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void switchToGamePanel(int coins) {
        GamePanel gamePanel = new GamePanel(frame, menuPanel, coins);
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
        SwingUtilities.invokeLater(() -> coinsLabel.setText("Coins: " + CoinManager.getCoins()));
    }

    private void switchToShop() {
        ShopPanel shopPanel = new ShopPanel(frame, this);
        frame.setContentPane(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

class ShopPanel extends JPanel {
    private final JFrame frame;
    private final JLabel coinsLabel;
    private final JLabel feedbackLabel;

    public ShopPanel(JFrame frame, JPanel callingPanel) {
        this.frame = frame;
        setLayout(new GridLayout(6, 1));

        coinsLabel = new JLabel("Coins: " + FinalPanel.CoinManager.getCoins());
        add(coinsLabel);

        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(feedbackLabel);

        JButton item1Button = new JButton("Buy Item 1 (1 Coins)");
        item1Button.addActionListener(e -> purchaseItem(1, (FinalPanel) callingPanel)); // aktualizuje se zobrazení kolik mincí má ten hráč
        add(item1Button);

        JButton item2Button = new JButton("Buy Item 2 (2 Coins)");
        item2Button.addActionListener(e -> purchaseItem(2, (FinalPanel) callingPanel));
        add(item2Button);

        JButton item3Button = new JButton("Buy Item 3 (3 Coins)");
        item3Button.addActionListener(e -> purchaseItem(3, (FinalPanel) callingPanel));
        add(item3Button);

        JButton item4Button = new JButton("Buy Item 4 (4 Coins)");
        item4Button.addActionListener(e -> purchaseItem(4, (FinalPanel) callingPanel));
        add(item4Button);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> switchBack((FinalPanel) callingPanel));
        add(backButton);
    }

    private void purchaseItem(int cost, FinalPanel panel) {
        if (panel != null && FinalPanel.CoinManager.getCoins() >= cost) {
            FinalPanel.CoinManager.addCoins(-cost);
            updateCoinsDisplay();
            panel.updateCoinsDisplay();
            feedbackLabel.setText("Purchase successful!");
            feedbackLabel.setForeground(Color.GREEN);
        } else {
            feedbackLabel.setText("Not enough coins!");
            feedbackLabel.setForeground(Color.RED);
        }
    }

    private void updateCoinsDisplay() {
        coinsLabel.setText("Coins: " + FinalPanel.CoinManager.getCoins());
    }

    private void switchBack(FinalPanel finalPanel) {
        finalPanel.updateCoinsDisplay();
        frame.setContentPane(finalPanel);
        frame.revalidate();
        frame.repaint();
    }
}
// TODO: items, random events, settings
