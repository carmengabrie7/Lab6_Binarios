/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.steam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Steam {
    
    private RandomAccessFile rCodes;
    private RandomAccessFile rGames;
    private RandomAccessFile rPlayers;

    private File carpetaSteam;
    private File carpetaDownloads;
    
    public Steam() {
        try {

            // Crear carpeta steam/
            carpetaSteam = new File("steam");
            if (!carpetaSteam.exists()) {
                carpetaSteam.mkdir();
            }

            // Crear carpeta steam/downloads/
            carpetaDownloads = new File("steam/downloads");
            if (!carpetaDownloads.exists()) {
                carpetaDownloads.mkdir();
            }

            // Inicializar archivos binarios
            rCodes   = new RandomAccessFile("steam/codes.stm", "rw");
            rGames   = new RandomAccessFile("steam/games.stm", "rw");
            rPlayers = new RandomAccessFile("steam/player.stm", "rw");

            // Si codes.stm está vacío, inicializar con 1,1,1
            if (rCodes.length() == 0) {
                rCodes.writeInt(1); // código para juegos
                rCodes.writeInt(1); // código para players
                rCodes.writeInt(1); // código para downloads
            }

        } catch (IOException ex) {
            System.out.println("Error al inicializar Steam: " + ex.getMessage());
        }
    }
    
    private int obtenerCodigo(int posicion) {
        try {
            // posicion es 0 = games, 4 = players, 8 = downloads
            rCodes.seek(posicion);

            int actual = rCodes.readInt();
            int siguiente = actual + 1;

            // volver y actualizar el archivo
            rCodes.seek(posicion);
            rCodes.writeInt(siguiente);

            return actual;

        } catch (IOException ex) {
            System.out.println("Error obteniendo código: " + ex.getMessage());
            return -1;
        }
    }
    
    public int getNextGameCode() {
        return obtenerCodigo(0); 
    }

    public int getNextPlayerCode() {
        return obtenerCodigo(4); 
    }

    public int getNextDownloadCode() {
        return obtenerCodigo(8);  
    }
}
