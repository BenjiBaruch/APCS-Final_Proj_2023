import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    String prompt;
    Color[] promptColors;
    Color[] guessColors;
    char[] guess;
    int head;
    double time, timeLimit;
    int wX, wY;
    Font font;
    JFrame window;

    public GamePanel(JFrame window) {
        prompt = "**PLACEHOLDER";
        guess = null;
        this.window = window;
        timeLimit = 10;
        time = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("data/Hegimeda.otf"));
        } catch (IOException | FontFormatException e) {
            System.out.println(e.toString());
            font = Font.getFont(Font.SERIF);
        }
    }

    public void newRound(String prompt, double timeLimit) {
        this.prompt = prompt;
        promptColors = new Color[prompt.length()];
        for (int i = 0; i < prompt.length(); i++)
            promptColors[i] = getPastel();
        guessColors = new Color[64];
        for (int i = 0; i < 64; i++)
            guessColors[i] = getPastel();
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
        Dimension d = window.getSize();
        wX = d.width;
        wY = d.height;
        Graphics2D g2 = (Graphics2D) g;
        int pSize = (int)((double) Math.min(wX, wY) * 0.8 / Math.max(6, prompt.length()));
        int pX = (int)((double) pSize * prompt.length() / -2) + wX/2;
        int pY = pSize * 2;
        int pArc = pSize / 6;
        int segmentsTotal = 150;
        int segmentsCurrent = (int)(segmentsTotal * (1 - (time / timeLimit)));
        int radius = pSize * prompt.length();
        final double halfpi = 1.57079633D;
        final double twopi = 6.28318531D;
        g2.setFont(font.deriveFont(pSize * 0.8f));
        for (int i = 0; i < prompt.length(); i++) {
            g2.setColor(promptColors[i]);
            g2.fillRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawString(prompt.substring(i,i+1), pX + (int)(pSize * 1.2 * (i + 0.3)), pY + (int)(pSize * 0.7));
        }
        g2.setStroke(new BasicStroke(5F));
        if (segmentsCurrent > segmentsTotal / 2) g2.setColor(new Color(Math.min(255, Math.max(0, 512-(int)(512*segmentsCurrent/segmentsTotal))), 255, 0));
        else g2.setColor(new Color(255, Math.min(255, Math.max(0, (int)(512*segmentsCurrent/segmentsTotal))) , 0));
        for (int i = 0; i < segmentsCurrent; i++) {
            double theta1 = -twopi * i / segmentsTotal + halfpi;
            double theta2 = -twopi * (i + 1) / segmentsTotal + halfpi;
            g2.drawLine(
                    (int)(radius * Math.cos(theta1) + (wX/2)),
                    (int)(radius * Math.sin(theta1) + (wY/2)),
                    (int)(radius * Math.cos(theta2) + (wX/2)),
                    (int)(radius * Math.sin(theta2) + (wY/2)));
        }

    }
}
