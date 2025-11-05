package Vista;

import Controlador.FirebaseControlador;
import Controlador.ConexionMonitor;

public class Main {

	public static void main(String[] args) {
		try {
			FirebaseControlador.inicializarFirebase();
			System.out.println("Conectado a Firebase");
		} catch (Exception e) {
			System.out.println("No se pudo conectar a Firebase. Usando modo offline con backup.dat");
		}
		
		// Iniciar el monitor de conexión en segundo plano
		ConexionMonitor.iniciarMonitoreo();
		
		Login frame = new Login();
		frame.setVisible(true);
	}

}

/*prueba1@prueba.com*/