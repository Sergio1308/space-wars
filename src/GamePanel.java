import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {

    public static int WIDTH = 1024;
    public static int HEIGHT = 768;

    public static int mouseX;
    public static int mouseY;
    public static boolean leftMouse;
    public static boolean space = false;

    private Thread thread;
    private BufferedImage image;
    private Graphics2D g;

    private int FPS;
    private int  sleepTime;
    private double millisPerFrame;
    private long timerFPS;

    public enum STATES {
        MENU,
        PLAY,
        GAMEOVER
    }

    public static STATES state = STATES.MENU;

    public static GameBack background;
    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<Explosion> explosions;
    public static ArrayList<PowerUp> powerUps;

    public static Wave wave;
    public static Menu menu;
    public static GameOver gameover;
    public static Player health;
    public static Aim aim1;
    public static Aim aim;

    public GamePanel() {
        super();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();

        addKeyListener(new Listeners());
        addMouseMotionListener(new Listeners());
        addMouseListener(new Listeners());
    }
    // Functions
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        FPS = 30;
        millisPerFrame = 1000.0 / FPS;
        sleepTime = 0;

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // improved graphics

        leftMouse = false;
        background = new GameBack();
        player = new Player();

        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        explosions = new ArrayList<Explosion>();
        powerUps = new ArrayList<PowerUp>();

        wave = new Wave();
        menu = new Menu();
        gameover = new GameOver();
        health = new Player();
        aim1 = new Aim(GamePanel.mouseX, GamePanel.mouseY, 72, 76, "image/aim1.png", 0, 0);
        aim = new Aim(GamePanel.mouseX, GamePanel.mouseY, 4, 4, "image/aim.png", 27, 12);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Cursor myCursor = kit.createCustomCursor(kit.getImage("")
                ,new Point(0, 0), "myCursor");

        while (true) {
            timerFPS = System.nanoTime();
            if (state.equals(STATES.MENU)) {
                this.setCursor(Cursor.getDefaultCursor());
                background.update();
                background.draw(g);
                menu.update();
                menu.draw(g);
                gameDraw();
            }
            if (state.equals(STATES.PLAY)) {
                this.setCursor(myCursor);
                gameUpdate();
                gameRender();
                gameDraw();
            }
            if (state.equals(STATES.GAMEOVER)) {
                this.setCursor(Cursor.getDefaultCursor());
                background.update();
                background.draw(g);
                gameover.draw(g);
                gameover.update();
                gameDraw();
            }

            timerFPS = (System.nanoTime() - timerFPS) / 1000000;
            if (millisPerFrame > timerFPS) {
                sleepTime = (int)(millisPerFrame - timerFPS);
            } else sleepTime = 1;
            try {
                Thread.sleep(sleepTime); // fps
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timerFPS = 0;
            sleepTime = 1;
        }
    }

    // update data of all objects
    public void gameUpdate() {
        background.update();
        player.update();
        aim1.update();
        aim.update();

        // Bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
            boolean remove = bullets.get(i).remove();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }
        // Enemies upd
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // bullets-enemies collides
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX();
            double ey = e.getY();
            double ew = e.getW();
            double eh = e.getH();

            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                double bx = b.getX();
                double by = b.getY();
                double bw = b.getW();
                double bh = b.getH();

                if ((bx > ex - bw) && (bx < ex + ew) && (by > ey - bh) && (by < ey + eh)) {
                    e.hit();
                    bullets.remove(j);
                    j--;
                    explosions.add(new Explosion(e.getX(), e.getY(), e.getR(), e.getR() + 30));
                    boolean remove = e.remove();

                    if (remove) {
                        // chance for powerup
                        double rand = Math.random();
                        if (rand < 0.010) powerUps.add(new PowerUp(1, e.getX(), e.getY()));
                        else if (rand < 0.120) powerUps.add(new PowerUp(2, e.getX(), e.getY()));
                        else if (rand < 0.020) powerUps.add(new PowerUp(3, e.getX(), e.getY()));

                        enemies.remove(i);
                        player.addScore(e.getType() + e.getRank());
                        i--;
                        e.explode();
                        return;
                    }
                }
            }
        }
        // PowerUp upd
        for (int i = 0; i < powerUps.size(); i++) {
            boolean remove = powerUps.get(i).update();
            if (remove) {
                powerUps.remove(i);
                i--;
            }
        }
        // player-enemy collides
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double ex = e.getX(); // get coordinates
            double ey = e.getY();
            double ew = e.getW();
            double eh = e.getH();

            double px = player.getX();
            double py = player.getY();
            double pw = player.getW();
            double ph = player.getH();

            if ((px > ex - pw) && (px < ex + ew) && (py > ey - ph) && (py < ey + eh)) {
                e.hit();
                player.hit();
                boolean remove = e.remove();
                if (remove) {
                    enemies.remove(i);
                    i--;
                    return;
                }
            }
        }
        // player-powerup collision
        double px = player.getX();
        double py = player.getY();
        double pw = player.getW();
        double ph = player.getH();

        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            double x = p.getX();
            double y = p.getY();

            // collected powerup
            if ((px > x - pw) && (px < x + pw) && (py > y - ph) && (py < y + ph)) {
                int type = p.getType();

                if (type == 1 && player.getHealth() < 3) {
                    player.gainLife();
                }
                if (type == 2) {
                    player.increasePower(1);
                }
                if (type == 3) {
                    player.increasePower(2);
                }

                powerUps.remove(i);
                i --;
                player.addScore(10);
            }
        }
        wave.update();

        // Explosion update
        for (int i = 0; i < explosions.size(); i++) {
            boolean remove = explosions.get(i).update();
            if (remove) {
                explosions.remove(i);
                i--;
            }
        }
    }

    // updating graphic elements
    public void gameRender() {
        background.draw(g);
        player.draw(g);

        // bullets draw
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        // enemies draw
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        // draw powerups
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }
        // aim draw
        aim1.draw(g);
        aim.draw(g);

        // wave draw
        if (wave.showWave()) {
            wave.draw(g);
        }
        // explosion draw
        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
        // draw player lives
        health.drawHealth(g);

        //draw player score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 25));
        g.drawString("Score " + player.getScore(), WIDTH - 140, 30);

        // draw player power
        g.setColor(Color.YELLOW);
        g.fillRect(17, 44, player.getPower() * 13, 12);
        g.setColor(Color.YELLOW.darker());
        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < player.getRequiredPower(); i++) {
            g.drawRect(17 + 13 * i, 44, 14, 12);
        }
        g.setStroke(new BasicStroke(1));
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose(); // cleaning
    }
}
