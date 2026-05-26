package servicio;

import Exceptions.DatoInvalidoException;
import Exceptions.MatriculaDuplicadaException;
import Exceptions.OdontologoNoEncontrado;
import modelo.*;
import modelo.Odontologo;
import repositorio.IRepositorio;
import repositorio.RepositorioOdontologo;

import java.util.ArrayList;
import java.util.List;

public class ServicioOdontologo implements IService<Odontologo> {

    private IRepositorio<Odontologo> odontologoIRepositorio;

    public ServicioOdontologo() {
        this.odontologoIRepositorio = new RepositorioOdontologo();
    }

    @Override
    public Odontologo registrar(Odontologo odontologo)
            throws DatoInvalidoException, MatriculaDuplicadaException {

        if (odontologo.getMatricula() == null ||
                odontologo.getMatricula().trim().isEmpty()) {
            throw new DatoInvalidoException(
                    "La matrícula del odontólogo es obligatoria."
            );
        }

        for (Odontologo o : odontologoIRepositorio.listarTodos()) {
            if (o.getMatricula().equals(odontologo.getMatricula())) {
                throw new MatriculaDuplicadaException(
                        "Ya existe un profesional registrado con matrícula: "
                                + odontologo.getMatricula()
                );
            }
        }

        return odontologoIRepositorio.guardar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id)
            throws OdontologoNoEncontrado {

        Odontologo odontologo = odontologoIRepositorio.buscarPorId(id);

        if (odontologo == null) {
            throw new OdontologoNoEncontrado(
                    "No existe un odontólogo con ID: " + id
            );
        }

        return odontologo;
    }

    @Override
    public void eliminarPorId(Long id)
            throws OdontologoNoEncontrado {

        buscarPorId(id);
        odontologoIRepositorio.eliminarPorId(id);
    }

    @Override
    public void actualizar(Odontologo odontologoModificado)
            throws OdontologoNoEncontrado {

        buscarPorId(odontologoModificado.getId());
        odontologoIRepositorio.actualizar(odontologoModificado);
    }

    @Override
    public List<Odontologo> listarTodos() {
        return odontologoIRepositorio.listarTodos();
    }

    public Odontologo buscarPorMatricula(String matricula)
            throws OdontologoNoEncontrado {

        for (Odontologo o : odontologoIRepositorio.listarTodos()) {
            if (o.getMatricula().equals(matricula)) {
                return o;
            }
        }

        throw new OdontologoNoEncontrado(
                "No existe un odontólogo con matrícula: " + matricula
        );
    }

    public List<Odontologo> obtenerPorEspecialidad(int opcion) {
        List<Odontologo> filtrados = new ArrayList<>();

        for (Odontologo o : odontologoIRepositorio.listarTodos()) {
            if ((opcion == 1 && o instanceof OdontologoGeneral) ||
                    (opcion == 2 && o instanceof Ortodoncista) ||
                    (opcion == 3 && o instanceof Endodoncista)) {

                filtrados.add(o);
            }
        }

        return filtrados;
    }
}
