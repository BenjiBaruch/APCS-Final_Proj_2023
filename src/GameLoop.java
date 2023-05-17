import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameLoop extends TimerTask {
    GamePanel panel;
    char[] guess;
    int head, rounds;
    double timeStart, time, timeLimit, timePrev;
    int[] fps = new int[200];
    int frameNum = 0;
    Dictionary dict;
    String prompt;

    public GameLoop(GamePanel panel) {
        this.panel = panel;
        panel.newRound("**PLACEHOLDER", 10000000000L);
        rounds = 0;
        dict = new Dictionary();
        newRound();
    }

    @Override
    public void run() {
        if (time > timeLimit) cancel();
        timePrev = time;
        time = System.nanoTime() - timeStart;
        panel.update(time);
        if (frameNum == 200) {
            frameNum = 0;
            double fpsSum = 0;
            for (double item : fps) fpsSum += item;
        } else {
            fps[frameNum++] = (int)(1000000000/(time - timePrev));
        }
    }

    public void newRound() {
        // Resets guess, time limit, and calls panel newRound method.
        guess = new char[64];
        head = 0;
        prompt = dict.randomPrompt(++rounds);
        timeStart = System.nanoTime();
        time = 0L;
        timeLimit = (Math.pow(5, -rounds/30D) + 1) * 5000000000L;
        System.out.println(timeLimit/1000000000L);
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
        if (dict.checkWord((String.valueOf(Arrays.copyOfRange(guess, 0, head))), prompt)) {
            newRound();
        } else {
            guess = new char[64];
            head = 0;
            panel.setGuess("");
        }
    }
}
