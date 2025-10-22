package Modelo_Pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workout implements Serializable {
	private static final long serialVersionUID = 1L;
	private int nivel;
	private String nombre;
	private int numeroEjercicios;
	private ArrayList<Ejercicio> ejercicios;
	private String video;
	
	
	public Workout(int nivel, String nombre, int numeroEjercicios, ArrayList<Ejercicio> ejercicios, String video) {
		this.nivel = nivel;
		this.nombre = nombre;
		this.numeroEjercicios = numeroEjercicios;
		this.video = video;
		this.ejercicios = ejercicios;
	}
	
	public Workout() {
		this.nivel = 0;
		this.nombre = null;
		this.numeroEjercicios = 0;
		this.ejercicios = new ArrayList<>(); // FIXED
		this.video = null;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNumeroEjercicios() {
		return numeroEjercicios;
	}

	public void setNumeroEjercicios(int numeroEjercicios) {
		this.numeroEjercicios = numeroEjercicios;
	}
	
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public ArrayList<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	
	
	
	
}