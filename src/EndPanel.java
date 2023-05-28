import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class EndPanel extends JPanel {
    private final JFrame window;
    private final Main game;
    private final Font font;
    private ArrayList<Integer> scores;
    private int score;

    public EndPanel(JFrame window, Main game, Font font) {
        this.window = window;
        this.game = game;
        this.font = font;
        score = 0;
        loadScores();
    }

    public void loadScores() {
        scores = new ArrayList<>(1);
    }

    public void newScore(int score) {
        // Adds score to array and writes it to score file
        this.score = score;
        scores.add(score);
    }
    public void writeCenteredString(Graphics2D g2, int y, int size, String str) {
        // Draws centered screen in frame
        g2.setFont(font.deriveFont(size));
        int x = (int)(window.getSize().getWidth()-g2.getFontMetrics().stringWidth(str))/2;
        System.out.println(x);
        g2.drawString(str, x, y);
    }
    public void writeLeftAlignedString(Graphics2D g2, int y, int size, String str) {
        // Draws left aligned string in frame
        g2.setFont(font.deriveFont(size));
        g2.setColor(Color.BLACK);
        int x = ((int)window.getSize().getWidth()/5)+size;
        System.out.println(x);
        // g2.drawRect(x, y, 100, 100);
        g2.drawString(str, x, y);
    }
    public void paintComponent(Graphics g) {
        // Draws end screen
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(8));
        g2.setFont(font);
        int wX = window.getWidth();
        int wY = window.getHeight();

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(wX / 8, wY / 8, wX * 3 / 4, wY * 3 / 4, wX / 14, wX / 14);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(wX / 8, wY / 8, wX * 3 / 4, wY * 3 / 4, wX / 14, wX / 14);
        writeCenteredString(g2, 20, 20, "HAHAHA YOU LOSE (loser)");
        writeLeftAlignedString(g2, 100, 20, "Score: " + score);
        g2.drawString("HELLO", 100, 100);
    }
}