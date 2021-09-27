import javax.swing.*;
import java.awt.*;

public class Aim {
    private double x;
    private double y;
    private double w;
    private double h;
    private double dx;
    private double dy;
    private String img;

    public Aim (int x, int y, int w, int h, String img, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.img = img;
        this.dx = dx;
        this.dy = dy;
    }

    public Rectangle getRect() {
        return new Rectangle((int) x, (int) y, (int) w, (int) h);
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getW() {return w;}
    public double getH() {return h;}

    public void update() {
        this.x = GamePanel.mouseX + dx;
        this.y = GamePanel.mouseY + dy;
    }

    public void draw(Graphics2D g) {
        Image im = new ImageIcon(img).getImage();
        g.drawImage(im, (int) this.x, (int) this.y, null);
    }
}