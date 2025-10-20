package Modelo_Pojos;

public class Ejercicio {

	private String nombre;
	private String descripcion;
	private String urlVideo;
	
	public Ejercicio(String nombre, String descripcion, String urlVideo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.urlVideo = urlVideo;
	}
	
	public Ejercicio() {
		this.nombre = null;
		this.descripcion = null;
		this.urlVideo = null;
	
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

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}
	
	
}
