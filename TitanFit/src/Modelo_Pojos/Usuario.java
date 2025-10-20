package Modelo_Pojos;

import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

public class Usuario {

	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private Date fechaNacimiento;
	private String password;
	private int nivel;

	public Usuario() {
		super();
		this.id = 0;
		this.nombre = null;
		this.apellidos = null;
		this.email = null;
		this.fechaNacimiento = null;
		this.password = null;
		this.nivel = 0;
	}

	public Usuario(int id, String nombre, String apellidos, String email, Date fechaNacimiento, String password,
			int nivel) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.password = password;
		this.nivel = nivel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public static String setPasswordConHash(String passwordPlano) {
		String salt = BCrypt.gensalt(12);
		return BCrypt.hashpw(passwordPlano, salt);
	}

	public boolean verificarPassword(String passwordPlano) {
		if (this.password == null)
			return false;
		return BCrypt.checkpw(passwordPlano, this.password);
	}
}
