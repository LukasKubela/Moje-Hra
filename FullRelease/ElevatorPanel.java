package FullRelease;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ElevatorPanel extends JPanel implements KeyListener {
    private final int y = 100;
    private final int size = 50;
    private Rectangle wall = new Rectangle(300, 75, 10, 100);
    private final JFrame frame;
    private final FullRelease.MenuPanel menuPanel;
    private final Player player;

    public ElevatorPanel(JFrame frame, FullRelease.MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanel = menuPanel;
        this.player = new Player(frame.getWidth() / 2 - size - 300, 425);
        this.wall = new Rectangle(frame.getWidth() / 2 - 5, 400, 10, 100);
        setFocusable(true); // nastaví panel jako schopný získat focus, což je nutné pro zachytávání vstupů z klávesnice
        requestFocusInWindow(); // žádá o focus okna pro tento komponent, aby mohl přijímat vstupy z klávesnice
        addKeyListener(this); // přidává tento Prototype.FullRelease.GamePanel jako posluchače událostí klávesnice
    }

    public void addNotify() {
        super.addNotify(); // volá stejnou metodu nadřazené třídy (JPanel)
        requestFocusInWindow(); // žádá o focus pro toto okno, což znamená, že panel bude moci reagovat na vstupy z klávesnice
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        player.draw(g);

        g.setColor(Color.BLACK);
        g.fillRect(wall.x, wall.y, wall.width, wall.height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                player.moveRight();
                break;
        }
        if (player.getBounds().intersects(wall)) {
            switchToGamePanel();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void switchToGamePanel() {
        GamePanel finalPanel = new GamePanel(frame, menuPanel);
        frame.getContentPane().removeAll();
        frame.setContentPane(finalPanel);
        finalPanel.applySettings();
        frame.revalidate();
        frame.repaint();
    }
}
