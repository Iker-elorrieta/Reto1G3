package Vista;

import Controlador.FirebaseControlador;

public class Main {

	public static void main(String[] args) {
		try {
			FirebaseControlador.inicializarFirebase();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		Login frame = new Login();
		frame.setVisible(true);
	}

}

/*prueba1@prueba.com*/