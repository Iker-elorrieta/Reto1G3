package Vista;

import Modelo_Pojos.Firebase;

public class Main {

	public static void main(String[] args) {
		try {
		Firebase firebase = new Firebase();
		firebase.inicializarFirebase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Login frame = new Login();
		frame.setVisible(true);
	}

}
