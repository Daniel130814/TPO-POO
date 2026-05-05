package repositorio;

import modelo.Paciente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RepositorioPaciente implements IRepositorio<Paciente> {

    // El HashMap obligatorio
    private Map<Long, Paciente> pacientes;
    private Long generadorId;

    public RepositorioPaciente() {
        this.pacientes = new HashMap<>();
        this.generadorId = 1L;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        paciente.setId(generadorId);
        pacientes.put(generadorId, paciente);
        generadorId++;
        return paciente; // Devuelve el paciente guardado
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return pacientes.get(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        pacientes.remove(id);
    }

    @Override
    public void actualizar(Paciente paciente) {
        // Al hacer put con un ID que ya existe, el HashMap lo actualiza
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

}
