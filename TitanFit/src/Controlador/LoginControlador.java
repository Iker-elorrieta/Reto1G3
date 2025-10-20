package Controlador;

import Modelo_Pojos.Usuario;

public class LoginControlador {

	public static Usuario loginUsuario(String email, String password) {
		return Modelo_Gestor.LoginGestor.loginUsuario(email, password);
	}
}
