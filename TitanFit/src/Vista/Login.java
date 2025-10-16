package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldLoginUsuario;
    private JTextField textFieldLoginContraseña;

    /**
     * Create the frame.
     */
    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 823, 683);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblLoginUsuario = new JLabel("Usuario");
        lblLoginUsuario.setBounds(211, 226, 57, 14);
        contentPane.add(lblLoginUsuario);

        textFieldLoginUsuario = new JTextField();
        textFieldLoginUsuario.setBounds(355, 223, 222, 20);
        textFieldLoginUsuario.setColumns(10);
        contentPane.add(textFieldLoginUsuario);

        textFieldLoginContraseña = new JTextField();
        textFieldLoginContraseña.setBounds(355, 273, 222, 20);
        textFieldLoginContraseña.setColumns(10);
        contentPane.add(textFieldLoginContraseña);

        JLabel lblLoginContraseña = new JLabel("Contraseña");
        lblLoginContraseña.setBounds(211, 276, 57, 14);
        contentPane.add(lblLoginContraseña);

        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setBounds(469, 346, 157, 23);
        contentPane.add(btnLogin);

        JButton btnRegister = new JButton("Registrarse");
        btnRegister.setBounds(200, 346, 146, 23);
        contentPane.add(btnRegister);

        JLabel lblLogo = new JLabel("");
        lblLogo.setBounds(273, 11, 263, 204);

        File file = new File("Recursos/unnamed.png");
        contentPane.add(lblLogo);
    
        try {
            if (file.exists()) {
                Image img = ImageIO.read(file);
                if (img != null) {  
                    Image scaledImg = img.getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(scaledImg));
                } 
            } 
          
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentPane.add(lblLogo);
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
    
}
