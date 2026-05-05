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

        // 1. Identificar al Paciente por DNI (Estilo Profe: limpio)
        String dni = vista.pedirDatoString("Ingrese DNI del paciente");
        Paciente paciente = servicioPaciente.buscarPorDni(dni);

        if (paciente == null) {
            vista.mostrarMensaje("Error: El paciente no existe. Regístrelo primero.");
            vista.pausar();
            return;
        }

        // 2. Elegir Odontólogo por Especialidad (Lógica delegada al servicio)
        vista.mostrarMensaje("\nSeleccione Especialidad: 1. General | 2. Ortodoncia | 3. Endodoncia");
        int esp = vista.pedirDatoInt("Opción");

        List<Odontologo> filtrados = servicioOdontologo.obtenerPorEspecialidad(esp);

        if (filtrados.isEmpty()) {
            vista.mostrarMensaje("No hay profesionales disponibles en esa especialidad.");
            vista.pausar();
            return;
        }

        vista.mostrarMensaje("\nOdontólogos disponibles:");
        for (Odontologo o : filtrados) {
            vista.mostrarMensaje("ID: " + o.getId() + " | Dr/a. " + o.getApellido() + " " + o.getNombre());
        }

        Long idOdon = vista.pedirDatoLong("Ingrese el ID del odontólogo elegido");
        Odontologo odontologo = servicioOdontologo.buscarPorId(idOdon);

        if (odontologo == null) {
            vista.mostrarMensaje("ID de odontólogo inválido.");
            vista.pausar();
            return;
        }

        // 3. Fecha y Hora
        String fechaStr = vista.pedirDatoString("Fecha (AAAA-MM-DD)");
        LocalDate fecha = LocalDate.parse(fechaStr);
        String horaStr = vista.pedirDatoString("Hora (HH:MM)");
        LocalTime hora = LocalTime.parse(horaStr);

        // 4. VALIDACIÓN DE OCUPADO (Delegada al ServicioTurno con el bucle for)
        if (servicioTurno.validarOcupado(idOdon, fecha, hora)) {
            vista.mostrarMensaje("Error: El odontólogo ya tiene un turno en ese horario.");
        } else {
            // 5. Datos específicos de Urgencia y Registro
            String intStr = vista.pedirDatoString("¿Requiere intervención? (si/no)");
            boolean intervencion = intStr.equalsIgnoreCase("si");

            // Creamos el objeto (el ID es null porque se genera en el Repo/DB)
            TurnoUrgente nuevo = new TurnoUrgente(null, paciente, odontologo, fecha, hora,
                    EstadoTurno.PENDIENTE, 15000.0, intervencion, 0.0);

            if (servicioTurno.registrar(nuevo) != null) {
                vista.mostrarMensaje("\n¡Turno reservado con éxito!");
                vista.mostrarMensaje("Paciente: " + paciente.getNombre() + " " + paciente.getApellido());
                vista.mostrarMensaje("Monto base a cobrar: $" + nuevo.calculaPrecioFinal());
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

}