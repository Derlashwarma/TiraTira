package com.example.game.Sound;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> soundMap = new HashMap<>();

    public SoundManager() {
        // Load sounds on initialization
        loadSound("Space", "src\\main\\java\\com\\example\\game\\Sound\\space.wav");
        loadSound("Enemy_TP", "src\\main\\java\\com\\example\\game\\Sound\\tp.wav");
        loadSound("Player", "src\\main\\java\\com\\example\\game\\Sound\\pbullet.wav");
        loadSound("Rocket", "src\\main\\java\\com\\example\\game\\Sound\\rocket.wav");
        loadSound("Strafer", "src\\main\\java\\com\\example\\game\\Sound\\strafer.wav");
        // Add more sounds as needed
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void loadSound(String soundName, String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundMap.put(soundName, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + filePath);
            e.printStackTrace();
        }
    }

    public void loopSound(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.stop(); // Stop if it's already playing
            clip.setFramePosition(0); // Rewind to the beginning
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop indefinitely
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }


    public void playSound(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }

    public void stopSound(String soundName) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            clip.stop();
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }

    public void setVolume(String soundName, float volume) {
        Clip clip = soundMap.get(soundName);
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
        } else {
            System.err.println("Sound not found: " + soundName);
        }
    }

    public void stopAllSounds() {
        for (Clip clip : soundMap.values()) {
            clip.stop();
        }
    }
}

