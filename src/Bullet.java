import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet {

    private double x;
    private double y;
    private double w;
    private double h;

    private double distX;
    private double distY;
    private int r;

    private double speed;

    Image img;

    public Bullet (double angle, double x, double y) {
        this.x = GamePanel.player.getX() + 25;
        this.y = GamePanel.player.getY() + 25;
        w = 7;
        h = 28;
        r = 2;
        speed = 20;

        distX = GamePanel.mouseX - x; // distance between aim and bullet
        distY = y - GamePanel.mouseY;
        img = new ImageIcon("image/bullet.png").getImage().getScaledInstance((int) w, (int) h, 1);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getW() { return w; }
    public double getH() { return h; }

    public boolean remove() {
        return y < 0 || y > GamePanel.HEIGHT || x < 0 || x > GamePanel.WIDTH;
    }

    public boolean update() {
        y = y - speed * distY/(Math.sqrt(distX*distX + distY*distY));
        x = x + speed * distX/(Math.sqrt(distX*distX + distY*distY));

        return x < -r || x > GamePanel.WIDTH + r ||
                y < -r || y > GamePanel.HEIGHT + r;
    }

    public void draw(Graphics2D g) {
        AffineTransform origXform;
        origXform = g.getTransform();
        AffineTransform newXform = (AffineTransform) (origXform.clone());
        if (distX > 0) newXform.rotate(Math.acos(distY/(Math.sqrt(distX*distX + distY*distY))), x, y); // rotate
        if (distX < 0) newXform.rotate(-Math.acos(distY/(Math.sqrt(distX*distX + distY*distY))), x, y);
        g.setTransform(newXform);
        g.drawImage(img, (int)x, (int)y, null);
        g.setTransform(origXform);
    }
}
