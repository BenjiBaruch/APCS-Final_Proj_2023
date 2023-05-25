import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    EnglishDict dict;
    Timer timer;
    int menu;
    Font font;
    public Main() {
        menu = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("data/Silvera Peach.otf"));
        } catch (IOException | FontFormatException e) {
            font = Font.getFont(Font.SERIF);
        }
        dict = new EnglishDict();
        createWindow(dict.getPromptList());
        }

    private HashMap<Character, Integer> countChars(String[] prompts) {
        HashMap<Character, Integer> charCounts = new HashMap<>(27);
        for (char i = 'A'; i <= 'Z'; i++) charCounts.put(i, 0);
        charCounts.put('_', 0);
        for (String prompt : prompts) {
            HashMap<Character, Integer> pA = new HashMap<>(4);
            for (char letter : prompt.toCharArray()) {
                if (pA.containsKey(letter)) pA.replace(letter, pA.get(letter)+1);
                else pA.put(letter, 1);
            }
            for (char i : pA.keySet()) if(charCounts.get(i) < pA.get(i)) charCounts.replace(i, pA.get(i));
        }
        System.out.println(charCounts);
        return charCounts;
    }

    private void createWindow(String[] prompts) {
        HashMap<Character, Integer> charCounts = countChars(prompts);

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Word Guessing Game (original)");
        window.addComponentListener(this);

        window.setSize(800, 650);
        gamePanel = new GamePanel(window, this, font);
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        homePanel = new HomePanel(window, this, font, charCounts);
        homePanel.setFocusable(true);
        homePanel.addKeyListener(this);
        endPanel = new EndPanel(window, this, font);
        endPanel.setFocusable(true);
        endPanel.addKeyListener(this);
        homePanel.grabFocus();
        if (menu == 1) window.add(gamePanel);
        else window.add(homePanel);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    public void startGame(int from, Tile[] tiles) {
        System.out.println("START GAME YOU FOOL");
        window.remove(from == 0 ? homePanel : endPanel);
        window.add(gamePanel);
        gamePanel.grabFocus();
        gamePanel.newGame(tiles);
        timer = new Timer();
        gameLoop = new GameLoop(gamePanel, dict, this);
        timer.scheduleAtFixedRate(gameLoop, 25L, 25L);
        menu = 1;
        window.revalidate();
    }
    public void homeScreen(int from) {
        gameLoop.cancel();
        gameLoop = null;
        window.remove(from == 1 ? gamePanel : endPanel);
        window.repaint();
        dict.autoPromptList();
        window.add(homePanel);
        homePanel.reset();
        homePanel.grabFocus();
        menu = 0;
        window.revalidate();
        homePanel.repaint();
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