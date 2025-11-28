package lab6_binarios.steam;

import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

public class Steam {

    private RandomAccessFile rCodes;
    public RandomAccessFile rGames;
    public RandomAccessFile rPlayers;

    private File carpetaSteam;
    private File carpetaDownloads;

    public Steam() {
        try {
            carpetaSteam = new File("steam");
            if (!carpetaSteam.exists()) carpetaSteam.mkdir();

            carpetaDownloads = new File("steam/downloads");
            if (!carpetaDownloads.exists()) carpetaDownloads.mkdir();

            rCodes = new RandomAccessFile("steam/codes.stm", "rw");
            rGames = new RandomAccessFile("steam/games.stm", "rw");
            rPlayers = new RandomAccessFile("steam/player.stm", "rw");

            if (rCodes.length() == 0) {
                rCodes.writeInt(1);
                rCodes.writeInt(1);
                rCodes.writeInt(1);
            }

        } catch (IOException ex) {
            System.out.println("Error inicializando Steam: " + ex.getMessage());
        }
    }

    private int obtenerCodigo(int pos) {
        try {
            rCodes.seek(pos);
            int actual = rCodes.readInt();
            rCodes.seek(pos);
            rCodes.writeInt(actual + 1);
            return actual;
        } catch (IOException e) {
            return -1;
        }
    }

    public int getNextGameCode()    { return obtenerCodigo(0); }
    public int getNextPlayerCode()  { return obtenerCodigo(4); }
    public int getNextDownloadCode(){ return obtenerCodigo(8); }

    public void addGame(String titulo, char sistemaOperativo, int edadMinima,
                        double precio, String rutaImagen) {
        try {
            int code = getNextGameCode();
            rGames.seek(rGames.length());

            rGames.writeInt(code);
            rGames.writeUTF(titulo);
            rGames.writeChar(sistemaOperativo);
            rGames.writeInt(edadMinima);
            rGames.writeDouble(precio);
            rGames.writeInt(0);
            rGames.writeUTF(rutaImagen);

        } catch (IOException e) {
            System.out.println("Error en addGame: " + e.getMessage());
        }
    }

    public void addPlayer(String username, String password, String nombre,
                          Calendar nacimiento, String rutaImagen, String tipoUsuario) {
        try {
            int code = getNextPlayerCode();
            long fechaMs = nacimiento.getTimeInMillis();

            rPlayers.seek(rPlayers.length());
            rPlayers.writeInt(code);
            rPlayers.writeUTF(username);
            rPlayers.writeUTF(password);
            rPlayers.writeUTF(nombre);
            rPlayers.writeLong(fechaMs);
            rPlayers.writeInt(0);
            rPlayers.writeUTF(rutaImagen);
            rPlayers.writeUTF(tipoUsuario);

        } catch (IOException e) {
            System.out.println("Error en addPlayer: " + e.getMessage());
        }
    }

