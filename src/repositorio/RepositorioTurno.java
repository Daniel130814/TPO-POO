package repositorio;

import modelo.Turno;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTurno implements IRepositorio<Turno> {

    // El ArrayList que pide el profe para los turnos
    private List<Turno> turnos;
    private Long generadorId;

    public RepositorioTurno() {
        this.turnos = new ArrayList<>();
        this.generadorId = 1L;
    }

    @Override
    public Turno guardar(Turno turno) {
        turno.setId(generadorId);
        turnos.add(turno); // Lo agregamos a la lista
        generadorId++;
        return turno;
    }

    @Override
    public Turno buscarPorId(Long id) {
        for (Turno turno : turnos) {
            // Usamos .equals() porque id es un Long (objeto), no un int
            if (turno.getId().equals(id)) {
                return turno;
            }
        }
        return null; // Si termina el for y no lo encontró
    }

    @Override
    public void eliminarPorId(Long id) {
        // Un for con la "i" para saber qué posición borrar
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId().equals(id)) {
                turnos.remove(i); // Borramos el de esa posición
                break; // Cortamos el for porque ya lo borramos
            }
        }
    }

    @Override
    public void actualizar(Turno turnoModificado) {
        // Buscamos en qué posición está el turno viejo y lo pisamos con el nuevo
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId().equals(turnoModificado.getId())) {
                turnos.set(i, turnoModificado); // .set() reemplaza en esa posición
                break;
            }
        }
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(turnos);
    }
}
