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

        vista.mostrarMensaje("\n¿Cuál es su especialidad?");
        vista.mostrarMensaje("1. Odontólogo General");
        vista.mostrarMensaje("2. Ortodoncista");
        vista.mostrarMensaje("3. Endodoncista");
        int opcionEspecialidad = vista.pedirDatoInt("Ingrese una opción");

        // Creamos la variable Odontologo (la clase padre) para que pueda guardar cualquiera de los 3 tipos
        Odontologo nuevoOdon = null;

        switch (opcionEspecialidad) {
            case 1:
                nuevoOdon = new OdontologoGeneral(null, nombre, apellido, matricula, 50000.0, true, 1);
                break;
            case 2:
                // Asumo los parámetros base, podés pedirle más datos si tu clase Ortodoncista los necesita
                nuevoOdon = new Ortodoncista(null, nombre, apellido, matricula, 50000.0, "Brackets");
                break;
            case 3:
                // Asumo los parámetros base, podés pedirle más datos si tu clase Endodoncista los necesita
                nuevoOdon = new Endodoncista(null, nombre, apellido, matricula, 50000.0, true);
                break;
            default:
                vista.mostrarMensaje("Especialidad no válida. Se cancela el registro.");
                vista.pausar();
                return;
        }

        // Lo registramos en el servicio
        if (servicioOdontologo.registrar(nuevoOdon) != null) {
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

        // 1. Pedimos y validamos el Paciente
        Long idPac = vista.pedirDatoLong("ID del Paciente");
        Paciente pTurno = servicioPaciente.buscarPorId(idPac);

        if (pTurno == null) {
            vista.mostrarMensaje("Error: No se encontró ningún paciente con ese ID.");
            vista.pausar();
            return; // Cortamos la ejecución acá si no existe
        }

        // 2. Sub-menú de Especialidades
        vista.mostrarMensaje("\n¿Qué especialista necesita?");
        vista.mostrarMensaje("1. Odontólogo General");
        vista.mostrarMensaje("2. Ortodoncista");
        vista.mostrarMensaje("3. Endodoncista");
        int tipoEspecialidad = vista.pedirDatoInt("Ingrese una opción");

        // 3. Filtramos y mostramos los Odontólogos de esa especialidad
        vista.mostrarMensaje("\n--- Odontólogos Disponibles ---");
        boolean hayDisponibles = false;

        for (Odontologo o : servicioOdontologo.listarTodos()) {
            boolean coincide = false;
            // Usamos instanceof para saber de qué tipo es cada odontólogo en la lista
            if (tipoEspecialidad == 1 && o instanceof OdontologoGeneral) coincide = true;
            else if (tipoEspecialidad == 2 && o instanceof Ortodoncista) coincide = true;
            else if (tipoEspecialidad == 3 && o instanceof Endodoncista) coincide = true;

            if (coincide) {
                vista.mostrarMensaje("ID: " + o.getId() + " | Nombre: " + o.getNombre() + " " + o.getApellido());
                hayDisponibles = true;
            }
        }

        if (!hayDisponibles) {
            vista.mostrarMensaje("Lo sentimos, no hay odontólogos registrados para esa especialidad en este momento.");
            vista.pausar();
            return;
        }

        // 4. Pedimos el Odontólogo de la lista mostrada
        Long idOdon = vista.pedirDatoLong("\nIngrese el ID del Odontólogo elegido");
        Odontologo oTurno = servicioOdontologo.buscarPorId(idOdon);

        if (oTurno == null) {
            vista.mostrarMensaje("Error: Odontólogo no encontrado.");
            vista.pausar();
            return;
        }

        // 5. Finalizamos el turno como lo teníamos armado
        String intervencionStr = vista.pedirDatoString("\n¿Requiere intervención? (si/no)");
        boolean requiereIntervencion = intervencionStr.equalsIgnoreCase("si");
        int duracion = vista.pedirDatoInt("Duración estimada en minutos");

        TurnoUrgente turno = new TurnoUrgente(null, pTurno, oTurno, LocalDate.now(), LocalTime.now(), EstadoTurno.PENDIENTE, 15000.0, requiereIntervencion, duracion);

        if (servicioTurno.registrar(turno) != null) {
            vista.mostrarMensaje("¡Turno asignado exitosamente al paciente " + pTurno.getNombre() + "!");
        } else {
            vista.mostrarMensaje("Hubo un error al registrar el turno.");
        }

        vista.pausar();

    }

    private void salir() {
        ejecutando = false;
        vista.mostrarMensaje("Guardando datos y saliendo del sistema. ¡Hasta luego!");
    }
}