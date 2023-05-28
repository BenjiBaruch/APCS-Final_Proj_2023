import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameLoop extends TimerTask {
    private final GamePanel panel;
    private char[] guess;
    private int head, rounds;
    private double timeStart, time, timeLimit;
    // private double timePrev;
    // private int[] fps = new int[200];
    // private int frameNum = 0;
    private final EnglishDict dict;
    private String prompt;
    private final Main game;

    public GameLoop(GamePanel panel, EnglishDict dict, Main game) {
        this.panel = panel;
        panel.newRound("**PLACEHOLDER", 10000000000L);
        rounds = 0;
        this.dict = dict;
        this.game = game;
        newRound();
    }

    @Override
    public void run() {
        if (time > timeLimit) game.endScreen(1, rounds);
        // timePrev = time;
        time = System.nanoTime() - timeStart;
        panel.update(time);
        // if (frameNum == 200) {
        //     frameNum = 0;
        //     double fpsSum = 0;
        //     for (double item : fps) fpsSum += item;
        // } else {
        //     fps[frameNum++] = (int)(1000000000/(time - timePrev));
        // }
    }

    private void newRound() {
        // Resets guess, time limit, and calls panel newRound method.
        guess = new char[64];
        head = 0;
        prompt = dict.randomPrompt(++rounds);
        timeStart = System.nanoTime();
        time = 0L;
        timeLimit = (Math.pow(5, -rounds/30D) + 1) * 5000000000L;
        // System.out.println(timeLimit/1000000000L);
        panel.newRound(prompt, timeLimit);
    }

    public void appendChar(int c) {
        // Appends char when a Latin letter is typed
        if (head == 64) return;
        guess[head++] = (char)c;
        panel.setGuess(String.valueOf(Arrays.copyOfRange(guess, 0, head)));
    }

    public void backspace() {
        // Removes a char when backspace, left arrow, or CTRL+Z are used.
        if (head == 0) return;
        guess[--head] = (char)0;
        panel.setGuess(String.valueOf(Arrays.copyOfRange(guess, 0, head)));
    }

    public void guessWord() {
        // Checks if an entered word is valid and resets guess when enter key or space bar is used
        if (head == 0) return;
        if (dict.checkWord((String.valueOf(Arrays.copyOfRange(guess, 0, head))), prompt, true)) {
            newRound();
        } else {
            guess = new char[64];
            head = 0;
            panel.setGuess("");
        }
    }

    public int getRounds() {
        return rounds;
    }
}
