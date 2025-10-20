package Vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo_Pojos.Usuario;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;

/**
 * Workouts visual mockup window.
 * - No Firebase or event logic, purely UI structure.
 */
public class Workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableWorkouts;
	private JComboBox<String> comboNiveles;

	public Workouts(Usuario usuario) {
		setTitle("TitanFit — Workouts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// Content pane with padding and white background (consistent with Login)
		contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);

		// Top header panel: greeting left, action buttons right
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		contentPane.add(topPanel, BorderLayout.NORTH);

		// Greeting label
		String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Usuario";
		JLabel lblGreeting = new JLabel("¡Hola, " + nombre + "!");
		lblGreeting.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblGreeting.setForeground(new Color(33, 150, 243));
		lblGreeting.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
		topPanel.add(lblGreeting, BorderLayout.WEST);

		// Top-right buttons panel
		JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		topRight.setBackground(Color.WHITE);
		JButton btnHistorial = new JButton("Historial");
		JButton btnMiPerfil = new JButton("Mi Perfil");
		// Style buttons similar to login
		btnHistorial.setFocusPainted(false);
		btnMiPerfil.setFocusPainted(false);
		btnHistorial.setBackground(Color.WHITE);
		btnHistorial.setForeground(new Color(33, 150, 243));
		btnHistorial.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		btnMiPerfil.setBackground(new Color(33, 150, 243));
		btnMiPerfil.setForeground(Color.WHITE);
		btnMiPerfil.setBorderPainted(false);
		btnHistorial.setPreferredSize(new Dimension(100, 28));
		btnMiPerfil.setPreferredSize(new Dimension(100, 28));
		topRight.add(btnHistorial);
		topRight.add(btnMiPerfil);
		topPanel.add(topRight, BorderLayout.EAST);

		// Main center area: filter panel on top of table
		JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
		centerPanel.setBackground(Color.WHITE);
		contentPane.add(centerPanel, BorderLayout.CENTER);

		// Filter panel using FlowLayout for a compact, clean look
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
		filterPanel.setBackground(Color.WHITE);
		filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,240,240)));
		JLabel lblFiltro = new JLabel("Filtrar por nivel:");
		lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboNiveles = new JComboBox<>();
		comboNiveles.setPreferredSize(new Dimension(160, 26));
		comboNiveles.addItem("Todos los niveles");
		for (int i = 0; i <= 5; i++) {
			comboNiveles.addItem("Nivel " + i);
		}
		filterPanel.add(lblFiltro);
		filterPanel.add(comboNiveles);
		centerPanel.add(filterPanel, BorderLayout.NORTH);

		// Table with workouts columns: Nombre, # Ejercicios, Nivel, Video
		String[] columnNames = { "Nombre", "# Ejercicios", "Nivel", "Video" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// no cell editable (no events yet)
				return false;
			}
		};

		// Mock rows for visual purposes
		model.addRow(new Object[] { "Full Body Blast", 8, 1, "Ver" });
		model.addRow(new Object[] { "Cardio Quickie", 5, 0, "Ver" });
		model.addRow(new Object[] { "Fuerza Superior", 10, 2, "Ver" });
		model.addRow(new Object[] { "Flexibilidad", 6, 0, "Ver" });

		tableWorkouts = new JTable(model);
		tableWorkouts.setRowHeight(30);
		tableWorkouts.getTableHeader().setReorderingAllowed(false);
		// Make the 'Video' column use a button renderer for visual
		tableWorkouts.getColumn("Video").setCellRenderer(new ButtonRenderer());

		JScrollPane scroll = new JScrollPane(tableWorkouts);
		centerPanel.add(scroll, BorderLayout.CENTER);

		// Bottom panel with a left-aligned "Atras" button
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(Color.WHITE);
		JButton btnAtras = new JButton("Atras");
		btnAtras.setPreferredSize(new Dimension(100, 30));
		btnAtras.setFocusPainted(false);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setForeground(new Color(33, 150, 243));
		btnAtras.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		bottomPanel.add(btnAtras, BorderLayout.WEST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		// Final window sizing and centering
		setSize(new Dimension(820, 520));
		setLocationRelativeTo(null);
	}

	// Renderer to show a non-functional button in the table's Video column
	private static class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
			setFocusable(false);
			setBackground(new Color(33, 150, 243));
			setForeground(Color.WHITE);
			setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
			setText(value == null ? "Ver" : value.toString());
			return this;
		}
	}

}