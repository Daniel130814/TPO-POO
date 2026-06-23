package servicio;

import exceptions.DatoInvalidoException;
import exceptions.DniDuplicadoException;
import exceptions.PacienteNoEncontradoException;
import modelo.Paciente;
import repositorio.IRepositorio;
import repositorio.RepositorioPaciente;

import java.util.ArrayList;
import java.util.List;

public class ServicioPaciente implements IService<Paciente> {

    private IRepositorio<Paciente> pacienteRepository;
    private ServicioTurno servicioTurno;

    public ServicioPaciente() {
        // Instanciamos el repositorio
        this.pacienteRepository = new RepositorioPaciente();
    }

    public void setServicioTurno(ServicioTurno servicioTurno) {
        this.servicioTurno = servicioTurno;
    }

    @Override
    public Paciente registrar(Paciente paciente)
            throws DatoInvalidoException, DniDuplicadoException {

        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del paciente es obligatorio.");
        }

        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            throw new DatoInvalidoException("El DNI del paciente es obligatorio.");
        }

        for (Paciente p : pacienteRepository.listarTodos()) {
            if (p.getDni().equals(paciente.getDni())) {
                throw new DniDuplicadoException(
                        "Ya existe un paciente registrado con DNI: " + paciente.getDni()
                );
            }
        }

        return pacienteRepository.guardar(paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) throws PacienteNoEncontradoException {

        Paciente paciente = pacienteRepository.buscarPorId(id);

        if (paciente == null) {
            throw new PacienteNoEncontradoException(
                    "No existe un paciente con ID: " + id
            );
        }

        return paciente;
    }

    @Override
    public void eliminarPorId(Long id) throws PacienteNoEncontradoException {

        buscarPorId(id); // valida existencia

        if (servicioTurno != null && !servicioTurno.buscarPorPaciente(id).isEmpty()) {
            throw new DatoInvalidoException(
                    "No se puede eliminar el paciente porque tiene turnos asociados."
            );
        }

        pacienteRepository.eliminarPorId(id);
    }


    @Override
    public void actualizar(Paciente pacienteModificado)
            throws PacienteNoEncontradoException, DatoInvalidoException, DniDuplicadoException {

        buscarPorId(pacienteModificado.getId());

        if (pacienteModificado.getNombre() == null || pacienteModificado.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del paciente es obligatorio.");
        }

        if (pacienteModificado.getDni() == null || pacienteModificado.getDni().trim().isEmpty()) {
            throw new DatoInvalidoException("El DNI del paciente es obligatorio.");
        }

        for (Paciente p : pacienteRepository.listarTodos()) {
            if (!p.getId().equals(pacienteModificado.getId()) &&
                    p.getDni().equals(pacienteModificado.getDni())) {

                throw new DniDuplicadoException(
                        "Ya existe un paciente registrado con DNI: " + pacienteModificado.getDni()
                );
            }
        }

        pacienteRepository.actualizar(pacienteModificado);
    }
    
    public List<Paciente> listarOrdenadosPorApellido() {
        List<Paciente> ordenados = new ArrayList<>(pacienteRepository.listarTodos());

        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                Paciente actual = ordenados.get(j);
                Paciente siguiente = ordenados.get(j + 1);

                if (actual.getApellido().compareToIgnoreCase(siguiente.getApellido()) > 0) {
                    ordenados.set(j, siguiente);
                    ordenados.set(j + 1, actual);
                }
            }
        }

        return ordenados;
    }

    @Override
    public List<Paciente> listarTodos() {
        return pacienteRepository.listarTodos();
    }

    public Paciente buscarPorDni(String dni)
            throws PacienteNoEncontradoException {

        for (Paciente p : pacienteRepository.listarTodos()) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }

        throw new PacienteNoEncontradoException(
                "No existe un paciente registrado con DNI: " + dni
        );
    }

    public void guardarEnArchivo(String ruta) {
        obtenerRepositorioPaciente().guardarEnArchivo(ruta);
    }

    public void cargarDesdeArchivo(String ruta) {
        obtenerRepositorioPaciente().cargarDesdeArchivo(ruta);
    }

    private RepositorioPaciente obtenerRepositorioPaciente() {
        return (RepositorioPaciente) pacienteRepository;
    }

}
