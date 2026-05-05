package servicio;

import java.util.List;

public interface IService<T> {
    T registrar(T t);
    T buscarPorId(Long id); // Usamos Long como exige la clínica
    void eliminarPorId(Long id);
    void actualizar(T t);   // Pasamos el objeto entero para poder modificar sus datos
    List<T> listarTodos();
}