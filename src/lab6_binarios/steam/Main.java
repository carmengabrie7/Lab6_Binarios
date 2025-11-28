package lab6_binarios.steam;

import lab6_binarios.GUIpantallas.LoginFrame;
import java.util.Calendar;

public class Main {

    public static void main(String[] args){
        Steam steam = new Steam();

        try{
            boolean adminExists = false;
            steam.rPlayers.seek(0);
            while(steam.rPlayers.getFilePointer() < steam.rPlayers.length()){
                steam.rPlayers.readInt();
                steam.rPlayers.readUTF();
                steam.rPlayers.readUTF();
                steam.rPlayers.readUTF();
                steam.rPlayers.readLong();
                steam.rPlayers.readInt();
                steam.rPlayers.readUTF();
                String tipo = steam.rPlayers.readUTF();
                if(tipo.equalsIgnoreCase("admin")){
                    adminExists=true;
                    break;
                }
            }
            if(!adminExists){
                Calendar cal = Calendar.getInstance();
                steam.addPlayer("admin","admin123","Administrador",cal,"steam/admin.png","admin");
            }
        }catch(Exception ex){ System.out.println("Error creando admin: "+ex.getMessage()); }

        LoginFrame login = new LoginFrame(steam);
        login.setVisible(true);
    }
}
