package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Modelo_Pojos.Usuario;
import Modelo_Pojos.Workout;
import Modelo_Pojos.Serie;

public class Ejercicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Datos del workout
	private Workout workout;
	private Usuario usuario;
	private ArrayList<Modelo_Pojos.Ejercicio> ejercicios;
	private int ejercicioActualIndex = 0;
	private int serieActual = 0;
	
	// Componentes de interfaz
	private JLabel lblCronometroWorkout;
	private JLabel lblNombreEjercicio;
	private JLabel lblDescripcionEjercicio;
	private JLabel lblProgresoEjercicios;
	private JButton btnIniciar;
	private JButton btnSalir;
	private JLabel lblEstadoActual;
	private JPanel seriesPanel; // will show series of current exercise
	private List<JLabel> lblTiemposSeries; // labels for each serie time
	
	// Timer state
	private javax.swing.Timer timer;
	private int remainingSeconds = 0; // time left for current countdown (work or rest)
	private boolean inDescanso = false;
	
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
		
		JLabel lblDescansoLabel = new JLabel("Descanso:");
		lblDescansoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescansoLabel.setForeground(colorTexto);
		infoPanel.add(lblDescansoLabel);
		
		JLabel lblDescanso = new JLabel("00:00:00");
		lblDescanso.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblDescanso.setForeground(colorVerde);
		infoPanel.add(lblDescanso);
		
		topPanel.add(infoPanel, BorderLayout.CENTER);
		
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
		if (ejercicios != null && !ejercicios.isEmpty()) {
			nombre0 = ejercicios.get(0).getNombre() != null ? ejercicios.get(0).getNombre() : "Sin nombre";
			desc0 = ejercicios.get(0).getDescripcion() != null ? ejercicios.get(0).getDescripcion() : "";
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
		
		// Panel de series: show only series for the current exercise
		seriesPanel = new JPanel();
		seriesPanel.setBackground(Color.WHITE);
		seriesPanel.setLayout(new BoxLayout(seriesPanel, BoxLayout.Y_AXIS));
		middlePanel.add(seriesPanel, BorderLayout.CENTER);
		
		lblTiemposSeries = new ArrayList<>();
		
		// Initial render of series for exercise 0
		updateSeriesDisplay();
		
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
		btnIniciar.addActionListener(e -> onStartClicked());
		bottomPanel.add(btnIniciar);
		
		btnSalir = new JButton("Salir");
		btnSalir.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnSalir.setBackground(Color.WHITE);
		btnSalir.setForeground(colorRojo);
		btnSalir.setFocusPainted(false);
		btnSalir.setBorder(BorderFactory.createLineBorder(colorRojo, 2));
		btnSalir.setPreferredSize(new Dimension(180, 40));
		btnSalir.addActionListener(ev ->{
			Workouts workoutsFrame = new Workouts(usuario);
			workoutsFrame.setVisible(true);
			dispose();
		} );
		bottomPanel.add(btnSalir);
		
		setLocationRelativeTo(null);
	}

	private void updateSeriesDisplay() {
		seriesPanel.removeAll();
		lblTiemposSeries.clear();
		
		if (ejercicios == null || ejercicios.isEmpty() || ejercicioActualIndex >= ejercicios.size()) {
			JPanel panelVacio = new JPanel();
			panelVacio.setBackground(new Color(250,250,250));
			panelVacio.add(new JLabel("No hay ejercicios"));
			seriesPanel.add(panelVacio);
			seriesPanel.revalidate();
			seriesPanel.repaint();
			return;
		}
		
		Modelo_Pojos.Ejercicio ej = ejercicios.get(ejercicioActualIndex);
		lblNombreEjercicio.setText(ej.getNombre() != null ? ej.getNombre() : "Sin nombre");
		lblDescripcionEjercicio.setText("<html><center>" + (ej.getDescripcion() != null ? ej.getDescripcion() : "") + "</center></html>");
		lblProgresoEjercicios.setText("Ejercicio " + (ejercicioActualIndex + 1) + " de " + ejercicios.size());
		
		ArrayList<Serie> series = ej.getSeries();
		if (series == null || series.isEmpty()) {
			JPanel panelVacio = new JPanel();
			panelVacio.setBackground(new Color(250,250,250));
			panelVacio.add(new JLabel("No hay series para este ejercicio"));
			seriesPanel.add(panelVacio);
		} else {
			for (int i = 0; i < series.size(); i++) {
				Serie s = series.get(i);
				JPanel panelSerie = new JPanel();
				panelSerie.setLayout(new BoxLayout(panelSerie, BoxLayout.Y_AXIS));
				panelSerie.setBackground(new Color(250, 250, 250));
				panelSerie.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
					new EmptyBorder(10, 10, 10, 10)
				));
				
				JLabel lblNombre = new JLabel("Serie " + (i+1));
				lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
				lblNombre.setForeground(colorPrimario);
				lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelSerie.add(lblNombre);
				panelSerie.add(Box.createRigidArea(new Dimension(0,8)));
				
				int durSec = s.getTiempo();
				JLabel lblTiempo = new JLabel(formatTime(durSec));
				lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 24));
				lblTiempo.setForeground(colorTexto);
				lblTiempo.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelSerie.add(lblTiempo);
				
				JLabel lblDesc = new JLabel("Descanso: " + formatTime(s.getDescanso()));
				lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
				lblDesc.setForeground(colorTexto);
				lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
				panelSerie.add(lblDesc);
				
				// highlight current serie
				if (i == serieActual) {
					panelSerie.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(colorNaranja, 2),
						new EmptyBorder(10, 10, 10, 10)
					));
				}
				
				seriesPanel.add(panelSerie);
				lblTiemposSeries.add(lblTiempo);
			}
		}
		seriesPanel.revalidate();
		seriesPanel.repaint();
	}
	
	private void onStartClicked() {
		// start or toggle the timer
		if (ejercicios == null || ejercicios.isEmpty()) return;
		Modelo_Pojos.Ejercicio ej = ejercicios.get(ejercicioActualIndex);
		ArrayList<Serie> series = ej.getSeries();
		if (series == null || series.isEmpty()) return;
		
		if (timer == null) {
			// initialize countdown with current serie time
			remainingSeconds = series.get(serieActual).getTiempo();
			inDescanso = false;
			timer = new javax.swing.Timer(1000, ev -> timerTick());
			timer.start();
			btnIniciar.setText("Pausar");
			lblEstadoActual.setText("Serie " + (serieActual+1) + " — En progreso");
		} else if (timer.isRunning()) {
			timer.stop();
			btnIniciar.setText("Continuar");
			lblEstadoActual.setText("Pausado");
		} else {
			timer.start();
			btnIniciar.setText("Pausar");
		}
	}
	
	private void timerTick() {
		// decrement remainingSeconds and update label
		if (ejercicios == null || ejercicios.isEmpty()) return;
		Modelo_Pojos.Ejercicio ej = ejercicios.get(ejercicioActualIndex);
		ArrayList<Serie> series = ej.getSeries();
		if (series == null || series.isEmpty()) return;
		
		if (remainingSeconds > 0) {
			remainingSeconds--;
			lblEstadoActual.setText((inDescanso ? "Descanso" : "Serie "+(serieActual+1)) + " — " + formatTime(remainingSeconds));
		} else {
			// finished current countdown
			if (!inDescanso) {
				// finished active serie, start descanso for this serie
				int descanso = series.get(serieActual).getDescanso();
				if (descanso > 0) {
					inDescanso = true;
					remainingSeconds = descanso;
					lblEstadoActual.setText("Descanso — " + formatTime(remainingSeconds));
				} else {
					// no descanso, move to next serie immediately
					advanceSerieOrExercise();
				}
			} else {
				// finished descanso, move to next serie
				inDescanso = false;
				advanceSerieOrExercise();
			}
		}
		// update the time label for the current serie if visible
		if (!lblTiemposSeries.isEmpty() && serieActual < lblTiemposSeries.size()) {
			// if inDescanso show remaining descanso in the serie label as well
			lblTiemposSeries.get(serieActual).setText(formatTime(inDescanso ? remainingSeconds : Math.max(0, series.get(serieActual).getTiempo() - (series.get(serieActual).getTiempo() - remainingSeconds))));
		}
	}
	
	private void advanceSerieOrExercise() {
		Modelo_Pojos.Ejercicio ej = ejercicios.get(ejercicioActualIndex);
		ArrayList<Serie> series = ej.getSeries();
		serieActual++;
		if (serieActual >= series.size()) {
			// finished this exercise, move to next exercise
			ejercicioActualIndex++;
			if (ejercicioActualIndex >= ejercicios.size()) {
				// finished workout
				timer.stop();
				btnIniciar.setText("Iniciar");
				lblEstadoActual.setText("Entrenamiento completado");
				return;
			} else {
				// switch to next exercise
				serieActual = 0;
				updateSeriesDisplay();
				ArrayList<Serie> nextSeries = ejercicios.get(ejercicioActualIndex).getSeries();
				remainingSeconds = nextSeries.get(0).getTiempo();
				lblEstadoActual.setText("Cambiando a siguiente ejercicio");
				return;
			}
		} else {
			// start next serie immediately
			remainingSeconds = series.get(serieActual).getTiempo();
			lblEstadoActual.setText("Serie " + (serieActual+1) + " — En progreso");
			updateSeriesDisplay();
		}
		updateSeriesDisplay();
	}
	
	private String formatTime(int segundos) {
		int mins = segundos / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d", mins, secs);
	}
}