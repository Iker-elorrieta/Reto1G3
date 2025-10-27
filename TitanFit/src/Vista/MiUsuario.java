package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import Modelo_Pojos.Usuario;

public class MiUsuario extends JFrame {

	private static final long serialVersionUID = 1L;

	public MiUsuario(final Usuario usuario) {
		setTitle("TitanFit — Mi Perfil");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);

		final String originalEmail = (usuario != null && usuario.getEmail() != null) ? usuario.getEmail() : null;

		JPanel contentPane = new JPanel(new BorderLayout(12, 12));
		contentPane.setBorder(new EmptyBorder(14, 14, 14, 14));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);

		// Header
		String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Usuario";
		JLabel lblHeader = new JLabel("Perfil — " + nombre);
		lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblHeader.setForeground(new Color(33, 150, 243));
		lblHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
		contentPane.add(lblHeader, BorderLayout.NORTH);

		// Form panel using GridBagLayout for better spacing
		JPanel form = new JPanel(new GridBagLayout());
		form.setBackground(Color.WHITE);
		form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230,230,230)), new EmptyBorder(12,12,12,12)));

		// Insets and label font
		Insets baseInsets = new Insets(10, 10, 10, 10);
		Font labelFont = new Font("SansSerif", Font.PLAIN, 14);

		// Nombre (label)
		GridBagConstraints gbcLblNombre = new GridBagConstraints();
		gbcLblNombre.insets = baseInsets;
		gbcLblNombre.anchor = GridBagConstraints.WEST;
		gbcLblNombre.gridx = 0; gbcLblNombre.gridy = 0;
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(labelFont);
		form.add(lblNombre, gbcLblNombre);

		// Nombre (field)
		GridBagConstraints gbcTxtNombre = new GridBagConstraints();
		gbcTxtNombre.insets = baseInsets;
		gbcTxtNombre.anchor = GridBagConstraints.WEST;
		gbcTxtNombre.gridx = 1; gbcTxtNombre.gridy = 0;
		gbcTxtNombre.weightx = 1; gbcTxtNombre.fill = GridBagConstraints.HORIZONTAL;
		final JTextField txtNombre = new JTextField();
		txtNombre.setColumns(28);
		txtNombre.setText(usuario != null && usuario.getNombre() != null ? usuario.getNombre() : "");
		form.add(txtNombre, gbcTxtNombre);

		// Apellidos (label)
		GridBagConstraints gbcLblApellidos = new GridBagConstraints();
		gbcLblApellidos.insets = baseInsets;
		gbcLblApellidos.anchor = GridBagConstraints.WEST;
		gbcLblApellidos.gridx = 0; gbcLblApellidos.gridy = 1;
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(labelFont);
		form.add(lblApellidos, gbcLblApellidos);

		// Apellidos (field)
		GridBagConstraints gbcTxtApellidos = new GridBagConstraints();
		gbcTxtApellidos.insets = baseInsets;
		gbcTxtApellidos.anchor = GridBagConstraints.WEST;
		gbcTxtApellidos.gridx = 1; gbcTxtApellidos.gridy = 1;
		gbcTxtApellidos.weightx = 1; gbcTxtApellidos.fill = GridBagConstraints.HORIZONTAL;
		final JTextField txtApellidos = new JTextField();
		txtApellidos.setColumns(28);
		txtApellidos.setText(usuario != null && usuario.getApellidos() != null ? usuario.getApellidos() : "");
		form.add(txtApellidos, gbcTxtApellidos);

		// Email (label)
		GridBagConstraints gbcLblEmail = new GridBagConstraints();
		gbcLblEmail.insets = baseInsets;
		gbcLblEmail.anchor = GridBagConstraints.WEST;
		gbcLblEmail.gridx = 0; gbcLblEmail.gridy = 2;
		JLabel lblEmail = new JLabel("Correo electrónico:");
		lblEmail.setFont(labelFont);
		form.add(lblEmail, gbcLblEmail);

		// Email (field)
		GridBagConstraints gbcTxtEmail = new GridBagConstraints();
		gbcTxtEmail.insets = baseInsets;
		gbcTxtEmail.anchor = GridBagConstraints.WEST;
		gbcTxtEmail.gridx = 1; gbcTxtEmail.gridy = 2;
		gbcTxtEmail.weightx = 1; gbcTxtEmail.fill = GridBagConstraints.HORIZONTAL;
		final JTextField txtEmail = new JTextField();
		txtEmail.setColumns(28);
		txtEmail.setText(usuario != null && usuario.getEmail() != null ? usuario.getEmail() : "");
		form.add(txtEmail, gbcTxtEmail);

		// Fecha de nacimiento (label)
		GridBagConstraints gbcLblFecha = new GridBagConstraints();
		gbcLblFecha.insets = baseInsets;
		gbcLblFecha.anchor = GridBagConstraints.WEST;
		gbcLblFecha.gridx = 0; gbcLblFecha.gridy = 3;
		JLabel lblFecha = new JLabel("Fecha de nacimiento (yyyy-MM-dd):");
		lblFecha.setFont(labelFont);
		form.add(lblFecha, gbcLblFecha);

		// Fecha (field)
		GridBagConstraints gbcTxtFecha = new GridBagConstraints();
		gbcTxtFecha.insets = baseInsets;
		gbcTxtFecha.anchor = GridBagConstraints.WEST;
		gbcTxtFecha.gridx = 1; gbcTxtFecha.gridy = 3;
		gbcTxtFecha.weightx = 1; gbcTxtFecha.fill = GridBagConstraints.HORIZONTAL;
		final JTextField txtFecha = new JTextField();
		txtFecha.setColumns(20);
		if (usuario != null && usuario.getFechaNacimiento() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			txtFecha.setText(sdf.format(usuario.getFechaNacimiento()));
		}
		form.add(txtFecha, gbcTxtFecha);
		
				// Nueva contraseña (label)
				GridBagConstraints gbcLblNewPass = new GridBagConstraints();
				gbcLblNewPass.insets = baseInsets;
				gbcLblNewPass.anchor = GridBagConstraints.WEST;
				gbcLblNewPass.gridx = 0; 
				gbcLblNewPass.gridy = 4;
				JLabel lblNewPass = new JLabel("Nueva contraseña (opcional):");
				lblNewPass.setFont(labelFont);
				form.add(lblNewPass, gbcLblNewPass);

		// Nueva contraseña (field)
		GridBagConstraints gbcTxtNewPass = new GridBagConstraints();
		gbcTxtNewPass.insets = baseInsets;
		gbcTxtNewPass.anchor = GridBagConstraints.WEST;
		gbcTxtNewPass.gridx = 1; gbcTxtNewPass.gridy = 4;
		gbcTxtNewPass.weightx = 1; gbcTxtNewPass.fill = GridBagConstraints.HORIZONTAL;
		final JPasswordField txtNewPass = new JPasswordField();
		txtNewPass.setColumns(28);
		form.add(txtNewPass, gbcTxtNewPass);

		// Separator space before password
		GridBagConstraints gbcSep = new GridBagConstraints();
		gbcSep.insets = baseInsets;
		gbcSep.gridx = 0; gbcSep.gridy = 5; gbcSep.gridwidth = 2; gbcSep.fill = GridBagConstraints.HORIZONTAL;
		form.add(new JLabel(""), gbcSep);

		contentPane.add(form, BorderLayout.CENTER);

		// Footer buttons
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
		footer.setBackground(Color.WHITE);
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(110, 36));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBackground(Color.WHITE);
		btnCancelar.setForeground(new Color(33, 150, 243));
		btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setPreferredSize(new Dimension(110, 36));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBackground(new Color(33, 150, 243));
		btnGuardar.setForeground(Color.WHITE);

		footer.add(btnCancelar);
		footer.add(btnGuardar);
		contentPane.add(footer, BorderLayout.SOUTH);

		btnCancelar.addActionListener(e ->{
			Workouts main = new Workouts(usuario);
			main.setVisible(true);
			MiUsuario.this.dispose();
			
		} 
				);

		btnGuardar.addActionListener(e -> {
			String nuevoNombre = txtNombre.getText().trim();
			String nuevosApellidos = txtApellidos.getText().trim();
			String nuevoEmail = txtEmail.getText().trim();
			String fechaStr = txtFecha.getText().trim();
			String nuevoPass = txtNewPass.getText().trim();

			if (nuevoNombre.isEmpty()) {
				JOptionPane.showMessageDialog(MiUsuario.this, "El nombre no puede estar vacío.", "Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (nuevoEmail.isEmpty()) {
				JOptionPane.showMessageDialog(MiUsuario.this, "El correo no puede estar vacío.", "Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Parse date if provided
			Date nuevaFecha = null;
			if (!fechaStr.isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					nuevaFecha = sdf.parse(fechaStr);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(MiUsuario.this, "Formato de fecha inválido. Use yyyy-MM-dd.", "Error",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Update Usuario object in memory with new values
			if (usuario != null) {
				usuario.setNombre(nuevoNombre);
				usuario.setApellidos(nuevosApellidos);
				usuario.setEmail(nuevoEmail);
				if (nuevaFecha != null)
					usuario.setFechaNacimiento(nuevaFecha);
				if (!nuevoPass.isEmpty()) {
					usuario.setPassword(nuevoPass);
				}
			}

			// Call controller to persist changes (delegates to gestor)
			boolean ok = Controlador.LoginControlador.actualizarUsuario(originalEmail, usuario, nuevoPass);
			if (ok) {
				JOptionPane.showMessageDialog(MiUsuario.this, "Perfil actualizado correctamente.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
				// Open Workouts window with updated user object
				Workouts main = new Workouts(usuario);
				main.setVisible(true);
				MiUsuario.this.dispose();
			} else {
				JOptionPane.showMessageDialog(MiUsuario.this, "No se pudo actualizar el perfil. Compruebe la conexión.",
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		setSize(new Dimension(560, 380));
		setLocationRelativeTo(null);
	}

}