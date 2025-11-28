package lab6_binarios.steam;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Calendar;

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
    
    //Funciones PRINCIPALES de la clase Steam
    public void addGame(String titulo, char sistemaOperativo, int edadMinima, double precio, int contadorDownloads, String rutaImagen){
        try{
            int code = getNextGameCode();
            rGames.seek(rGames.length());
            
            rGames.writeInt(code); // int code
            rGames.writeUTF(titulo); // String titulo
            rGames.writeChar(sistemaOperativo); // char sistema operativo
            rGames.writeInt(edadMinima); // int edad minima
            rGames.writeDouble(precio); // double precio
            rGames.writeInt(0); //int contadorDownloads
            rGames.writeUTF(rutaImagen); // String ruta imagen
            
            System.out.println("Juego agregado con codigo: "+code);
        } catch(IOException e){
            System.out.println("Error al agregar juego: " + e.getMessage());
        }
    }
    
    public void addPlayer(String username, String password, String nombre, Calendar nacimiento, int contadorDownloads, String rutaImagen, String tipoUsuario){
        try{
            int code = getNextPlayerCode();
            rPlayers.seek(rPlayers.length());
            long fechaMs = nacimiento.getTimeInMillis();
            
            rPlayers.writeInt(code);
            rPlayers.writeUTF(username);
            rPlayers.writeUTF(password);
            rPlayers.writeUTF(nombre);
            rPlayers.writeLong(fechaMs);
            rPlayers.writeInt(0);
            rPlayers.writeUTF(rutaImagen);
            rPlayers.writeUTF(tipoUsuario);
            
            System.out.println("Jugador agregado con codigo: "+code);
        }catch (IOException ex){
            System.out.println("Error al agregar player: "+ex.getMessage());
            
        }
    }
    
    public boolean downloadGame (int codeGame, int codePlayer, char soSolicitado){
        try{
            rGames.seek(0);
            long posJuego = -1;
            
            while (rGames.getFilePointer()< rGames.length()){
                long posActual = rGames.getFilePointer();
                
                int c = rGames.readInt();
                String titulo = rGames.readUTF();
                char soJuego = rGames.readChar();
                int edadMin = rGames.readInt();
                double precio = rGames.readDouble();
                int descJuego = rGames.readInt();
                String rutaImagenJuego = rGames.readUTF();
                
                if (c==codeGame){
                    posJuego = posActual;
                    break;
                }
            }
            if (posJuego== -1){
                return false;
            }
            
            rGames.seek(posJuego);
            int gCode = rGames.readInt();
            String titulo = rGames.readUTF();
            char soJuego = rGames.readChar();
            int edadMinima = rGames.readInt();
            double precio = rGames.readDouble();
            int contadorDescargasJuego = rGames.readInt();
            String rutaImagenJuego = rGames.readUTF();
            
            if (soJuego != soSolicitado){
                return false;
            }
            
            rPlayers.seek(0);
            long posPlayer = -1;
            
            while (rPlayers.getFilePointer()< rPlayers.length()){
               long posActual = rPlayers.getFilePointer();

            int c = rPlayers.readInt();
            String username = rPlayers.readUTF();
            String password = rPlayers.readUTF();
            String nombre = rPlayers.readUTF();
            long nacimientoMs = rPlayers.readLong();
            int descPlayer = rPlayers.readInt();
            String rutaImagenPlayer = rPlayers.readUTF();
            String tipoUsuario = rPlayers.readUTF();

            if (c == codePlayer) {
                posPlayer = posActual;
                break;
            } 
            }
            if (posPlayer == -1){
                return false;
            }
            
            rPlayers.seek(posPlayer);

        int pCode = rPlayers.readInt();
        String username = rPlayers.readUTF();
        String password = rPlayers.readUTF();
        String nombre = rPlayers.readUTF();
        long nacimiento = rPlayers.readLong();
        int contadorDescargasPlayer = rPlayers.readInt();
        String rutaImagenPlayer = rPlayers.readUTF();
        String tipoUsuario = rPlayers.readUTF();
            
        Calendar calNac = Calendar.getInstance();
        calNac.setTimeInMillis(nacimiento);

        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - calNac.get(Calendar.YEAR);

        if (hoy.get(Calendar.MONTH) < calNac.get(Calendar.MONTH) ||
            (hoy.get(Calendar.MONTH) == calNac.get(Calendar.MONTH) &&
             hoy.get(Calendar.DAY_OF_MONTH) < calNac.get(Calendar.DAY_OF_MONTH))) {
            edad--;
        }

        if (edad < edadMinima) {
            return false;
        }
        
        int codeDownload = getNextDownloadCode();
        
        File dlFile = new File ("steam/downloads/download_"+codeDownload+".stm");
        RandomAccessFile rDL = new RandomAccessFile (dlFile,"rw");
        
        rDL.writeUTF("Fecha: "+hoy.getTime());
        rDL.writeUTF("Imagen: "+rutaImagenJuego);
        rDL.writeUTF ("Download #"+codeDownload);
        rDL.writeUTF(nombre+" ha bajado "+titulo);
        rDL.writeUTF("Precio: "+precio);
        
        rDL.close();
        
        rGames.seek( 
           posJuego +
            4 +
            (2 + titulo.length()*2) +
            2 +
            4 +
            8
        );
        
        rGames.writeInt(contadorDescargasJuego +1);
        
        rPlayers.seek (
        posPlayer +
        4 + (2 + username.length()*2)+
        (2 + password.length()*2) +
        (2 + nombre.length()*2)+
                8
        );
        
        rPlayers.writeInt(contadorDescargasPlayer + 1);
        return true;
        }catch (IOException ex){
            System.out.println("Error en downloadGame: "+ex.getMessage());
            return false;
        }
    }public boolean updatePriceFor(int codGame, double newPrice) {
    try {
        rGames.seek(0);

        while (rGames.getFilePointer() < rGames.length()) {

            long inicioRegistro = rGames.getFilePointer();

            int code = rGames.readInt();
            String titulo = rGames.readUTF();
            char so = rGames.readChar();
            int edadMinima = rGames.readInt();

            long posPrecio = rGames.getFilePointer(); // posición exacta del precio
            double precioActual = rGames.readDouble();

            int contadorDownloads = rGames.readInt();
            String rutaImagen = rGames.readUTF();

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
    public void reportForClient(int codPlayer, String fileOut) {

    try {
        rPlayers.seek(0);

        while (rPlayers.getFilePointer() < rPlayers.length()) {

            int code = rPlayers.readInt();
            String username = rPlayers.readUTF();
            String password = rPlayers.readUTF();
            String nombre = rPlayers.readUTF();
            long nacimiento = rPlayers.readLong();
            int contadorDownloads = rPlayers.readInt();
            String rutaImagen = rPlayers.readUTF();
            String tipoUsuario = rPlayers.readUTF();

            if (code == codPlayer) {
                PrintWriter pw = new PrintWriter(new File(fileOut));

                pw.println("========= REPORTE DEL CLIENTE =========");
                pw.println("Código: " + code);
                pw.println("Username: " + username);
                pw.println("Nombre: " + nombre);
                pw.println("Password: " + password);
                pw.println("Fecha nacimiento (ms): " + nacimiento);
                pw.println("Descargas realizadas: " + contadorDownloads);
                pw.println("Imagen: " + rutaImagen);
                pw.println("Tipo de usuario: " + tipoUsuario);

                pw.close();
                System.out.println("REPORTE CREADO");
                return;
            }
        }

        System.out.println("NO SE PUEDE CREAR REPORTE");

    } catch (Exception ex) {
        System.out.println("NO SE PUEDE CREAR REPORTE");
    }
}
public void printGames() {
    try {
        rGames.seek(0);

        while (rGames.getFilePointer() < rGames.length()) {

            int code = rGames.readInt();
            String titulo = rGames.readUTF();
            char so = rGames.readChar();
            int edadMinima = rGames.readInt();
            double precio = rGames.readDouble();
            int contadorDownloads = rGames.readInt();
            String rutaImagen = rGames.readUTF();

            System.out.println("\n===== VIDEOJUEGO =====");
            System.out.println("Código: " + code);
            System.out.println("Título: " + titulo);
            System.out.println("SO: " + so);
            System.out.println("Edad mínima: " + edadMinima);
            System.out.println("Precio: $" + precio);
            System.out.println("Descargas: " + contadorDownloads);
            System.out.println("Imagen: " + rutaImagen);
        }

    } catch (Exception ex) {
        System.out.println("Error en printGames: " + ex.getMessage());
    }
}

}
