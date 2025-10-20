// java
package Modelo_Gestor;

import org.mindrot.jbcrypt.BCrypt;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import Modelo_Pojos.Firebase;
import Modelo_Pojos.Usuario;

import java.util.HashMap;
import java.util.Map;

public class LoginGestor {

	public static Usuario loginUsuario(String email, String password) {
		try {
			try {
				new Firebase();
				Firebase.inicializarFirebase();
			} catch (Throwable t) {
			}

			Firestore db = FirestoreClient.getFirestore();
			DocumentReference docRef = db.collection("Usuarios").document(email);
			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();

			if (document.exists()) {
				// Try the normalized key first, fallback to legacy accented key
				String hashAlmacenado = document.getString("password");
				if (hashAlmacenado == null) {
					hashAlmacenado = document.getString("contraseña");
				}

				if (hashAlmacenado == null) {
					// no password stored for this user
					return null;
				}

				hashAlmacenado = hashAlmacenado.trim();

				if (BCrypt.checkpw(password, hashAlmacenado)) {
					Usuario usuario = new Usuario();
					usuario.setNombre(document.getString("nombre"));
					usuario.setEmail(document.getId());
					usuario.setApellidos(document.getString("apellidos"));
					// guard against missing 'nivel'
					if (document.contains("nivel") && document.getLong("nivel") != null) {
						usuario.setNivel(document.getLong("nivel").intValue());
					} else {
						usuario.setNivel(0);
					}
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
			try {
				new Firebase();
				Firebase.inicializarFirebase();
			} catch (Throwable t) {
			}

			Firestore db = FirestoreClient.getFirestore();
			DocumentReference docRef = db.collection("Usuarios").document(usuario.getEmail());
			String hashedPassword = Usuario.setPasswordConHash(passwordSinHash);
			usuario.setPassword(hashedPassword);

			Map<String, Object> data = new HashMap<>();
			data.put("nombre", usuario.getNombre());
			data.put("apellidos", usuario.getApellidos());
			data.put("fechaNacimiento", usuario.getFechaNacimiento());
			data.put("password", usuario.getPassword());
			data.put("nivel", usuario.getNivel());

			ApiFuture<WriteResult> result = docRef.set(data);
			result.get();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
