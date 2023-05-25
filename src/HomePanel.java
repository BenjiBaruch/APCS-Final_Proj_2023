import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.Timer;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    Color selectedColor1 = Color.YELLOW;
    Color selectedColor2 = new Color(0xd4af37);
    boolean startingGame;
    JFrame window;
    Main game;
    Font font;
    double[][] tilePos;
    Color[] tileColors, buttonColors;
    String[] tileChars;
    Tile[] tiles;
    HashMap<Character, Integer> charCounts;
    int upset, downset;
    int sizeFactor = 8;
    GameStartAnim anim;
    public HomePanel(JFrame window, Main game, Font font, HashMap<Character, Integer> charCounts) {
        this.game = game;
        this.window = window;
        this.font = font;
        this.charCounts = charCounts;
        upset = downset = 50;
        startingGame = false;
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
        repaint();
    }
    public void down() {
        selected = ++selected%4;
        repaint();
    }
    public void select() {
        switch (selected) {
            case 0 -> startGame();
            case 3 -> exit();
        }
    }
    public void exit() {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
    public void reset() {
        upset = downset = 50;
        generateTiles2();
    }
    private void startGame() {
        startingGame = true;
        Timer timer = new Timer();
        anim = new GameStartAnim(this);
        timer.scheduleAtFixedRate(anim, 25L, 25L);
    }
    public void animTick() {
        upset += 25;
        downset -= 25;
        // System.out.println("uh " + upset + ", ");
        if (upset > 1000) {
            anim.cancel();
            game.startGame(0, tiles);
        }
        repaint();
    }
    public void drawButton(Graphics2D g2, String text, Font f, int arcSize, int y, int i) {
        int wX = window.getSize().width;
        g2.setFont(f);
        FontMetrics metrics = g2.getFontMetrics(f);
        g2.setColor(buttonColors[i+1]);
        g2.fillRoundRect(wX/2-(metrics.stringWidth(text)*3/5), y, metrics.stringWidth(text)*6/5, metrics.getHeight()*6/5, arcSize, arcSize);
        if (selected == i) g2.setColor(selectedColor1);
        else g2.setColor(Color.BLACK);
        g2.drawRoundRect(wX/2-(metrics.stringWidth(text)*3/5), y, metrics.stringWidth(text)*6/5, metrics.getHeight()*6/5, arcSize, arcSize);
        if (selected == i) g2.setColor(selectedColor2);
        g2.drawString(text, (wX-metrics.stringWidth(text))/2, y+metrics.getHeight()*7/8);

    }

    public static void drawTiles(Graphics2D g2, Font pieceFont, Tile[] tiles, int size, int animTick) {
        // System.out.println(animTick);
        final int animLength = 10;
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.BLACK);
        g2.setFont(pieceFont);
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());
        for (Tile t : tiles) if (t != null) {
            int x, y;
            double r;
            if (animTick < 0 || !t.inAnim) {
                x = t.x;
                y = t.y;
                r = t.r;
            }
            else if (animTick > animLength) {
                // System.out.print("YOOO    " + t.toX + ',' + t.toY);
                x = t.toX;
                y = t.toY;
                r = 0;
            } else {
                x = ((t.fromX * (animLength - animTick)) + (t.toX * animTick)) / animLength;
                y = ((t.fromY * (animLength - animTick)) + (t.toY * animTick)) / animLength;
                r = t.r * (t.inPrompt ? (animLength - animTick) : animTick) / animLength;
            }
            g2.setColor(t.c);
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x, y, size, size, size/6D, size/6D);
            AffineTransform transform = new AffineTransform();
            transform.rotate(r, x+size/2D, y+size/2D);
            Shape rect2 = transform.createTransformedShape(rect);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            g2.draw(rect2);
            int sx = x - metrics.stringWidth(t.s)/2 + (int)(size/2 * Math.cos(r));
            int sy = y + metrics.getHeight()/3 + size/2;
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
        int size = Math.min(wX, wY) / sizeFactor;
        Font pieceFont = font.deriveFont(size * .8F);
        g2.setFont(pieceFont);
        FontMetrics metrics = g2.getFontMetrics(g2.getFont());

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
        drawTiles(g2, font.deriveFont((float)wX/sizeFactor * .6F), tiles, size, -1);

        // Draw text
        drawButton(g2, "WORD", titleFont, size/6, wY/3 - 18 - upset, -1);
        drawButton(g2, "PLAY", buttonFont, size/6, wY/3 + metrics.getHeight() * 5 / 4 - downset, 0);
        drawButton(g2, "INSTRUCTIONS", buttonFont, size/6, wY/3 + metrics.getHeight() * 9 / 4 - downset, 1);
        drawButton(g2, "LEADERBOARD", buttonFont, size/6, wY/3 + metrics.getHeight() * 13 / 4 - downset, 2);
        drawButton(g2, "QUIT", buttonFont, size/6, wY/3 + metrics.getHeight() * 17 / 4 - downset, 3);
    }
}
