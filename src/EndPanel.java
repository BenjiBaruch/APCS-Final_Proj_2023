import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        scores = new ArrayList<>(10);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/leaderboard.txt"));
            for (int i = 0; i < 10; i++) scores.add(Integer.parseInt(reader.readLine().strip()));
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to read to file");
        }
    }

    public void newScore(int score) {
        // Adds score to array and writes it to score file
        this.score = score;
        for (int i = 0; i < 10; i++) {
            if (scores.get(i) < score) {
                scores.add(i, score);
                break;
            }
        }
        while (scores.size() > 10) scores.remove(10);
        try {
            FileWriter writer = new FileWriter("data/leaderboard.txt", false);
            for (int i : scores) writer.write(i + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }
    public void writeCenteredString(Graphics2D g2, int y, int size, String str) {
        // Draws centered screen in frame
        g2.setFont(font.deriveFont((float)size));
        int x = (int)(window.getSize().getWidth()-g2.getFontMetrics().stringWidth(str))/2;
        System.out.println(x);
        g2.drawString(str, x, y);
    }
    public void writeLeftAlignedString(Graphics2D g2, int y, int size, String str) {
        // Draws left aligned string in frame
        g2.setFont(font.deriveFont((float)size));
        int x = ((int)window.getSize().getWidth()/5);
        g2.drawString(str, x, y);
    }
    public void writeMiddleAlignedString(Graphics2D g2, int y, int size, String str) {
        // Draws left aligned string in frame
        g2.setFont(font.deriveFont((float)size));
        int x = ((int)window.getSize().getWidth()/2);
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
        writeCenteredString(g2, wY/4 + 10, 50, "HAHAHA YOU LOSE (loser)");
        writeLeftAlignedString(g2, wY/4 + 80, 60, "Score: " + score);
        writeLeftAlignedString(g2, wY/4 + 160, 60, "Leaderboard:");
        for (int i = 0; i < 5; i++) {
            writeLeftAlignedString(g2, wY/4 + 200 + 30 * i, 25, i+1 + ": " + scores.get(i));
        }
        for (int i = 0; i < 5; i++) {
            writeMiddleAlignedString(g2, wY/4 + 200 + 30 * i, 25, i+6 + ": " + scores.get(i+5));
        }
        writeCenteredString(g2, wY*7/8 - 40, 15, "I RAN OUT OF TIME TO MAKE A GOOD LOOKING END SCREEN");
        writeCenteredString(g2, wY*7/8 - 20, 15, "JUST PRESS 'ENTER', OKAY?");
    }
}