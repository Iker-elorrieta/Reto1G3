package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Modelo_Pojos.Usuario;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;



public class Historial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
    private JTable table;

	public Historial(Usuario usuario) {
		setTitle("TitanFit — Historial");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);


		contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setBackground(new Color(245, 245, 239));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(245, 245, 239));
		contentPane.add(topPanel, BorderLayout.NORTH);

		String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Usuario";
		JLabel lblBienvenido = new JLabel("Historial de " + nombre);

		JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		topRight.setBackground(Color.WHITE);
		JButton btnMiPerfil = new JButton("Mi Perfil");
		btnMiPerfil.setFocusPainted(false);
		btnMiPerfil.setBackground(new Color(33, 150, 243));
		btnMiPerfil.setForeground(Color.WHITE);
		btnMiPerfil.setBorderPainted(false);
		btnMiPerfil.setPreferredSize(new Dimension(100, 28));

		btnMiPerfil.addActionListener(e -> {
			MiUsuario miUsuario = new MiUsuario(usuario);
			miUsuario.setVisible(true);
			Historial.this.dispose();
		});
		topRight.add(btnMiPerfil);
		topPanel.add(topRight, BorderLayout.EAST);

		// Panel principal con JSplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(450);
		splitPane.setEnabled(false);
		splitPane.setBackground(new Color(245, 245, 239));
		contentPane.add(splitPane, BorderLayout.CENTER);

		String[] columnas = {"Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Ejercicios Completados"};
        Object[][] datos = {
        };

       
        DefaultTableModel model = new DefaultTableModel(datos, columnas);

        
        table = new JTable(model);

       
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(35, 73, 566, 327);

       
        contentPane.add(scrollPane);


		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(Color.WHITE);
		JButton btnAtras = new JButton("Atras");
		btnAtras.setPreferredSize(new Dimension(100, 30));
		btnAtras.setFocusPainted(false);
		btnAtras.setBackground(Color.WHITE);
		btnAtras.setForeground(new Color(33, 150, 243));
		btnAtras.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
		btnAtras.addActionListener(e ->{
			Workouts main = new Workouts(usuario);
			main.setVisible(true);
			Historial.this.dispose();
			
		} 
				);
		bottomPanel.add(btnAtras, BorderLayout.WEST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		setSize(new Dimension(1000, 520));
		setLocationRelativeTo(null);

		
	}

	


	

}