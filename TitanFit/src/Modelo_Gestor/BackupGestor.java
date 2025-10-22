package Modelo_Gestor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import Controlador.FirebaseControlador;
import Modelo_Pojos.Ejercicio;
import Modelo_Pojos.Usuario;
import Modelo_Pojos.Workout;

public class BackupGestor {

	static List<Usuario> listaUsuarios = new ArrayList<>();
	static List<Workout> listaWorkouts = new ArrayList<>();

	public static void recogerUsuarios() {
		System.out.println("Iniciando recogida de usuarios...");
		Firestore db = FirestoreClient.getFirestore();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("Usuarios").get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			
			System.out.println("Documentos de usuarios encontrados: " + documents.size());

			for (QueryDocumentSnapshot document : documents) {
				Usuario usuario = new Usuario();
				usuario.setNombre(document.getString("nombre"));
				usuario.setEmail(document.getId());
				usuario.setApellidos(document.getString("apellidos"));
				usuario.setPassword(document.getString("contraseña"));
				if (document.contains("nivel") && document.getLong("nivel") != null) {
					usuario.setNivel(document.getLong("nivel").intValue());
				} else {
					usuario.setNivel(0);
				}
				// Robust fechaNacimiento handling
				Object fechaObj = document.get("fechaNacimiento");
				if (fechaObj != null) {
					if (fechaObj instanceof com.google.cloud.Timestamp) {
						usuario.setFechaNacimiento(((com.google.cloud.Timestamp)fechaObj).toDate());
					} else if (fechaObj instanceof java.util.Date) {
						usuario.setFechaNacimiento((java.util.Date)fechaObj);
					} else if (fechaObj instanceof String) {
						try {
							java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
							usuario.setFechaNacimiento(sdf.parse((String)fechaObj));
						} catch (Exception ex) {
							usuario.setFechaNacimiento(null);
						}
					} else {
						usuario.setFechaNacimiento(null);
					}
				}
				listaUsuarios.add(usuario);
				System.out.println("Usuario agregado: " + usuario.getNombre());
			}
			
			System.out.println("Total usuarios recogidos: " + listaUsuarios.size());
		} catch (Exception e) {
			System.err.println("Error al recoger usuarios:");
			e.printStackTrace();
		}
	}

	public static void recogerWorkouts() {
		System.out.println("Iniciando recogida de workouts...");
		Firestore db = FirestoreClient.getFirestore();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("Workouts").get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			
			System.out.println("Documentos de workouts encontrados: " + documents.size());

			for (QueryDocumentSnapshot document : documents) {
				Workout workout = new Workout();

				workout.setNombre(document.getString("nombre"));
				if (document.contains("numeroEjercicios") && document.getLong("numeroEjercicios") != null) {
					workout.setNumeroEjercicios(document.getLong("numeroEjercicios").intValue());
				}
				if (document.contains("nivel") && document.getLong("nivel") != null) {
					workout.setNivel(document.getLong("nivel").intValue());
				}
				workout.setVideo(document.getString("video"));

				// Recoger ejercicios de cada workout
				ApiFuture<QuerySnapshot> ejerciciosFuture = document.getReference().collection("Ejercicios").get();
				List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosFuture.get().getDocuments();
				
				System.out.println("Ejercicios encontrados para workout '" + workout.getNombre() + "': " + ejerciciosDocs.size());

				for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
					Ejercicio ejercicio = new Ejercicio();
					ejercicio.setNombre(ejDoc.getId());
					ejercicio.setDescripcion(ejDoc.getString("descripcion"));
					ejercicio.setFoto(ejDoc.getString("foto"));
					if (ejDoc.contains("tiempo") && ejDoc.getLong("tiempo") != null) {
						ejercicio.setDuracion(ejDoc.getLong("tiempo").intValue() / 1000);
					}
					workout.getEjercicios().add(ejercicio);
				}

				listaWorkouts.add(workout);
				System.out.println("Workout agregado: " + workout.getNombre());
			}
			
			System.out.println("Total workouts recogidos: " + listaWorkouts.size());
		} catch (Exception e) {
			System.err.println("Error al recoger workouts:");
			e.printStackTrace();
		}
	}

	public static void realizarBackup() {
		try {
	        FirebaseControlador.inicializarFirebase();
	        recogerUsuarios();
	        recogerWorkouts();

	        FileOutputStream fos = new FileOutputStream("backup.dat");
	        ObjectOutputStream oos = new ObjectOutputStream(fos);

	        oos.writeObject(listaUsuarios);
	        oos.writeObject(listaWorkouts);

	        oos.close();
	        fos.close();

	        System.out.println("Backup binario creado con éxito: backup.dat");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}