import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet {
    // Fields
    private double x;
    private double y;
    private double w;
    private double h;

    private double distX;
    private double distY;
    private double dist;
    private int r;

//    private double dx;
//    private double dy;
//    private double rad;
    private double speed;

    private Color color;

    Image img = new ImageIcon("image/bullet.png").getImage();

    // Constructor
    public Bullet (double angle, double x, double y) {
        this.x = GamePanel.player.getX() + 29;
        this.y = GamePanel.player.getY() + 25;
        w = 10;
        h = 42;
        r = 2;
        speed = 20;

        distX = GamePanel.mouseX - x; // разница от прицела до пули
        distY = y - GamePanel.mouseY;

        color = Color.WHITE;
    }

    //  Functions
    public double getX() { return x; }
    public double getY() { return y; }
    public double getW() { return w; }
    public double getH() { return h; }
    public int getR() { return r; }

    public boolean remove() {
        if (y < 0 || y > GamePanel.HEIGHT || x < 0 || x > GamePanel.WIDTH) {
            return true;
        }
        return false;
    }

    public boolean update() {

        y = y - speed * distY/(Math.sqrt(distX*distX + distY*distY));
        x = x + speed * distX/(Math.sqrt(distX*distX + distY*distY));


        if (x < -r || x > GamePanel.WIDTH + r ||
                y < -r || y > GamePanel.HEIGHT + r) {
            return true;
        } return false;
    }

    public void draw(Graphics2D g) {
//        g.setColor(color);
//        g.fillOval((int) x, (int) y, r, 2 * r);
        AffineTransform origXform; //создание объекта
        origXform = g.getTransform(); //получаем текущее значение
        AffineTransform newXform = (AffineTransform) (origXform.clone()); //клон текущего знач
        if (distX > 0) newXform.rotate(Math.acos(distY/(Math.sqrt(distX*distX + distY*distY))), x, y); //поворот изображения
        if (distX < 0) newXform.rotate(-Math.acos(distY/(Math.sqrt(distX*distX + distY*distY))), x, y);
        g.setTransform(newXform); //установка текущюю трансформацию
        g.drawImage(img, (int)x, (int)y, null); // зарисовка
        g.setTransform(origXform); //возвращение старого значения
    }
}
