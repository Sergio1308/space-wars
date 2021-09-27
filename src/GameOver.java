import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameOver {
    // Fields
    private int buttonWidth;
    private int buttonHeight;
    private Color color1;
    private String s;
    private String s2;
    private int transp;

    public static Player player;

    private int rnd;
    private long Timer;
    private long Delay;
    private long TimerDiff;

    private Image img1 = new ImageIcon("image/SpaceOver1.png").getImage();
    private Image img2 = new ImageIcon("image/SpaceOver2.png").getImage();
    //private Image img3 = new ImageIcon("image/SpaceOver3.png").getImage();

    // Constructor
    public GameOver() {
        buttonWidth = 170;
        buttonHeight = 60;

        s = "Game Over!";
        s2 = "Restart";
        Timer = 50;
        Delay = 1700;
        TimerDiff = 50;
        transp = 0;

        player = new Player();

        rnd = (int) (Math.random() * 2);
    }
    public void update() {
//        if (GamePanel.mouseX > GamePanel.WIDTH / 2  - buttonWidth / 2 &&
//                GamePanel.mouseX < GamePanel.WIDTH / 2 + buttonWidth / 2 &&
//                GamePanel.mouseY > GamePanel.HEIGHT / 2 - buttonHeight / 2 &&
//                GamePanel.mouseY < GamePanel.HEIGHT / 2 + buttonHeight / 2) {
//            transp = 60;
//            if (GamePanel.leftMouse) {
//                GamePanel.state = GamePanel.STATES.PLAY;
//            }
//        } else {
//            transp = 0;
//        }
    }

    // Functions
    public void draw(Graphics2D g) {

        if (rnd == 0) g.drawImage(img1, 0, 0, null);
        if (rnd == 1) g.drawImage(img2, 0, 0, null);
        //if (rnd == 2) g.drawImage(img3, 0, 0, null);

//        // button
//        g.setColor(Color.WHITE);
//        g.setStroke(new BasicStroke(3));
//        g.drawRect(GamePanel.WIDTH / 3 + buttonWidth / 2 - 10,
//                GamePanel.HEIGHT / 2 + buttonHeight * 3, buttonWidth, buttonHeight);
//        g.setColor(new Color(255, 255, 255, transp));
//        g.fillRect(GamePanel.WIDTH / 3 + buttonWidth / 2 - 10,
//                GamePanel.HEIGHT / 2 + buttonHeight * 3,buttonWidth, buttonHeight);
//        g.setStroke(new BasicStroke(1));
//        // text
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Consolas", Font.BOLD, 40));
//        g.drawString(s2, (int)(GamePanel.WIDTH / 3 + buttonWidth / 2),
//                (GamePanel.HEIGHT / 2 + buttonHeight * 4 - 15));

        // flashing inscription
        double divider = Delay / 180;
        double alpha = TimerDiff / divider;
        alpha = 255 * Math.sin(Math.toRadians(alpha));

        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        long length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.setFont(new Font("Broadway", Font.BOLD, 62));
        g.setColor(new Color(0, 0, 10, (int)alpha));
        g.drawString(s, (int)(GamePanel.WIDTH / 2 - length / 2 - 7), (GamePanel.HEIGHT / 2));
        g.setFont(new Font("Broadway", Font.BOLD, 60));
        g.setColor(new Color(255, 0, 0, (int)alpha));
        g.drawString(s, GamePanel.WIDTH / 2 - (int) (length / 2), GamePanel.HEIGHT / 2);

        if (Timer == 0) {
            Timer = System.nanoTime();
        }
        if (Timer > 0) {
            TimerDiff += (System.nanoTime() - Timer) / 1000000;
            Timer = System.nanoTime();
        }
        if (TimerDiff > Delay) {
            Timer = 0;
            TimerDiff = 0;
        }
    }

}
