package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

import Modelo_Pojos.Usuario;
import Modelo_Pojos.Workout;

public class Ejercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Datos del workout
	private Workout workout;
	private Usuario usuario;
	private ArrayList<Modelo_Pojos.Ejercicio> ejercicios;
	private int ejercicioActualIndex = 0;
	private int serieActual = 0;
	private int totalSeries = 0; // dinámico: igual al número de ejercicios del workout
	
	// Componentes de interfaz
	private JLabel lblCronometroWorkout;
	private JLabel lblNombreEjercicio;
	private JLabel lblDescripcionEjercicio;
	private JLabel lblProgresoEjercicios;
	private JButton btnIniciar;
	private JButton btnSalir;
	private JLabel[] lblTiemposSeries;
	private JLabel lblEstadoActual;
	
	// Colores - Usando el mismo esquema de Workouts
	private Color colorFondo = new Color(245, 245, 239);
	private Color colorPrimario = new Color(33, 150, 243);
	private Color colorVerde = new Color(76, 175, 80);
	private Color colorNaranja = new Color(255, 152, 0);
	private Color colorRojo = new Color(244, 67, 54);
	private Color colorTexto = new Color(60, 60, 60);

	/**
	 * Constructor - Crea la interfaz visual
	 */
	public Ejercicio(Workout workout, Usuario usuario) {
		this.workout = workout;
		this.usuario = usuario;
		this.ejercicios = workout.getEjercicios();

		// Determinar cuántas "series" mostrar: una por ejercicio
		this.totalSeries = (this.ejercicios == null) ? 0 : this.ejercicios.size();
		
		setTitle("TitanFit — Entrenamiento en Progreso");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1000, 600);
		
		contentPane = new JPanel();
		contentPane.setBackground(colorFondo);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));
		
		// Panel superior
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(colorFondo);
		contentPane.add(topPanel, BorderLayout.NORTH);
		
		// Información del workout y cronómetro
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		infoPanel.setBackground(colorFondo);
		
		String workoutNombre = workout != null && workout.getNombre() != null ? workout.getNombre() : "-";
		JLabel lblWorkoutNombre = new JLabel("Workout: " + workoutNombre);
		lblWorkoutNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblWorkoutNombre.setForeground(colorPrimario);
		infoPanel.add(lblWorkoutNombre);
		
		JLabel lblSeparador1 = new JLabel("|");
		lblSeparador1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblSeparador1.setForeground(new Color(180, 180, 180));
		infoPanel.add(lblSeparador1);
		
		int totalEjercicios = (ejercicios == null) ? 0 : ejercicios.size();
		lblProgresoEjercicios = new JLabel("Ejercicio " + (ejercicioActualIndex + 1) + " de " + totalEjercicios);
		lblProgresoEjercicios.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblProgresoEjercicios.setForeground(colorTexto);
		infoPanel.add(lblProgresoEjercicios);
		
		JLabel lblSeparador2 = new JLabel("|");
		lblSeparador2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblSeparador2.setForeground(new Color(180, 180, 180));
		infoPanel.add(lblSeparador2);
		
		JLabel lblCronoLabel = new JLabel("Tiempo Total:");
		lblCronoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCronoLabel.setForeground(colorTexto);
		infoPanel.add(lblCronoLabel);
		
		lblCronometroWorkout = new JLabel("00:00:00");
		lblCronometroWorkout.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblCronometroWorkout.setForeground(colorVerde);
		infoPanel.add(lblCronometroWorkout);
		
		JLabel lblSeparador3 = new JLabel("|");
		lblSeparador3.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblSeparador3.setForeground(new Color(180, 180, 180));
		infoPanel.add(lblSeparador3);
		
		JLabel lblDescanso = new JLabel("Descanso:");
		lblDescanso.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescanso.setForeground(colorTexto);
		infoPanel.add(lblDescanso);
		
		lblDescanso = new JLabel("00:00:00");
		lblDescanso.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblDescanso.setForeground(colorVerde);
		infoPanel.add(lblDescanso);
		
		topPanel.add(infoPanel, BorderLayout.CENTER);
		
		// Panel central - Información del ejercicio actual
		JPanel centralPanel = new JPanel(new BorderLayout(10, 10));
		centralPanel.setBackground(Color.WHITE);
		centralPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
			new EmptyBorder(20, 20, 20, 20)
		));
		contentPane.add(centralPanel, BorderLayout.CENTER);
		
		// Nombre del ejercicio
		String nombre0 = "Sin ejercicios";
		String desc0 = "";
		int dur0 = 0;
		if (ejercicios != null && !ejercicios.isEmpty()) {
			nombre0 = ejercicios.get(0).getNombre() != null ? ejercicios.get(0).getNombre() : "Sin nombre";
			desc0 = ejercicios.get(0).getDescripcion() != null ? ejercicios.get(0).getDescripcion() : "";
			dur0 = ejercicios.get(0).getDuracion();
		}
		lblNombreEjercicio = new JLabel(nombre0);
		lblNombreEjercicio.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblNombreEjercicio.setForeground(colorPrimario);
		lblNombreEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
		centralPanel.add(lblNombreEjercicio, BorderLayout.NORTH);
		
		// Panel del medio con descripción y series
		JPanel middlePanel = new JPanel(new BorderLayout(10, 10));
		middlePanel.setBackground(Color.WHITE);
		centralPanel.add(middlePanel, BorderLayout.CENTER);
		
		// Descripción del ejercicio
		lblDescripcionEjercicio = new JLabel("<html><center>" + desc0 + "</center></html>");
		lblDescripcionEjercicio.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcionEjercicio.setForeground(colorTexto);
		lblDescripcionEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescripcionEjercicio.setBorder(new EmptyBorder(10, 0, 20, 0));
		middlePanel.add(lblDescripcionEjercicio, BorderLayout.NORTH);
		
		// Panel de series (una columna por ejercicio)
		JPanel seriesPanel = new JPanel(new GridLayout(1, Math.max(1, totalSeries), 15, 0));
		seriesPanel.setBackground(Color.WHITE);
		middlePanel.add(seriesPanel, BorderLayout.CENTER);
		
		lblTiemposSeries = new JLabel[Math.max(1, totalSeries)];
		
		if (totalSeries == 0) {
			// placeholder cuando no hay ejercicios
			JPanel panelVacio = new JPanel();
			panelVacio.setBackground(new Color(250,250,250));
			panelVacio.add(new JLabel("No hay ejercicios"));
			seriesPanel.add(panelVacio);
		} else {
			for (int i = 0; i < totalSeries; i++) {
				Modelo_Pojos.Ejercicio ej = ejercicios.get(i);
				JPanel panelSerie = new JPanel();
				panelSerie.setLayout(new BoxLayout(panelSerie, BoxLayout.Y_AXIS));
				panelSerie.setBackground(new Color(250, 250, 250));
				panelSerie.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
					new EmptyBorder(15, 15, 15, 15)
				));
				
				// Obtener la foto para este ejercicio
				String fotoPath = ej != null ? ej.getFoto() : null;
				JLabel lblImagen = new JLabel();
				lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
				lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
				lblImagen.setPreferredSize(new Dimension(100, 100));
				if (fotoPath != null && !fotoPath.isEmpty()) {
					try {
						ImageIcon icon;
						if (fotoPath.startsWith("http")) {
							icon = new ImageIcon(new java.net.URL(fotoPath));
						} else {
							icon = new ImageIcon(fotoPath);
						}
						Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
						lblImagen.setIcon(new ImageIcon(img));
					} catch (Exception e) {
						lblImagen.setText("Sin imagen");
					}
				} else {
					lblImagen.setText("Sin imagen");
				}
				panelSerie.add(lblImagen);
				panelSerie.add(Box.createRigidArea(new Dimension(0, 10)));
				
				// Mostrar el nombre del ejercicio en lugar de "Serie X"
				String nombreEj = ej != null && ej.getNombre() != null ? ej.getNombre() : ("Ejercicio " + (i+1));
				JLabel lblSerieNum = new JLabel(nombreEj);
				lblSerieNum.setFont(new Font("SansSerif", Font.BOLD, 16));
				lblSerieNum.setForeground(colorPrimario);
				lblSerieNum.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelSerie.add(lblSerieNum);
				
				panelSerie.add(Box.createRigidArea(new Dimension(0, 10)));
				
				int durationSec = ej != null ? ej.getDuracion() : 0; // usar la duración real del ejercicio (en segundos)
				lblTiemposSeries[i] = new JLabel(formatTime(durationSec));
				lblTiemposSeries[i].setFont(new Font("SansSerif", Font.BOLD, 28));
				lblTiemposSeries[i].setForeground(colorTexto);
				lblTiemposSeries[i].setAlignmentX(Component.CENTER_ALIGNMENT);
				panelSerie.add(lblTiemposSeries[i]);
				
				seriesPanel.add(panelSerie);
			}
		}
		
		// Estado actual (cuenta regresiva, en serie, descanso, etc.)
		lblEstadoActual = new JLabel("Presiona Iniciar para comenzar");
		lblEstadoActual.setFont(new Font("SansSerif", Font.ITALIC, 14));
		lblEstadoActual.setForeground(new Color(120, 120, 120));
		lblEstadoActual.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstadoActual.setBorder(new EmptyBorder(15, 0, 0, 0));
		middlePanel.add(lblEstadoActual, BorderLayout.SOUTH);
		
		// Panel inferior - Botones
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		bottomPanel.setBackground(colorFondo);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnIniciar.setBackground(colorVerde);
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setFocusPainted(false);
		btnIniciar.setBorderPainted(false);
		btnIniciar.setPreferredSize(new Dimension(180, 40));
		// Sin funcionalidad por ahora
		bottomPanel.add(btnIniciar);
		
		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnSalir.setBackground(Color.WHITE);
		btnSalir.setForeground(colorRojo);
		btnSalir.setFocusPainted(false);
		btnSalir.setBorder(BorderFactory.createLineBorder(colorRojo, 2));
		btnSalir.setPreferredSize(new Dimension(180, 40));
		btnSalir.addActionListener(e ->{
			Workouts workoutsFrame = new Workouts(usuario);
			workoutsFrame.setVisible(true);
			dispose();
		} );
		bottomPanel.add(btnSalir);
		
		setLocationRelativeTo(null);
	}

	private String formatTime(int segundos) {
		int mins = segundos / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d", mins, secs);
	}
}