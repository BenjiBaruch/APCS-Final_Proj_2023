import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Tile {
    public int x, y;
    public double r;
    public Color c;
    public String s;

    public Tile(int x, int y, double r, Color c, String s) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.c = c;
        this.s = s;
    }
}
