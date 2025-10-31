package Controlador;

import Modelo_Pojos.Workout;
import java.util.List;

public class WorkoutControlador {

	public static List<Workout> obtenerWorkouts() {
		return Modelo_Gestor.WorkoutGestor.obtenerWorkouts();
	}

	public static boolean registrarHistorico(String userEmail, String workoutId, int porcentaje, long tiempoMillis, boolean incrementarNivel) {
		return Modelo_Gestor.WorkoutGestor.registrarHistorico(userEmail, workoutId, porcentaje, tiempoMillis, incrementarNivel);
	}
}