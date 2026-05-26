package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoControl extends Turno {
    private double duracion;
    private String tipoConsulta;
    private boolean requiereRadiografia;
    private boolean tieneObraSocial;

    public TurnoControl(double duracion, String tipoConsulta, boolean requiereRadiografia, boolean tieneObraSocial) {
        this.duracion = duracion;
        this.tipoConsulta = tipoConsulta;
        this.requiereRadiografia = requiereRadiografia;
        this.tieneObraSocial = tieneObraSocial;
    }

    public TurnoControl(Long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, Double precioBase, double duracion, String tipoConsulta, boolean requiereRadiografia, boolean tieneObraSocial) {
        super(id, paciente, odontologo, fecha, hora, estado, precioBase);
        this.duracion = duracion;
        this.tipoConsulta = tipoConsulta;
        this.requiereRadiografia = requiereRadiografia;
        this.tieneObraSocial = tieneObraSocial;
    }

    @Override
    public double calculaDuracion() {
        double minutos= requiereRadiografia?45:0.0;

        return minutos+duracion;
    }

    @Override
    public double calculaPrecioFinal() {
        double recargoDuracion=duracion>60 ? 5000:0.0;
        double subTotal=getPrecioBase()+ recargoDuracion;
        return tieneObraSocial ? subTotal * 0.75 : subTotal;
    }
}
