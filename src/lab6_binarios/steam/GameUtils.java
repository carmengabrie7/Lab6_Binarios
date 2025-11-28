/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.steam;

/**
 *
 * @author ALISSONRAQUELMARTINE
 */


import java.io.RandomAccessFile;

public class GameUtils {

    private RandomAccessFile rGames;

    public GameUtils(RandomAccessFile rGames) {
        this.rGames = rGames;
    }

    // ===== 3.4 updatePriceFor =====
    public boolean updatePriceFor(int codGame, double newPrice) {
        try {
            rGames.seek(0);

            while (rGames.getFilePointer() < rGames.length()) {

                long inicio = rGames.getFilePointer();

                int code = rGames.readInt();
                String titulo = rGames.readUTF();
                char so = rGames.readChar();
                int edadMin = rGames.readInt();

                long posPrecio = rGames.getFilePointer(); 
                rGames.readDouble(); // precio actual

                int contador = rGames.readInt();
                String imagen = rGames.readUTF();

                if (code == codGame) {
                    rGames.seek(posPrecio);
                    rGames.writeDouble(newPrice);
                    return true;
                }
            }

        } catch (Exception ex) {
            System.out.println("Error en updatePriceFor: " + ex.getMessage());
        }

        return false;
    }
}

