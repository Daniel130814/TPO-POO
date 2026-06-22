package controller;

import modelo.Paciente;
import servicio.ServicioPaciente;

import java.util.List;

public class PacienteController {
    private final ServicioPaciente servicioPaciente;

    public PacienteController(ServicioPaciente servicioPaciente) {
        this.servicioPaciente = servicioPaciente;
    }

    public Paciente registrar(Paciente paciente) {
        return servicioPaciente.registrar(paciente);
    }

    public Paciente buscarPorId(Long id) {
        return servicioPaciente.buscarPorId(id);
    }

    public Paciente buscarPorDni(String dni) {
        return servicioPaciente.buscarPorDni(dni);
    }

    public void actualizar(Paciente paciente) {
        servicioPaciente.actualizar(paciente);
    }

    public void eliminarPorId(Long id) {
        servicioPaciente.eliminarPorId(id);
    }

    public List<Paciente> listarTodos() {
        return servicioPaciente.listarTodos();
    }

    public List<Paciente> listarOrdenadosPorApellido() {
        return servicioPaciente.listarOrdenadosPorApellido();
    }
}
