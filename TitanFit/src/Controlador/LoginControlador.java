package Controlador;

import Modelo_Pojos.Usuario;

public class LoginControlador {
	

	public static Usuario loginUsuario(String email, String password) {
		return Modelo_Gestor.LoginGestor.loginUsuario(email, password);
	}
	
	public static boolean registrarUsuario(Usuario usuario, String password) {
		return Modelo_Gestor.LoginGestor.registrarUsuario(usuario, password);
	}

	public static boolean actualizarUsuario(String originalEmail, Usuario usuario, String nuevaPasswordPlain) {
		return Modelo_Gestor.LoginGestor.actualizarUsuario(originalEmail, usuario, nuevaPasswordPlain);
	}
}