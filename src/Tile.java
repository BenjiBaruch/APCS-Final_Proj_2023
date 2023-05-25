import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Tile {
    public int x, y, toX, toY, fromX, fromY;
    public double r;
    public Color c;
    public String s;
    public boolean inAnim, inPrompt;

    public Tile(int x, int y, double r, Color c, String s) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.c = c;
        this.s = s;
        fromX = toX = x;
        fromY = toY = y;
        inAnim = inPrompt = false;
    }

    public void setToCoords(int x, int y) {
        toX = x;
        toY = y;
    }
    public void setFromCoords(int x, int y) {
        fromX = x;
        fromY = y;
    }

    public void globalToFrom() {
        fromX = x;
        fromY = y;
    }

    public void toToFrom() {
        fromX = toX;
        fromY = toY;
        toX = x;
        toY = y;
    }

    public void setBools(boolean inAnim, boolean inPrompt) {
        this.inAnim = inAnim;
        this.inPrompt = inPrompt;
    }

    public void breakpoint() {

    }
}
