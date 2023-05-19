import java.util.Timer;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    JFrame window;
    GamePanel gamePanel;
    HomePanel homePanel;
    EndPanel endPanel;
    GameLoop gameLoop;
    Timer timer;
    int menu;
    public Main() {
        menu = 0;
        createWindow();
        timer = new Timer();
        gameLoop = new GameLoop(gamePanel);
        timer.scheduleAtFixedRate(gameLoop, 25L, 25L);
    }
    public void createWindow() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Word Guessing Game (original)");

        gamePanel = new GamePanel(window, this);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        homePanel = new HomePanel(window, this);
        homePanel.setFocusable(true);
        homePanel.addKeyListener(this);
        endPanel = new EndPanel(window, this);
        endPanel.setFocusable(true);
        endPanel.addKeyListener(this);
        homePanel.grabFocus();
        window.add(homePanel);
        window.setSize(800, 650);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    public void startGame() {

    }
    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (menu == 0) {
            if (code == 37 || code == 38 || code == 65 || code == 87) homePanel.up();
            else if (code == 39 || code == 40 || code == 68 || code == 83) homePanel.down();
            else if (code == 9 || code == 10 || code == 32) homePanel.select();
            else if (code == 27) homePanel.exit();
        }
        if (menu == 1) {
            if ('A' <= (char)code && (char)code <= 'Z') gameLoop.appendChar((char)code);
            else if ('a' <= (char)code && (char)code <= 'z') gameLoop.appendChar((char)code - ('a'-'A'));
            else if (code == 8 || code == 37 || code == 65483) gameLoop.backspace();
            else if (code == 9 || code == 10 || code == 32 || code == 40) gameLoop.guessWord();
            else if (code == 27) gameLoop.escape();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}