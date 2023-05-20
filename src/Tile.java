import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Tile {

    /*

    THIS IS WHY WE NEED STRUCTS


     */




    Color color;
    String letter;
    int x, y;
    double r;
    AffineTransform transform;
    RoundRectangle2D rect;

    public Tile(Color color, String letter) {
        this.color = color;
        this.letter = letter;
    }
    public Tile(Color color, String letter, int x, int y, double r, int size) {
        this.color = color;
        this.letter = letter;
        this.x = x;
        this.y = y;
        this.r = r;
        double offset = size/2D;
        transform = new AffineTransform();
        transform.rotate(r, x+offset, y+offset);
        if (size > 0) {
            rect = new RoundRectangle2D.Double(x, y, size, size, size / 6D, size / 6D);
        }
    }

    public void resize(int size) {
        if (size > 0) rect = new RoundRectangle2D.Double(x, y, size, size, size/6D, size/6D);
    }

    public AffineTransform getTransform(int size) {
        return transform;
    }

    public RoundRectangle2D getRect() {
        return rect;
    }

    public Color getColor() {
        return color;
    }

    public String getLetter() {
        return letter;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setR(double r) {
        this.r = r;
    }
}
