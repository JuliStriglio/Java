package model;
import java.util.Date;
public class Instalacion {
	
	private int id;
	private String nombre;
	private String tipo;
	private Date horarios;
	private String direccion;
	private Double precioxhora;

	
	
	
	 // Constructor SIN id (para crear nuevos)
	public Instalacion(String nombre, String tipo, Date horarios, String direccion, Double precioxhora) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.horarios = horarios;
		this.direccion = direccion;
		this.precioxhora = precioxhora; }
	
	
	// Contructor CON id (para leer en la bd)
	
	public Instalacion(int id, String nombre, String tipo, Date horarios, String direccion, Double precioxhora) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.horarios = horarios;
		this.direccion = direccion;
		this.precioxhora = precioxhora;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getHorarios() {
		return horarios;
	}

	public void setHorarios(Date horarios) {
		this.horarios = horarios;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Double getPrecioxhora() {
		return precioxhora;
	}

	public void setPrecioxhora(Double precioxhora) {
		this.precioxhora = precioxhora;
	} 
	
	
}
