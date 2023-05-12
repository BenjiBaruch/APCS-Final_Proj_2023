import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    String prompt;
    char[] guess;
    int head;
    double time, timeLimit;
    int wX, wY;
    Font font;

    public GamePanel() {
        prompt = "**PLACEHOLDER";
        guess = null;
        timeLimit = 10;
        time = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\Carnevalee Freakshow.ttf"));
        } catch (IOException | FontFormatException e) {
            System.out.println(e.toString());
            font = Font.getFont(Font.SERIF);
        }
    }

    public void newRound(String prompt, double timeLimit) {
        this.prompt = prompt;
        this.timeLimit = timeLimit;
        guess = null;
        time = 0;
    }

    public void update() {
        repaint();
    }

    public void update(double time) {
        this.time = time;
        repaint();
    }

    public void setGuess(char[] guess) {
        this.guess = guess;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Color getPastel() {
        // Returns pastel color (random color normalized to 0.75-1.00 interval)

        // Get random doubles between 0 and 1 for r, g, and b
        double r = Math.random();
        double g = Math.random();
        double b = Math.random();
        // Get the max and min of the doubles
        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));
        // Get range, but multiply it by four to squish the values together
        double range = (max - min) * 4;
        // Normalize r, g, and b to be between 0.75 and 1.00 with a mean of 0.875
        r = (r - min) / range + 0.75;
        g = (g - min) / range + 0.75;
        b = (b - min) / range + 0.75;
        return new Color((float)r, (float)g, (float)b);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int pSize = (int)((double) Math.min(wX, wY) * 0.8 / Math.max(6, prompt.length()));
        int pX = (int)((double) pSize * prompt.length() / -2);
        int pY = pSize * 2;
        int pArc = pSize / 6;
        int segmentsTotal = 50;
        int segmentsCurrent = (int)(segmentsTotal * time / timeLimit);
        int radius = pSize * prompt.length();
        final double halfpi = 1.57079633D;
        final double twopi = 6.28318531D;
        g2.setFont(font.deriveFont(pSize * 0.8f));
        for (int i = 0; i < prompt.length(); i++) {
            g2.setColor(getPastel());
            g2.fillRoundRect(pX, pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawString(prompt.substring(i,i+1), pX, pY);
        }
        g2.setStroke(new BasicStroke(5F));
        for (int i = 0; i < segmentsCurrent; i++) {
            if (i < segmentsTotal / 2) g2.setColor(new Color(Math.min(255, Math.max(0, (int)(512*segmentsCurrent/segmentsTotal))), 255, 0));
            if (i < segmentsTotal / 2) g2.setColor(new Color(255, Math.min(255, Math.max(0, 512 - (int)(512*segmentsCurrent/segmentsTotal))) , 0));
            g2.drawLine(
                    (int)(radius * Math.cos(-twopi * segmentsCurrent / segmentsCurrent + halfpi)),
                    (int)(radius * Math.sin(twopi * segmentsCurrent / segmentsCurrent + halfpi)),
                    (int)(radius * Math.cos(-twopi * (segmentsCurrent + 1) / segmentsCurrent + halfpi)),
                    (int)(radius * Math.sin(twopi * (segmentsCurrent + 1) / segmentsCurrent + halfpi)));
        }

    }
}
