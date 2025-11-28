package lab6_binarios.GUIpantallas;

import lab6_binarios.steam.Steam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private Steam steam;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(Steam steam) {
        this.steam = steam;
        setTitle("Steam Login");
        setSize(500,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230,230,250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> performLogin());

        add(panel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            steam.rPlayers.seek(0);
            boolean found = false;
            String tipoUsuario = "";
            int codePlayer = -1;
            while (steam.rPlayers.getFilePointer() < steam.rPlayers.length()) {
                int code = steam.rPlayers.readInt();
                String user = steam.rPlayers.readUTF();
                String pass = steam.rPlayers.readUTF();
                steam.rPlayers.readUTF();
                steam.rPlayers.readLong();
                steam.rPlayers.readInt();
                steam.rPlayers.readUTF();
                String tipo = steam.rPlayers.readUTF();

                if(user.equals(username) && pass.equals(password)){
                    found = true;
                    tipoUsuario = tipo;
                    codePlayer = code;
                    break;
                }
            }

            if(found){
                playAudio("audios/login.wav");
                JOptionPane.showMessageDialog(this, "Login exitoso!");
                dispose();
                if(tipoUsuario.equalsIgnoreCase("admin")){
                    AdminFrame af = new AdminFrame(steam);
                    af.setVisible(true);
                } else {
                    UserFrame uf = new UserFrame(steam, codePlayer);
                    uf.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Error al verificar login: "+ex.getMessage());
        }
    }

    private void playAudio(String path){
        try{
            javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem.getAudioInputStream(new java.io.File(path));
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }catch(Exception ex){ System.out.println("Error audio: "+ex.getMessage()); }
    }
}