package modelo;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private Long id;
    private Paciente paciente; //es una asociación
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;

    // Metodo Constructor
    public Turno() {
    }

    public Turno(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoTurno.PENDIENTE;
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

    public LocalTime getHora() {return hora;}

    public EstadoTurno getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", paciente=" + paciente +
                ", odontologo=" + odontologo +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                '}';
    }
}
