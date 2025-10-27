package Modelo_Pojos;

import java.io.Serializable;
import java.util.Date;

public class Historico implements Serializable  {

	private static final long serialVersionUID = 1L;
	private int id_workout;
	private Date fecha;
	private int tiempo;
	private int porcentaje;
	
	public Historico() {
		super();
		this.id_workout = 0;
		this.fecha = null;
		this.tiempo = 0;
		this.porcentaje = 0;
	}

	public Historico(int id_workout, Date fecha, int tiempo, int porcentaje) {
		super();
		this.id_workout = id_workout;
		this.fecha = fecha;
		this.tiempo = tiempo;
		this.porcentaje = porcentaje;
	}

	public int getId_workout() {
		return id_workout;
	}

	public void setId_workout(int id_workout) {
		this.id_workout = id_workout;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Override
	public String toString() {
		return "Historico [id_workout=" + id_workout + ", fecha=" + fecha + ", tiempo=" + tiempo + ", porcentaje="
				+ porcentaje + "]";
	}
	
	
	
}
