package servicio;

import Exceptions.DatoInvalidoException;
import Exceptions.TurnoNoEncontradoException;
import Exceptions.TurnoYaReservadoException;
import modelo.EstadoTurno;
import modelo.Turno;
import modelo.TurnoUrgente; // Importamos esto para la validación especial
import repositorio.IRepositorio;
import repositorio.RepositorioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public Turno buscarPorId(Long id) throws TurnoNoEncontradoException {

        Turno turno = turnoRepository.buscarPorId(id);

        if (turno == null) {
            throw new TurnoNoEncontradoException(
                    "No existe un turno con ID: " + id
            );
        }

        return turno;
    }

    @Override
    public void eliminarPorId(Long id) throws TurnoNoEncontradoException {
        buscarPorId(id);
        turnoRepository.eliminarPorId(id);
    }

    @Override
    public void actualizar(Turno turnoModificado)
            throws TurnoNoEncontradoException {

        buscarPorId(turnoModificado.getId());
        turnoRepository.actualizar(turnoModificado);
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

    public boolean tieneTurnosPendientes(Long pacienteId) {
        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getPaciente() != null &&
                    turno.getPaciente().getId().equals(pacienteId) &&
                    !turno.getFecha().isBefore(LocalDate.now())) {

                return true;
            }
        }

        return false;
    }

    public boolean tieneTurnosPendientesOdontologo(Long odontologoId) {
        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getOdontologo() != null &&
                    turno.getOdontologo().getId().equals(odontologoId) &&
                    !turno.getFecha().isBefore(LocalDate.now())) {

                return true;
            }
        }

        return false;
    }

    public boolean validarOcupado(Long idOdon, LocalDate fecha, LocalTime hora) {
        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getOdontologo() != null &&
                    turno.getOdontologo().getId().equals(idOdon) &&
                    turno.getFecha().equals(fecha) &&
                    turno.getHora().equals(hora) &&
                    turno.getEstado() != EstadoTurno.CANCELADO) {

                return true;
            }
        }

        return false;
    }

    public List<Turno> buscarPorRango(LocalDate desde, LocalDate hasta) {
        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (!turno.getFecha().isBefore(desde) &&
                    !turno.getFecha().isAfter(hasta)) {

                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorFecha(LocalDate fecha) {
        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getFecha().equals(fecha)) {
                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorEstado(EstadoTurno estado) {
        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getEstado() == estado) {
                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorPaciente(Long pacienteId) {
        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getPaciente() != null &&
                    turno.getPaciente().getId().equals(pacienteId)) {

                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorOdontologo(Long odontologoId) {
        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getOdontologo() != null &&
                    turno.getOdontologo().getId().equals(odontologoId)) {

                encontrados.add(turno);
            }
        }

        return encontrados;
    }
}
