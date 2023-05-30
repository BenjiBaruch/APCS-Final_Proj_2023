import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundThingy extends Thread {
    AudioInputStream audioStream;

    @Override
    public void run() {
        String path = "data/song.wav";
        try (Clip clip = AudioSystem.getClip()){
            clip.open(AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile()));
            clip.start();
            clip.setFramePosition(0);
            System.out.println(clip.isRunning() ? "Clip is running" : "God is dead");
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
