import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Player {

    private double x;
    private double y;
    private double w;
    private double h;

    private double dx;
    private double dy;

    private int r = 5;

    private int speed;

    private int health;
    public static boolean healthy;

    private Color color1;

    private int powerLevel;
    private int power;
    private int[] requiredPower = {
            1, 2, 3, 4, 5
    };

    public static boolean up;
    public static boolean down;
    public static boolean left;
    public static boolean right;

    private float angle = 90;
    public static boolean isShotgun = false;
    public int bulletsAmount = 5;
    private float minAngle = 0;
    private float currentAngle = minAngle;
    private float density = angle / bulletsAmount;

    public static boolean isFiring;
    private long firingTimer;
    private long firingDelay;

    private int score;

    Image img = new ImageIcon("image/player.png").getImage();

    private double ang1; // angle player rotation
    private double distX; // x-distance from mouse cursor
    private double distY; // y-distance from mouse cursor

    public Player () {
        x = (float) GamePanel.WIDTH / 2; // spawn
        y = 500;
        w = 58;
        h = 74;

        speed = 10;

        dx = 0;
        dy = 0;
        ang1 = 0;

        health = 3;
        healthy = false;

        color1 = Color.WHITE;

        up  = false;
        down = false;
        left = false;
        right = false;

        isFiring = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;

        score = 0;
    }

    public double getX(){
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() { return w; }

    public double getH() { return h; }

    public int getR() { return r; }

    public int getScore() { return score; }

    public int getHealth() { return health; }

    public void gainLife() {
        health++;
    }

    public void hit() {
        health--;
        if (health <= 0) {
            healthy = true;
            GamePanel.state = GamePanel.STATES.GAMEOVER;
        }
        powerLevel = 0;
        power = 0;
    }
    public void addScore(int i) { score += i; }

    public void increasePower(int i) {
        if (power + i < requiredPower.length)
            power += i;
            if (power >= requiredPower[powerLevel]) {
                power -= requiredPower[powerLevel];
                powerLevel++;
            }
    }

    public int getPowerLevel() { return powerLevel; }
    public int getPower() { return power; }
    public int getRequiredPower() { return requiredPower[powerLevel]; }

    public void update() {
        distX = GamePanel.mouseX - x;
        distY = y - GamePanel.mouseY;

        if (distX > 0) ang1 = Math.acos(distY/(Math.sqrt(distX*distX + distY*distY)));
        if (distX < 0) ang1 =- Math.acos(distY/(Math.sqrt(distX*distX + distY*distY)));

        if (up && y > 20) {
            y -= speed;
        }
        if (down && y < GamePanel.HEIGHT - h) {
            y += speed;
        }
        if (left && x > 0) {
            x -= speed;
        }
        if (right && x < GamePanel.WIDTH - w) {
            x += speed;
        }
        if (up && left || up && right || down && left || down && right) {
            dy = dy * Math.sin(Math.sin(Math.PI/4));
            dx = dx * Math.cos(Math.sin(Math.PI/4));
        }

        y += dy;
        x += dx;

        dy = 0; // stop moving
        dx = 0;

        // firing
        if (isFiring) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                firingTimer = System.nanoTime();
                // extra firing
                if (isShotgun) {
                    isFiring = false;
                    currentAngle = 360;
                    int x1 = 500;
                    while (x1 > -500) {
                        GamePanel.bullets.add(new Bullet(currentAngle, x + x1, y));
                        x1 -= 100;
                    }
                }
                else if (powerLevel < 2) {
                    GamePanel.bullets.add(new Bullet(currentAngle, x, y));
                }
                else if (powerLevel < 4) {
                    currentAngle = 45;
                    GamePanel.bullets.add(new Bullet(currentAngle, x + 15, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x - 15, y));
                }
                else {
                    currentAngle = 90;
                    GamePanel.bullets.add(new Bullet(currentAngle, x, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x + 55, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x - 55, y));
                }
                isFiring = false;
            }
        }
    }

    public void draw(Graphics2D g) {
        AffineTransform origXform;
        origXform = g.getTransform();
        AffineTransform newXform = (AffineTransform) (origXform.clone());
        newXform.rotate(ang1, x + 29, y + 25);
        g.setTransform(newXform);
        g.drawImage(img, (int)x, (int)y, null);
        g.setTransform(origXform);

        g.setColor(Color.WHITE);
        ((Graphics2D) g).drawString("Enemies: " + GamePanel.enemies.size(),
                GamePanel.WIDTH - 160, GamePanel.HEIGHT - 20);
    }

    public void drawHealth(Graphics2D g) {
        for (int i = 0; i < GamePanel.player.getHealth(); i++) {
            g.setColor(color1);
            g.fillOval(15 + (25 * i), 20,
                    GamePanel.player.getR() * 3, GamePanel.player.getR() * 3);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1.darker());
            g.fillOval(15 + (25 * i), 20,
                    GamePanel.player.getR() * 3, GamePanel.player.getR() * 3);
            g.setStroke(new BasicStroke(1));
        }
    }
}
