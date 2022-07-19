import java.awt.*;

public class Wave {

    private int waveNumber;
    private int waveMultiplier;

    private long waveTimer;
    private long waveDelay;
    private long waveTimerDiff;

    private boolean waveStart;

    private String waveText;

    public Wave() {
        waveNumber = 0;
        waveMultiplier = 5;

        waveTimer = 0;
        waveDelay = 2000;
        waveTimerDiff = 0;
        waveStart = true;

        waveText = " <<<  W A V E  ";
    }

    public void createEnemies() {
        GamePanel.enemies.clear();
        Enemy e;

        for (int i = 0; i < waveNumber * 5; i++) {
            GamePanel.enemies.add(new Enemy(1, 1));
        }
    }
    public int getWaveNumber() { return waveNumber; }

    public void update() {
        if (GamePanel.enemies.size() == 0 && waveTimer == 0) {
            waveTimer = System.nanoTime();
            waveNumber++;
            waveStart = false;
        }
        if (waveStart && GamePanel.enemies.size() == 0) {
            createEnemies();
        }
        if (waveTimer > 0) {
            waveTimerDiff += (System.nanoTime() - waveTimer) / 1000000;
            waveTimer = System.nanoTime();
        }
        if (waveTimerDiff > waveDelay) {
            createEnemies();
            waveTimer = 0;
            waveTimerDiff = 0;
        }
    }

    public boolean showWave() {
        return waveTimer != 0;
    }

    public void draw(Graphics2D g) {
        double divider = (float) waveDelay / 180;
        double alpha = waveTimerDiff / divider;
        alpha = 255 * Math.sin(Math.toRadians(alpha));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.setColor(new Color(255, 255, 255, (int)alpha));
        String s = waveText + waveNumber + "  >>> ";
        long length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.drawString(s, GamePanel.WIDTH / 2 - (int) (length / 2), GamePanel.HEIGHT / 2);
    }
}
