package controlador;

import modelo.EstadoTurno;
import modelo.Odontologo;
import modelo.Paciente;
import modelo.Turno;
import modelo.TurnoUrgente;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;
import vista.VistaClinica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
                case 1:
                    reservarTurno();
                    break;
                case 2:
                    buscarTurnoPorId();
                    break;
                case 3:
                    listarTurnos();
                    break;
                case 4:
                    cancelarTurno();
                    break;
                case 5:
                    buscarTurnosPorFecha();
                    break;
                case 6:
                    buscarTurnosPorEstado();
                    break;
                case 7:
                    buscarTurnosPorPaciente();
                    break;
                case 8:
                    buscarTurnosPorOdontologo();
                    break;
                case 9:
                    buscarTurnosPorRango();
                    break;
                case 0:
                    enMenu = false;
                    break;
                default:
                    vista.mostrarMensaje("Opcion invalida.");
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

            int esp = vista.pedirDatoInt("Opcion");

            List<Odontologo> filtrados =
                    servicioOdontologo.obtenerPorEspecialidad(esp);

            if (filtrados.isEmpty()) {
                vista.mostrarMensaje(
                        "No hay profesionales disponibles en esa especialidad."
                );
                vista.pausar();
                return;
            }

            vista.mostrarMensaje("\nOdontologos disponibles:");

            for (Odontologo o : filtrados) {
                vista.mostrarMensaje(
                        "ID: " + o.getId() +
                                " | Dr/a. " + o.getApellido() +
                                " " + o.getNombre()
                );
            }

            Long idOdon =
                    vista.pedirDatoLong("Ingrese el ID del odontologo elegido");

            Odontologo odontologo =
                    servicioOdontologo.buscarPorId(idOdon);

            LocalDate fecha = pedirFecha("Fecha (AAAA-MM-DD)");

            LocalTime hora = pedirHora("Hora (HH:MM)");

            boolean intervencion = vista
                    .pedirDatoString("Requiere intervencion? (si/no)")
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

            vista.mostrarMensaje("\nTurno reservado con exito!");
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
        mostrarListadoTurnos(lista, "\n--- LISTADO DE TURNOS ---");
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

    private void buscarTurnosPorFecha() {
        try {
            LocalDate fecha = pedirFecha("Fecha a buscar (AAAA-MM-DD)");

            List<Turno> lista = servicioTurno.buscarPorFecha(fecha);
            mostrarListadoTurnos(lista, "\n--- TURNOS DEL " + fecha + " ---");

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarTurnosPorEstado() {
        try {
            EstadoTurno estado = pedirEstadoTurno();

            List<Turno> lista = servicioTurno.buscarPorEstado(estado);
            mostrarListadoTurnos(lista, "\n--- TURNOS EN ESTADO " + estado + " ---");

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarTurnosPorPaciente() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID del paciente");
            Paciente paciente = servicioPaciente.buscarPorId(id);

            List<Turno> lista = servicioTurno.buscarPorPaciente(id);
            mostrarListadoTurnos(
                    lista,
                    "\n--- TURNOS DE " + paciente.getNombre() + " " + paciente.getApellido() + " ---"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarTurnosPorOdontologo() {
        try {
            Long id = vista.pedirDatoLong("Ingrese ID del odontologo");
            Odontologo odontologo = servicioOdontologo.buscarPorId(id);

            List<Turno> lista = servicioTurno.buscarPorOdontologo(id);
            mostrarListadoTurnos(
                    lista,
                    "\n--- TURNOS DE DR/A. " + odontologo.getNombre() + " " + odontologo.getApellido() + " ---"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void buscarTurnosPorRango() {
        try {
            LocalDate desde = pedirFecha("Fecha desde (AAAA-MM-DD)");

            LocalDate hasta = pedirFecha("Fecha hasta (AAAA-MM-DD)");

            List<Turno> lista = servicioTurno.buscarPorRango(desde, hasta);
            mostrarListadoTurnos(
                    lista,
                    "\n--- TURNOS ENTRE " + desde + " Y " + hasta + " ---"
            );

        } catch (Exception e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        vista.pausar();
    }

    private void mostrarListadoTurnos(List<Turno> lista, String titulo) {
        if (lista.isEmpty()) {
            vista.mostrarMensaje("No hay turnos para mostrar.");
        } else {
            vista.mostrarMensaje(titulo);

            for (Turno t : lista) {
                vista.mostrarMensaje(
                        "ID: " + t.getId() +
                                " | " + t.getFecha() +
                                " " + t.getHora() +
                                " | Paciente: " + t.getPaciente().getApellido() +
                                " | Odontologo: " + t.getOdontologo().getApellido() +
                                " | Estado: " + t.getEstado()
                );
            }
        }
    }

    private LocalDate pedirFecha(String mensaje) {
        try {
            return LocalDate.parse(vista.pedirDatoString(mensaje));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha invalido. Use AAAA-MM-DD.");
        }
    }

    private LocalTime pedirHora(String mensaje) {
        try {
            return LocalTime.parse(vista.pedirDatoString(mensaje));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora invalido. Use HH:MM.");
        }
    }

    private EstadoTurno pedirEstadoTurno() {
        vista.mostrarMensaje("\nEstados disponibles:");
        vista.mostrarMensaje("1. PENDIENTE");
        vista.mostrarMensaje("2. CONFIRMADO");
        vista.mostrarMensaje("3. CANCELADO");
        vista.mostrarMensaje("4. COMPLETADO");

        int opcion = vista.pedirDatoInt("Seleccione estado");

        switch (opcion) {
            case 1:
                return EstadoTurno.PENDIENTE;
            case 2:
                return EstadoTurno.CONFIRMADO;
            case 3:
                return EstadoTurno.CANCELADO;
            case 4:
                return EstadoTurno.COMPLETADO;
            default:
                throw new IllegalArgumentException("Opcion de estado no valida.");
        }
    }
}
