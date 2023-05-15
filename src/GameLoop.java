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
            System.out.println("avg fps: " + (fpsSum / 200));
        } else {
            fps[head++] = (int)(1/(time - timePrev));
        }
    }

    public void newRound() {
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
        if (head == 64) return;
        guess[head] = (char)c;
        // System.out.println("h: " + head + ", s:" + String.valueOf(guess));
        panel.setGuess(String.valueOf(Arrays.copyOfRange(guess, 0, head++)));
    }

    public void backspace() {
        if (head == 0) return;
        guess[--head] = (char)0;
        // System.out.println("h: " + head + ", s:" + String.valueOf(guess));
        panel.setGuess(String.valueOf(Arrays.copyOfRange(guess, 0, head)));
    }

    public void guessWord() {
        if (head == 0) return;
        System.out.println("checking");
        if (dict.checkWord((String.valueOf(Arrays.copyOfRange(guess, 0, head))), prompt)) {
            newRound();
        }
    }
}
