package repositorio;

import modelo.Turno;
import modelo.TurnoControl;
import modelo.TurnoUrgente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTurno implements IRepositorio<Turno> {

    private List<Turno> turnos;
    private Long generadorId;

    public RepositorioTurno() {
        this.turnos = new ArrayList<>();
        this.generadorId = 1L;
    }

    @Override
    public Turno guardar(Turno turno) {
        turno.setId(generadorId);
        turnos.add(turno);
        generadorId++;
        return turno;
    }

    public Turno guardarConId(Turno turno) {
        turnos.add(turno);
        actualizarGenerador(turno.getId());
        return turno;
    }

    public void limpiar() {
        turnos.clear();
        generadorId = 1L;
    }

    @Override
    public Turno buscarPorId(Long id) {
        for (Turno turno : turnos) {
            if (turno.getId().equals(id)) {
                return turno;
            }
        }

        return null;
    }

    @Override
    public void eliminarPorId(Long id) {
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId().equals(id)) {
                turnos.remove(i);
                break;
            }
        }
    }

    @Override
    public void actualizar(Turno turnoModificado) {
        for (int i = 0; i < turnos.size(); i++) {
            if (turnos.get(i).getId().equals(turnoModificado.getId())) {
                turnos.set(i, turnoModificado);
                break;
            }
        }
    }

    @Override
    public List<Turno> listarTodos() {
        return new ArrayList<>(turnos);
    }

    public void guardarEnArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();

        for (Turno turno : listarTodos()) {
            if (turno instanceof TurnoUrgente) {
                TurnoUrgente urgente = (TurnoUrgente) turno;
                lineas.add(datosBase("URGENTE", urgente) + ";" +
                        urgente.isRequiereIntervencion() + ";" +
                        urgente.getDuracion());
            } else if (turno instanceof TurnoControl) {
                TurnoControl control = (TurnoControl) turno;
                lineas.add(datosBase("CONTROL", control) + ";" +
                        control.getDuracion() + ";" +
                        limpiar(control.getTipoConsulta()) + ";" +
                        control.isRequiereRadiografia() + ";" +
                        control.isTieneObraSocial());
            }
        }

        escribirArchivo(ruta, lineas);
    }

    private String datosBase(String tipo, Turno turno) {
        return tipo + ";" +
                turno.getId() + ";" +
                turno.getPaciente().getId() + ";" +
                turno.getOdontologo().getId() + ";" +
                turno.getFecha() + ";" +
                turno.getHora() + ";" +
                turno.getEstado() + ";" +
                turno.getPrecioBase();
    }

    private void escribirArchivo(String ruta, List<String> lineas) {
        try {
            Path path = Paths.get(ruta);
            Path carpeta = path.getParent();

            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }

            Files.write(path, lineas);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de turnos.", e);
        }
    }

    private void actualizarGenerador(Long id) {
        if (id >= generadorId) {
            generadorId = id + 1;
        }
    }

    private String limpiar(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
}
