import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener, ComponentListener {
    JFrame window;
    GamePanel gamePanel;
    HomePanel homePanel;
    EndPanel endPanel;
    GameLoop gameLoop;
    Timer timer;
    int menu;
    Font font;
    public Main() {
        menu = 1;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("data/Silvera Peach.otf"));
        } catch (IOException | FontFormatException e) {
            font = Font.getFont(Font.SERIF);
        }
        createWindow();
        timer = new Timer();
        gameLoop = new GameLoop(gamePanel);
        timer.scheduleAtFixedRate(gameLoop, 25L, 25L);
    }
    private void createWindow() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Word Guessing Game (original)");
        window.addComponentListener(this);

        window.setSize(800, 650);
        gamePanel = new GamePanel(window, this, font);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        homePanel = new HomePanel(window, this, font);
        homePanel.setFocusable(true);
        homePanel.addKeyListener(this);
        endPanel = new EndPanel(window, this, font);
        endPanel.setFocusable(true);
        endPanel.addKeyListener(this);
        homePanel.grabFocus();
        window.add(gamePanel);

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

    @Override
    public void componentResized(ComponentEvent e) {
        // try {
        //     homePanel.resizeTiles();
        // } catch (NullPointerException theLetter_E_isAlreadyTaken) {
        //     System.out.println("homePanel not found");
        // }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}