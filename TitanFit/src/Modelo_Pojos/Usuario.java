package Modelo_Pojos;

import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;


public class Usuario {

    private int id;
    private String nombre;
    private String username;
    private String apellidos;
    private String email;
    private Date fechaUsuario;
    private String password; 

    public Usuario(int id, String nombre, String username, String apellidos, String email, Date fechaUsuario,
                   String passwordPlano) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaUsuario = fechaUsuario;
        this.password = hashearPassword(passwordPlano);
    }

    public Usuario() {
        super();
        this.id = 0;
        this.nombre = null;
        this.username = null;
        this.apellidos = null;
        this.email = null;
        this.fechaUsuario = null;
        this.password = null;
    }

    // ===== Getters y Setters =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getFechaUsuario() { return fechaUsuario; }
    public void setFechaUsuario(Date fechaUsuario) { this.fechaUsuario = fechaUsuario; }

    public String getPassword() { return password; }

    public void setPassword(String passwordPlano) {
        this.password = hashearPassword(passwordPlano);
    }

    
    private String hashearPassword(String passwordPlano) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(passwordPlano, salt);
    }
    
    public boolean verificarPassword(String passwordPlano) {
        if (this.password == null) return false;
        return BCrypt.checkpw(passwordPlano, this.password);
    }
}
