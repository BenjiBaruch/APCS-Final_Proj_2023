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
    private JFrame window;
    private GamePanel gamePanel;
    private HomePanel homePanel;
    private EndPanel endPanel;
    private GameLoop gameLoop;
    private final EnglishDict dict;
    private int menu;
    private Font font;
    private SoundThingy sound;
    String[] prompts;
    HashMap<Character, Integer> charCounts;
    public Main() {
        menu = 0;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("data/Silvera Peach.otf"));
        } catch (IOException | FontFormatException e) {
            font = Font.getFont(Font.SERIF);
        }
        dict = new EnglishDict();
        sound = new SoundThingy();
        // sound.start();
        prompts = dict.getPromptList();
        createWindow();
        }

    private HashMap<Character, Integer> countChars(String[] prompts) {
        /*
         Counts the number of characters in each prompt, stores each char's maximums
         Used for generating tiles, so there will be enough of each tile for each prompt in a game
         */
        HashMap<Character, Integer> charCounts = new HashMap<>(27);
        for (char i = 'A'; i <= 'Z'; i++) charCounts.put(i, 0);
        charCounts.put('_', 0);
        for (String prompt : prompts) {
            HashMap<Character, Integer> pA = new HashMap<>(4);
            for (char i = 'A'; i <= 'Z'; i++) pA.put(i, 0);
            pA.put('_', 0);
            for (char letter : prompt.toCharArray()) {
                pA.replace(letter, pA.get(letter)+1);
            }
            for (char i : pA.keySet()) if(charCounts.get(i) < pA.get(i)) charCounts.replace(i, pA.get(i));
        }
        System.out.println(charCounts);
        return charCounts;
    }

    private void createWindow() {
        // Creates the window, believe it or not
        charCounts = countChars(prompts);

        // Initiates and configures JFrame
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Word Guessing Game (original)");
        window.setSize(800, 650);
        window.addComponentListener(this);

        // Initiates and configures panels
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

        // Configures windows
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    public void gameScreen(int from, Tile[] tiles) {
        // Initiates a game and changes active screen to gameScreen
        System.out.println("START GAME YOU FOOL");
        dict.resetUsedList();
        window.remove(from == 0 ? homePanel : endPanel);
        window.add(gamePanel);
        gamePanel.grabFocus();
        gamePanel.newGame(tiles);
        Timer timer = new Timer();
        gameLoop = new GameLoop(gamePanel, dict, this);
        timer.scheduleAtFixedRate(gameLoop, 25L, 25L);
        menu = 1;
        window.revalidate();
    }
    public void homeScreen(int from) {
        // Ends game and sets active screen to homeScreen
        // homeScreen contains exit button, instructions, and new game button
        if (from == 1) gameLoop.cancel();
        gameLoop = null;
        window.remove(from == 1 ? gamePanel : endPanel);
        window.repaint();
        dict.autoPromptList();
        prompts = dict.getPromptList();
        charCounts = countChars(prompts);
        homePanel.setCharCounts(charCounts);
        window.add(homePanel);
        homePanel.reset();
        homePanel.grabFocus();
        menu = 0;
        window.revalidate();
        homePanel.repaint();
    }

    public void endScreen(int from, int score) {
        // Ends game and sets active screen to endScreen
        // endScreen contains score and leaderboard
        gameLoop.cancel();
        gameLoop = null;
        window.remove(from == 1 ? gamePanel : homePanel);
        window.add(endPanel);
        menu = 2;
        endPanel.newScore(score-1);
        endPanel.grabFocus();
        window.revalidate();
        endPanel.repaint();
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handles keyboard inputs
        int code = e.getKeyCode();
        if (menu == 0) {
            // Home screen keyboard inputs
            if (code == 37 || code == 38 || code == 65 || code == 87) homePanel.up();
            else if (code == 39 || code == 40 || code == 68 || code == 83) homePanel.down();
            else if (code == 9 || code == 10 || code == 32) homePanel.select();
            else if (code == 27) homePanel.exit();
        }
        if (menu == 1) {
            // Game screen keyboard inputs
            if ('A' <= (char)code && (char)code <= 'Z') gameLoop.appendChar((char)code);
            else if ('a' <= (char)code && (char)code <= 'z') gameLoop.appendChar((char)code - ('a'-'A'));
            else if (code == 8 || code == 37 || code == 65483) gameLoop.backspace();
            else if (code == 9 || code == 10 || code == 32 || code == 40) gameLoop.guessWord();
            else if (code == 27) endScreen(1, gameLoop.getRounds());
        }
        if (menu == 2) {
            // End screen keyboard inputs
            if (code == 27 || code == 9 || code == 10) homeScreen(2);
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