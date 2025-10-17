package Vista;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;


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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registro frame = new Registro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Registro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 587, 472);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		JLabel lblLogo = new JLabel();
		lblLogo.setBounds(10, 119, 168, 142);
		contentPane.add(lblLogo);

		File file = new File("Recursos/unnamed.png");
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

	
		JButton btnRegistroAtras = new JButton("Atras");
		btnRegistroAtras.setBounds(10, 380, 89, 23);
		contentPane.add(btnRegistroAtras);

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(453, 380, 100, 23);
		contentPane.add(btnRegistrarse);

		
		JLabel lblRegistroNombre = new JLabel("Nombre");
		lblRegistroNombre.setBounds(258, 81, 48, 14);
		contentPane.add(lblRegistroNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(316, 78, 182, 20);
		contentPane.add(textFieldNombre);
		textFieldNombre.setColumns(10);

		JLabel lblRegistroApellidos = new JLabel("Apellidos");
		lblRegistroApellidos.setBounds(258, 139, 60, 14);
		contentPane.add(lblRegistroApellidos);

		textFieldApellidos = new JTextField();
		textFieldApellidos.setBounds(316, 136, 182, 20);
		contentPane.add(textFieldApellidos);
		textFieldApellidos.setColumns(10);

		JLabel lblRegistroCorreo = new JLabel("Correo electrónico");
		lblRegistroCorreo.setBounds(188, 197, 118, 14);
		contentPane.add(lblRegistroCorreo);

		textFieldCorreo = new JTextField();
		textFieldCorreo.setBounds(316, 194, 182, 20);
		contentPane.add(textFieldCorreo);
		textFieldCorreo.setColumns(10);

		JLabel lblRegistroContraseña = new JLabel("Contraseña");
		lblRegistroContraseña.setBounds(217, 247, 84, 14);
		contentPane.add(lblRegistroContraseña);

		passwordField = new JPasswordField();
		passwordField.setBounds(316, 241, 182, 20);
		contentPane.add(passwordField);

		JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento");
		lblFechaNacimiento.setBounds(190, 297, 118, 14);
		contentPane.add(lblFechaNacimiento);

		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(316, 297, 182, 20);
		contentPane.add(dateChooser);
	}
}
