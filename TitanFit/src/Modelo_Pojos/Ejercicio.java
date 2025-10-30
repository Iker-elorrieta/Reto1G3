package Modelo_Pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class Ejercicio implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String descripcion;
	private String foto;
	private ArrayList<Serie> series;
	
	public Ejercicio(String nombre, String descripcion, String foto,  ArrayList<Serie> series) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.foto = foto;
		this.series = series;
	}
	
	public Ejercicio() {
		super();
		this.nombre = null;
		this.descripcion = null;
		this.foto = null;
		this.series = new ArrayList<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public ArrayList<Serie> getSeries() {
		if (this.series == null) this.series = new ArrayList<>();
		return series;
	}

	public void setSeries(ArrayList<Serie> series) {
		this.series = series;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	public int getDuracion() {
	    if (series != null && !series.isEmpty()) {
	        return series.get(0).getTiempo(); 
	    }
	    return 0;
	}

	public int getDescanso() {
	    if (series != null && !series.isEmpty()) {
	        return series.get(0).getDescanso(); 
	    }
	    return 60; 
	}
}
