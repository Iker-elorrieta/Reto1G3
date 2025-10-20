package Modelo_Gestor;

import org.mindrot.jbcrypt.BCrypt;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import Modelo_Pojos.Usuario;

public class LoginGestor {

	public static Usuario loginUsuario(String email, String password) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			DocumentReference docRef = db.collection("Usuarios").document(email);
			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();
			if (document.exists()) {
				String hashAlmacenado = document.getString("contraseña").trim();
				if (hashAlmacenado != null && BCrypt.checkpw(password, hashAlmacenado)) {
					Usuario usuario = new Usuario();
					usuario.setNombre(document.getString("nombre"));
					usuario.setEmail(document.getId());
					usuario.setApellidos(document.getString("apellidos"));
					usuario.setNivel(document.getLong("nivel").intValue());
					usuario.setFechaNacimiento(document.getDate("fechaNacimiento"));
					usuario.setPassword(hashAlmacenado);
					return usuario;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean registrarUsuario(Usuario usuario, String passwordSinHash) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			DocumentReference docRef = db.collection("Usuarios").document(usuario.getEmail());
			String hashedPassword = Usuario.setPasswordConHash(passwordSinHash);
			usuario.setPassword(hashedPassword);
			ApiFuture<WriteResult> result = docRef.set(usuario);
			result.get();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
