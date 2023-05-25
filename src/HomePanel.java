import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    Font font;
    double[][] tilePos;
    Color[] tileColors;
    Color[] buttonColors;
    String[] tileChars;
    Tile[] tiles;
    HashMap<Character, Integer> charCounts;
    int sizeFactor = 8;
    public HomePanel(JFrame window, Main game, Font font, HashMap<Character, Integer> charCounts) {
        this.game = game;
        this.window = window;
        this.font = font;
        this.charCounts = charCounts;
        buttonColors = new Color[5];
        for (int i = 0; i < 5; i++) buttonColors[i] = GamePanel.getPastel();
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
        int offset = window.getWidth() / sizeFactor / 2;
        Character[] chars = new Character[50];
        int charLen = 0;
        for (char c = 'A'; c <= 'Z'; c++) for (int i = 0; i < charCounts.get(c); i++) chars[charLen++] = c;
        chars = Arrays.copyOfRange(chars, 0, charLen);
        Collections.shuffle(Arrays.asList(chars));
        System.out.println(Arrays.toString(chars));
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(
                    positions.get(i).x - offset,
                    positions.get(i).y - offset,
                    Math.random() - 0.5,
                    GamePanel.getPastel(),
                    i < charLen ? String.valueOf(chars[i]) : String.valueOf((char)((int)(Math.random()*26)+'A')));
        }
    }

    private ArrayList<Point> getPoints() {
        // help from http://devmag.org.za/2009/05/03/poisson-disk-sampling/
        final int width = window.getWidth();
        final int height = window.getHeight();
        final int minDistance = 80;
        final int count = 27;
        final int size = Math.min(width, height) / sizeFactor;
        final double cellSize = minDistance / Math.sqrt(2);

        final int gridW = (int)Math.ceil(width/cellSize);
        final int gridH = (int)Math.ceil(height/cellSize);
        Point[][] grid = new Point[gridW][gridH];


        ArrayList<Point> pl = new ArrayList<>(27);
        ArrayList<Point> sl = new ArrayList<>(27);

        Point p1 = null;
        while (p1 == null || inNeighborhood(grid, p1, minDistance, cellSize, width/2, height/2))
            p1 = new Point((int)(Math.random()*width), (int)(Math.random()*height));
        pl.add(p1);
        sl.add(p1);

        Point fp1 = formatPoint(p1, cellSize);
        grid[fp1.x][fp1.y] = p1;

        while (!pl.isEmpty()) {
            Point p2 = pl.remove((int)(Math.random()*pl.size()));
            for (int i = 0; i < count; i++) {
                Point p3 = newPoint(p2, minDistance, size / 2);
                if (p3.x > 0 && p3.x < width && p3.y > 0 && p3.y < height &&
                    !inNeighborhood(grid, p3, minDistance, cellSize, width/2, height/2)) {
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

    private Point newPoint(Point p, int minDistance, int hS) {
        double r = minDistance * (Math.random() + 1);
        double theta = 2 * Math.PI * Math.random();
        return new Point(
                (int)(p.x + r * Math.cos(theta)),
                (int)(p.y + r * Math.sin(theta))
        );
    }

    private boolean inNeighborhood(Point[][] grid, Point p1, int minDistance, double cellSize, int hW, int hH) {
        Point gridPoint = formatPoint(p1, cellSize);
        if ((p1.x-hW)*(p1.x-hW) + (p1.y-hH)*(p1.y-hH) < Math.min(hW,hH)*Math.min(hW,hH)*3/5) return true;
        for (int ix = Math.max(0, gridPoint.x-2); ix <= Math.min(grid.length-1, gridPoint.x+2); ix++)
            for (int iy = Math.max(0, gridPoint.y-2); iy <= Math.min(grid[0].length-1, gridPoint.y+2); iy++)
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
    public void drawButton(Graphics2D g2, String text, Font f, int arcSize, int y, int i) {
        int wX = window.getSize().width;
        int wY = window.getSize().height;
        g2.setFont(f);
        FontMetrics metrics = g2.getFontMetrics(f);
        g2.setColor(buttonColors[i]);
        g2.fillRoundRect(wX/2-(metrics.stringWidth(text)*3/5), y, metrics.stringWidth(text)*6/5, metrics.getHeight()*6/5, arcSize, arcSize);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(wX/2-(metrics.stringWidth(text)*3/5), y, metrics.stringWidth(text)*6/5, metrics.getHeight()*6/5, arcSize, arcSize);
        g2.drawString(text, (wX-metrics.stringWidth(text))/2, y+metrics.getHeight()*7/8);

    }

    public void drawTiles(Graphics2D g2, JFrame window) {
        int wX = window.getWidth();
        int wY = window.getHeight();
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.BLACK);
        Font pieceFont = font.deriveFont((float)wX/sizeFactor * .6F);
        g2.setFont(pieceFont);
        int size = Math.min(wX, wY) / sizeFactor;
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());
        for (Tile t : tiles) if (t != null) {
            g2.setColor(t.c);
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(t.x, t.y, size, size, size/6D, size/6D);
            AffineTransform transform = new AffineTransform();
            transform.rotate(t.r, t.x+size/2D, t.y+size/2D);
            Shape rect2 = transform.createTransformedShape(rect);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            g2.draw(rect2);
            int sx = t.x - metrics.stringWidth(t.s)/2 + (int)(size/2 * Math.cos(t.r));
            int sy = t.y + metrics.getHeight()/3 + size/2;
            g2.drawString(t.s, sx, sy);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int wX = window.getSize().width;
        int wY = window.getSize().height;

        // Set fonts
        Font titleFont = font.deriveFont(wX/13F);
        Font buttonFont = font.deriveFont(wX/25F);
        Font pieceFont = font.deriveFont((float)wX/sizeFactor * .6F);
        g2.setFont(pieceFont);
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());
        int size = Math.min(wX, wY) / sizeFactor;

        // Draw circle
        int radius = (int)(Math.min(wX, wY) * 0.4);
        g2.setStroke(new BasicStroke(Math.min(wX, wY) / 80F));
        g2.setColor(Color.RED);
        for (int i = 0; i < 150; i++) {
            double theta1 = (2*Math.PI * i / 150) + (Math.PI / 2);
            double theta2 = (2*Math.PI * (i + 1) / 150) + (Math.PI / 2);
            g2.drawLine(
                    wX/2 + (int)(radius * Math.cos(theta1)),
                    wY/2 + (int)(radius * -Math.sin(theta1)),
                    wX/2 + (int)(radius * Math.cos(theta2)),
                    wY/2 + (int)(radius * -Math.sin(theta2)));
        }

        // Draw tiles
        drawTiles(g2, window);

        // Draw text
        final int upset = 50;
        drawButton(g2, "WORD", titleFont, size/6, wY/3 - 18 - upset, 0);
        drawButton(g2, "PLAY", buttonFont, size/6, wY/3 + metrics.getHeight() * 5 / 4 - upset, 1);
        drawButton(g2, "INSTRUCTIONS", buttonFont, size/6, wY/3 + metrics.getHeight() * 9 / 4 - upset, 2);
        drawButton(g2, "LEADERBOARD", buttonFont, size/6, wY/3 + metrics.getHeight() * 13 / 4 - upset, 3);
        drawButton(g2, "QUIT", buttonFont, size/6, wY/3 + metrics.getHeight() * 17 / 4 - upset, 4);
    }
}
