package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo_Pojos.Usuario;
import Modelo_Pojos.Ejercicio;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.ListSelectionModel;

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
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.stream.Collectors;

import Modelo_Pojos.Workout;

public class Workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableWorkouts;
	private JTable tableEjercicios;
	private JComboBox<String> comboNiveles;
	private DefaultTableModel model;
	private DefaultTableModel modelEjercicios;
	private List<Workout> workoutsData = new ArrayList<>();
	private List<Workout> visibleData = new ArrayList<>();
	private int nivelUsuario = 0;
	private Workout workoutSeleccionado = null;
	private JButton btnIniciar;
	private Usuario usuario; 

	public Workouts(Usuario usuario) {
		this.usuario = usuario;
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
		JLabel lblGreeting = new JLabel("¡Hola, " + nombre + "! (Nivel " + nivelUsuario + ")");
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
		

		btnHistorial.addActionListener(e -> {
			Historial historial = new Historial(usuario);
			historial.setVisible(true);
			Workouts.this.dispose();
		});

		topRight.add(btnHistorial);
		topRight.add(btnMiPerfil);
		topPanel.add(topRight, BorderLayout.EAST);

		// Panel principal con JSplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(450);
		splitPane.setEnabled(false);
		splitPane.setBackground(new Color(245, 245, 239));
		contentPane.add(splitPane, BorderLayout.CENTER);

		// Panel izquierdo: Lista de workouts
		JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
		leftPanel.setBackground(new Color(245, 245, 239));
		splitPane.setLeftComponent(leftPanel);

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
		leftPanel.add(filterPanel, BorderLayout.NORTH);

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
		tableWorkouts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableWorkouts.getTableHeader().setReorderingAllowed(false);
		tableWorkouts.getColumn("Video").setCellRenderer(new ButtonRenderer("Ver Video"));
		tableWorkouts.getColumn("Video").setCellEditor(new ButtonEditor("Ver Video"));
		
		// Listener para selección de workout
		tableWorkouts.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = tableWorkouts.getSelectedRow();
				if (selectedRow >= 0 && selectedRow < visibleData.size()) {
					workoutSeleccionado = visibleData.get(selectedRow);
					mostrarEjerciciosDelWorkout();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tableWorkouts);
		leftPanel.add(scroll, BorderLayout.CENTER);

		// Panel derecho: Detalles del workout y ejercicios
		JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
		rightPanel.setBackground(new Color(245, 245, 239));
		splitPane.setRightComponent(rightPanel);

		JLabel lblEjerciciosTitle = new JLabel("Ejercicios del Workout");
		lblEjerciciosTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblEjerciciosTitle.setForeground(new Color(33, 150, 243));
		lblEjerciciosTitle.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		lblEjerciciosTitle.setBackground(new Color(245, 245, 239));
		lblEjerciciosTitle.setOpaque(true);
		rightPanel.add(lblEjerciciosTitle, BorderLayout.NORTH);

		String[] columnNamesEjercicios = { "#", "Nombre", "Descripción" };
		modelEjercicios = new DefaultTableModel(columnNamesEjercicios, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		modelEjercicios.addRow(new Object[] { "", "Selecciona un workout", "" });

		tableEjercicios = new JTable(modelEjercicios);
		tableEjercicios.setRowHeight(30);
		tableEjercicios.getTableHeader().setReorderingAllowed(false);
		tableEjercicios.getColumnModel().getColumn(0).setPreferredWidth(30);
		tableEjercicios.getColumnModel().getColumn(0).setMaxWidth(40);
		tableEjercicios.getColumnModel().getColumn(1).setPreferredWidth(120);

		JScrollPane scrollEjercicios = new JScrollPane(tableEjercicios);
		rightPanel.add(scrollEjercicios, BorderLayout.CENTER);

		// Panel con botón Iniciar
		JPanel panelIniciar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
		panelIniciar.setBackground(new Color(245, 245, 239));
		btnIniciar = new JButton("Iniciar Workout");
		btnIniciar.setPreferredSize(new Dimension(180, 35));
		btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnIniciar.setFocusPainted(false);
		btnIniciar.setBackground(new Color(76, 175, 80));
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setBorderPainted(false);
		btnIniciar.setEnabled(false);
		btnIniciar.addActionListener(e -> iniciarWorkout());
		panelIniciar.add(btnIniciar);
		rightPanel.add(panelIniciar, BorderLayout.SOUTH);

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

		setSize(new Dimension(1000, 520));
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

		Map<Integer, Long> counts = workoutsData.stream()
			.collect(Collectors.groupingBy(Workout::getNivel, LinkedHashMap::new, Collectors.counting()));
		
		ArrayList<Integer> levels = new ArrayList<>(counts.keySet());
		Collections.sort(levels);
		
		for (Integer lvl : levels) {
			String accesible = (lvl <= nivelUsuario) ? "" : " 🔒";
			comboNiveles.addItem("Nivel " + lvl + " (" + counts.get(lvl) + ")" + accesible);
		}
		
		// Seleccionar por defecto "Todos los niveles"
		if (comboNiveles.getItemCount() >= 1) comboNiveles.setSelectedIndex(0);
	}

	private void applyFilter() {
		if (model == null) return;
		model.setRowCount(0);
		visibleData.clear();
		workoutSeleccionado = null;
		limpiarEjercicios();
		
		String sel = (String) comboNiveles.getSelectedItem();
		for (Workout w : workoutsData) {
			int nivel = w.getNivel();
			boolean include = false;
			
			if (sel == null || sel.startsWith("Todos los niveles")) {
				include = true; // Mostrar todos los niveles
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
				String nombreWorkout = w.getNombre();
				// Añadir candado visual a workouts no accesibles
				if (nivel > nivelUsuario) {
					nombreWorkout = "🔒 " + nombreWorkout;
				}
				model.addRow(new Object[] { nombreWorkout, w.getNumeroEjercicios(), nivel, "Ver Video" });
			}
		}
		if (model.getRowCount() == 0) model.addRow(new Object[] { "Sin resultados", "", "", "" });
	}

	private void mostrarEjerciciosDelWorkout() {
		modelEjercicios.setRowCount(0);
		btnIniciar.setEnabled(false);
		
		if (workoutSeleccionado == null) {
			modelEjercicios.addRow(new Object[] { "", "Selecciona un workout", "" });
			return;
		}

		ArrayList<Ejercicio> ejercicios = workoutSeleccionado.getEjercicios();
		
		boolean workoutBloqueado = workoutSeleccionado.getNivel() > nivelUsuario;
		
		if (ejercicios == null || ejercicios.isEmpty()) {
			modelEjercicios.addRow(new Object[] { "", "No hay ejercicios disponibles", "" });
		} else {
			
			for (int i = 0; i < ejercicios.size(); i++) {
				Ejercicio ej = ejercicios.get(i);
				String numero = workoutBloqueado ? "🔒" : String.valueOf(i + 1);
				String nombre = ej.getNombre() != null ? ej.getNombre() : "Sin nombre";
				String descripcion = ej.getDescripcion() != null ? ej.getDescripcion() : "Sin descripción";
				
				modelEjercicios.addRow(new Object[] { numero, nombre, descripcion });
			}
			
			if (!workoutBloqueado) {
				btnIniciar.setEnabled(true);
			}
		}
	}

	private void limpiarEjercicios() {
		modelEjercicios.setRowCount(0);
		modelEjercicios.addRow(new Object[] { "", "Selecciona un workout", "" });
		btnIniciar.setEnabled(false);
	}

	private void iniciarWorkout() {
		if (workoutSeleccionado == null) {
			JOptionPane.showMessageDialog(this, 
				"Por favor selecciona un workout primero.", 
				"Info", 
				JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if (workoutSeleccionado.getNivel() > nivelUsuario) {
			JOptionPane.showMessageDialog(this, 
				"Este workout está bloqueado.\n\n" +
				"Nivel requerido: " + workoutSeleccionado.getNivel() + "\n" +
				"Tu nivel actual: " + nivelUsuario + "\n\n" +
				"¡Sigue entrenando para desbloquearlo!", 
				"Workout Bloqueado", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (workoutSeleccionado.getEjercicios() == null || workoutSeleccionado.getEjercicios().isEmpty()) {
			JOptionPane.showMessageDialog(this, 
				"Este workout no tiene ejercicios disponibles.", 
				"Sin Ejercicios", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Vista.Ejercicio ventanaEjercicio = new Vista.Ejercicio(workoutSeleccionado, usuario);
		ventanaEjercicio.setVisible(true);
		this.dispose();
	}

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