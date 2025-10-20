package Modelo_Pojos;

import java.util.ArrayList;

public class Workout {

	private int nivel;
	private String nombre;
	private int numeroEjercicios;
	private ArrayList<Ejercicio> ejercicios;
	
	
	public Workout(int nivel, String nombre, int numeroEjercicios, ArrayList<Ejercicio> ejercicios) {
		this.nivel = nivel;
		this.nombre = nombre;
		this.numeroEjercicios = numeroEjercicios;
		this.ejercicios = ejercicios;
	}
	
	public Workout() {
		this.nivel = 0;
		this.nombre = null;
		this.numeroEjercicios = 0;
		this.ejercicios = null;
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

	public ArrayList<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	
	
	
	
}
