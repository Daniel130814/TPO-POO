package controlador;

import modelo.Endodoncista;
import modelo.Odontologo;
import modelo.OdontologoGeneral;
import modelo.Ortodoncista;
import servicio.ServicioOdontologo;
import servicio.ServicioTurno;
import vista.VistaClinica;

import java.util.List;

public class OdontologoController {

    private ServicioOdontologo servicioOdontologo;
    private ServicioTurno servicioTurno;
    private VistaClinica vista;

    public OdontologoController(VistaClinica vista, ServicioOdontologo servicioOdontologo, ServicioTurno servicioTurno) {
        this.vista = vista;
        this.servicioOdontologo = servicioOdontologo;
        this.servicioTurno = servicioTurno;
    }

    public void iniciar() {
        boolean enMenu = true;
        while (enMenu) {
            int opcion = vista.mostrarMenuOdontologos();
            switch (opcion) {
                case 1:
                    registrarOdontologo();
                    break;
                case 2:
                    buscarOdontologoPorId();
                    break;
                case 3:
                    buscarOdontologoPorMatricula();
                    break;
                case 4:
                    listarOdontologos();
                    break;
                case 5:
                    eliminarOdontologo();
                    break;
                case 0:
                    enMenu = false;
                    break;
                default:
                    vista.mostrarMensaje("Opcion invalida.");
            }
        }
    }

    private void registrarOdontologo() {
        try {
            vista.mostrarMensaje("\n-- Registro de Odontologo --");

            String nombre = vista.pedirDatoString("Nombre");
            String apellido = vista.pedirDatoString("Apellido");
            String matricula = vista.pedirDatoString("Matricula");

            vista.mostrarMensaje(
                    "\nEspecialidad: 1. General | 2. Ortodoncia | 3. Endodoncia"
            );

            int opcion = vista.pedirDatoInt("Opcion");

            Odontologo nuevo;
            double sueldoBase = 50000.0;

            switch (opcion) {
                case 1:
                    nuevo = new OdontologoGeneral(null, nombre, apellido, matricula, sueldoBase, true, 1);
                    break;
                case 2:
                    nuevo = new Ortodoncista(null, nombre, apellido, matricula, sueldoBase, "Brackets");
                    break;
                case 3:
                    nuevo = new Endodoncista(null, nombre, apellido, matricula, sueldoBase, true);
                    break;
                default:
                    vista.mostrarMensaje("Opcion no valida.");
                    vista.pausar();
                    return;
            }

            servicioOdontologo.registrar(nuevo);

            vista.mostrarMensaje("Odontologo registrado exitosamente.");

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarOdontologoPorId() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID a buscar");
            Odontologo o = servicioOdontologo.buscarPorId(id);

            vista.mostrarMensaje(
                    "Encontrado: Dr/a. " +
                            o.getNombre() + " " +
                            o.getApellido() +
                            " [Matricula: " +
                            o.getMatricula() + "]"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarOdontologoPorMatricula() {
        try {
            String mat = vista.pedirDatoString("Ingrese Matricula a buscar");
            Odontologo o = servicioOdontologo.buscarPorMatricula(mat);

            vista.mostrarMensaje(
                    "Encontrado: Dr/a. " +
                            o.getNombre() + " " +
                            o.getApellido() +
                            " [ID: " + o.getId() + "]"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void listarOdontologos() {
        List<Odontologo> lista = servicioOdontologo.listarTodos();

        if (lista.isEmpty()) {
            vista.mostrarMensaje("No hay odontologos registrados.");
        } else {
            vista.mostrarMensaje("\n--- LISTADO DE ODONTOLOGOS ---");
            for (Odontologo o : lista) {
                vista.mostrarMensaje(
                        "ID: " + o.getId() +
                                " | Dr/a. " + o.getNombre() +
                                " " + o.getApellido() +
                                " | Matricula: " + o.getMatricula()
                );
            }
        }

        vista.pausar();
    }

    private void eliminarOdontologo() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID del odontologo a eliminar");

            servicioOdontologo.buscarPorId(id);

            if (servicioTurno.tieneTurnosPendientesOdontologo(id)) {
                vista.mostrarMensaje(
                        "No se puede eliminar: el profesional tiene turnos pendientes."
                );
            } else {
                servicioOdontologo.eliminarPorId(id);
                vista.mostrarMensaje("Odontologo eliminado correctamente.");
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }
}
