package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoUrgente extends Turno  {
    private boolean requiereIntervencion; //requiere una intervención como por ejemplo una extracción?
    private double duracion;


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
        double intervencion= requiereIntervencion ? 60:20;
        return intervencion+duracion;
    }

    @Override
    public double calculaPrecioFinal() {
        double subFinal= getPrecioBase()+ getPrecioBase()*0.25;
        double recargo= duracion>90 ? 5000:0.0;
        return subFinal+recargo;
    }
}
