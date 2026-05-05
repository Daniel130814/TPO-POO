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
        vista.mostrarMensaje("\n-- Reserva de Turno (Urgencia) --");

        // 1. Identificar Paciente
        Long idPac = vista.pedirDatoLong("ID del Paciente");
        Paciente paciente = servicioPaciente.buscarPorId(idPac);
        if (paciente == null) {
            vista.mostrarMensaje("Error: Paciente no encontrado.");
            return;
        }

        // 2. Elegir Especialidad y Odontólogo
        vista.mostrarMensaje("\nEspecialidad requerida: 1. General | 2. Ortodoncia | 3. Endodoncia");
        int esp = vista.pedirDatoInt("Opción");

        filtrarYMostrarOdontologos(esp);

        Long idOdon = vista.pedirDatoLong("ID del Odontólogo elegido");
        Odontologo odontologo = servicioOdontologo.buscarPorId(idOdon);

        if (odontologo == null) {
            vista.mostrarMensaje("Error: Odontólogo no encontrado.");
            return;
        }

        // 3. Fecha y Hora con Validación de Disponibilidad
        String fechaStr = vista.pedirDatoString("Fecha (AAAA-MM-DD)");
        LocalDate fecha = LocalDate.parse(fechaStr);
        String horaStr = vista.pedirDatoString("Hora (HH:MM)");
        LocalTime hora = LocalTime.parse(horaStr);

        if (validarOcupado(idOdon, fecha, hora)) {
            vista.mostrarMensaje("Error: El odontólogo ya tiene un turno en ese horario.");
        } else {
            String intStr = vista.pedirDatoString("¿Requiere intervención? (si/no)");
            boolean intervencion = intStr.equalsIgnoreCase("si");

            TurnoUrgente nuevo = new TurnoUrgente(null, paciente, odontologo, fecha, hora, EstadoTurno.PENDIENTE, 15000.0, intervencion, 0.0);

            if (servicioTurno.registrar(nuevo) != null) {
                vista.mostrarMensaje("¡Turno reservado con éxito!");
                vista.mostrarMensaje("Precio Final: $" + nuevo.calculaPrecioFinal());
            }
        }
        vista.pausar();
    }

    private void buscarTurnoPorId() {
        Long id = vista.pedirDatoLong("Ingrese ID del Turno");
        Turno t = servicioTurno.buscarPorId(id);
        if (t != null) {
            vista.mostrarMensaje("Turno " + id + ": " + t.getPaciente().getApellido() + " con " + t.getOdontologo().getApellido());
            vista.mostrarMensaje("Fecha: " + t.getFecha() + " Hora: " + t.getHora() + " | Estado: " + t.getEstado());
        } else {
            vista.mostrarMensaje("No existe turno con ese ID.");
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
        Long id = vista.pedirDatoLong("ID del turno a cancelar");
        Turno t = servicioTurno.buscarPorId(id);
        if (t != null) {
            t.setEstado(EstadoTurno.CANCELADO);
            vista.mostrarMensaje("Turno cancelado correctamente.");
        } else {
            vista.mostrarMensaje("No se encontró el turno.");
        }
        vista.pausar();
    }

    // Métodos auxiliares de lógica
    private void filtrarYMostrarOdontologos(int opcion) {
        for (Odontologo o : servicioOdontologo.listarTodos()) {
            if ((opcion == 1 && o instanceof OdontologoGeneral) ||
                    (opcion == 2 && o instanceof Ortodoncista) ||
                    (opcion == 3 && o instanceof Endodoncista)) {
                vista.mostrarMensaje("ID: " + o.getId() + " | Dr/a. " + o.getApellido());
            }
        }
    }

    private boolean validarOcupado(Long idOdon, LocalDate f, LocalTime h) {
        return servicioTurno.listarTodos().stream()
                .anyMatch(t -> t.getOdontologo().getId().equals(idOdon)
                        && t.getFecha().equals(f)
                        && t.getHora().equals(h)
                        && t.getEstado() != EstadoTurno.CANCELADO);
    }
}