package Modelo_Gestor;

import java.util.ArrayList;
import java.util.List;

import Modelo_Pojos.Ejercicio;
import Modelo_Pojos.Serie;

public class EjercicioGestor {

    public interface Listener {
        void onState(String state); // simple states: "Presiona Iniciar...", "Pausado", "Realizando ejercicio"
        void onWorkoutTime(int totalSeconds); // hh:mm:ss counter seconds
        void onSeriesUpdate(int ejercicioIndex, int serieIndex, int remainingSeconds, List<Integer> originalTimes, boolean inDescanso);
        void onFinished();
    }

    private ArrayList<Ejercicio> ejercicios;
    private Listener listener;

    // state
    private volatile boolean running = false;
    private Thread countdownThread;

    private int ejercicioIndex = 0;
    private int serieIndex = 0;
    private int remainingSeconds = 0;
    private boolean inDescanso = false;
    private int totalActiveSeconds = 0;

    public EjercicioGestor(ArrayList<Modelo_Pojos.Ejercicio> ejercicios) {
        this.ejercicios = ejercicios != null ? ejercicios : new ArrayList<>();
    }

    public void setListener(Listener l) {
        this.listener = l;
    }

    public boolean isRunning() {
        return running;
    }

    public void setIndices(int ejercicioIndex, int serieIndex) {
        this.ejercicioIndex = ejercicioIndex;
        this.serieIndex = serieIndex;
        if (ejercicioIndex < ejercicios.size()) {
            ArrayList<Serie> s = ejercicios.get(ejercicioIndex).getSeries();
            if (s != null && serieIndex < s.size()) {
                remainingSeconds = s.get(serieIndex).getTiempo();
            }
        }
    }

    // getters to expose progress/time so caller can persist historico
    public int getEjercicioIndex() {
        return ejercicioIndex;
    }

    public int getSerieIndex() {
        return serieIndex;
    }

    public int getTotalActiveSeconds() {
        return totalActiveSeconds;
    }

    public int getTotalEjercicios() {
        return ejercicios != null ? ejercicios.size() : 0;
    }

    public int getCompletedExercisesPercentage() {
        int total = getTotalEjercicios();
        if (total == 0) return 0;
        int completed = Math.min(ejercicioIndex, total);
        // integer division gives floor: e.g. 2*100/3 == 66
        int pct = (completed * 100) / total;
        if (pct > 100) pct = 100;
        return pct;
    }

    public void start() {
        if (running || ejercicios.isEmpty()) return;
        running = true;
        if (listener != null) listener.onState("Realizando ejercicio");

        // single countdown thread that also updates the workout timer
        countdownThread = new Thread(() -> {
            try {
                ensureRemainingInitialized();
                while (running) {
                    Thread.sleep(1000);
                    if (!running) break;

                    // increment workout chrono while running
                    totalActiveSeconds++;
                    if (listener != null) listener.onWorkoutTime(totalActiveSeconds);

                    if (remainingSeconds > 0) {
                        remainingSeconds--;
                    } else {
                        // finished current period
                        ArrayList<Serie> series = ejercicios.get(ejercicioIndex).getSeries();
                        if (!inDescanso) {
                            int descanso = series.get(serieIndex).getDescanso();
                            if (descanso > 0) {
                                inDescanso = true;
                                remainingSeconds = descanso;
                            } else {
                                // no descanso: advance and then pause the gestor (stop chrono)
                                advanceSerieOrExerciseInternal();
                                running = false; // stop chrono
                                if (listener != null) listener.onState("Pausado");
                                notifySeriesUpdate();
                                break;
                            }
                        } else {
                            // finished descanso: advance and pause chrono
                            inDescanso = false;
                            advanceSerieOrExerciseInternal();
                            running = false; // stop chrono after rest
                            if (listener != null) listener.onState("Pausado");
                            notifySeriesUpdate();
                            break;
                        }
                    }

                    notifySeriesUpdate();
                }
            } catch (InterruptedException ex) {
                // thread interrupted on pause/stop
            }
        }, "ejercicio-countdown");
        countdownThread.start();
    }

    public void pause() {
        running = false;
        if (listener != null) listener.onState("Pausado");
        if (countdownThread != null) countdownThread.interrupt();
    }

    private void ensureRemainingInitialized() {
        if (remainingSeconds <= 0) {
            if (ejercicioIndex < ejercicios.size()) {
                ArrayList<Serie> s = ejercicios.get(ejercicioIndex).getSeries();
                if (s != null && serieIndex < s.size()) remainingSeconds = s.get(serieIndex).getTiempo();
            }
        }
    }

    private void advanceSerieOrExerciseInternal() {
        // move to next serie or exercise; does not call listener directly here (notifySeriesUpdate will be called by caller)
        ArrayList<Serie> series = ejercicios.get(ejercicioIndex).getSeries();
        serieIndex++;
        if (serieIndex >= series.size()) {
            ejercicioIndex++;
            if (ejercicioIndex >= ejercicios.size()) {
                // finished workout
                running = false;
                if (countdownThread != null) countdownThread.interrupt();
                if (listener != null) listener.onFinished();
                return;
            } else {
                serieIndex = 0;
                ArrayList<Serie> nextSeries = ejercicios.get(ejercicioIndex).getSeries();
                if (nextSeries != null && !nextSeries.isEmpty()) remainingSeconds = nextSeries.get(0).getTiempo();
                return;
            }
        } else {
            remainingSeconds = series.get(serieIndex).getTiempo();
        }
    }

    private void notifySeriesUpdate() {
        if (listener == null) return;
        // build original times list of current exercise
        List<Integer> originals = new ArrayList<>();
        if (ejercicioIndex < ejercicios.size()) {
            ArrayList<Serie> series = ejercicios.get(ejercicioIndex).getSeries();
            if (series != null) {
                for (Serie s : series) originals.add(s.getTiempo());
            }
        }
        listener.onSeriesUpdate(ejercicioIndex, serieIndex, remainingSeconds, originals, inDescanso);
    }

}