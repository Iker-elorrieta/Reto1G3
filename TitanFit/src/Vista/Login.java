package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo_Pojos.Usuario;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import java.beans.Beans;

import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldLoginUsuario;
    private JPasswordField textFieldLoginContraseña;

    /**
     * Create the frame.
     */
    public Login() {
        setTitle("TitanFit — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Single content pane, absolute positioning (null layout)
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null); // absolute layout
        setContentPane(contentPane);

        // Logo (will be set at runtime if available)
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(150, 20, 120, 120);
        contentPane.add(lblLogo);

        // Usuario
        JLabel lblLoginUsuario = new JLabel("Usuario");
        lblLoginUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLoginUsuario.setBounds(80, 160, 80, 22);
        contentPane.add(lblLoginUsuario);

        textFieldLoginUsuario = new JTextField();
        textFieldLoginUsuario.setBounds(170, 156, 170, 28);
        contentPane.add(textFieldLoginUsuario);

        // Contraseña
        JLabel lblLoginContraseña = new JLabel("Contraseña");
        lblLoginContraseña.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblLoginContraseña.setBounds(80, 200, 80, 22);
        contentPane.add(lblLoginContraseña);

        textFieldLoginContraseña = new JPasswordField();
        textFieldLoginContraseña.setBounds(170, 196, 170, 28);
        contentPane.add(textFieldLoginContraseña);

        // Buttons
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		char[] passwordChars = textFieldLoginContraseña.getPassword();
        		String password = new String(passwordChars);
        		System.out.println(password);
        		
        		Usuario user = Controlador.LoginControlador.loginUsuario(textFieldLoginUsuario.getText(),password);
        		if ( user != null ) {
        			JFrame workouts =new Workouts(user);
        			workouts.setVisible(true);
				} else {
					// Failed login
					System.out.println("Login failed. Invalid username or password.");
				}
        		
        	}
        });
        btnLogin.setBounds(65, 250, 140, 36);
        btnLogin.setFocusPainted(false);
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("Registrarse");
        btnRegister.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        	}
        });
        btnRegister.setBounds(215, 250, 140, 36);
        btnRegister.setFocusPainted(false);
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(33, 150, 243));
        btnRegister.setBorderPainted(true);
        contentPane.add(btnRegister);

        // Fixed size so WindowBuilder can render; runtime centers the frame
        setSize(new Dimension(420, 360));

        // Runtime-only: load logo image and center the window
        if (!Beans.isDesignTime()) {
            try {
                Image img = null;
                URL res = getClass().getResource("/unnamed.png");
                if (res != null) {
                    img = ImageIO.read(res);
                } else {
                    File file = new File("Recursos/unnamed.png");
                    if (file.exists()) {
                        img = ImageIO.read(file);
                    }
                }

                if (img != null) {
                    Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(scaledImg));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setLocationRelativeTo(null);
        }
    }

}