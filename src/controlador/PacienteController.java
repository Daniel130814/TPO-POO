package controlador;

import modelo.Domicilio;
import modelo.Paciente;
import servicio.ServicioPaciente;
import servicio.ServicioTurno; // Importante para la validación
import vista.VistaClinica;
import java.util.List;

public class PacienteController {

    private ServicioPaciente servicioPaciente;
    private ServicioTurno servicioTurno; // Agregado para validar bajas
    private VistaClinica vista;

    public PacienteController(VistaClinica vista, ServicioPaciente servicioPaciente, ServicioTurno servicioTurno) {
        this.vista = vista;
        this.servicioPaciente = servicioPaciente;
        this.servicioTurno = servicioTurno;
    }

    public void iniciar() {
        boolean enMenu = true;
        while (enMenu) {
            int opcion = vista.mostrarMenuPacientes();
            switch (opcion) {
                case 1: registrarPaciente(); break;
                case 2: buscarPacientePorId(); break;
                case 3: buscarPacientePorDni(); break;
                case 4: listarPacientes(); break;
                case 5: eliminarPaciente(); break;
                case 0: enMenu = false; break;
                default: vista.mostrarMensaje("Opción inválida.");
            }
        }
    }

    private void registrarPaciente() {
        try {
            vista.mostrarMensaje("\n-- Registro de Paciente --");

            String nombre = vista.pedirDatoString("Nombre");
            String apellido = vista.pedirDatoString("Apellido");
            String dni = vista.pedirDatoString("DNI");
            String email = vista.pedirDatoString("Email");
            String calle = vista.pedirDatoString("Calle");
            int nro = vista.pedirDatoInt("Número");
            String localidad = vista.pedirDatoString("Localidad");
            String provincia = vista.pedirDatoString("Provincia");

            Domicilio dom = new Domicilio(calle, nro, localidad, provincia);

            Paciente nuevo = new Paciente(nombre, apellido, dni, email, dom);

            servicioPaciente.registrar(nuevo);

            vista.mostrarMensaje("Paciente registrado exitosamente.");

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarPacientePorId() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID a buscar");

            Paciente p = servicioPaciente.buscarPorId(id);

            vista.mostrarMensaje(
                    "Encontrado: " +
                            p.getNombre() + " " +
                            p.getApellido() +
                            " [DNI: " + p.getDni() + "]"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarPacientePorDni() {
        try {
            String dni = vista.pedirDatoString("Ingrese DNI a buscar");

            Paciente p = servicioPaciente.buscarPorDni(dni);

            vista.mostrarMensaje(
                    "Encontrado: " +
                            p.getNombre() + " " +
                            p.getApellido() +
                            " [ID: " + p.getId() + "]"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void listarPacientes() {
        List<Paciente> lista = servicioPaciente.listarOrdenadosPorApellido();
        if (lista.isEmpty()) {
            vista.mostrarMensaje("No hay pacientes registrados.");
        } else {
            vista.mostrarMensaje("\n--- LISTADO DE PACIENTES ---");
            for (Paciente p : lista) {
                vista.mostrarMensaje("ID: " + p.getId() + " | " + p.getNombre() + " " + p.getApellido() + " | DNI: " + p.getDni());
            }
        }
        vista.pausar();
    }


    private void eliminarPaciente() {
        try {
            Long id = vista.pedirDatoLong(
                    "Ingrese ID del paciente a eliminar"
            );

            servicioPaciente.buscarPorId(id);

            if (servicioTurno.tieneTurnosPendientes(id)) {
                vista.mostrarMensaje(
                        "No se puede eliminar: el paciente tiene turnos pendientes."
                );
            } else {
                servicioPaciente.eliminarPorId(id);
                vista.mostrarMensaje(
                        "Paciente eliminado correctamente."
                );
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }
}