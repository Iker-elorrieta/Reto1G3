package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Modelo_Pojos.Usuario;
import Modelo_Pojos.Workout;
import Modelo_Pojos.Serie;
import Controlador.EjercicioControlador;

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
    private JLabel lblDescanso; // moved to field so threads can update it
	private JPanel seriesPanel; // will show series of current exercise
	private List<JLabel> lblTiemposSeries; // labels for each serie time
    private List<Integer> tiemposSeriesOriginales; // store original tiempo for cada serie

    // Colors (simple palette)
    private Color colorFondo = new Color(245, 245, 239);
    private Color colorPrimario = new Color(33, 150, 243);
    private Color colorVerde = new Color(76, 175, 80);
    private Color colorNaranja = new Color(255, 152, 0);
    private Color colorRojo = new Color(244, 67, 54);
    private Color colorTexto = new Color(60, 60, 60);
    private Color colorAmarillo = new Color(255, 204, 0);

    // Controller (MVC)
    private EjercicioControlador controlador;

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
		
		// Descanso label (shows descanso countdown)
		lblDescanso = new JLabel("00:00:00");
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
		middlePanel.add(seriesPanel, BorderLayout.CENTER);
		
		lblTiemposSeries = new ArrayList<>();
		tiemposSeriesOriginales = new ArrayList<>();

		// create controller and wire to view (controller will attach gestor listener)
		controlador = new EjercicioControlador(this, ejercicios);

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
		// Simple action: delegate to controller
		btnIniciar.addActionListener(e -> {
			controlador.toggleStartPause(ejercicioActualIndex, serieActual);
		});
		btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnIniciar.setBackground(colorVerde);
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setFocusPainted(false);
		btnIniciar.setBorderPainted(false);
		btnIniciar.setPreferredSize(new Dimension(180, 40));
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
         tiemposSeriesOriginales.clear();
        
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
            // update descanso top label to zero when no series
            if (lblDescanso != null) lblDescanso.setText("00:00");
         } else {
            // Layout as a grid of squares; number of columns adapts to number of series
            int cols = Math.min(4, Math.max(1, series.size()));
            seriesPanel.setLayout(new GridLayout(0, cols, 12, 12));
            for (int i = 0; i < series.size(); i++) {
                Serie s = series.get(i);

                // Square panel representing a serie: shows id (number) and tiempo
                JPanel square = new JPanel(new BorderLayout());
                square.setBackground(new Color(250, 250, 250));
                square.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    new EmptyBorder(10, 10, 10, 10)
                ));
                square.setPreferredSize(new Dimension(140, 120));

                JLabel lblId = new JLabel("#" + (i + 1), SwingConstants.CENTER);
                lblId.setFont(new Font("SansSerif", Font.BOLD, 18));
                lblId.setForeground(colorPrimario);
                square.add(lblId, BorderLayout.NORTH);

                JLabel lblTiempo = new JLabel(formatTime(s.getTiempo()), SwingConstants.CENTER);
                lblTiempo.setFont(new Font("SansSerif", Font.BOLD, 22));
                lblTiempo.setForeground(colorTexto);
                square.add(lblTiempo, BorderLayout.CENTER);

                JLabel lblDesc = new JLabel("Desc: " + formatTime(s.getDescanso()), SwingConstants.CENTER);
                lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
                lblDesc.setForeground(colorTexto);
                square.add(lblDesc, BorderLayout.SOUTH);

                // highlight current serie
                if (i == serieActual) {
                    // active serie: yellow border
                    square.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorAmarillo, 2),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }

                seriesPanel.add(square);
                lblTiemposSeries.add(lblTiempo);
                tiemposSeriesOriginales.add(s.getTiempo());
            }
        }
        // update the top descanso label to reflect current serie's descanso
        int descansoInicial = 0;
        if (serieActual < series.size()) descansoInicial = series.get(serieActual).getDescanso();
        if (lblDescanso != null) lblDescanso.setText(formatTime(descansoInicial));
         seriesPanel.revalidate();
         seriesPanel.repaint();
     }
	
	
	private String formatTime(int segundos) {
		int mins = segundos / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d", mins, secs);
	}
	
	private String formatTimeHMS(int segundos) {
		int hours = segundos / 3600;
		int mins = (segundos % 3600) / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d:%02d", hours, mins, secs);
	}

	// --- Methods called by the controller (keep simple) ---
	public void setEstado(String estado) {
		SwingUtilities.invokeLater(() -> {
			lblEstadoActual.setText(estado);
			// also adjust button text for basic UX
			if ("Pausado".equals(estado)) btnIniciar.setText("Continuar");
			else if ("Realizando ejercicio".equals(estado)) btnIniciar.setText("Pausar");
			else if ("Presiona Iniciar para comenzar".equals(estado) || "Entrenamiento completado".equals(estado)) btnIniciar.setText("Iniciar");
		});
	}

	public void setWorkoutTime(int totalSeconds) {
		SwingUtilities.invokeLater(() -> lblCronometroWorkout.setText(formatTimeHMS(totalSeconds)));
	}

	public void onSeriesUpdate(int ejercicioIndex, int serieIndex, int remainingSeconds, List<Integer> originalTimes, boolean inDescanso) {
		SwingUtilities.invokeLater(() -> {
			if (ejercicioIndex != ejercicioActualIndex) {
				ejercicioActualIndex = ejercicioIndex;
				serieActual = serieIndex;
				updateSeriesDisplay();
			} else {
				serieActual = serieIndex;
				// re-render so the active border moves to the current serie
				updateSeriesDisplay();
			}

			// update series times
			if (!lblTiemposSeries.isEmpty()) {
				for (int i = 0; i < lblTiemposSeries.size(); i++) {
					if (i == serieIndex && !inDescanso) lblTiemposSeries.get(i).setText(formatTime(remainingSeconds));
					else {
						int t = 0;
						if (i < originalTimes.size()) t = originalTimes.get(i);
						lblTiemposSeries.get(i).setText(formatTime(t));
					}
				}
			}

			int descansoToShow = 0;
			if (inDescanso) descansoToShow = remainingSeconds;
			else {
				if (ejercicioActualIndex < ejercicios.size()) {
					ArrayList<Serie> sList = ejercicios.get(ejercicioActualIndex).getSeries();
					if (sList != null && serieActual < sList.size()) descansoToShow = sList.get(serieActual).getDescanso();
				}
			}
			if (lblDescanso != null) lblDescanso.setText(formatTime(descansoToShow));
		});
	}

	public void onFinished() {
		SwingUtilities.invokeLater(() -> {
			btnIniciar.setText("Iniciar");
			lblEstadoActual.setText("Entrenamiento completado");
		});
	}
}