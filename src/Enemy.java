import javax.swing.*;
import java.awt.*;

public class Enemy {
    // Fields
    private double x;
    private double y;
    private double h;
    private double w;
    private int r;

    private double speed;
    private double dx; // bias
    private double dy;
    private double rad;

    private double health;


    private int type;
    private int rank;

    private boolean ready;

    private Color color;

    Image img = new ImageIcon("image/enemyleft.png").getImage();
    Image img2 = new ImageIcon("image/enemyright.png").getImage();

    // Constructor
    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;

        switch (type) {
            case (1): color = Color.GREEN;
            switch (rank) {
                case (1):

                    x = Math.random() * GamePanel.WIDTH;
                    y = 20;
                    w = 72;
                    h = 76;
                    speed = 2;
                    health = 1;
                    double angle = Math.toRadians(Math.random() * 360);
                    dx = Math.sin(angle) * speed;
                    dy = Math.cos(angle) * speed;
            }
        }
        ready = false;
    }

    // Functions
    public double getX() { return x; }  // getter
    public double getY() { return y; }
    public double getW() { return w; }
    public double getH() { return h; }
    public int getR() { return r; }

    public int getType() { return type; }
    public int getRank() { return rank; }

    public boolean remove() {
        if (health <= 0) {
            return true;
        }
        return false;
    }

    public void hit() { health--; }

    public void explode() {
        if (rank > 1) {
            int amount = 0;
            if (type == 2) {
                amount = 3;
            }
            for (int i = 0; i < amount; i++) {
                Enemy e = new Enemy(getType(), getRank() - 1);
                e.x = this.x;
                e.y = this.y;
                double angle = 0;
                if (!ready) {
                    angle = Math.random() * 130 + 20;
                }
                else {
                    angle = Math.random() * 360;
                }
                e.rad = Math.toRadians(angle);

                GamePanel.enemies.add(e);

            }
        }
    }

    public void update() {
        y += dy;
        x += dx;

        if (x < 0 && dx < 0) dx =- dx;
        if (x > GamePanel.WIDTH && dx > 0) dx = -dx;
        if (y < 0 && dy < 0) dy =- dy;
        if (y > GamePanel.HEIGHT && dy > 0) dy = -dy;
    }

    public void draw(Graphics2D g) {
        if(dx < 0) g.drawImage(img, (int)x, (int)y, null);
        if(dx > 0) g.drawImage(img2, (int)x, (int)y, null);
    }
}
