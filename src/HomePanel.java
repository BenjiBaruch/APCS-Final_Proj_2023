import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    Font font;
    Tile[] tiles;
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
        int tileCount = 20;
        tiles = new Tile[tileCount];
        int separation = Math.min(window.getX(), window.getY()) / 8;
        int size = Math.min(window.getX(), window.getY()) / 12;
        for (int tile = 0; tile < tileCount; tile++) {
            boolean posFound = false;
            int x, y;
            FindXY:
            for (int i = 0; i < 40; i++) {
                x = (int)(Math.random()*1000);
                y = (int)(Math.random()*1000);
                if ((x-500)*(x-500)+(y-500)*(y-500) < 1000) continue FindXY;
                for (int j = 0; j < tile; j++)
                    if (!sufficientDistance(x, y, tiles[j].getX(), tiles[j].getY(), separation))
                        continue FindXY;
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
        for (Tile tile : tiles) if (tile != null) tile.resize(Math.min(window.getX(), window.getY()) / 12);
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

        int wX = window.getX();
        int wY = window.getY();

        Font titleFont = font.deriveFont(wX/20F);
        Font buttonFont = font.deriveFont(wX/50F);

        for (Tile tile : tiles) if (tile != null) {
        }
    }
}
