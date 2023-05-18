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

        gamePanel = new GamePanel(window);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        homePanel = new HomePanel();
        homePanel.setFocusable(true);
        homePanel.addKeyListener(this);
        endPanel = new EndPanel();
        endPanel.setFocusable(true);
        endPanel.addKeyListener(this);
        homePanel.grabFocus();
        window.add(homePanel);
        window.setSize(800, 650);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
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
        if (menu == 1) {
            if ('A' <= (char)code && (char)code <= 'Z') gameLoop.appendChar((char)code);
            else if ('a' <= (char)code && (char)code <= 'z') gameLoop.appendChar((char)code - ('a'-'A'));
            else if (code == 8 || code == 37 || code == 65483) gameLoop.backspace();
            else if (code == 9 || code == 10 || code == 32 || code == 40) gameLoop.guessWord();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}