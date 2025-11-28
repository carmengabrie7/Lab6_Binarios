package lab6_binarios.GUIpantallas;

import lab6_binarios.steam.Steam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Calendar;

public class AdminFrame extends JFrame {

    private Steam steam;

    public AdminFrame(Steam steam){
        this.steam = steam;
        setTitle("Steam - Admin");
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // ==================== PANEL PLAYERS ====================
        JPanel playersPanel = new JPanel(new GridBagLayout());
        playersPanel.setBackground(new Color(240,250,255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField userField = new JTextField(15);
        JTextField passField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField imgField = new JTextField(15);
        JComboBox<String> tipoBox = new JComboBox<>(new String[]{"admin","normal"});

        JButton addPlayerBtn = new JButton("Agregar Player");
        JButton modifyPlayerBtn = new JButton("Modificar Player");
        JButton deletePlayerBtn = new JButton("Eliminar Player");

        gbc.gridx=0; gbc.gridy=0; playersPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx=1; playersPanel.add(userField, gbc);
        gbc.gridx=0; gbc.gridy=1; playersPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx=1; playersPanel.add(passField, gbc);
        gbc.gridx=0; gbc.gridy=2; playersPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; playersPanel.add(nameField, gbc);
        gbc.gridx=0; gbc.gridy=3; playersPanel.add(new JLabel("Imagen ruta:"), gbc);
        gbc.gridx=1; playersPanel.add(imgField, gbc);
        gbc.gridx=0; gbc.gridy=4; playersPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx=1; playersPanel.add(tipoBox, gbc);

        gbc.gridx=0; gbc.gridy=5; playersPanel.add(addPlayerBtn, gbc);
        gbc.gridx=1; playersPanel.add(modifyPlayerBtn, gbc);
        gbc.gridx=0; gbc.gridy=6; gbc.gridwidth=2; playersPanel.add(deletePlayerBtn, gbc);

        // ==================== PANEL GAMES ====================
        JPanel gamesPanel = new JPanel(new GridBagLayout());
        gamesPanel.setBackground(new Color(255,250,240));

        JTextField titleField = new JTextField(15);
        JComboBox<String> soBox = new JComboBox<>(new String[]{"W","M","L"});
        JTextField edadField = new JTextField(5);
        JTextField priceField = new JTextField(5);
        JTextField imgGameField = new JTextField(15);
        JTextField codePriceField = new JTextField(5);
        JTextField codeModifyField = new JTextField(5);
        JTextField codeDeleteField = new JTextField(5);
        JTextField codeReportField = new JTextField(5);
        JTextField reportOutField = new JTextField(15);

        JButton addGameBtn = new JButton("Agregar Game");
        JButton modifyGameBtn = new JButton("Modificar Precio");
        JButton deleteGameBtn = new JButton("Eliminar Game");
        JButton reportBtn = new JButton("Generar Reporte");

        gbc.gridx=0; gbc.gridy=0; gamesPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx=1; gamesPanel.add(titleField, gbc);
        gbc.gridx=0; gbc.gridy=1; gamesPanel.add(new JLabel("SO:"), gbc);
        gbc.gridx=1; gamesPanel.add(soBox, gbc);
        gbc.gridx=0; gbc.gridy=2; gamesPanel.add(new JLabel("Edad mínima:"), gbc);
        gbc.gridx=1; gamesPanel.add(edadField, gbc);
        gbc.gridx=0; gbc.gridy=3; gamesPanel.add(new JLabel("Precio:"), gbc);
        gbc.gridx=1; gamesPanel.add(priceField, gbc);
        gbc.gridx=0; gbc.gridy=4; gamesPanel.add(new JLabel("Imagen ruta:"), gbc);
        gbc.gridx=1; gamesPanel.add(imgGameField, gbc);
        gbc.gridx=0; gbc.gridy=5; gamesPanel.add(addGameBtn, gbc);

        gbc.gridx=0; gbc.gridy=6; gamesPanel.add(new JLabel("Código Game Precio:"), gbc);
        gbc.gridx=1; gamesPanel.add(codePriceField, gbc);
        gbc.gridx=0; gbc.gridy=7; gamesPanel.add(modifyGameBtn, gbc);

        gbc.gridx=0; gbc.gridy=8; gamesPanel.add(new JLabel("Código Game Delete:"), gbc);
        gbc.gridx=1; gamesPanel.add(codeDeleteField, gbc);
        gbc.gridx=0; gbc.gridy=9; gamesPanel.add(deleteGameBtn, gbc);

        gbc.gridx=0; gbc.gridy=10; gamesPanel.add(new JLabel("Código Player Report:"), gbc);
        gbc.gridx=1; gamesPanel.add(codeReportField, gbc);
        gbc.gridx=0; gbc.gridy=11; gamesPanel.add(new JLabel("Archivo salida:"), gbc);
        gbc.gridx=1; gamesPanel.add(reportOutField, gbc);
        gbc.gridx=0; gbc.gridy=12; gamesPanel.add(reportBtn, gbc);

        // ==================== BOTONES ====================
        addPlayerBtn.addActionListener(e->{
            Calendar nac = Calendar.getInstance();
            steam.addPlayer(userField.getText(), passField.getText(), nameField.getText(), nac, imgField.getText(), (String)tipoBox.getSelectedItem());
            JOptionPane.showMessageDialog(this,"Player agregado");
            playAudio("audios/registro.wav");
        });

        modifyPlayerBtn.addActionListener(e->{
            JOptionPane.showMessageDialog(this,"Función modificar player aún no implementada en RAF directamente");
        });

        deletePlayerBtn.addActionListener(e->{
            JOptionPane.showMessageDialog(this,"Función eliminar player aún no implementada en RAF directamente");
        });

        addGameBtn.addActionListener(e->{
            steam.addGame(titleField.getText(), soBox.getSelectedItem().toString().charAt(0),
                    Integer.parseInt(edadField.getText()), Double.parseDouble(priceField.getText()), imgGameField.getText());
            JOptionPane.showMessageDialog(this,"Game agregado");
            playAudio("audios/registro.wav");
        });

        modifyGameBtn.addActionListener(e->{
            int cod = Integer.parseInt(codePriceField.getText());
            double precio = Double.parseDouble(priceField.getText());
            boolean ok = steam.updatePriceFor(cod, precio);
            if(ok) JOptionPane.showMessageDialog(this,"Precio actualizado");
            else JOptionPane.showMessageDialog(this,"Error al actualizar");
        });

        deleteGameBtn.addActionListener(e->{
            JOptionPane.showMessageDialog(this,"Función eliminar game aún no implementada en RAF directamente");
        });

        reportBtn.addActionListener(e->{
            steam.reportForClient(Integer.parseInt(codeReportField.getText()), reportOutField.getText());
            playAudio("audios/registro.wav");
        });

        tabs.addTab("Players", playersPanel);
        tabs.addTab("Games", gamesPanel);

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