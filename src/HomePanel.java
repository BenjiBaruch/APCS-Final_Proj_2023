import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

public class HomePanel extends JPanel {
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    Font font;
    double[][] tilePos;
    Color[] tileColors;
    String[] tileChars;
    int sizeFactor = 8;
    public HomePanel(JFrame window, Main game, Font font) {
        this.game = game;
        this.window = window;
        this.font = font;
        selected = 0;
        generateTiles();
    }
    private boolean sufficientDistance(double x1, double y1, double x2, double y2, double threshold) {
        return (x1-x2)*(x1-x2) + (y2-y1)*(y2-y1) > threshold * threshold;
    }
    private void generateTiles() {
        int tileCount = 10;
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
                if ((x-.5D)*(x-.5D)+(y-.5D)*(y-.5D) < .5D) continue;
                for (double[] posArr : tilePos) if (posArr != null)
                    if (!sufficientDistance(x, y, posArr[0], posArr[1], .05D))
                        continue FindXY;
                posFound = true;
                break;
            }
            if (!posFound) break;
            char c = (char)((int)(Math.random()*26)+'A');
            double r = Math.random() - 0.5;
            double[] pos = {x, y, r};
            System.out.println("iuzDBGuiB ahfsSH EHdzfSGD");
            tilePos[tile] = pos;
            System.out.println(Arrays.toString(tilePos[tile]));
            tileColors[tile] = GamePanel.getPastel();
            System.out.println(tileColors[tile]);
            tileChars[tile] = String.valueOf(c);
            System.out.println(tileChars[tile]);
        }
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

        for (int i = 0; i < tilePos.length; i++) if (tilePos[i] != null) {
            System.out.println("iter " + i);
            System.out.println(Arrays.toString(tilePos));
            System.out.println(Arrays.toString(tileColors));
            System.out.println(Arrays.toString(tileChars));
            g2.setColor(tileColors[i]);
            int tX = (int)(tilePos[i][0]*(wX-size*2));
            int tY = (int)(tilePos[i][1]*(wY-size*2));
            if (tX < 0 || tY < 0 || tX>wX || tY>wY) System.out.println("frick " + tX + " frack " + tY);
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(tX, tY, size, size, size/6D, size/6D);
            AffineTransform transform = new AffineTransform();
            transform.rotate(tilePos[i][2], tX+size/2D, tY+size/2D);
            Shape rect2 = transform.createTransformedShape(rect);
            g2.fill(rect2);
            g2.setColor(Color.BLACK);
            g2.draw(rect2);
            g2.drawString(tileChars[i], tX, tY);
        }
    }
}
