package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class Historial extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Historial frame = new Historial();
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
    public Historial() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 665, 519);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        String[] columnas = {"Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Ejercicios Completados"};
        Object[][] datos = {
        };

       
        DefaultTableModel model = new DefaultTableModel(datos, columnas);

        
        table = new JTable(model);

       
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(35, 73, 566, 327);

       
        contentPane.add(scrollPane);

        JButton btnAtras = new JButton("Atras");
        btnAtras.setBounds(550, 435, 89, 23);
        contentPane.add(btnAtras);
    }
}
