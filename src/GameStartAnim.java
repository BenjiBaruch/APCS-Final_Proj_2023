import java.util.TimerTask;

public class GameStartAnim extends TimerTask {
    HomePanel panel;
    public GameStartAnim(HomePanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        panel.animTick();
    }
}
