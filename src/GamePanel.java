import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends JPanel {
    private String prompt, guess;
    private Color[] guessColors;
    private double time, timeLimit;
    private Tile[] tiles;
    private int wX, wY, wSize, rounds;
    private final Font font;
    private final JFrame window;
    private final Main game;
    private long tick, animStartTick;

    public GamePanel(JFrame window, Main game, Font font) {
        this.window = window;
        this.game = game;
        this.font = font;
        Dimension d = window.getSize();
        wX = d.width;
        wY = d.height;
        wSize = Math.min(wX, wY);
        tick = animStartTick = 0;
    }
    public void newGame(Tile[] tiles) {
        prompt = "**PLACEHOLDER";
        this.tiles = tiles;
        guess = "";
        timeLimit = 10;
        time = 0;
        rounds = -2;
    }

    public void newRound(String prompt, double timeLimit) {
        // Grabs new prompt, reset time and guess, changes prompt/guess colors, increments round.
        this.prompt = prompt;
        rounds++;
        // promptColors = new Color[prompt.length()];
        // for (int i = 0; i < prompt.length(); i++)
        //     promptColors[i] = getPastel();
        guessColors = new Color[64];
        for (int i = 0; i < 64; i++)
            guessColors[i] = getPastel();
        this.timeLimit = timeLimit;
        guess = "";
        time = 0;
        if (!prompt.equals("**PLACEHOLDER"))
            startTileAnim((int)((double) wSize * 0.8 / Math.max(6, prompt.length())));

        boolean killGod = true;
        for (Tile t : tiles) if (t.inAnim) killGod = false;
        if (killGod) System.out.println("deus est mortuus");
    }

    public void update() {
        repaint();
    }

    public void update(double time) {
        this.time = time;
        tick++;
        repaint();
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public static Color getPastel() {
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

    public static Color pastelize(Color c) {
        // Makes a color pastel by normalizing its RGB values between 0.875 and 1.0;
        int min = Math.min(c.getRed(), Math.min(c.getGreen(), c.getBlue()));
        int max = Math.max(c.getRed(), Math.max(c.getGreen(), c.getBlue()));
        int range = (max - min) * 8;
        return new Color(
                    (c.getRed() - min) / range + 224,
                    (c.getGreen() - min) / range + 224,
                    (c.getBlue() - min) / range + 224
        );
    }
    public void startTileAnim(int pSize) {
        animStartTick = tick;
        int pX = (int)((double) pSize * prompt.length() * -0.6D) + wX/2;
        int pY = wSize/2 - (int)(pSize*1.5) + wY - wSize;
        ArrayList<Tile> inNewPrompt = new ArrayList<>(prompt.length());
        // System.out.println("p len " + prompt.length());
        for (char letter : prompt.toCharArray()) {
            Tile[] hasChar = new Tile[4];
            int charCount = 0;
            for (Tile tile : tiles)
                if (tile.s.charAt(0) == letter && !inNewPrompt.contains(tile))
                    hasChar[charCount++] = tile;
            Tile addedTile = hasChar[(int)(Math.random()*charCount)];
            inNewPrompt.add(addedTile);
            // System.out.println(charCount);
        }
        // System.out.println("iNP size " + inNewPrompt.size());
        for (int i = 0; i < inNewPrompt.size(); i++) {
            Tile tile = inNewPrompt.get(i);
            // System.out.print(tile.s);
            tile.fromX = tile.toX;
            tile.fromY = tile.toY;
            tile.toX = pX + (int)(pSize * 1.2 * i);
            tile.toY = pY;
            tile.setBools(true, true);
            tile.breakpoint();
            if (!tile.inAnim) System.out.println("IUGSD JNvfs ");
            if (!Arrays.asList(tiles).contains(tile)) System.out.println("I will wage war against god");
        }

        for (Tile tile : tiles) {
            if (!inNewPrompt.contains(tile)) {
                if (tile.inPrompt) {
                    tile.fromX = tile.toX;
                    tile.fromY = tile.toY;
                    tile.toX = tile.x;
                    tile.toY = tile.y;
                    tile.setBools(true, false);
                }
                else if (tile.inAnim){
                    tile.setBools(false, false);
                }
            }
        }

        boolean killGod = true;
        for (Tile t : tiles) if (t.inAnim) killGod = false;
        if (killGod) System.out.println("DEUS EST MORTUUS");
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set variables
        Dimension d = window.getSize();
        wX = d.width;
        wY = d.height;
        wSize = Math.min(wX, wY);
        Graphics2D g2 = (Graphics2D) g;
        int pSize = (int)((double) wSize * 0.8 / Math.max(6, prompt.length()));
        pSize = Math.min(pSize, wSize / 8);
        int pX = (int)((double) pSize * prompt.length() * -0.6D) + wX/2;
        int pY = wSize/2 - (int)(pSize*1.5) + wY - wSize;
        int pArc = pSize / 6;
        int segmentsTotal = 150;
        int segmentsCurrent = (int)(segmentsTotal * (1 - (time / timeLimit)));
        int radius = (int)(wSize * 0.4);

        // Configure g2
        g2.setFont(font.deriveFont(pSize * 0.8F));
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
        FontMetrics metrics = g2.getFontMetrics(g2.getFont()); // https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
        HomePanel.drawTiles(g2, g2.getFont(), tiles, pSize, (int)(tick - animStartTick));
        /*
        for (int i = 0; i < prompt.length(); i++) {
            String letter = prompt.substring(i,i+1);
            g2.setColor(promptColors[i]);
            g2.fillRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.drawString(letter,
                    pX + (int)(pSize * 1.2 * (i + 0.4)) - (metrics.stringWidth(letter) / 2),
                    pY + (int)(pSize * 0.75));
        }
         */

        // Round counter
        g2.drawString(Integer.toString(rounds), (wX-wSize)/2 + (pSize / 5), (wY-wSize)/2 + (pSize * 4 / 5));

        // Guess
        pSize = (int)((double) wSize * 0.6 / Math.max(10, guess.length()));
        pX = (int)((double) pSize * guess.length() * -0.6D) + wX/2;
        pY = wSize/2 + (int)(pSize*1.5) + wY - wSize;
        pArc = pSize / 6;
        g2.setStroke(new BasicStroke(3));
        g2.setFont(font.deriveFont(pSize * 0.8f));
        metrics = g2.getFontMetrics(g2.getFont());
        for (int i = 0; i < guess.length(); i++) {
            String letter = guess.substring(i,i+1);
            g2.setColor(guessColors[i]);
            g2.fillRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(pX + (int)(pSize * 1.2 * i), pY, pSize, pSize, pArc, pArc);
            g2.drawString(letter,
                    pX + (int)(pSize * 1.2 * (i + 0.4)) - (metrics.stringWidth(letter) / 2),
                    pY + (int)(pSize * 0.75));
        }
    }
}
