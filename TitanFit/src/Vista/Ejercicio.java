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
	private int totalSeries = 3; // Por defecto 3 series por ejercicio
	
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
		
		JLabel lblWorkoutNombre = new JLabel("Workout: " + workout.getNombre());
		lblWorkoutNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblWorkoutNombre.setForeground(colorPrimario);
		infoPanel.add(lblWorkoutNombre);
		
		JLabel lblSeparador1 = new JLabel("|");
		lblSeparador1.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblSeparador1.setForeground(new Color(180, 180, 180));
		infoPanel.add(lblSeparador1);
		
		lblProgresoEjercicios = new JLabel("Ejercicio 1 de " + ejercicios.size());
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
		lblNombreEjercicio = new JLabel(ejercicios.get(0).getNombre());
		lblNombreEjercicio.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblNombreEjercicio.setForeground(colorPrimario);
		lblNombreEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
		centralPanel.add(lblNombreEjercicio, BorderLayout.NORTH);
		
		// Panel del medio con descripción y series
		JPanel middlePanel = new JPanel(new BorderLayout(10, 10));
		middlePanel.setBackground(Color.WHITE);
		centralPanel.add(middlePanel, BorderLayout.CENTER);
		
		// Descripción del ejercicio
		lblDescripcionEjercicio = new JLabel("<html><center>" + ejercicios.get(0).getDescripcion() + "</center></html>");
		lblDescripcionEjercicio.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcionEjercicio.setForeground(colorTexto);
		lblDescripcionEjercicio.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescripcionEjercicio.setBorder(new EmptyBorder(10, 0, 20, 0));
		middlePanel.add(lblDescripcionEjercicio, BorderLayout.NORTH);
		
		// Panel de series
		JPanel seriesPanel = new JPanel(new GridLayout(1, 3, 15, 0));
		seriesPanel.setBackground(Color.WHITE);
		middlePanel.add(seriesPanel, BorderLayout.CENTER);
		
		lblTiemposSeries = new JLabel[totalSeries];
		
		for (int i = 0; i < totalSeries; i++) {
			JPanel panelSerie = new JPanel();
			panelSerie.setLayout(new BoxLayout(panelSerie, BoxLayout.Y_AXIS));
			panelSerie.setBackground(new Color(250, 250, 250));
			panelSerie.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
				new EmptyBorder(15, 15, 15, 15)
			));
			
			// Añadir imagen del ejercicio encima del número de serie
			String imagenPath = ejercicios.get(ejercicioActualIndex).getFoto();
			JLabel lblImagen = new JLabel();
			lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
			lblImagen.setPreferredSize(new Dimension(100, 100));
			if (imagenPath != null && !imagenPath.isEmpty()) {
				try {
					ImageIcon icon = new ImageIcon(imagenPath);
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
			
			JLabel lblSerieNum = new JLabel("Serie " + (i + 1));
			lblSerieNum.setFont(new Font("SansSerif", Font.BOLD, 16));
			lblSerieNum.setForeground(colorPrimario);
			lblSerieNum.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelSerie.add(lblSerieNum);
			
			panelSerie.add(Box.createRigidArea(new Dimension(0, 10)));
			
			lblTiemposSeries[i] = new JLabel("00:30");
			lblTiemposSeries[i].setFont(new Font("SansSerif", Font.BOLD, 28));
			lblTiemposSeries[i].setForeground(colorTexto);
			lblTiemposSeries[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			panelSerie.add(lblTiemposSeries[i]);
			
			seriesPanel.add(panelSerie);
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
}