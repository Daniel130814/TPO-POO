package controller;

import modelo.Odontologo;
import servicio.ServicioOdontologo;

import java.util.List;

public class OdontologoController {
    private final ServicioOdontologo servicioOdontologo;

    public OdontologoController(ServicioOdontologo servicioOdontologo) {
        this.servicioOdontologo = servicioOdontologo;
    }

    public Odontologo registrar(Odontologo odontologo) {
        return servicioOdontologo.registrar(odontologo);
    }

    public Odontologo buscarPorId(Long id) {
        return servicioOdontologo.buscarPorId(id);
    }

    public Odontologo buscarPorMatricula(String matricula) {
        return servicioOdontologo.buscarPorMatricula(matricula);
    }

    public void actualizar(Odontologo odontologo) {
        servicioOdontologo.actualizar(odontologo);
    }

    public void eliminarPorId(Long id) {
        servicioOdontologo.eliminarPorId(id);
    }

    public List<Odontologo> listarTodos() {
        return servicioOdontologo.listarTodos();
    }

    public List<Odontologo> obtenerPorEspecialidad(int opcion) {
        return servicioOdontologo.obtenerPorEspecialidad(opcion);
    }
}
