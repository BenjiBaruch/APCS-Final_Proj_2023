import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class EndPanel extends JPanel {
    JFrame window;
    Main game;
    Font font;
    ArrayList<Integer> scores;
    int score;

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
        this.score = score;
        scores.add(score);
    }
    public void writeCenteredString(Graphics2D g2, int y, int size, String str) {
        g2.setFont(font.deriveFont(size));
        int x = (int)(window.getSize().getWidth()-g2.getFontMetrics().stringWidth(str))/2;
        System.out.println(x);
        g2.drawString(str, x, y);
    }
    public void writeLeftAlignedString(Graphics2D g2, int y, int size, String str) {
        g2.setFont(font.deriveFont(size));
        int x = ((int)window.getSize().getWidth()/5)+size;
        System.out.println(x);
        g2.drawString(str, x, y);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(8));
        int wX = window.getWidth();
        int wY = window.getHeight();

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(wX / 8, wY / 8, wX * 3 / 4, wY * 3 / 4, wX / 14, wX / 14);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(wX / 8, wY / 8, wX * 3 / 4, wY * 3 / 4, wX / 14, wX / 14);
        writeCenteredString(g2, 200, 20, "HAHAHA YOU LOSE (loser)");
        writeLeftAlignedString(g2, 450, 20, "Score: " + score);
    }
}