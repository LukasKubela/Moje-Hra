package FullRelease;

import java.awt.*;

public class Player {
    private int x;
    private int y;
    private final int size = 50;
    private final int speed = 10;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(x, y, size, size);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}