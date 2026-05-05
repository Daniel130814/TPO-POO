package repositorio;

import modelo.Odontologo;
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
        return odontologo; // Devolvemos el objeto guardado
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        // Búsqueda instantánea gracias al casillero del HashMap
        return odontologos.get(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        odontologos.remove(id);
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        // Al hacer put con un ID que ya existe, se sobrescriben los datos viejos con los nuevos
        odontologos.put(odontologo.getId(), odontologo);
    }

    @Override
    public List<Odontologo> listarTodos() {
        // Sacamos todos los valores del mapa y los convertimos en una lista tradicional
        return new ArrayList<>(odontologos.values());
    }
}