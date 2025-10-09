package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private int id;
    private Usuario usuario;
    private Instalacion instalacion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoReserva estado;
    private Double monto;

    // Constructor SIN id (para crear nuevas reservas)
    public Reserva(Usuario usuario, Instalacion instalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Double monto) {
        this.usuario = usuario;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = EstadoReserva.PENDIENTE;
        this.monto = monto;
    }

    // Constructor CON id (para leer de la BD)
    public Reserva(int id, Usuario usuario, Instalacion instalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, EstadoReserva estado, Double monto) {
        this.id = id;
        this.usuario = usuario;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.monto = monto;
    }

    public Reserva() {}

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Instalacion getInstalacion() {
        return instalacion;
    }
    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    public LocalTime getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    public EstadoReserva getEstado() {
        return estado;
    }
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }
    public Double getMonto() {
        return monto;
    }
    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
