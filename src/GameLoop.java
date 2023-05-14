import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class GameLoop extends TimerTask {
    GamePanel panel;
    char[] guess;
    int head, rounds;
    double timeStart, time, timeLimit;
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
        time = System.nanoTime() - timeStart;
        panel.update(time);
    }

    public void newRound() {
        char[] guess = new char[64];
        head = 0;
        prompt = dict.randomPrompt(++rounds);
        timeStart = System.nanoTime();
        time = 0L;
        timeLimit = ((long)Math.pow(5, -rounds/30D) + 1) * 5000000000L;
        System.out.println(timeLimit/1000000000L);
        panel.newRound(prompt, timeLimit);
    }

    public void appendChar(int c) {
        guess[head++] = (char)c;
        panel.setGuess(guess);
    }

    public void backspace() {
        guess[head--] = (char)0;
        panel.setGuess(guess);
    }

    public void guessWord() {
        if (dict.checkWord((String.valueOf(Arrays.copyOfRange(guess, 0, head))), prompt)) {
            newRound();
        }
    }
}
