package repositorio;

import java.util.List;

public interface IRepositorio<T> {
    T guardar(T t);

    T buscarPorId(Long id);

    void eliminarPorId(Long id);

    void actualizar(T t);

    List<T> listarTodos();
}