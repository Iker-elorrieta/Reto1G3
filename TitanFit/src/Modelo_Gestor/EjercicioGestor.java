package Modelo_Gestor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Modelo_Pojos.Ejercicio;
import Modelo_Pojos.Serie;

public class EjercicioGestor {

	public interface Listener {
		void onState(String state); // Estados: "Presiona Iniciar...", "Pausado", "Realizando ejercicio"

		void onWorkoutTime(int totalSeconds); // hh:mm:ss contador

		void onSeriesUpdate(int ejercicioIndex, int serieIndex, int remainingSeconds, List<Integer> originalTimes,
				boolean inDescanso);

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

	// Constructor que carga desde backup si no hay internet o si los ejercicios
	// están vacíos
	public EjercicioGestor(ArrayList<Modelo_Pojos.Ejercicio> ejercicios, boolean hayInternet) {
		if (!hayInternet || ejercicios == null || ejercicios.isEmpty()) {
			this.ejercicios = cargarDesdeBackup();
			System.out.println("Ejercicios cargados desde backup.dat (sin internet o lista vacía)");
		} else {
			this.ejercicios = ejercicios;
			guardarBackup(); // Guardar backup cuando hay internet y ejercicios válidos
		}
	}

	// Cargar ejercicios desde backup.dat
	@SuppressWarnings("unchecked")
	private ArrayList<Ejercicio> cargarDesdeBackup() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("backup.dat"))) {
			ArrayList<Ejercicio> loaded = (ArrayList<Ejercicio>) ois.readObject();
			System.out.println("Backup cargado exitosamente: " + loaded.size() + " ejercicios");
			return loaded;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al cargar backup.dat: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	// Guardar ejercicios en backup.dat
	public void guardarBackup() {
		if (ejercicios == null || ejercicios.isEmpty()) {
			System.out.println("No hay ejercicios para guardar en backup");
			return;
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backup.dat"))) {
			oos.writeObject(ejercicios);
			System.out.println("Backup guardado exitosamente: " + ejercicios.size() + " ejercicios");
		} catch (IOException e) {
			System.err.println("Error al guardar backup.dat: " + e.getMessage());
		}
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
		if (total == 0)
			return 0;
		int completed = Math.min(ejercicioIndex, total);
		// integer division gives floor: e.g. 2*100/3 == 66
		int pct = (completed * 100) / total;
		if (pct > 100)
			pct = 100;
		return pct;
	}

	public void start() {
		if (running || ejercicios.isEmpty())
			return;
		running = true;
		if (listener != null)
			listener.onState("Realizando ejercicio");

		// Contador de tiempo
		countdownThread = new Thread(() -> {
			try {
				ensureRemainingInitialized();
				while (running) {
					Thread.sleep(1000);
					if (!running)
						break;

					// Contador de tiempo activo total
					totalActiveSeconds++;
					if (listener != null)
						listener.onWorkoutTime(totalActiveSeconds);

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

								advanceSerieOrExerciseInternal();
								running = false; // stop chrono
								if (listener != null)
									listener.onState("Pausado");
								notifySeriesUpdate();
								break;
							}
						} else {
							// Final descanso
							inDescanso = false;
							advanceSerieOrExerciseInternal();
							running = false; // stop chrono after rest
							if (listener != null)
								listener.onState("Pausado");
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
		if (listener != null)
			listener.onState("Pausado");
		if (countdownThread != null)
			countdownThread.interrupt();
	}

	private void ensureRemainingInitialized() {
		if (remainingSeconds <= 0) {
			if (ejercicioIndex < ejercicios.size()) {
				ArrayList<Serie> s = ejercicios.get(ejercicioIndex).getSeries();
				if (s != null && serieIndex < s.size())
					remainingSeconds = s.get(serieIndex).getTiempo();
			}
		}
	}

	private void advanceSerieOrExerciseInternal() {
		// Cambio a siguiente serie o ejercicio
		ArrayList<Serie> series = ejercicios.get(ejercicioIndex).getSeries();
		serieIndex++;
		if (serieIndex >= series.size()) {
			ejercicioIndex++;
			if (ejercicioIndex >= ejercicios.size()) {
				// workout terminado
				running = false;
				if (countdownThread != null)
					countdownThread.interrupt();
				if (listener != null)
					listener.onFinished();
				return;
			} else {
				serieIndex = 0;
				ArrayList<Serie> nextSeries = ejercicios.get(ejercicioIndex).getSeries();
				if (nextSeries != null && !nextSeries.isEmpty())
					remainingSeconds = nextSeries.get(0).getTiempo();
				return;
			}
		} else {
			remainingSeconds = series.get(serieIndex).getTiempo();
		}
	}

	private void notifySeriesUpdate() {
		if (listener == null)
			return;
		// Enviar tiempos originales de la serie actual
		List<Integer> originals = new ArrayList<>();
		if (ejercicioIndex < ejercicios.size()) {
			ArrayList<Serie> series = ejercicios.get(ejercicioIndex).getSeries();
			if (series != null) {
				for (Serie s : series)
					originals.add(s.getTiempo());
			}
		}
		listener.onSeriesUpdate(ejercicioIndex, serieIndex, remainingSeconds, originals, inDescanso);
	}

}