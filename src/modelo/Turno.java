package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Turno {
    // vamos a aplicar herencia en turno, se ve reflejado en turno control y turno urgente
    // Si el turno es urgente se le va a cobrar un recargo de un 25% y si dura mes de 90 minutos un recargo de $5000
    // En la consulta de rutina se le va a cobrar $5000 si la duración pasa de 60 minutos
    // Tienen un descuento del 25% los clientes con obra social (no aplica a turnos urgentes)
    private Long id;
    private Paciente paciente; //es una asociación
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    protected double PrecioBase;

    // Metodo Constructor
    public Turno() {
    }

    public abstract double calculaDuracion ();
    public abstract double calculaPrecioFinal();


    public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, Double precioBase) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        PrecioBase = precioBase;
    }

    //Metodo Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public void setPrecioBase(Double precioBase) {
        PrecioBase = precioBase;
    }


    //Metodo Getter


    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public Double getPrecioBase() {
        return PrecioBase;
    }


    // Metodo toString

    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", paciente=" + paciente +
                ", odontologo=" + odontologo +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                ", PrecioBase=" + PrecioBase +
                '}';
    }
}
