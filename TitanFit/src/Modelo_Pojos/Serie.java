package Modelo_Pojos;

import java.io.Serializable;

public class Serie implements Serializable {

	private static final long serialVersionUID = 1L;

	private int serieId;
	private int descanso;
	private int tiempo;
	
	public Serie(int serieId, int descanso, int tiempo) {
		this.serieId = serieId;
		this.descanso = descanso;
		this.tiempo = tiempo;
	}
	
	public Serie() {
		this.serieId = 0;
		this.descanso = 0;
		this.tiempo = 0;
	}
	public int getSerieId() {
		return serieId;
	}
	public void setSerieId(int serieId) {
		this.serieId = serieId;
	}
	
	public int getDescanso() {
		return descanso;
	}

	public void setDescanso(int descanso) {
		this.descanso = descanso;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	
	
}