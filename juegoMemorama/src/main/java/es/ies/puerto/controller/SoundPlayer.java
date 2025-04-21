package es.ies.puerto.controller;

import javafx.animation.PauseTransition;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class SoundPlayer {

    /**
     * Reproduce un sonido con volumen ajustable y retraso opcional.
     *
     * @param soundFile Nombre del archivo en la carpeta sounds)
     * @param volume Volumen entre 0.0 y 1.0
     * @param delaySeconds Retraso antes de sonar, en segundos (puede ser 0)
     */
    public static void play(String soundFile, double volume, double delaySeconds) {
        try {
            AudioClip clip = new AudioClip(SoundPlayer.class.getResource("/sounds/" + soundFile).toExternalForm());
            clip.setVolume(Math.max(0.0, Math.min(volume, 1.0)));

            if (delaySeconds > 0) {
                PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));
                delay.setOnFinished(e -> clip.play());
                delay.play();
            } else {
                clip.play();
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido: " + soundFile);
            e.printStackTrace();
        }
    }

}
