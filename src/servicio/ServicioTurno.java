package servicio;

import modelo.Turno;
import modelo.TurnoUrgente; // Importamos esto para la validación especial
import repositorio.IRepositorio;
import repositorio.RepositorioTurno;
import java.util.List;

public class ServicioTurno implements IService<Turno> {
    private IRepositorio<Turno> turnoRepository;

    public ServicioTurno() {
        this.turnoRepository = new RepositorioTurno();
    }

    @Override
    public Turno registrar(Turno turno) {
        // Validación 1: No puede haber un turno sin paciente (Fantasma)
        if (turno.getPaciente() == null) {
            System.out.println("Error: El turno debe tener un paciente asignado.");
            return null;
        }

        // Validación 2: No puede haber un turno sin doctor
        if (turno.getOdontologo() == null) {
            System.out.println("Error: El turno debe tener un odontólogo asignado.");
            return null;
        }

        // Validación 3 (¡La validación de nivel Dios!):
        // Usamos el polimorfismo que creaste para ver si el doctor atiende la urgencia.
        if (turno instanceof TurnoUrgente) {
            if (!turno.getOdontologo().atiendeUrgencias()) {
                System.out.println("Error: El profesional seleccionado NO atiende urgencias. Debe derivarlo.");
                return null; // Cortamos la ejecución, no se guarda el turno.
            }
        }

        // Si pasó todos los controles de seguridad, lo guardamos en la lista
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

    @Override
    public void actualizar(Turno turnoModificado) {
        turnoRepository.actualizar(turnoModificado);
        System.out.println("Turno actualizado exitosamente.");
    }

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.listarTodos();
    }

}
