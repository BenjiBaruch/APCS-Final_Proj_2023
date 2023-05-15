import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    String prompt, guess;
    Color[] promptColors, guessColors;
    double time, timeLimit;
    int wX, wY, wSize;
    Font font;
    JFrame window;
    final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public GamePanel(JFrame window) {
        prompt = "**PLACEHOLDER";
        guess = "";
        this.window = window;
        timeLimit = 10;
        time = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("data/Silvera Peach.otf"));
        } catch (IOException | FontFormatException e) {
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
        guess = "";
        time = 0;
    }

    public void update() {
        repaint();
    }

    public void update(double time) {
        this.time = time;
        repaint();
    }

    public void setGuess(String guess) {
        System.out.println(guess);
        this.guess = guess;
    }

    public void setTime(double time) {
        this.time = time;
    }

    private Color getPastel() {
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

    private Color pastelize(Color c) {
        int min = Math.min(c.getRed(), Math.min(c.getGreen(), c.getBlue()));
        int max = Math.max(c.getRed(), Math.max(c.getGreen(), c.getBlue()));
        int range = (max - min) * 4;
        return new Color(
                    (c.getRed() - min) / range + 192,
                    (c.getGreen() - min) / range + 192,
                    (c.getBlue() - min) / range + 192
        );
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set variables
        Dimension d = window.getSize();
        wX = d.width;
        wY = d.height;
        wSize = Math.min(wX, wY) ;
        Graphics2D g2 = (Graphics2D) g;
        int pSize = (int)((double) wSize * 0.8 / Math.max(6, prompt.length()));
        int pX = (int)((double) pSize * prompt.length() / -2) + wX/2;
        int pY = wSize/2 - (int)(pSize*1.5);
        int pArc = pSize / 6;
        int segmentsTotal = 150;
        int segmentsCurrent = (int)(segmentsTotal * (1 - (time / timeLimit)));
        int radius = (int)(wSize * 0.4);
        final double halfpi = 1.57079633D;
        final double twopi = 6.28318531D;

        // Configure g2
        g2.setFont(font.deriveFont(pSize * 0.8f));
        g2.setStroke(new BasicStroke(wSize / 80F));
        g2.setBackground(Color.WHITE);

        // Glow
        if (segmentsCurrent > segmentsTotal / 2) g2.setColor(new Color(Math.min(255, Math.max(0, 512-(512*segmentsCurrent/segmentsTotal))), 255, 0));
        else g2.setColor(new Color(255, Math.min(255, Math.max(0, (512*segmentsCurrent/segmentsTotal))) , 0));
        Paint paint = g2.getPaint();
        Color faded1 = new Color(g2.getColor().getRed()/2+128, g2.getColor().getGreen()/2+128, 128);
        Color faded2 = new Color(faded1.getRed()/8+223, faded1.getGreen()/8+223, faded1.getBlue()/8+223);
        g2.setColor(faded2);
        g2.fillRect((wX-wSize)/2, (wY-wSize)/2, wSize, wSize);
        Color[] circColor = {faded1, faded2};
        float[] dist = {0F, 0.8F};
        // System.out.println("wX = " + wX + ", wY = " + wY + ", wSize = " + wSize);
        // wX = 800, wY = 650, wSize = 650
        final Paint radial = new RadialGradientPaint(wX/2F, wY/2F,  wSize/2F, dist, circColor);
        g2.setPaint(radial);
        g2.fillOval(wX/2 - wSize/2, wY/2 - wSize/2, wSize, wSize);
        // g2.fillRect(wX/2, wY/2, wSize/4, wSize/4);
        g2.setPaint(paint);

        // Timer
        g2.fillOval(
                wX/2 - wSize/80,
                wY/2 - wSize/80 - radius,
                wSize / 40, wSize / 40);
        g2.fillOval(
                wX/2 - wSize/80 + (int)(radius * Math.cos((2*Math.PI * segmentsCurrent / segmentsTotal) + (Math.PI / 2))),
                wY/2 - wSize/80 + (int)(radius * -Math.sin((2*Math.PI * segmentsCurrent / segmentsTotal) + (Math.PI / 2))),
                wSize / 40, wSize / 40);
        for (int i = 0; i < segmentsCurrent; i++) {
            double theta1 = (2*Math.PI * i / segmentsTotal) + (Math.PI / 2);
            double theta2 = (2*Math.PI * (i + 1) / segmentsTotal) + (Math.PI / 2);
            g2.drawLine(
                    wX/2 + (int)(radius * Math.cos(theta1)),
                    wY/2 + (int)(radius * -Math.sin(theta1)),
                    wX/2 + (int)(radius * Math.cos(theta2)),
                    wY/2 + (int)(radius * -Math.sin(theta2)));
        }

        // Prompt
        g2.setStroke(new BasicStroke(4));
        for (int i = 0; i < prompt.length(); i++) {
            g2.setColor(promptColors[i]);
            g2.fillRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.drawString(prompt.substring(i,i+1), pX + (int)(pSize * 1.2 * (i + 0.24)), pY + (int)(pSize * 0.75));
        }

        // Guess
        pSize = (int)((double) wSize * 0.6 / Math.max(10, guess.length()));
        pX = (int)((double) pSize * guess.length() / -2) + wX/2;
        pY = wSize/2 + (int)(pSize*1.5);
        pArc = pSize / 6;
        g2.setStroke(new BasicStroke(3));
        g2.setFont(font.deriveFont(pSize * 0.8f));
        for (int i = 0; i < guess.length(); i++) {
            g2.setColor(guessColors[i]);
            g2.fillRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.drawString(guess.substring(i,i+1), pX + (int)(pSize * 1.2 * (i + 0.24)), pY + (int)(pSize * 0.75));
        }

    }
}
