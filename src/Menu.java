import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Menu {

    // Fields
    private int buttonWidth;
    private int buttonHeight;

    private Color color1;
    private String startStr;
    private int transp;

    private long waveTimer;
    private long waveDelay;
    private long waveTimerDiff;

    private String waveText;
    private Image img = new ImageIcon("image\\SpaceMenu.jpg").getImage();

    // Constructor
    public Menu() {
        buttonWidth = 120;
        buttonHeight = 60;
        color1 = Color.white;
        transp = 0;
        startStr = "Play!";

        waveTimer = 50;
        waveDelay = 1300;
        waveTimerDiff = 50;
        waveText = " Or press Space to play! ";

    }

    // Functions
    public void update() {
        if (GamePanel.mouseX > GamePanel.WIDTH / 2  - buttonWidth / 2 &&
                GamePanel.mouseX < GamePanel.WIDTH / 2 + buttonWidth / 2 &&
                GamePanel.mouseY > GamePanel.HEIGHT / 2 - buttonHeight / 2 &&
                GamePanel.mouseY < GamePanel.HEIGHT / 2 + buttonHeight / 2) {
            transp = 60;
            if (GamePanel.leftMouse) {
                GamePanel.state = GamePanel.STATES.PLAY; //ERROR
            }
        } else {
            transp = 0;
        }
    }


    public void draw(Graphics2D g) {

        // background image
        g.drawImage(img, 0, 0, GamePanel.WIDTH,GamePanel.HEIGHT + 100, null);

        // flashing inscription under "Play" button
        double divider = waveDelay / 180;
        double alpha = waveTimerDiff / divider;
        alpha = 255 * Math.sin(Math.toRadians(alpha));

        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        String s = waveText;
        long length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();

        g.setFont(new Font("Consolas", Font.PLAIN, 40));
        g.setColor(new Color(255, 255, 255, (int)alpha));
        g.drawString(s, GamePanel.WIDTH / 2 - (int) (length / 2), GamePanel.HEIGHT - (50));
        g.setColor(new Color(255, 0, 0, (int)alpha));
        g.drawString(s, GamePanel.WIDTH / 2 - (int) (length / 2) - 1, GamePanel.HEIGHT - (50));

        if (waveTimer == 0) {
            waveTimer = System.nanoTime();
        }
        
        if (waveTimer > 0) {
            waveTimerDiff += (System.nanoTime() - waveTimer) / 1000000;
            waveTimer = System.nanoTime();
        }
        
        if (waveTimerDiff > waveDelay) {
            waveTimer = 0;
            waveTimerDiff = 0;
        }

        // "Play" button
        g.setColor(color1);
        g.setStroke(new BasicStroke(5));
        g.drawRect(GamePanel.WIDTH / 2 - buttonWidth / 2,
                GamePanel.HEIGHT / 2 - buttonHeight / 2,buttonWidth, buttonHeight);
        g.setColor(new Color(255, 255, 255, transp));
        g.fillRect(GamePanel.WIDTH / 2 - buttonWidth / 2,
                GamePanel.HEIGHT / 2 - buttonHeight / 2,buttonWidth, buttonHeight);
        g.setStroke(new BasicStroke(1));

        g.setColor(color1);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        g.drawString(startStr, (int)(GamePanel.WIDTH / 3 + buttonWidth),
                (GamePanel.HEIGHT / 2 + buttonHeight / 4));
    }
}
