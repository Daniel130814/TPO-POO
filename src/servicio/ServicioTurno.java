package servicio;

import modelo.EstadoTurno;
import modelo.Turno;
import modelo.TurnoUrgente; // Importamos esto para la validación especial
import repositorio.IRepositorio;
import repositorio.RepositorioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ServicioTurno implements IService<Turno> {
    private IRepositorio<Turno> turnoRepository;

    public ServicioTurno() {
        this.turnoRepository = new RepositorioTurno();
    }

    @Override
    public Turno registrar(Turno turno) {
        if (turno.getPaciente() == null) {
            System.out.println("Error: El turno debe tener un paciente asignado.");
            return null;
        }

        if (turno.getOdontologo() == null) {
            System.out.println("Error: El turno debe tener un odontólogo asignado.");
            return null;
        }

        if (turno instanceof TurnoUrgente) {
            if (!turno.getOdontologo().atiendeUrgencias()) {
                System.out.println("Error: El profesional seleccionado NO atiende urgencias. Debe derivarlo.");
                return null;
            }
        }

        System.out.println("Éxito: Turno registrado correctamente.");
        return turnoRepository.guardar(turno);
    }

    @Override
    public Turno buscarPorId(Long id) {
        return turnoRepository.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        turnoRepository.eliminarPorId(id);
        System.out.println("Turno eliminado exitosamente.");
    }

    public boolean tieneTurnosPendientes(Long pacienteId) {
        for (Turno t : turnoRepository.listarTodos()) {
            // Si el ID del paciente coincide Y la fecha no ha pasado
            if (t.getPaciente().getId().equals(pacienteId) && !t.getFecha().isBefore(LocalDate.now())) {
                return true;
            }
        }
        return false; // No tiene turnos futuros
    }

    public boolean tieneTurnosPendientesOdontologo(Long odontologoId) {
        for (Turno t : turnoRepository.listarTodos()) {
            // Si el ID del odontólogo coincide Y la fecha NO ha pasado
            if (t.getOdontologo().getId().equals(odontologoId) && !t.getFecha().isBefore(LocalDate.now())) {
                return true;
            }
        }
        return false; // No tiene turnos futuros
    }

    @Override
    public void actualizar(Turno turnoModificado) {
        turnoRepository.actualizar(turnoModificado);
        System.out.println("Turno actualizado exitosamente.");
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }


    public boolean validarOcupado(Long idOdon, LocalDate f, LocalTime h) {
        for (Turno t : turnoRepository.listarTodos()) {
            if (t.getOdontologo().getId().equals(idOdon) &&
                    t.getFecha().equals(f) &&
                    t.getHora().equals(h) &&
                    t.getEstado() != EstadoTurno.CANCELADO) {
                return true;
            }
        }
        return false;
    }
}
