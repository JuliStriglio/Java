package model;

public class Usuario {
	
	private int id;
	private String nombre;
	private String email;
	private String password;
	private Boolean rol; // false = usuario, true=admin 
	
	 // Constructor SIN id (para crear nuevos)
    public Usuario(String nombre, String email, String password, Boolean rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    
    public Usuario() {
        
    }

    // Constructor CON id (para leer de la DB)
    public Usuario(int id, String nombre, String email, String password, Boolean rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getRol() {
		return rol;
	}
	public void setRol(Boolean rol) {
		this.rol = rol;
	}
	
}

	
	
