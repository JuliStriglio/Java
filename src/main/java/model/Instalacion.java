package model;
import java.time.LocalTime;
public class Instalacion {
	
	private int id;
	private String nombre;
	private TipoInstalacion tipo;
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	private String direccion;
	private Double precioxhora;

	 // Constructor SIN id (para crear nuevos)
	public Instalacion(String nombre, TipoInstalacion tipo, LocalTime horaApertura,  LocalTime horaCierre, String direccion, Double precioxhora) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.direccion = direccion;
		this.precioxhora = precioxhora; }
	
	
	// Contructor CON id (para leer en la bd)
	public Instalacion(int id, String nombre, TipoInstalacion tipo,LocalTime horaApertura,  LocalTime horaCierre, String direccion, Double precioxhora) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
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

	public TipoInstalacion getTipo() {
		return tipo;
	}

	public void setTipo(TipoInstalacion tipo) {
		this.tipo = tipo;
	}

	public LocalTime getHoraApertura() {
		return horaApertura;
	}
	
	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}
	
	public LocalTime getHoraCierre() {
		return horaCierre;
	}
	
	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
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
