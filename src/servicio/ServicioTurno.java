package servicio;

import Exceptions.DatoInvalidoException;
import Exceptions.TurnoYaReservadoException;
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
    public Turno registrar(Turno turno)
            throws DatoInvalidoException, TurnoYaReservadoException {

        if (turno.getPaciente() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un paciente asignado."
            );
        }

        if (turno.getOdontologo() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un odontólogo asignado."
            );
        }

        if (validarOcupado(
                turno.getOdontologo().getId(),
                turno.getFecha(),
                turno.getHora())) {

            throw new TurnoYaReservadoException(
                    "El odontólogo ya tiene un turno reservado en ese horario."
            );
        }

        if (turno instanceof TurnoUrgente &&
                !turno.getOdontologo().atiendeUrgencias()) {

            throw new DatoInvalidoException(
                    "El profesional seleccionado no atiende urgencias."
            );
        }

        return turnoRepository.guardar(turno);
    }

    @Override
    public Turno buscarPorId(Long id) throws DatoInvalidoException {

        Turno turno = turnoRepository.buscarPorId(id);

        if (turno == null) {
            throw new DatoInvalidoException(
                    "No existe un turno con ID: " + id
            );
        }

        return turno;
    }

    @Override
    public void eliminarPorId(Long id) throws DatoInvalidoException {
        buscarPorId(id);
        turnoRepository.eliminarPorId(id);
    }

    @Override
    public void actualizar(Turno turnoModificado)
            throws DatoInvalidoException {

        buscarPorId(turnoModificado.getId());
        turnoRepository.actualizar(turnoModificado);
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

    public boolean tieneTurnosPendientes(Long pacienteId) {
        return turnoRepository.listarTodos()
                .stream()
                .anyMatch(t ->
                        t.getPaciente() != null &&
                                t.getPaciente().getId().equals(pacienteId) &&
                                !t.getFecha().isBefore(LocalDate.now()));
    }

    public boolean tieneTurnosPendientesOdontologo(Long odontologoId) {
        return turnoRepository.listarTodos()
                .stream()
                .anyMatch(t ->
                        t.getOdontologo() != null &&
                                t.getOdontologo().getId().equals(odontologoId) &&
                                !t.getFecha().isBefore(LocalDate.now()));
    }

    public boolean validarOcupado(Long idOdon, LocalDate fecha, LocalTime hora) {
        return turnoRepository.listarTodos()
                .stream()
                .anyMatch(t ->
                        t.getOdontologo() != null &&
                                t.getOdontologo().getId().equals(idOdon) &&
                                t.getFecha().equals(fecha) &&
                                t.getHora().equals(hora) &&
                                t.getEstado() != EstadoTurno.CANCELADO);
    }

    public List<Turno> buscarPorRango(LocalDate desde, LocalDate hasta) {
        return turnoRepository.listarTodos()
                .stream()
                .filter(t ->
                        !t.getFecha().isBefore(desde) &&
                                !t.getFecha().isAfter(hasta))
                .toList();
    }

    public List<Turno> buscarPorPaciente(Long pacienteId) {
        return turnoRepository.listarTodos()
                .stream()
                .filter(t ->
                        t.getPaciente() != null &&
                                t.getPaciente().getId().equals(pacienteId))
                .toList();
    }

    public List<Turno> buscarPorOdontologo(Long odontologoId) {
        return turnoRepository.listarTodos()
                .stream()
                .filter(t ->
                        t.getOdontologo() != null &&
                                t.getOdontologo().getId().equals(odontologoId))
                .toList();
    }
}
