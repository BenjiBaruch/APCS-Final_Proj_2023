import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    Font font;
    double[][] tilePos;
    Color[] tileColors;
    String[] tileChars;
    Tile[] tiles;
    int sizeFactor = 8;
    public HomePanel(JFrame window, Main game, Font font) {
        this.game = game;
        this.window = window;
        this.font = font;
        selected = 0;
        generateTiles2();
    }
    private boolean sufficientDistance(double x1, double y1, double x2, double y2, double threshold) {
        return (x1-x2)*(x1-x2) + (y2-y1)*(y2-y1) > threshold * threshold;
    }
    private void generateTiles1() {
        int tileCount = 20;
        tilePos = new double[tileCount][3];
        tileColors = new Color[tileCount];
        tileChars = new String[tileCount];
        for (int tile = 0; tile < tileCount; tile++) {
            boolean posFound = false;
            double x, y;
            x = y = "I get an error if I don't do this for some reason".hashCode();
            FindXY:
            for (int i = 0; i < 40; i++) {
                x = Math.random();
                y = Math.random();
                if ((x-.5)*(x-.5)+(y-.5)*(y-.5) < .15) continue;
                for (double[] posArr : tilePos) if (posArr != null)
                    if (!sufficientDistance(x, y, posArr[0], posArr[1], .2))
                        continue FindXY;
                posFound = true;
                break;
            }
            if (!posFound) break;
            char c = (char)((int)(Math.random()*26)+'A');
            double r = Math.random() - 0.5;
            double[] pos = {x, y, r};
            // System.out.println("iuzDBGuiB ahfsSH EHdzfSGD");
            tilePos[tile] = pos;
            // System.out.println(Arrays.toString(tilePos[tile]));
            tileColors[tile] = GamePanel.getPastel();
            // System.out.println(tileColors[tile]);
            tileChars[tile] = String.valueOf(c);
            // System.out.println(tileChars[tile]);
        }
    }

    private void generateTiles2() {
        ArrayList<Point> positions = getPoints();
        tiles = new Tile[positions.size()];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(
                    positions.get(i).x,
                    positions.get(i).y,
                    Math.random() - 0.5,
                    GamePanel.getPastel(),
                    String.valueOf((char)((int)(Math.random()*26)+'A')));
        }
    }

    private ArrayList<Point> getPoints() {
        // help from http://devmag.org.za/2009/05/03/poisson-disk-sampling/
        final int width = window.getWidth();
        final int height = window.getHeight();
        final int minDistance = 20;
        final int count = 27;
        final double cellSize = minDistance / Math.sqrt(2);

        final int gridW = (int)Math.ceil(width/cellSize);
        final int gridH = (int)Math.ceil(height/cellSize);
        Point[][] grid = new Point[gridW][gridH];


        ArrayList<Point> pl = new ArrayList<>(27);
        ArrayList<Point> sl = new ArrayList<>(27);

        Point p1 = new Point((int)(Math.random()*width), (int)(Math.random()*height));
        pl.add(p1);
        sl.add(p1);

        Point fp1 = formatPoint(p1, cellSize);
        grid[fp1.x][fp1.y] = p1;

        while (!pl.isEmpty()) {
            Point p2 = pl.remove((int)(Math.random()*pl.size()));
            for (int i = 0; i < count; i++) {
                Point p3 = newPoint(p2, minDistance);
                if (p3.x > 0 && p3.x < width && p3.y > 0 && p3.y < height &&
                    !inNeighborhood(grid, p3, minDistance, cellSize)) {
                    pl.add(p3);
                    sl.add(p3);
                    Point fp3 = formatPoint(p3, cellSize);
                    grid[fp3.x][fp3.y] = p3;
                }
            }
        }
        return sl;
    }

    private Point formatPoint(Point p, double cellSize) {
        int x = (int)(p.x / cellSize);
        int y = (int)(p.y / cellSize);
        return new Point(x, y);
    }

    private Point newPoint(Point p, int minDistance) {
        double r = minDistance * (Math.random() + 1);
        double theta = 2 * Math.PI * Math.random();
        return new Point(
                (int)(p.x + r * Math.cos(theta)),
                (int)(p.y + r * Math.sin(theta))
        );
    }

    private boolean inNeighborhood(Point[][] grid, Point p1, int minDistance, double cellSize) {
        Point gridPoint = formatPoint(p1, cellSize);
        for (int ix = gridPoint.x-2; ix <= gridPoint.x+2; ix++)
            for (int iy = gridPoint.y-2; iy <= gridPoint.y+2; iy++)
                if (grid[ix][iy] != null) {
                    Point p2 = grid[ix][iy];
                    if ((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) < minDistance*minDistance)
                        return true;
                }
        return false;
    }

    // public void resizeTiles() {
    //     for (Tile tile : tiles) if (tile != null) tile.resize(Math.min(window.getSize().width, window.getSize().height) / sizeFactor);
    // }

    public void up() {
        if (selected == 0) selected = 3;
        else selected--;
    }
    public void down() {
        selected = ++selected%4;
    }
    public void select() {

    }
    public void exit() {

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        g2.setFont(font);

        int wX = window.getSize().width;
        int wY = window.getSize().height;

        Font titleFont = font.deriveFont(wX/20F);
        Font buttonFont = font.deriveFont(wX/50F);
        int size = Math.min(wX, wY) / sizeFactor;

        for (Tile t : tiles) if (t != null) {
            // System.out.println("iter " + i);
            // System.out.println(Arrays.toString(tilePos));
            // System.out.println(Arrays.toString(tileColors));
            // System.out.println(Arrays.toString(tileChars));
            g2.setColor(t.c);
            if (t.x < 0 || t.y < 0 || t.x > wX || t.y > wY)
                System.out.println("frick " + t.x + " frack " + t.y);
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(t.x, t.y, size, size, size/6D, size/6D);
            AffineTransform transform = new AffineTransform();
            transform.rotate(t.r, t.x+size/2D, t.y+size/2D);
            Shape rect2 = transform.createTransformedShape(rect);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            g2.draw(rect2);
            // g2.drawString(tileChars[i], tX, tY);
        }
    }
}
