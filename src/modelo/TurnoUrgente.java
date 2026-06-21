package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoUrgente extends Turno {
    private boolean requiereIntervencion;
    private double duracion;

    public TurnoUrgente() {
    }

    public TurnoUrgente(boolean requiereIntervencion, double duracion) {
        this.requiereIntervencion = requiereIntervencion;
        this.duracion = duracion;
    }

    public TurnoUrgente(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, Double precioBase, boolean requiereIntervencion, double duracion) {
        super(id, paciente, odontologo, fecha, hora, estado, precioBase);
        this.requiereIntervencion = requiereIntervencion;
        this.duracion = duracion;
    }

    @Override
    public double calculaDuracion() {
        double intervencion = requiereIntervencion ? 60 : 20;
        return intervencion + duracion;
    }

    @Override
    public double calculaPrecioFinal() {
        double subFinal = getPrecioBase() + getPrecioBase() * 0.25;
        double recargo = duracion > 90 ? 5000 : 0.0;
        return subFinal + recargo;
    }

    public boolean isRequiereIntervencion() {
        return requiereIntervencion;
    }

    public void setRequiereIntervencion(boolean requiereIntervencion) {
        this.requiereIntervencion = requiereIntervencion;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "TurnoUrgente{" +
                "id=" + getId() +
                ", paciente=" + getPaciente() +
                ", odontologo=" + getOdontologo() +
                ", fecha=" + getFecha() +
                ", hora=" + getHora() +
                ", estado=" + getEstado() +
                ", precioBase=" + getPrecioBase() +
                ", requiereIntervencion=" + requiereIntervencion +
                ", duracion=" + duracion +
                '}';
    }
}
