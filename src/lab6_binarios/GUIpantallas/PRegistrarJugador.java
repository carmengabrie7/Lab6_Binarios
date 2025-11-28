/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6_binarios.GUIpantallas;
import lab6_binarios.steam.Steam;
import java.awt.*;
import java.util.Date;
import javax.swing.*;
/**
 *
 * @author andre
 */
public class PRegistrarJugador extends JFrame{
      public PRegistrarJugador(Steam steam) {
        setTitle("Registrar Player");
        setSize(350, 250);
        setLayout(new GridLayout(6, 2));

        JTextField txtUser = new JTextField();
        JTextField txtPass = new JTextField();
        JTextField txtNombre = new JTextField();
        JSpinner fechaNacimiento = new JSpinner(new SpinnerDateModel());
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"admin", "normal"});
        JButton btnRegistrar = new JButton("Registrar");

        add(new JLabel("Usuario:")); add(txtUser);
        add(new JLabel("Password:")); add(txtPass);
        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Fecha Nac:")); add(fechaNacimiento);
        add(new JLabel("Tipo:")); add(cmbTipo);
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            steam.addPlayer(txtUser.getText(), txtPass.getText(), txtNombre.getText(), 
                            ((Date)fechaNacimiento.getValue()).getTime(), 
                            cmbTipo.getSelectedItem().toString());   
            JOptionPane.showMessageDialog(this, "Jugador registrado");
            dispose();
        });
    }
}
