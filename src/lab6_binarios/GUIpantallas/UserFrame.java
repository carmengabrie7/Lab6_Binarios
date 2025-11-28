package lab6_binarios.GUIpantallas;

import lab6_binarios.steam.Steam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UserFrame extends JFrame {

    private Steam steam;
    private int codePlayer;

    public UserFrame(Steam steam, int codePlayer){
        this.steam = steam;
        this.codePlayer = codePlayer;
        setTitle("Steam Usuario");
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        JPanel catalogPanel = new JPanel();
        catalogPanel.setLayout(new BoxLayout(catalogPanel, BoxLayout.Y_AXIS));

        try{
            steam.rGames.seek(0);
            while(steam.rGames.getFilePointer() < steam.rGames.length()){
                int code = steam.rGames.readInt();
                String title = steam.rGames.readUTF();
                char so = steam.rGames.readChar();
                int edad = steam.rGames.readInt();
                double price = steam.rGames.readDouble();
                int desc = steam.rGames.readInt();
                String imgPath = steam.rGames.readUTF();

                JPanel gamePanel = new JPanel(new FlowLayout());
                gamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gamePanel.setMaximumSize(new Dimension(450,100));

                JLabel titleLabel = new JLabel(title + " ($"+price+") SO: "+so+" Edad: "+edad);
                JButton downloadBtn = new JButton("Descargar");

                downloadBtn.addActionListener(e->{
                    boolean ok = steam.downloadGame(code, codePlayer, so);
                    if(ok){
                        JOptionPane.showMessageDialog(this,"Descarga realizada");
                        playAudio("audios/descarga.wav");
                    } else {
                        JOptionPane.showMessageDialog(this,"No se puede descargar");
                    }
                });

                gamePanel.add(titleLabel);
                gamePanel.add(downloadBtn);
                catalogPanel.add(gamePanel);
            }

        }catch(Exception ex){
            System.out.println("Error cargando catálogo: "+ex.getMessage());
        }

        JScrollPane scroll = new JScrollPane(catalogPanel);
        tabs.addTab("Catálogo", scroll);
        add(tabs);
    }

    private void playAudio(String path){
        try{
            javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem.getAudioInputStream(new File(path));
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }catch(Exception ex){ System.out.println("Error audio: "+ex.getMessage()); }
    }
}