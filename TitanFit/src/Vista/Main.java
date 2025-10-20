package Vista;

import Modelo_Pojos.Firebase;

public class Main {

	public static void main(String[] args) {
		try {
			Firebase.inicializarFirebase();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		Login frame = new Login();
		frame.setVisible(true);
	}

}
