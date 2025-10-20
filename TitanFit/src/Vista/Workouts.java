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
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.stream.Collectors;

import Modelo_Pojos.Workout;

/**
 * Workouts visual window.
 * - Loads workouts in background.
 * - Filters by user level with a combo.
 * - 'Ver Video' button opens workout.video URL.
 */
public class Workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableWorkouts;
	private JComboBox<String> comboNiveles;
	private DefaultTableModel model;
	private List<Workout> workoutsData = new ArrayList<>();
	private List<Workout> visibleData = new ArrayList<>();
	private int nivelUsuario = 0;

	public Workouts(Usuario usuario) {
		setTitle("TitanFit — Workouts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		nivelUsuario = (usuario != null) ? usuario.getNivel() : 0;

		contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setBackground(new Color(245, 245, 239));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(245, 245, 239));
		contentPane.add(topPanel, BorderLayout.NORTH);

		String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Usuario";
		JLabel lblGreeting = new JLabel("¡Hola, " + nombre + "!");
		lblGreeting.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblGreeting.setForeground(new Color(33, 150, 243));
		lblGreeting.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
		topPanel.add(lblGreeting, BorderLayout.WEST);

		JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		topRight.setBackground(Color.WHITE);
		JButton btnHistorial = new JButton("Historial");
		JButton btnMiPerfil = new JButton("Mi Perfil");

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

		btnMiPerfil.addActionListener(e -> {
			MiUsuario miUsuario = new MiUsuario(usuario);
			miUsuario.setVisible(true);
			Workouts.this.dispose();
		});
		

		btnHistorial.addActionListener(e -> JOptionPane.showMessageDialog(Workouts.this, "Historial no implementado.", "Info", JOptionPane.INFORMATION_MESSAGE));

		topRight.add(btnHistorial);
		topRight.add(btnMiPerfil);
		topPanel.add(topRight, BorderLayout.EAST);

		JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
		centerPanel.setBackground(new Color(245, 245, 239));
		contentPane.add(centerPanel, BorderLayout.CENTER);

		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
		filterPanel.setBackground(new Color(245, 245, 239));
		filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240,240,240)));
		JLabel lblFiltro = new JLabel("Filtrar por nivel:");
		lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 14));
		comboNiveles = new JComboBox<>();
		comboNiveles.setPreferredSize(new Dimension(200, 26));
		comboNiveles.addActionListener(e -> applyFilter());
		filterPanel.add(lblFiltro);
		filterPanel.add(comboNiveles);
		centerPanel.add(filterPanel, BorderLayout.NORTH);

		String[] columnNames = { "Nombre", "# Ejercicios", "Nivel", "Video" };
		model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3; 
			}
		};

		model.addRow(new Object[] { "Cargando...", "", "", "" });

		tableWorkouts = new JTable(model);
		tableWorkouts.setRowHeight(30);
		tableWorkouts.getTableHeader().setReorderingAllowed(false);
		tableWorkouts.getColumn("Video").setCellRenderer(new ButtonRenderer("Ver Video"));
		tableWorkouts.getColumn("Video").setCellEditor(new ButtonEditor("Ver Video"));

		JScrollPane scroll = new JScrollPane(tableWorkouts);
		centerPanel.add(scroll, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(Color.WHITE);
		JButton btnAtras = new JButton("Atras");
		btnAtras.setPreferredSize(new Dimension(100, 30));
		btnAtras.setFocusPainted(false);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setForeground(new Color(33, 150, 243));
		btnAtras.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		btnAtras.addActionListener(e -> Workouts.this.dispose());
		bottomPanel.add(btnAtras, BorderLayout.WEST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		setSize(new Dimension(820, 520));
		setLocationRelativeTo(null);

		loadWorkouts();
	}

	private void loadWorkouts() {
		SwingWorker<List<Workout>, Void> worker = new SwingWorker<List<Workout>, Void>() {
			@Override
			protected List<Workout> doInBackground() throws Exception {
				return Controlador.WorkoutControlador.obtenerWorkouts();
			}

			@Override
			protected void done() {
				try {
					List<Workout> lista = get();
					workoutsData = (lista == null) ? new ArrayList<>() : lista;
					populateLevels();
					applyFilter();
				} catch (Exception ex) {
					ex.printStackTrace();
					model.setRowCount(0);
					model.addRow(new Object[] { "Error cargando workouts", "", "", "" });
				}
			}
		};
		worker.execute();
	}

	private void populateLevels() {
		comboNiveles.removeAllItems();
		int total = workoutsData.size();
		comboNiveles.addItem("Todos los niveles (" + total + ")");
		comboNiveles.addItem("Hasta mi nivel (" + nivelUsuario + ")");

		// Count workouts per existing level and list only those levels
		Map<Integer, Long> counts = workoutsData.stream()
			.collect(Collectors.groupingBy(Workout::getNivel, LinkedHashMap::new, Collectors.counting()));
		ArrayList<Integer> levels = new ArrayList<>(counts.keySet());
		Collections.sort(levels);
		for (Integer lvl : levels) {
			comboNiveles.addItem("Nivel " + lvl + " (" + counts.get(lvl) + ")");
		}
		// Default to 'Hasta mi nivel'
		if (comboNiveles.getItemCount() >= 2) comboNiveles.setSelectedIndex(1);
	}

	private void applyFilter() {
		if (model == null) return;
		model.setRowCount(0);
		visibleData.clear();
		String sel = (String) comboNiveles.getSelectedItem();
		for (Workout w : workoutsData) {
			int nivel = w.getNivel();
			boolean include = false;
			if (sel == null || sel.startsWith("Hasta mi nivel")) {
				include = nivel <= nivelUsuario;
			} else if (sel.startsWith("Todos los niveles")) {
				include = true;
			} else if (sel.startsWith("Nivel ")) {
				try {
					String lvStr = sel.substring("Nivel ".length());
					int idxPar = lvStr.indexOf(' ');
					if (idxPar > 0) lvStr = lvStr.substring(0, idxPar);
					include = nivel == Integer.parseInt(lvStr.trim());
				} catch (Exception ignore) { include = false; }
			}
			if (include) {
				visibleData.add(w);
				model.addRow(new Object[] { w.getNombre(), w.getNumeroEjercicios(), nivel, "Ver Video" });
			}
		}
		if (model.getRowCount() == 0) model.addRow(new Object[] { "Sin resultados", "", "", "" });
	}

	// Renderer to show a non-functional button in the table's Video column
	private static class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer(String text) {
			setOpaque(true);
			setFocusable(false);
			setBackground(new Color(33, 150, 243));
			setForeground(Color.WHITE);
			setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
			setText(text);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
			setText(value == null ? "Ver Video" : value.toString());
			return this;
		}
	}

	// Editor to handle button clicks and open the first exercise URL
	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private final JButton button;
		private int editingRow = -1;

		public ButtonEditor(String text) {
			button = new JButton(text);
			button.setOpaque(true);
			button.setFocusable(false);
			button.setBackground(new Color(33, 150, 243));
			button.setForeground(Color.WHITE);
			button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
			button.addActionListener(e -> { fireEditingStopped(); openVideoForRow(editingRow); });
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.editingRow = row;
			button.setText(value == null ? "Ver Video" : value.toString());
			return button;
		}

		@Override
		public Object getCellEditorValue() { return "Ver Video"; }
	}

	private void openVideoForRow(int row) {
		try {
			if (row < 0 || row >= visibleData.size()) return;
			Workout w = visibleData.get(row);
			String url = w.getVideo();
			if (url == null || url.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "No hay URL de video disponible.", "Info", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
			} else {
				JOptionPane.showMessageDialog(this, "Abrir el navegador no es compatible en este sistema.", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "No se pudo abrir el enlace.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
