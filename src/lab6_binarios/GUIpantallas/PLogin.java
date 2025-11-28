/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.GUIpantallas;
import lab6_binarios.steam.Steam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
/**
 *
 * @author andre
 */
public class PLogin extends JFrame {
   private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    private Steam steam; // referencia al sistema

    public PLogin(Steam steam) {
        this.steam = steam;
        setTitle("Steam - Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnIngresar = new JButton("Ingresar");
        add(btnIngresar);

        btnIngresar.addActionListener(e -> {
            Player p = steam.login(txtUsuario.getText(), new String(txtPassword.getPassword()));
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Datos incorrectos");
            } else if (p.getTipoUsuario().equals("admin")) {
                new MenuAdmin(steam, p).setVisible(true);
                this.dispose();
            } else {
                new MenuUser(steam, p).setVisible(true);
                this.dispose();
            }
        });
    }
}
