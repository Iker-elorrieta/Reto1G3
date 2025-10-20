package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Date;
import java.beans.Beans;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import Modelo_Pojos.Usuario;
import Modelo_Gestor.LoginGestor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Registro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldNombre;
    private JTextField textFieldApellidos;
    private JTextField textFieldCorreo;
    private JPasswordField passwordField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Registro frame = new Registro();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Registro() {
        setTitle("TitanFit — Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Title label replacing the logo
        JLabel lblTitulo = new JLabel("Regístrate!");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(120, 16, 200, 30);
        contentPane.add(lblTitulo);

        // Compact form
        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setFont(labelFont);
        lblNombre.setBounds(60, 70, 100, 22);
        contentPane.add(lblNombre);

        textFieldNombre = new JTextField();
        textFieldNombre.setBounds(160, 66, 200, 28);
        contentPane.add(textFieldNombre);

        JLabel lblApellidos = new JLabel("Apellidos");
        lblApellidos.setFont(labelFont);
        lblApellidos.setBounds(60, 106, 100, 22);
        contentPane.add(lblApellidos);

        textFieldApellidos = new JTextField();
        textFieldApellidos.setBounds(160, 102, 200, 28);
        contentPane.add(textFieldApellidos);

        JLabel lblCorreo = new JLabel("Correo");
        lblCorreo.setFont(labelFont);
        lblCorreo.setBounds(60, 142, 100, 22);
        contentPane.add(lblCorreo);

        textFieldCorreo = new JTextField();
        textFieldCorreo.setBounds(160, 138, 200, 28);
        contentPane.add(textFieldCorreo);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(labelFont);
        lblPassword.setBounds(60, 178, 100, 22);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 174, 200, 28);
        contentPane.add(passwordField);

        JLabel lblFecha = new JLabel("Fecha nacimiento");
        lblFecha.setFont(labelFont);
        lblFecha.setBounds(60, 214, 120, 22);
        contentPane.add(lblFecha);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(160, 210, 200, 28);
        contentPane.add(dateChooser);

        // Buttons styled like Login
        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String nombre = textFieldNombre.getText().trim();
                String apellidos = textFieldApellidos.getText().trim();
                String correo = textFieldCorreo.getText().trim();
                char[] passChars = passwordField.getPassword();
                String password = passChars != null ? new String(passChars) : "";
                Date fecha = dateChooser.getDate();

                if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || password.isEmpty() || fecha == null) {
                    JOptionPane.showMessageDialog(Registro.this, "Por favor complete todos los campos.", "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(nombre);
                    usuario.setApellidos(apellidos);
                    usuario.setEmail(correo);
                    usuario.setFechaNacimiento(fecha);
                    usuario.setNivel(0);

                    boolean ok = LoginGestor.registrarUsuario(usuario, password);
                    if (ok) {
                        JOptionPane.showMessageDialog(Registro.this, "Registro correcto. Puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        Login loginFrame = new Login();
                        loginFrame.setVisible(true);
                        Registro.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Registro.this, "Error al registrar. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Registro.this, "Error interno: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRegistrarse.setBounds(160, 254, 120, 36);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setBackground(new Color(33, 150, 243));
        btnRegistrarse.setForeground(Color.WHITE);
        contentPane.add(btnRegistrarse);

        JButton btnAtras = new JButton("Atras");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login loginFrame = new Login();
        		loginFrame.setVisible(true);
        		Registro.this.dispose();
        	}
        });
        btnAtras.setBounds(300, 254, 80, 36);
        btnAtras.setFocusPainted(false);
        btnAtras.setBackground(Color.WHITE);
        btnAtras.setForeground(new Color(33, 150, 243));
        contentPane.add(btnAtras);

        setSize(new Dimension(420, 340));

        // Center window at runtime only (keep design-time safe)
        if (!Beans.isDesignTime()) {
            setLocationRelativeTo(null);
        }

        

     
    }
}