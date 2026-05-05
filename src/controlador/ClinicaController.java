package controlador;

import modelo.*;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;
import vista.VistaClinica;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClinicaController implements Runnable {

    private ServicioPaciente servicioPaciente;
    private ServicioOdontologo servicioOdontologo;
    private ServicioTurno servicioTurno;
    private VistaClinica vista;
    private boolean ejecutando;

    public ClinicaController(VistaClinica vista) {
        this.servicioPaciente = new ServicioPaciente();
        this.servicioOdontologo = new ServicioOdontologo();
        this.servicioTurno = new ServicioTurno();
        this.vista = vista;
        this.ejecutando = true;
    }

    @Override
    public void run() {
        vista.mostrarMensaje("=== BIENVENIDO AL SISTEMA DE LA CLÍNICA ===");

        while (ejecutando) {
            int opcion = vista.mostrarMenuInicial();

            switch (opcion) {
                case 1: registrarPaciente(); break;
                case 2: listarPacientes(); break;
                case 3: registrarOdontologo(); break;
                case 4: listarOdontologos(); break;
                case 5: asignarTurno(); break;
                case 0: salir(); break;
                default: vista.mostrarMensaje("Opción inválida.");
            }
        }
        vista.cerrar();
    }

    private void registrarPaciente() {
        vista.mostrarMensaje("\n-- Datos del Paciente --");
        String nombre = vista.pedirDatoString("Nombre");
        String apellido = vista.pedirDatoString("Apellido");
        String dni = vista.pedirDatoString("DNI");

        String calle = vista.pedirDatoString("Calle del domicilio");
        int numero = vista.pedirDatoInt("Número");

        Domicilio dom = new Domicilio(calle, numero, "CABA", "Buenos Aires");
        Paciente paciente = new Paciente(null, nombre, apellido, dni, "email@clinica.com", LocalDate.now(), dom);

        if (servicioPaciente.registrar(paciente) != null) {
            vista.mostrarMensaje("Paciente registrado exitosamente.");
        }
        vista.pausar();
    }

    private void listarPacientes() {
        vista.mostrarMensaje("\n--- Lista de Pacientes ---");
        for (Paciente p : servicioPaciente.listarTodos()) {
            vista.mostrarMensaje("ID: " + p.getId() + " | Nombre: " + p.getNombre() + " " + p.getApellido() + " | DNI: " + p.getDni());
        }
        vista.pausar();
    }

    private void registrarOdontologo() {
        vista.mostrarMensaje("\n-- Datos del Odontólogo --");
        String nombre = vista.pedirDatoString("Nombre");
        String apellido = vista.pedirDatoString("Apellido");
        String matricula = vista.pedirDatoString("Matrícula");

        OdontologoGeneral odon = new OdontologoGeneral(null, nombre, apellido, matricula, 50000.0, true, 1);

        if (servicioOdontologo.registrar(odon) != null) {
            vista.mostrarMensaje("Odontólogo registrado exitosamente.");
        }
        vista.pausar();
    }

    private void listarOdontologos() {
        vista.mostrarMensaje("\n--- Lista de Odontólogos ---");
        for (Odontologo o : servicioOdontologo.listarTodos()) {
            vista.mostrarMensaje("ID: " + o.getId() + " | Nombre: " + o.getNombre() + " " + o.getApellido() + " | Matrícula: " + o.getMatricula());
        }
        vista.pausar();
    }

    private void asignarTurno() {
        vista.mostrarMensaje("\n-- Asignar Turno --");
        Long idPac = vista.pedirDatoLong("ID del Paciente");
        Long idOdon = vista.pedirDatoLong("ID del Odontólogo");

        Paciente pTurno = servicioPaciente.buscarPorId(idPac);
        Odontologo oTurno = servicioOdontologo.buscarPorId(idOdon);

        if (pTurno != null && oTurno != null) {

            // 1. En vez del motivo, le preguntamos tus variables reales:
            String intervencionStr = vista.pedirDatoString("¿Requiere intervención? (si/no)");
            boolean requiereIntervencion = intervencionStr.equalsIgnoreCase("si"); // Si escribe "si", es true. Si no, false.

            int duracion = vista.pedirDatoInt("Duración estimada en minutos");

            // 2. Ahora sí, llamamos al constructor EXACTO como lo tenés en la foto (línea 15)
            // Orden: id, paciente, odontologo, fecha, hora, estado, precioBase, requiereIntervencion, duracion
            TurnoUrgente turno = new TurnoUrgente(null, pTurno, oTurno, LocalDate.now(), LocalTime.now(), EstadoTurno.PENDIENTE, 15000.0, requiereIntervencion, duracion);

            if (servicioTurno.registrar(turno) != null) {
                vista.mostrarMensaje("Turno registrado exitosamente.");
            }
        } else {
            vista.mostrarMensaje("Error: Paciente u Odontólogo no encontrado.");
        }
    }

    private void salir() {
        ejecutando = false;
        vista.mostrarMensaje("Guardando datos y saliendo del sistema. ¡Hasta luego!");
    }
}