package Modelo_Gestor;

import Modelo_Pojos.Workout;
import Modelo_Pojos.Ejercicio;
import Modelo_Pojos.Serie;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Controlador.FirebaseControlador;

import java.util.ArrayList;
import java.util.List;

public class WorkoutGestor {

	public static List<Workout> obtenerWorkouts() {
		List<Workout> lista = new ArrayList<>();
		try {
			Firestore db = FirestoreClient.getFirestore();

			CollectionReference workoutsCol = db.collection("Workouts");
			ApiFuture<QuerySnapshot> future = workoutsCol.get();
			QuerySnapshot workoutsSnap = future.get();

			for (QueryDocumentSnapshot workoutDoc : workoutsSnap.getDocuments()) {
				Workout w = new Workout();
                // store document id so callers can reference it later
                w.setId(workoutDoc.getId());
				try {
					w.setNivel(Integer.parseInt(workoutDoc.getId()));
				} catch (Exception ignore) {
					w.setNivel(0);
				}
				w.setNombre(workoutDoc.getString("nombre"));
				Long numeroEj = workoutDoc.getLong("numeroEjercicios");
				w.setNumeroEjercicios(numeroEj == null ? 0 : numeroEj.intValue());

				List<Ejercicio> ejercicios = new ArrayList<>();
				for (QueryDocumentSnapshot ejDoc : workoutDoc.getReference().collection("Ejercicios").get().get()
						.getDocuments()) {
					String ejNombre = ejDoc.getId();
					String descripcion = ejDoc.getString("descripcion");
					String foto = ejDoc.getString("foto");

					// Create the list once per ejercicio and add all series into it
					ArrayList<Serie> series = new ArrayList<>();
					for (QueryDocumentSnapshot serieDoc : ejDoc.getReference().collection("Series").get().get()
							.getDocuments()) {
						try {
							int serieId = Integer.parseInt(serieDoc.getId());
							Long descansoLong = serieDoc.getLong("descanso");
							Long tiempoLong = serieDoc.getLong("tiempo");
							int descanso = (descansoLong == null) ? 0 : descansoLong.intValue() / 1000;
							int tiempo = (tiempoLong == null) ? 0 : tiempoLong.intValue() / 1000;
							series.add(new Serie(serieId, descanso, tiempo));
						} catch (Exception ignore) {
						}
					}

					ejercicios.add(new Ejercicio(ejNombre, descripcion, foto, series));
				}
				w.setEjercicios(new ArrayList<Ejercicio>(ejercicios));

				String v = workoutDoc.getString("video");
				if (v == null && !ejercicios.isEmpty()) {
					v = ejercicios.get(0).getFoto();
				}
				w.setVideo(v);

				lista.add(w);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	/**
	 * Registrar en Firestore que el usuario ha completado un workout.
	 * Incrementa opcionalmente el nivel del usuario en 1 y añade un documento en Usuarios/{email}/Historico
	 * con los campos: id_workout (DocumentReference a Workouts/{id}), porcentaje, tiempo, fecha (now).
	 *
	 * @param userEmail email del usuario (document id en la collecion Usuarios)
	 * @param workoutId id del workout (document id en la collecion Workouts)
	 * @param porcentaje porcentaje completado (int)
	 * @param tiempoMillis tiempo total en milisegundos (long)
	 * @param incrementarNivel si es true, incrementa Usuarios/{email}.nivel en 1
	 * @return true si se completó la operación, false en caso de error
	 */
	public static boolean registrarHistorico(String userEmail, String workoutId, int porcentaje, long tiempoMillis, boolean incrementarNivel) {
		try {
			// ensure Firebase initialized
			try { FirebaseControlador.inicializarFirebase(); } catch (Throwable t) {}

			Firestore db = FirestoreClient.getFirestore();
			DocumentReference userRef = db.collection("Usuarios").document(userEmail);

			// Incrementar el nivel del usuario en 1 solamente si se solicita
			if (incrementarNivel) {
				ApiFuture<WriteResult> updateFuture = userRef.update("nivel", FieldValue.increment(1));
				updateFuture.get();
			}

			// Preparar referencia al workout
			DocumentReference workoutRef = db.collection("Workouts").document(workoutId);

			Map<String, Object> data = new HashMap<>();
			data.put("id_workout", workoutRef);
			data.put("porcentaje", porcentaje);
			data.put("tiempo", tiempoMillis);
			data.put("fecha", new Date());

			// Añadir historico bajo Usuarios/{email}/Historico
			ApiFuture<DocumentReference> addFuture = userRef.collection("Historico").add(data);
			addFuture.get();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}