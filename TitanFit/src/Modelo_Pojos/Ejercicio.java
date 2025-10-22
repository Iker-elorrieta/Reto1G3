package Modelo_Pojos;

import java.io.Serializable;

public class Ejercicio implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String descripcion;
	private String foto;
	private int duracion;

	public Ejercicio(String nombre, String descripcion, String imagen, int duracion) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.foto = imagen;
		this.duracion = duracion;
	}

	public Ejercicio() {
		super();
		this.nombre = null;
		this.descripcion = null;
		this.foto = null;
		this.duracion = 0;
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

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

}