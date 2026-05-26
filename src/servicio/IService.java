package servicio;

import java.util.List;

public interface IService<T> {

    T registrar(T entidad) throws Exception;

    T buscarPorId(Long id) throws Exception;

    void eliminarPorId(Long id) throws Exception;

    void actualizar(T entidad) throws Exception;

    List<T> listarTodos();
}