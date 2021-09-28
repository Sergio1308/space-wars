import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class Player {
    
    // Fields
    private double x;
    private double y;
    private double w;  // object width
    private double h;  // object height

    private double dx;  // bias coefficient
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

    public Rectangle getRect() {
        return new Rectangle((int) x, (int) y, 58, 74);
    }

    private double ang1;  // player rotation angle
    private double distX;  // subtracting the distance in x from the mouse cursor
    private double distY;
    private double dist;  // distance from mouse cursor


    // Constructor
    public Player () {
        x = GamePanel.WIDTH / 2; // spawn position
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

    // Functions
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
        healthy = true;
        if (health <= 0) {
            GamePanel.state = GamePanel.STATES.GAMEOVER;
        }
    }
    public void addScore(int i) { score += i; }

    public void increasePower(int i) {
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

        distX = GamePanel.mouseX - x;  // subtracting the x distance from the mouse cursor
        distY = y - GamePanel.mouseY;
        dist = (Math.sqrt(distX * distX + distY * distY));  // calculating the hypotenuse from player to crosshair

        if (distX > 0) ang1 = Math.acos(distY/(Math.sqrt(distX * distX + distY * distY)));  // crosshair on the right
        if (distX < 0) ang1 =- Math.acos(distY/(Math.sqrt(distX * distX + distY * distY)));

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

        dy = 0;  // stop moving
        dx = 0;

        // firing
        if (isFiring) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                firingTimer = System.nanoTime();
                if (powerLevel < 2) {
                    GamePanel.bullets.add(new Bullet(currentAngle, x, y));
                }
                else if (powerLevel < 4) {
                    GamePanel.bullets.add(new Bullet(currentAngle, x + 5, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x - 5, y));
                }
                else {
                    GamePanel.bullets.add(new Bullet(currentAngle, x, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x + 15, y));
                    GamePanel.bullets.add(new Bullet(currentAngle, x - 15, y));
                }
                isFiring = false;

                if(isShotgun) {
                    minAngle = -45;
                    for (int i = 0; i < bulletsAmount; i++) {
                        GamePanel.bullets.add(new Bullet(currentAngle, x, y));
                        currentAngle += density;
                    }
                    currentAngle = minAngle;
                }
                else{
                    GamePanel.bullets.add(new Bullet(0, x, y));
                }
            }
        }
    }

    public void draw(Graphics2D g) {
        AffineTransform origXform;  
        origXform = g.getTransform();  // get current position
        AffineTransform newXform = (AffineTransform) (origXform.clone());  // clone current position
        newXform.rotate(ang1, x + 29, y + 25);  // rotate current image
        g.setTransform(newXform);  // set received value
        g.drawImage(img, (int)x, (int)y, null);  // filling
        g.setTransform(origXform);  // return the previous value

        g.setColor(Color.WHITE);
        ((Graphics2D) g).drawString("Enemies: " + GamePanel.enemies.size(), GamePanel.WIDTH - 160, GamePanel.HEIGHT - 20);
    }

    public void drawHealth(Graphics2D g) {
        for (int i = 0; i < GamePanel.player.getHealth(); i++) {
            g.setColor(color1);
            g.fillOval(15 + (25 * i), 20, GamePanel.player.getR() * 3, GamePanel.player.getR() * 3);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1.darker());
            g.fillOval(15 + (25 * i), 20, GamePanel.player.getR() * 3, GamePanel.player.getR() * 3);
            g.setStroke(new BasicStroke(1));
        }
    }
}
