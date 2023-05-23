import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    Font font;
    Tile[] tiles;
    int sizeFactor = 8;
    public HomePanel(JFrame window, Main game, Font font) {
        this.game = game;
        this.window = window;
        this.font = font;
        selected = 0;
        generateTiles();
    }
    private boolean sufficientDistance(int x1, int y1, int x2, int y2, int threshold) {
        return (x1-x2)*(x1-x2) + (y2-y1)*(y2-y1) > threshold * threshold;
    }
    private void generateTiles() {
        int tileCount = 10;
        tiles = new Tile[tileCount];
        int separation = Math.min(window.getSize().width, window.getSize().height) / sizeFactor;
        int size = Math.min(window.getSize().width, window.getSize().height) / sizeFactor;
        for (int tile = 0; tile < tileCount; tile++) {
            boolean posFound = false;
            int x, y;
            x = y = "I get an error if I don't do this for some reason".hashCode();
            FindXY:
            for (int i = 0; i < 40; i++) {
                x = (int)(Math.random()*900)+50;
                y = (int)(Math.random()*900)+50;
                // if ((x-500)*(x-500)+(y-500)*(y-500) < 1000) continue;
                // for (int j = 0; j < tile; j++)
                //     if (!sufficientDistance(x, y, tiles[j].getX(), tiles[j].getY(), separation))
                //         continue FindXY;
                posFound = true;
                break;
            }
            if (!posFound) break;
            char c = (char)((int)(Math.random()*26)+'A');
            double r = Math.random() - 0.5;
            tiles[tile] = new Tile(GamePanel.getPastel(), String.valueOf(c), x, y, r, size);
        }
    }

    public void resizeTiles() {
        for (Tile tile : tiles) if (tile != null) tile.resize(Math.min(window.getSize().width, window.getSize().height) / sizeFactor);
    }

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

        for (Tile tile : tiles) if (tile != null) {
            g2.setColor(tile.getColor());
            int tX = tile.getX()*wX/2000;
            int tY = tile.getY()*wY/2000;
            if (tX < 0 || tY < 0 || tX>wX || tY>wY) System.out.println("frick " + tX + " frack " + tY);
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(tX, tY, size, size, size/6D, size/6D);
            AffineTransform transform = new AffineTransform();
            transform.rotate(tile.getR(), tX+size/2D, tY+size/2D);
            Shape rect2 = transform.createTransformedShape(rect);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            g2.draw(rect2);
            g2.drawString(tile.getLetter(), tX, tY);
            g2.rotate(-tile.getR());
        }
    }
}
