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

}