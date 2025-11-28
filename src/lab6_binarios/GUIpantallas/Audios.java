/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.GUIpantallas;

import javax.sound.sampled.*;
import java.io.File;
/**
 *
 * @author andre
 */
public class Audios {
       public static void playSound(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            AudioInputStream audio = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loginSound() {
        playSound("resources/audio/login.wav");
    }

    public static void registroSound() {
        playSound("resources/audio/registro.wav");
    }

    public static void descargaSound() {
        playSound("resources/audio/descarga.wav");
    }

    public static void reporteSound() {
        playSound("resources/audio/reporte.wav");
    }
}
