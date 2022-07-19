import javax.swing.*;
import java.awt.*;

public class GameBack {

    private Color color;

    private Image img1 = new ImageIcon("image/SpaceBack.png").getImage();
    private Image img2 = new ImageIcon("image/SpaceBack2.png").getImage();

    public static Wave wave;

    public GameBack() {
        color = Color.BLUE;
    }

    public void update() {
        wave = new Wave();
    }
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.drawImage(img1, 0, 0, null);
    }
}

