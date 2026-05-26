package servicio;

import Exceptions.DatoInvalidoException;
import Exceptions.DniDuplicadoException;
import Exceptions.PacienteNoEncontradoException;
import modelo.Paciente;
import repositorio.IRepositorio;
import repositorio.RepositorioPaciente;
import java.util.List;

public class ServicioPaciente implements IService<Paciente> {

    private IRepositorio<Paciente> pacienteRepository;

    public ServicioPaciente() {
        // Instanciamos el repositorio
        this.pacienteRepository = new RepositorioPaciente();
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

        pacienteRepository.eliminarPorId(id);
    }


    @Override
    public void actualizar(Paciente pacienteModificado)
            throws PacienteNoEncontradoException {

        buscarPorId(pacienteModificado.getId());

        pacienteRepository.actualizar(pacienteModificado);
    }
    
    public List<Paciente> listarOrdenadosPorApellido() {
        return pacienteRepository.listarTodos()
                .stream()
                .sorted((p1, p2) ->
                        p1.getApellido().compareToIgnoreCase(p2.getApellido()))
                .toList();
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


}
