package Controlador;

import Modelo_Pojos.Workout;
import java.util.List;

public class WorkoutControlador {

	public static List<Workout> obtenerWorkouts() {
		return Modelo_Gestor.WorkoutGestor.obtenerWorkouts();
	}
}