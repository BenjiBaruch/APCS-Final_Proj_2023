import javax.swing.*;

public class HomePanel extends JPanel {
    // Tile Indices: 0 = char; 1 = X; 2 = Y; 3 = rot, 4 = r, 5 = g; 6 = b;
    int[][] tiles;
    // Selected: 0 = new game; 1 = instructions; 2 = leaderboard; 3 = quit
    int selected;
    JFrame window;
    Main game;
    public HomePanel(JFrame window, Main game) {
        this.game = game;
        this.window = window;
        selected = 0;
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
}
