package servicio;

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
    public Paciente registrar(Paciente paciente) {
        // Validamos que los datos básicos no estén vacíos
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre del paciente es obligatorio.");
            return null;
        }

        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            System.out.println("Error: El DNI del paciente es obligatorio.");
            return null;
        }

        // Validación de Negocio (CU1): El DNI no puede estar duplicado
        List<Paciente> pacientesExistentes = pacienteRepository.listarTodos();
        for (Paciente p : pacientesExistentes) {
            if (p.getDni().equals(paciente.getDni())) {
                System.out.println("Error: Ya existe un paciente registrado con el DNI " + paciente.getDni());
                return null; // Cortamos la ejecución y no guardamos nada
            }
        }

        // Si pasó todas las validaciones, lo mandamos a guardar
        System.out.println("Éxito: Paciente registrado correctamente.");
        return pacienteRepository.guardar(paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        // Validar si el ID existe antes de eliminar
        pacienteRepository.eliminarPorId(id);
        System.out.println("Paciente eliminado exitosamente.");
    }

    @Override
    public void actualizar(Paciente pacienteModificado) {
        // Directamente delegamos al repositorio
        pacienteRepository.actualizar(pacienteModificado);
        System.out.println("Paciente actualizado exitosamente.");
    }

    @Override
    public List<Paciente> listarTodos() {
        return pacienteRepository.listarTodos();
    }

    public Paciente buscarPorDni(String dni) {
        return listarTodos().stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

}
