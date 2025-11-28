/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.GUIpantallas;

import lab6_binarios.steam.Steam;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author andre
 */
public class PMenuUser extends JFrame {
    
     public PMenuUser(Steam steam, Player user) {
        setTitle("Steam - Usuario");
        setSize(400, 300);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        JButton btnCatalogo = new JButton("Ver Catálogo de Juegos");
        JButton btnDescargas = new JButton("Ver mis descargas");
        JButton btnPerfil = new JButton("Mi Perfil");

        add(btnCatalogo);
        add(btnDescargas);
        add(btnPerfil);

        btnCatalogo.addActionListener(e -> new Catalogo(steam, user).setVisible(true));
    }
}
