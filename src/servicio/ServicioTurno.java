package servicio;

import exceptions.DatoInvalidoException;
import exceptions.TurnoNoEncontradoException;
import exceptions.TurnoYaReservadoException;
import modelo.EstadoTurno;
import modelo.Odontologo;
import modelo.Paciente;
import modelo.Turno;
import modelo.TurnoControl;
import modelo.TurnoUrgente;
import repositorio.IRepositorio;
import repositorio.RepositorioTurno;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        validarDatosTurno(turno);

        if (turno.getPaciente() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un paciente asignado."
            );
        }

        if (turno.getOdontologo() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un odontologo asignado."
            );
        }

        if (validarOcupado(
                turno.getOdontologo().getId(),
                turno.getFecha(),
                turno.getHora())) {

            throw new TurnoYaReservadoException(
                    "El odontologo ya tiene un turno reservado en ese horario."
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
        if (id == null || id <= 0) {
            throw new DatoInvalidoException("El ID del turno debe ser mayor a cero.");
        }

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
            throws TurnoNoEncontradoException, DatoInvalidoException, TurnoYaReservadoException {

        buscarPorId(turnoModificado.getId());

        validarDatosTurno(turnoModificado);

        if (turnoModificado.getPaciente() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un paciente asignado."
            );
        }

        if (turnoModificado.getOdontologo() == null) {
            throw new DatoInvalidoException(
                    "El turno debe tener un odontologo asignado."
            );
        }

        if (validarOcupado(
                turnoModificado.getOdontologo().getId(),
                turnoModificado.getFecha(),
                turnoModificado.getHora(),
                turnoModificado.getId())) {

            throw new TurnoYaReservadoException(
                    "El odontologo ya tiene un turno reservado en ese horario."
            );
        }

        if (turnoModificado instanceof TurnoUrgente &&
                !turnoModificado.getOdontologo().atiendeUrgencias()) {

            throw new DatoInvalidoException(
                    "El profesional seleccionado no atiende urgencias."
            );
        }

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
        return validarOcupado(idOdon, fecha, hora, null);
    }

    private boolean validarOcupado(Long idOdon, LocalDate fecha, LocalTime hora, Long idTurnoIgnorado) {
        if (idOdon == null || fecha == null || hora == null) {
            return false;
        }

        for (Turno turno : turnoRepository.listarTodos()) {
            if (idTurnoIgnorado != null && turno.getId().equals(idTurnoIgnorado)) {
                continue;
            }

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
        if (desde == null || hasta == null) {
            throw new DatoInvalidoException("Las fechas del rango son obligatorias.");
        }

        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException("La fecha desde no puede ser posterior a la fecha hasta.");
        }

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
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha de busqueda es obligatoria.");
        }

        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getFecha().equals(fecha)) {
                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new DatoInvalidoException("El estado de busqueda es obligatorio.");
        }

        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getEstado() == estado) {
                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public List<Turno> buscarPorPaciente(Long pacienteId) {
        if (pacienteId == null || pacienteId <= 0) {
            throw new DatoInvalidoException("El ID del paciente debe ser mayor a cero.");
        }

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
        if (odontologoId == null || odontologoId <= 0) {
            throw new DatoInvalidoException("El ID del odontologo debe ser mayor a cero.");
        }

        List<Turno> encontrados = new ArrayList<>();

        for (Turno turno : turnoRepository.listarTodos()) {
            if (turno.getOdontologo() != null &&
                    turno.getOdontologo().getId().equals(odontologoId)) {

                encontrados.add(turno);
            }
        }

        return encontrados;
    }

    public void guardarEnArchivo(String ruta) {
        obtenerRepositorioTurno().guardarEnArchivo(ruta);
    }

    public void cargarDesdeArchivo(String ruta, ServicioPaciente servicioPaciente, ServicioOdontologo servicioOdontologo) {
        Path path = Paths.get(ruta);

        if (!Files.exists(path)) {
            return;
        }

        try {
            obtenerRepositorioTurno().limpiar();

            List<String> lineas = Files.readAllLines(path);

            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                Turno turno = crearTurnoDesdeLinea(linea, servicioPaciente, servicioOdontologo);
                obtenerRepositorioTurno().guardarConId(turno);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de turnos.", e);
        }
    }

    private Turno crearTurnoDesdeLinea(String linea, ServicioPaciente servicioPaciente, ServicioOdontologo servicioOdontologo) {
        String[] datos = linea.split(";", -1);

        if (datos.length < 10) {
            throw new IllegalStateException("Linea invalida en turnos: " + linea);
        }

        String tipo = datos[0];
        Long id = Long.parseLong(datos[1]);
        Long pacienteId = Long.parseLong(datos[2]);
        Long odontologoId = Long.parseLong(datos[3]);
        LocalDate fecha = LocalDate.parse(datos[4]);
        LocalTime hora = LocalTime.parse(datos[5]);
        EstadoTurno estado = EstadoTurno.valueOf(datos[6]);
        Double precioBase = Double.parseDouble(datos[7]);

        Paciente paciente = servicioPaciente.buscarPorId(pacienteId);
        Odontologo odontologo = servicioOdontologo.buscarPorId(odontologoId);

        if (tipo.equals("URGENTE")) {
            return new TurnoUrgente(
                    id,
                    paciente,
                    odontologo,
                    fecha,
                    hora,
                    estado,
                    precioBase,
                    Boolean.parseBoolean(datos[8]),
                    Double.parseDouble(datos[9])
            );
        }

        if (tipo.equals("CONTROL")) {
            if (datos.length < 12) {
                throw new IllegalStateException("Linea invalida en turnos: " + linea);
            }

            return new TurnoControl(
                    id,
                    paciente,
                    odontologo,
                    fecha,
                    hora,
                    estado,
                    precioBase,
                    Double.parseDouble(datos[8]),
                    datos[9],
                    Boolean.parseBoolean(datos[10]),
                    Boolean.parseBoolean(datos[11])
            );
        }

        throw new IllegalStateException("Tipo de turno invalido: " + tipo);
    }

    private RepositorioTurno obtenerRepositorioTurno() {
        return (RepositorioTurno) turnoRepository;
    }

    private void validarDatosTurno(Turno turno) {
        if (turno == null) {
            throw new DatoInvalidoException("El turno no puede ser nulo.");
        }

        if (turno.getFecha() == null) {
            throw new DatoInvalidoException("La fecha del turno es obligatoria.");
        }

        if (turno.getHora() == null) {
            throw new DatoInvalidoException("La hora del turno es obligatoria.");
        }

        if (turno.getEstado() == null) {
            throw new DatoInvalidoException("El estado del turno es obligatorio.");
        }

        if (turno.getPrecioBase() == null || turno.getPrecioBase() <= 0) {
            throw new DatoInvalidoException("El precio base del turno debe ser mayor a cero.");
        }

        if (turno.getFecha().isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se puede reservar un turno en una fecha pasada.");
        }
    }
}
