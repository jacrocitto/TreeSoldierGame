import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public abstract class Sound {
    public static void play(String filePath){
        File soundFile = new File(filePath);
        try {
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(soundFile));
            audioClip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