    public boolean downloadGame(int codeGame, int codePlayer, char soSolicitado) {
        try {
            rGames.seek(0);
            long posJuego = -1;

            int gCode = 0; String titulo = ""; char soJuego = 0;
            int edadMinima = 0; double precio = 0; int descJuego = 0;
            String rutaImgJuego = "";

            while (rGames.getFilePointer() < rGames.length()) {
                long inicio = rGames.getFilePointer();
                gCode = rGames.readInt();
                titulo = rGames.readUTF();
                soJuego = rGames.readChar();
                edadMinima = rGames.readInt();
                precio = rGames.readDouble();
                descJuego = rGames.readInt();
                rutaImgJuego = rGames.readUTF();
                if (gCode == codeGame) {
                    posJuego = inicio;
                    break;
                }
            }

            if (posJuego == -1) return false;
            if (soJuego != soSolicitado) return false;

            rPlayers.seek(0);
            long posPlayer = -1;

            int pCode = 0; String username = "", password = "", nombre = "";
            long nacMs = 0; int descPlayer = 0;
            String rutaImgPlayer = "", tipo = "";

            while (rPlayers.getFilePointer() < rPlayers.length()) {
                long inicio = rPlayers.getFilePointer();
                pCode = rPlayers.readInt();
                username = rPlayers.readUTF();
                password = rPlayers.readUTF();
                nombre = rPlayers.readUTF();
                nacMs = rPlayers.readLong();
                descPlayer = rPlayers.readInt();
                rutaImgPlayer = rPlayers.readUTF();
                tipo = rPlayers.readUTF();
                if (pCode == codePlayer) {
                    posPlayer = inicio;
                    break;
                }
            }

            if (posPlayer == -1) return false;

            Calendar nac = Calendar.getInstance();
            nac.setTimeInMillis(nacMs);
            Calendar hoy = Calendar.getInstance();
            int edad = hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
            if (hoy.get(Calendar.MONTH) < nac.get(Calendar.MONTH) ||
                    (hoy.get(Calendar.MONTH) == nac.get(Calendar.MONTH) &&
                     hoy.get(Calendar.DAY_OF_MONTH) < nac.get(Calendar.DAY_OF_MONTH)))
                edad--;
            if (edad < edadMinima) return false;

            int codeDownload = getNextDownloadCode();
            File out = new File("steam/downloads/download_" + codeDownload + ".stm");
            PrintWriter pw = new PrintWriter(out);
            pw.println("Fecha: " + hoy.getTime());
            pw.println("Imagen: " + rutaImgJuego);
            pw.println("Download #" + codeDownload);
            pw.println(nombre + " ha bajado " + titulo);
            pw.println("Precio: " + precio);
            pw.close();

            rGames.seek(posJuego);
            rGames.readInt(); rGames.readUTF(); rGames.readChar();
            rGames.readInt(); rGames.readDouble();
            long posContJ = rGames.getFilePointer();
            rGames.writeInt(descJuego + 1);

            rPlayers.seek(posPlayer);
            rPlayers.readInt(); rPlayers.readUTF(); rPlayers.readUTF();
            rPlayers.readUTF(); rPlayers.readLong();
            long posContP = rPlayers.getFilePointer();
            rPlayers.writeInt(descPlayer + 1);

            return true;

        } catch (Exception ex) {
            System.out.println("Error en downloadGame: " + ex.getMessage());
            return false;
        }
    }

