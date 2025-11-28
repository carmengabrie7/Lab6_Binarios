/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.GUIpantallas;

import java.awt.GridLayout;
import javax.swing.JButton;
import lab6_binarios.steam.Steam;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author andre
 */
public class PMenuAdmin extends JFrame{
     public PMenuAdmin(Steam steam, Player admin) {
        setTitle("Steam - Admin");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(null);

        JButton btnRegistrarPlayer = new JButton("Registrar Player");
        JButton btnModificarPlayer = new JButton("Modificar Player");
        JButton btnRegistrarJuego = new JButton("Registrar Juego");
        JButton btnCambiarPrecio = new JButton("Cambiar Precio Juego");
        JButton btnVerJuegos = new JButton("Ver Juegos");
        JButton btnGenerarReporte = new JButton("Generar Reporte Cliente");

        add(btnRegistrarPlayer);
        add(btnModificarPlayer);
        add(btnRegistrarJuego);
        add(btnCambiarPrecio);
        add(btnVerJuegos);
        add(btnGenerarReporte);

        btnRegistrarPlayer.addActionListener(e -> new PRegistrarJugador(steam).setVisible(true));
        btnRegistrarJuego.addActionListener(e -> new PRegistrarJuego(steam).setVisible(true));
    }
}
