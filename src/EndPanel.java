import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    JFrame window;
    Main game;
    Font font;
    public EndPanel(JFrame window, Main game, Font font) {
        this.window = window;
        this.game = game;
        this.font = font;
    }
}
