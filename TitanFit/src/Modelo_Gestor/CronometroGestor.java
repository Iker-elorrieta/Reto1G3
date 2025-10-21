package Modelo_Gestor;

import javax.swing.SwingUtilities;

/**
 * Gestor para manejar los cronómetros del workout
 */
public class CronometroGestor {
	
	private Thread hilo;
	private int tiempoSegundos;
	private boolean activo;
	private boolean pausado;
	private TipoCronometro tipo;
	private CronometroListener listener;
	
	public enum TipoCronometro {
		ASCENDENTE, DESCENDENTE
	}
	
	public interface CronometroListener {
		void onTick(int segundos);
		void onFinalizado();
	}
	
	public CronometroGestor(TipoCronometro tipo, int tiempoInicial) {
		this.tipo = tipo;
		this.tiempoSegundos = tiempoInicial;
		this.activo = false;
		this.pausado = false;
	}
	
	public void setListener(CronometroListener listener) {
		this.listener = listener;
	}
	
	public void iniciar() {
		if (activo) return;
		
		activo = true;
		pausado = false;
		
		hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				while (activo) {
					if (!pausado) {
						try {
							Thread.sleep(1000);
							
							// Actualizar tiempo según el tipo
							if (tipo == TipoCronometro.ASCENDENTE) {
								tiempoSegundos++;
							} else {
								tiempoSegundos--;
							}
							
							// Notificar cambio en el hilo de la UI
							if (listener != null) {
								SwingUtilities.invokeLater(() -> {
									listener.onTick(tiempoSegundos);
								});
							}
							
							// Si es descendente y llegó a 0, finalizar
							if (tipo == TipoCronometro.DESCENDENTE && tiempoSegundos <= 0) {
								activo = false;
								if (listener != null) {
									SwingUtilities.invokeLater(() -> {
										listener.onFinalizado();
									});
								}
							}
							
						} catch (InterruptedException e) {
							break;
						}
					} else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							break;
						}
					}
				}
			}
		});
		hilo.start();
	}
	
	public void pausar() {
		pausado = true;
	}
	
	public void reanudar() {
		pausado = false;
	}
	
	public void detener() {
		activo = false;
		if (hilo != null) {
			hilo.interrupt();
		}
	}
	
	public int getTiempoActual() {
		return tiempoSegundos;
	}
	
	public void setTiempoActual(int tiempo) {
		this.tiempoSegundos = tiempo;
	}
	
	public boolean isPausado() {
		return pausado;
	}
	
	public boolean isActivo() {
		return activo;
	}
	
	public static String formatearTiempo(int segundos) {
		int mins = segundos / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d", mins, secs);
	}
	
	public static String formatearTiempoCompleto(int segundos) {
		int horas = segundos / 3600;
		int mins = (segundos % 3600) / 60;
		int secs = segundos % 60;
		return String.format("%02d:%02d:%02d", horas, mins, secs);
	}
}