    public boolean updatePriceFor(int codGame, double newPrice) {
        try {
            rGames.seek(0);
            while (rGames.getFilePointer() < rGames.length()) {
                long inicio = rGames.getFilePointer();
                int code = rGames.readInt();
                rGames.readUTF();
                rGames.readChar();
                rGames.readInt();
                long posPrecio = rGames.getFilePointer();
                double actual = rGames.readDouble();
                rGames.readInt();
                rGames.readUTF();
                if (code == codGame) {
                    rGames.seek(posPrecio);
                    rGames.writeDouble(newPrice);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error updatePriceFor: " + e.getMessage());
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
                long nac = rPlayers.readLong();
                int desc = rPlayers.readInt();
                String ruta = rPlayers.readUTF();
                String tipo = rPlayers.readUTF();

                if (code == codPlayer) {
                    PrintWriter pw = new PrintWriter(new File(fileOut));
                    pw.println("===== REPORTE CLIENTE =====");
                    pw.println("Código: " + code);
                    pw.println("Username: " + username);
                    pw.println("Nombre: " + nombre);
                    pw.println("Password: " + password);
                    pw.println("Nacimiento(ms): " + nac);
                    pw.println("Descargas: " + desc);
                    pw.println("Imagen: " + ruta);
                    pw.println("Tipo: " + tipo);
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

    public void printGames() {
        try {
            rGames.seek(0);
            while (rGames.getFilePointer() < rGames.length()) {
                int code = rGames.readInt();
                String titulo = rGames.readUTF();
                char so = rGames.readChar();
                int edad = rGames.readInt();
                double precio = rGames.readDouble();
                int desc = rGames.readInt();
                String ruta = rGames.readUTF();

                System.out.println("\n=== GAME ===");
                System.out.println("Code: " + code);
                System.out.println("Título: " + titulo);
                System.out.println("SO: " + so);
                System.out.println("Edad: " + edad);
                System.out.println("Precio: " + precio);
                System.out.println("Descargas: " + desc);
                System.out.println("Imagen: " + ruta);
            }
        } catch (Exception e) {
            System.out.println("Error printGames: " + e.getMessage());
        }
    }

    // ================== MÉTODOS PARA GUI ==================

    public int validateUser(String username, String password) {
        try {
            rPlayers.seek(0);
            while (rPlayers.getFilePointer() < rPlayers.length()) {
                int code = rPlayers.readInt();
                String user = rPlayers.readUTF();
                String pass = rPlayers.readUTF();
                rPlayers.readUTF(); // nombre
                rPlayers.readLong();
                rPlayers.readInt();
                rPlayers.readUTF();
                rPlayers.readUTF(); // tipo
                if (user.equals(username) && pass.equals(password)) return code;
            }
        } catch (Exception e) {}
        return -1;
    }

    public String getUserType(int codPlayer) {
        try {
            rPlayers.seek(0);
            while (rPlayers.getFilePointer() < rPlayers.length()) {
                int code = rPlayers.readInt();
                rPlayers.readUTF(); // user
                rPlayers.readUTF(); // pass
                rPlayers.readUTF(); // nombre
                rPlayers.readLong();
                rPlayers.readInt();
                rPlayers.readUTF(); // img
                String tipo = rPlayers.readUTF();
                if (code == codPlayer) return tipo;
            }
        } catch (Exception e) {}
        return "normal";
    }

    public void listPlayers(JTextArea area) {
        try {
            rPlayers.seek(0);
            while (rPlayers.getFilePointer() < rPlayers.length()) {
                int code = rPlayers.readInt();
                String user = rPlayers.readUTF();
                rPlayers.readUTF(); // pass
                String nombre = rPlayers.readUTF();
                rPlayers.readLong();
                int desc = rPlayers.readInt();
                String img = rPlayers.readUTF();
                String tipo = rPlayers.readUTF();
                area.append("Code: " + code + " User: " + user + " Nombre: " + nombre + " Tipo: " + tipo + " Descargas: " + desc + "\n");
            }
        } catch (Exception e) {}
    }

    public void listGames(JTextArea area) {
        try {
            rGames.seek(0);
            while (rGames.getFilePointer() < rGames.length()) {
                int code = rGames.readInt();
                String titulo = rGames.readUTF();
                char so = rGames.readChar();
                int edad = rGames.readInt();
                double precio = rGames.readDouble();
                int desc = rGames.readInt();
                String ruta = rGames.readUTF();
                area.append("Code: " + code + " Título: " + titulo + " SO: " + so + " Edad: " + edad + " Precio: " + precio + " Descargas: " + desc + "\n");
            }
        } catch (Exception e) {}
    }

    public void listDownloads(JTextArea area) {
        File folder = new File("steam/downloads");
        if (!folder.exists()) return;
        for (File f : folder.listFiles()) {
            if (f.getName().endsWith(".stm")) area.append(f.getName() + "\n");
        }
    }

    public void listDownloadsForPlayer(int codPlayer, JTextArea area) {
        File folder = new File("steam/downloads");
        if (!folder.exists()) return;
        for (File f : folder.listFiles()) {
            if (!f.getName().endsWith(".stm")) continue;
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = br.readLine();
                while(line != null) {
                    if (line.contains("Download #")) {
                        String nombreLine = br.readLine();
                        if (nombreLine.contains(String.valueOf(codPlayer))) {
                            area.append(f.getName() + "\n");
                        }
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e) {}
        }
    }

    public int getDownloadsCount(int codPlayer) {
        try {
            rPlayers.seek(0);
            while (rPlayers.getFilePointer() < rPlayers.length()) {
                int code = rPlayers.readInt();
                rPlayers.readUTF(); rPlayers.readUTF(); rPlayers.readUTF(); rPlayers.readLong();
                int desc = rPlayers.readInt();
                rPlayers.readUTF(); rPlayers.readUTF();
                if (code == codPlayer) return desc;
            }
        } catch (Exception e) {}
        return 0;
    }
}
