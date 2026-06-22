package controller;

import modelo.EstadoTurno;
import modelo.Turno;
import servicio.ServicioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TurnoController {
    private ServicioTurno servicioTurno;

    public TurnoController(ServicioTurno servicioTurno) {
        this.servicioTurno = servicioTurno;
    }

    public Turno registrar(Turno turno) {
        return servicioTurno.registrar(turno);
    }

    public Turno buscarPorId(Long id) {
        return servicioTurno.buscarPorId(id);
    }

    public void actualizar(Turno turno) {
        servicioTurno.actualizar(turno);
    }

    public void cambiarEstado(Long id, EstadoTurno estado) {
        Turno turno = servicioTurno.buscarPorId(id);
        turno.setEstado(estado);
        servicioTurno.actualizar(turno);
    }

    public void eliminarPorId(Long id) {
        servicioTurno.eliminarPorId(id);
    }

    public List<Turno> listarTodos() {
        return servicioTurno.listarTodos();
    }

    public boolean validarOcupado(Long idOdon, LocalDate fecha, LocalTime hora) {
        return servicioTurno.validarOcupado(idOdon, fecha, hora);
    }

    public List<Turno> buscarPorFecha(LocalDate fecha) {
        return servicioTurno.buscarPorFecha(fecha);
    }

    public List<Turno> buscarPorEstado(EstadoTurno estado) {
        return servicioTurno.buscarPorEstado(estado);
    }

    public List<Turno> buscarPorPaciente(Long pacienteId) {
        return servicioTurno.buscarPorPaciente(pacienteId);
    }

    public List<Turno> buscarPorOdontologo(Long odontologoId) {
        return servicioTurno.buscarPorOdontologo(odontologoId);
    }

    public List<Turno> buscarPorRango(LocalDate desde, LocalDate hasta) {
        return servicioTurno.buscarPorRango(desde, hasta);
    }
}
