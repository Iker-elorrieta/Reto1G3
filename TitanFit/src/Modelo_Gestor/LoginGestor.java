// java
package Modelo_Gestor;

import org.mindrot.jbcrypt.BCrypt;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;

import Controlador.FirebaseControlador;
import Modelo_Pojos.Firebase;
import Modelo_Pojos.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginGestor {

	public static Usuario loginUsuario(String email, String password) {
		try {
		
			if (Firebase.isConectado()) {
				try {
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
				} catch (ExecutionException ee) {
					// Marcar Firebase como desconectado
					Firebase.marcarDesconectado();
					System.out.println("Error de red detectado. Intentando con backup.dat...");
					return loginDesdeBackup(email, password);
				}
			}
			else {
				return loginDesdeBackup(email, password);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Marcar Firebase como desconectado y usar backup
			Firebase.marcarDesconectado();
			System.out.println("Error en login Firebase. Intentando con backup.dat...");
			return loginDesdeBackup(email, password);
		}
	}
	
	private static Usuario loginDesdeBackup(String email, String password) {
		try {
			File archivo = new File("backup.dat");

			if (!archivo.exists()) {
				System.out.println("Backup no encontrado");
				return null;
			}

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo));

			ArrayList<Usuario> lista = (ArrayList<Usuario>) ois.readObject();
			ois.close();

			for (Usuario u : lista) {
				if (u.getEmail().equals(email)) {

					String hashAlmacenado = u.getPassword().trim();

					if (BCrypt.checkpw(password, hashAlmacenado)) {
						System.out.println("Login exitoso desde backup.dat");
						return u;
					} else {
						return null;
					}
				}
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean registrarUsuario(Usuario usuario, String passwordSinHash) {
		try {
			try {
				FirebaseControlador.inicializarFirebase();
			} catch (Throwable t) {
			}

			Firestore db = FirestoreClient.getFirestore();
			DocumentReference docRef = db.collection("Usuarios").document(usuario.getEmail());

			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();
			if (document.exists()) {

				return false;
			}

			String hashedPassword = Usuario.setPasswordConHash(passwordSinHash);
			usuario.setPassword(hashedPassword);

			Map<String, Object> data = new HashMap<>();
			data.put("nombre", usuario.getNombre());
			data.put("apellidos", usuario.getApellidos());
			data.put("fechaNacimiento", usuario.getFechaNacimiento());
			data.put("contraseña", usuario.getPassword());
			data.put("nivel", usuario.getNivel());
			data.put("esTrainer", false);

			ApiFuture<WriteResult> result = docRef.set(data);
			result.get();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean actualizarUsuario(String originalEmail, Usuario usuario, String nuevaPasswordPlain) {
		try {
			try {
				FirebaseControlador.inicializarFirebase();
			} catch (Throwable t) {
			}

			Firestore db = FirestoreClient.getFirestore();

			DocumentReference originalRef = null;
			DocumentSnapshot originalDoc = null;
			if (originalEmail != null && !originalEmail.isEmpty()) {
				originalRef = db.collection("Usuarios").document(originalEmail);
				ApiFuture<DocumentSnapshot> f = originalRef.get();
				originalDoc = f.get();
			}

			Map<String, Object> data = new HashMap<>();
			data.put("nombre", usuario.getNombre());
			data.put("apellidos", usuario.getApellidos());
			data.put("fechaNacimiento", usuario.getFechaNacimiento());

			if (nuevaPasswordPlain != null && !nuevaPasswordPlain.isEmpty()) {
				String hashed = Usuario.setPasswordConHash(nuevaPasswordPlain);
				data.put("password", hashed);
			} else if (originalDoc != null && originalDoc.exists() && originalDoc.getString("password") != null) {
				data.put("password", originalDoc.getString("password"));
			} else if (usuario.getPassword() != null) {
				data.put("password", usuario.getPassword());
			}

			data.put("nivel", usuario.getNivel());

			if (originalDoc != null && originalDoc.exists() && originalDoc.get("esTrainer") != null) {
				data.put("esTrainer", originalDoc.get("esTrainer"));
			} else {
				data.put("esTrainer", false);
			}

			DocumentReference targetRef = db.collection("Usuarios").document(usuario.getEmail());
			if (originalEmail == null || originalEmail.isEmpty() || originalEmail.equals(usuario.getEmail())) {
				ApiFuture<WriteResult> write = targetRef.set(data, SetOptions.merge());
				write.get();
				return true;
			} else {
				ApiFuture<WriteResult> writeNew = targetRef.set(data);
				writeNew.get();
				if (originalRef != null) {
					ApiFuture<WriteResult> del = originalRef.delete();
					del.get();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}