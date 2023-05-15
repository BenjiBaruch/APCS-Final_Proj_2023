import java.util.Timer;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
    JFrame window;
    GamePanel panel;
    GameLoop loop;
    Timer timer;
    public Main() {
        createWindow();
        timer = new Timer();
        loop = new GameLoop(panel);
        timer.scheduleAtFixedRate(loop, 25L, 25L);
    }
    public void createWindow() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Word Guessing Game (original)");

        panel = new GamePanel(window);
        panel.setFocusable(true);
        panel.grabFocus();
        panel.addKeyListener(this);
        window.add(panel);
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
        if ('A' <= (char)code && (char)code <= 'Z') loop.appendChar((char)code);
        else if ('a' <= (char)code && (char)code <= 'z') loop.appendChar((char)code - ('a'-'A'));
        else if (code == 8 || code == 37 || code == 65483) loop.backspace();
        else if (code == 9 || code == 10 || code == 32 || code == 40) loop.guessWord();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}