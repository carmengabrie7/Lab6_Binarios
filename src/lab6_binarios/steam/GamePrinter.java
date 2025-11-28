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

public class GamePrinter {

    private RandomAccessFile rGames;

    public GamePrinter(RandomAccessFile rGames) {
        this.rGames = rGames;
    }

    // ===== 3.6 printGames =====
    public void printGames() {
        try {
            rGames.seek(0);

            while (rGames.getFilePointer() < rGames.length()) {

                int code = rGames.readInt();
                String titulo = rGames.readUTF();
                char so = rGames.readChar();
                int edadMin = rGames.readInt();
                double precio = rGames.readDouble();
                int downloads = rGames.readInt();
                String imagen = rGames.readUTF();

                System.out.println("\n===== VIDEOJUEGO =====");
                System.out.println("Código: " + code);
                System.out.println("Título: " + titulo);
                System.out.println("SO: " + so);
                System.out.println("Edad mínima: " + edadMin);
                System.out.println("Precio: $" + precio);
                System.out.println("Descargas: " + downloads);
                System.out.println("Imagen: " + imagen);
            }

        } catch (Exception ex) {
            System.out.println("Error en printGames: " + ex.getMessage());
        }
    }
}

