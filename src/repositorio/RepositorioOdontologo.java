package repositorio;

import modelo.Endodoncista;
import modelo.Odontologo;
import modelo.OdontologoGeneral;
import modelo.Ortodoncista;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {

    private Map<Long, Odontologo> odontologos;
    private Long generadorId;

    public RepositorioOdontologo() {
        this.odontologos = new HashMap<>();
        this.generadorId = 1L;
    }

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        odontologo.setId(generadorId);
        odontologos.put(generadorId, odontologo);
        generadorId++;
        return odontologo;
    }

    public Odontologo guardarConId(Odontologo odontologo) {
        odontologos.put(odontologo.getId(), odontologo);
        actualizarGenerador(odontologo.getId());
        return odontologo;
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return odontologos.get(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        odontologos.remove(id);
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        odontologos.put(odontologo.getId(), odontologo);
    }

    @Override
    public List<Odontologo> listarTodos() {
        return new ArrayList<>(odontologos.values());
    }

    public void guardarEnArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();

        for (Odontologo odontologo : listarTodos()) {
            if (odontologo instanceof OdontologoGeneral) {
                OdontologoGeneral general = (OdontologoGeneral) odontologo;
                lineas.add(datosBase("GENERAL", general) + ";" +
                        general.isAtiendeNinios() + ";" +
                        general.getConsultorioAsignado());
            } else if (odontologo instanceof Ortodoncista) {
                Ortodoncista ortodoncista = (Ortodoncista) odontologo;
                lineas.add(datosBase("ORTODONCISTA", ortodoncista) + ";" +
                        limpiar(ortodoncista.getTipoBrackets()));
            } else if (odontologo instanceof Endodoncista) {
                Endodoncista endodoncista = (Endodoncista) odontologo;
                lineas.add(datosBase("ENDODONCISTA", endodoncista) + ";" +
                        endodoncista.isUsaMicroscopio());
            }
        }

        escribirArchivo(ruta, lineas);
    }

    public void cargarDesdeArchivo(String ruta) {
        Path path = Paths.get(ruta);

        if (!Files.exists(path)) {
            return;
        }

        try {
            odontologos.clear();
            generadorId = 1L;

            List<String> lineas = Files.readAllLines(path);

            for (String linea : lineas) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 7) {
                    throw new IllegalStateException("Linea invalida en odontologos: " + linea);
                }

                String tipo = datos[0];
                Long id = Long.parseLong(datos[1]);
                String nombre = datos[2];
                String apellido = datos[3];
                String matricula = datos[4];
                double salarioBase = Double.parseDouble(datos[5]);
                Odontologo odontologo;

                if (tipo.equals("GENERAL")) {
                    if (datos.length < 8) {
                        throw new IllegalStateException("Linea invalida en odontologos: " + linea);
                    }

                    odontologo = new OdontologoGeneral(
                            id,
                            nombre,
                            apellido,
                            matricula,
                            salarioBase,
                            Boolean.parseBoolean(datos[6]),
                            Integer.parseInt(datos[7])
                    );
                } else if (tipo.equals("ORTODONCISTA")) {
                    odontologo = new Ortodoncista(
                            id,
                            nombre,
                            apellido,
                            matricula,
                            salarioBase,
                            datos[6]
                    );
                } else if (tipo.equals("ENDODONCISTA")) {
                    odontologo = new Endodoncista(
                            id,
                            nombre,
                            apellido,
                            matricula,
                            salarioBase,
                            Boolean.parseBoolean(datos[6])
                    );
                } else {
                    throw new IllegalStateException("Tipo de odontologo invalido: " + tipo);
                }

                guardarConId(odontologo);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de odontologos.", e);
        }
    }

    private String datosBase(String tipo, Odontologo odontologo) {
        return tipo + ";" +
                odontologo.getId() + ";" +
                limpiar(odontologo.getNombre()) + ";" +
                limpiar(odontologo.getApellido()) + ";" +
                limpiar(odontologo.getMatricula()) + ";" +
                odontologo.getSalarioBase();
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
            throw new IllegalStateException("No se pudo guardar el archivo de odontologos.", e);
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
