package Controlador;

import java.util.List;

import Modelo_Gestor.EjercicioGestor;
import Vista.Ejercicio;

public class EjercicioControlador {

    private Ejercicio vista;
    private EjercicioGestor gestor;

    public EjercicioControlador(Ejercicio vista, java.util.ArrayList<Modelo_Pojos.Ejercicio> ejercicios) {
        this.vista = vista;
        this.gestor = new EjercicioGestor(ejercicios);
        this.gestor.setListener(new EjercicioGestor.Listener() {
            @Override
            public void onState(String state) {
                vista.setEstado(state);
            }

            @Override
            public void onWorkoutTime(int totalSeconds) {
                vista.setWorkoutTime(totalSeconds);
            }

            @Override
            public void onSeriesUpdate(int ejercicioIndex, int serieIndex, int remainingSeconds, List<Integer> originalTimes, boolean inDescanso) {
                vista.onSeriesUpdate(ejercicioIndex, serieIndex, remainingSeconds, originalTimes, inDescanso);
            }

            @Override
            public void onFinished() {
                vista.onFinished();
            }
        });
    }

    public void toggleStartPause(int ejercicioIndex, int serieIndex) {
        if (gestor.isRunning()) {
            gestor.pause();
        } else {
            gestor.setIndices(ejercicioIndex, serieIndex);
            gestor.start();
        }
    }

    public boolean isRunning() {
        return gestor.isRunning();
    }
}