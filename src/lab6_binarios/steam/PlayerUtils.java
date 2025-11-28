/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.steam;

/**
 *
 * @author ALISSONRAQUELMARTINE
 */
import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class PlayerUtils {

    private RandomAccessFile rPlayers;

    public PlayerUtils(RandomAccessFile rPlayers) {
        this.rPlayers = rPlayers;
    }

    // ===== 3.5 reportForClient =====
    public void reportForClient(int codPlayer, String fileOut) {

        try {
            rPlayers.seek(0);

            while (rPlayers.getFilePointer() < rPlayers.length()) {

                int code = rPlayers.readInt();
                String username = rPlayers.readUTF();
                String password = rPlayers.readUTF();
                String nombre = rPlayers.readUTF();
                long nacimiento = rPlayers.readLong();
                int contador = rPlayers.readInt();
                String imagen = rPlayers.readUTF();
                String tipoUsuario = rPlayers.readUTF();

                if (code == codPlayer) {

                    PrintWriter pw = new PrintWriter(new File(fileOut));

                    pw.println("========= REPORTE DEL CLIENTE =========");
                    pw.println("Código: " + code);
                    pw.println("Username: " + username);
                    pw.println("Nombre: " + nombre);
                    pw.println("Password: " + password);
                    pw.println("Nacimiento (ms): " + nacimiento);
                    pw.println("Descargas: " + contador);
                    pw.println("Imagen: " + imagen);
                    pw.println("Tipo: " + tipoUsuario);

                    pw.close();

                    System.out.println("REPORTE CREADO");
                    return;
                }
            }

            System.out.println("NO SE PUEDE CREAR REPORTE");

        } catch (Exception e) {
            System.out.println("NO SE PUEDE CREAR REPORTE");
        }
    }
}

