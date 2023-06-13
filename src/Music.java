import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Music extends JFrame {
    private static Clip clip;
    private static FloatControl volumeControl;
    private static boolean isPlaying = false;

    public Music() {
    }

    public static void playMusic() {
        if (!isPlaying) {
            try {
                File musicFile = new File("./assets/music.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);

                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                clip.loop(Clip.LOOP_CONTINUOUSLY);

                clip.start();
                isPlaying = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
        }
    }

    public static void decreaseVolume() {
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            float decreaseAmount = -10f;

            float newVolume = currentVolume + decreaseAmount;
            volumeControl.setValue(newVolume);
        }
    }

    public static void increaseVolume() {
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            float increaseAmount = 10f;

            float newVolume = currentVolume + increaseAmount;
            volumeControl.setValue(newVolume);
        }
    }
}