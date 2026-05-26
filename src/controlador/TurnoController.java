package controlador;

import modelo.*;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;
import vista.VistaClinica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TurnoController {

    private ServicioTurno servicioTurno;
    private ServicioPaciente servicioPaciente;
    private ServicioOdontologo servicioOdontologo;
    private VistaClinica vista;

    public TurnoController(VistaClinica vista, ServicioTurno servicioTurno, ServicioPaciente servicioPaciente, ServicioOdontologo servicioOdontologo) {
        this.vista = vista;
        this.servicioTurno = servicioTurno;
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
    }

    public void iniciar() {
        boolean enMenu = true;
        while (enMenu) {
            int opcion = vista.mostrarMenuTurnos();
            switch (opcion) {
                case 1: reservarTurno(); break;
                case 2: buscarTurnoPorId(); break;
                case 3: listarTurnos(); break;
                case 4: cancelarTurno(); break;
                case 0: enMenu = false; break;
                default: vista.mostrarMensaje("Opción inválida.");
            }
        }
    }

    private void reservarTurno() {
        try {
            vista.mostrarMensaje("\n-- Reserva de Turno (Urgencia) --");

            String dni = vista.pedirDatoString("Ingrese DNI del paciente");
            Paciente paciente = servicioPaciente.buscarPorDni(dni);

            vista.mostrarMensaje("\nSeleccione Especialidad:");
            vista.mostrarMensaje("1. General | 2. Ortodoncia | 3. Endodoncia");

            int esp = vista.pedirDatoInt("Opción");

            List<Odontologo> filtrados =
                    servicioOdontologo.obtenerPorEspecialidad(esp);

            if (filtrados.isEmpty()) {
                vista.mostrarMensaje(
                        "No hay profesionales disponibles en esa especialidad."
                );
                vista.pausar();
                return;
            }

            vista.mostrarMensaje("\nOdontólogos disponibles:");

            for (Odontologo o : filtrados) {
                vista.mostrarMensaje(
                        "ID: " + o.getId() +
                                " | Dr/a. " + o.getApellido() +
                                " " + o.getNombre()
                );
            }

            Long idOdon =
                    vista.pedirDatoLong("Ingrese el ID del odontólogo elegido");

            Odontologo odontologo =
                    servicioOdontologo.buscarPorId(idOdon);

            LocalDate fecha = LocalDate.parse(
                    vista.pedirDatoString("Fecha (AAAA-MM-DD)")
            );

            LocalTime hora = LocalTime.parse(
                    vista.pedirDatoString("Hora (HH:MM)")
            );

            boolean intervencion = vista
                    .pedirDatoString("¿Requiere intervención? (si/no)")
                    .equalsIgnoreCase("si");

            TurnoUrgente nuevo = new TurnoUrgente(
                    null,
                    paciente,
                    odontologo,
                    fecha,
                    hora,
                    EstadoTurno.PENDIENTE,
                    15000.0,
                    intervencion,
                    0.0
            );

            servicioTurno.registrar(nuevo);

            vista.mostrarMensaje("\n¡Turno reservado con éxito!");
            vista.mostrarMensaje(
                    "Paciente: " +
                            paciente.getNombre() + " " +
                            paciente.getApellido()
            );

            vista.mostrarMensaje(
                    "Monto base a cobrar: $" +
                            nuevo.calculaPrecioFinal()
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarTurnoPorId() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID del Turno");

            Turno t = servicioTurno.buscarPorId(id);

            vista.mostrarMensaje(
                    "Turno " + id + ": " +
                            t.getPaciente().getApellido() +
                            " con " +
                            t.getOdontologo().getApellido()
            );

            vista.mostrarMensaje(
                    "Fecha: " + t.getFecha() +
                            " Hora: " + t.getHora() +
                            " | Estado: " + t.getEstado()
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void listarTurnos() {
        List<Turno> lista = servicioTurno.listarTodos();
        if (lista.isEmpty()) {
            vista.mostrarMensaje("No hay turnos registrados.");
        } else {
            vista.mostrarMensaje("\n--- LISTADO DE TURNOS ---");
            for (Turno t : lista) {
                vista.mostrarMensaje("ID: " + t.getId() + " | " + t.getFecha() + " " + t.getHora() + " | Paciente: " + t.getPaciente().getApellido() + " | Estado: " + t.getEstado());
            }
        }
        vista.pausar();
    }

    private void cancelarTurno() {
        try {
            Long id = vista.pedirDatoLong("ID del turno a cancelar");

            Turno t = servicioTurno.buscarPorId(id);

            t.setEstado(EstadoTurno.CANCELADO);
            servicioTurno.actualizar(t);

            vista.mostrarMensaje("Turno cancelado correctamente.");

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

}