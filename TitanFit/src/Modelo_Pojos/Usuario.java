package Modelo_Pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private Date fechaNacimiento;
	private String password;
	private int nivel;
	private ArrayList<Historico> historico;

	public Usuario() {
		super();
		this.id = 0;
		this.nombre = "";
		this.apellidos = "";
		this.email = "";
		this.fechaNacimiento = null;
		this.password = "";
		this.nivel = 0;
		this.historico = new ArrayList<Historico>();
	}

	public Usuario(int id, String nombre, String apellidos, String email, Date fechaNacimiento, String password,
			int nivel, ArrayList<Historico> historico) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.fechaNacimiento = fechaNacimiento;
		this.password = password;
		this.nivel = nivel;
		this.historico = historico;
	}

	public ArrayList<Historico> getHistorico() {
		return historico;
	}

	public void setHistorico(ArrayList<Historico> historico) {
		this.historico = historico;
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