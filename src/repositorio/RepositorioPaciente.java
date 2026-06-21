package repositorio;

import modelo.Domicilio;
import modelo.Paciente;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioPaciente implements IRepositorio<Paciente> {

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
        return paciente;
    }

    public Paciente guardarConId(Paciente paciente) {
        pacientes.put(paciente.getId(), paciente);
        actualizarGenerador(paciente.getId());
        return paciente;
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
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public List<Paciente> listarTodos() {
        return new ArrayList<>(pacientes.values());
    }

    public void guardarEnArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();

        for (Paciente paciente : listarTodos()) {
            Domicilio domicilio = paciente.getDomicilio();
            lineas.add(
                    paciente.getId() + ";" +
                            limpiar(paciente.getNombre()) + ";" +
                            limpiar(paciente.getApellido()) + ";" +
                            limpiar(paciente.getDni()) + ";" +
                            limpiar(paciente.getEmail()) + ";" +
                            paciente.getFechaIngreso() + ";" +
                            limpiar(domicilio.getCalle()) + ";" +
                            domicilio.getNumero() + ";" +
                            limpiar(domicilio.getLocalidad()) + ";" +
                            limpiar(domicilio.getProvincia())
            );
        }

        escribirArchivo(ruta, lineas);
    }

    public void cargarDesdeArchivo(String ruta) {
        Path path = Paths.get(ruta);

        if (!Files.exists(path)) {
            return;
        }

        try {
            pacientes.clear();
            generadorId = 1L;

            List<String> lineas = Files.readAllLines(path);

            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 10) {
                    throw new IllegalStateException("Linea invalida en pacientes: " + linea);
                }

                Long id = Long.parseLong(datos[0]);
                LocalDate fechaIngreso = LocalDate.parse(datos[5]);
                Domicilio domicilio = new Domicilio(
                        datos[6],
                        Integer.parseInt(datos[7]),
                        datos[8],
                        datos[9]
                );

                Paciente paciente = new Paciente(
                        id,
                        datos[1],
                        datos[2],
                        datos[3],
                        datos[4],
                        fechaIngreso,
                        domicilio
                );

                guardarConId(paciente);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de pacientes.", e);
        }
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
            throw new IllegalStateException("No se pudo guardar el archivo de pacientes.", e);
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
