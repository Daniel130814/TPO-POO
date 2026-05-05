package servicio;

import modelo.Odontologo;
import modelo.Odontologo;
import repositorio.IRepositorio;
import repositorio.RepositorioOdontologo;
import java.util.List;

public class ServicioOdontologo implements IService<Odontologo> {

    private IRepositorio<Odontologo> odontologoIRepositorio;

    public ServicioOdontologo() {
        this.odontologoIRepositorio = new RepositorioOdontologo();
    }

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        if (odontologo.getMatricula()==null|| odontologo.getMatricula().trim().isEmpty()){
            System.out.println("Error: La matrícula del odontólogo es obligatoria.");
            return null;
        }
        List<Odontologo> odontologosExistentes= odontologoIRepositorio.listarTodos();
        for (Odontologo o : odontologosExistentes){
            if (o.getMatricula().equals(odontologo.getMatricula())){
                System.out.println("Error: Ya existe un profesional registrado con la matrícula " + odontologo.getMatricula());
                return null;
            }
        }
        System.out.println("Éxito: Odontólogo registrado correctamente.");
        return odontologoIRepositorio.guardar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return odontologoIRepositorio.buscarPorId(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        odontologoIRepositorio.eliminarPorId(id);
        System.out.println("Odontólogo eliminado exitosamente.");
    }

    @Override
    public void actualizar(Odontologo odontologoModificado) {
        odontologoIRepositorio.actualizar(odontologoModificado);
        System.out.println("Odontólogo actualizado exitosamente.");
    }

    @Override
    public List<Odontologo> listarTodos() {
        return odontologoIRepositorio.listarTodos();
    }

    public Odontologo buscarPorMatricula(String mat) {
        return listarTodos().stream().filter(o -> o.getMatricula().equals(mat)).findFirst().orElse(null);
    }

}
