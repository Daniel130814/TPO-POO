import modelo.*;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Inicializamos nuestros servicios
        ServicioPaciente servicioPaciente = new ServicioPaciente();
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo();
        ServicioTurno servicioTurno = new ServicioTurno();

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        System.out.println("=== BIENVENIDO AL SISTEMA DE LA CLÍNICA ===");

        while (opcion != 0) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Registrar nuevo Paciente");
            System.out.println("2. Listar todos los Pacientes");
            System.out.println("3. Registrar nuevo Odontólogo (General)");
            System.out.println("4. Listar todos los Odontólogos");
            System.out.println("5. Asignar Turno");
            System.out.println("0. Salir del sistema");
            System.out.print("Ingrese una opción: ");

            // Leemos la opción elegida
            opcion = scanner.nextInt();
            scanner.nextLine(); // ¡SÚPER IMPORTANTE! Esto limpia el "Enter" que queda en el teclado

            switch (opcion) {
                case 1:
                    System.out.println("\n-- Datos del Paciente --");
                    System.out.print("Nombre: ");
                    String nombrePac = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellidoPac = scanner.nextLine();
                    System.out.print("DNI: ");
                    String dni = scanner.nextLine();

                    System.out.print("Calle del domicilio: ");
                    String calle = scanner.nextLine();
                    System.out.print("Número del domicilio: ");
                    int numero = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer de nuevo

                    Domicilio dom = new Domicilio(calle, numero, "CABA", "Buenos Aires");
                    Paciente nuevoPaciente = new Paciente(nombrePac, apellidoPac, dni, dom);

                    // Se lo pasamos al servicio para que lo valide y lo guarde
                    servicioPaciente.registrar(nuevoPaciente);
                    break;

                case 2:
                    System.out.println("\n--- Lista de Pacientes ---");
                    for (Paciente p : servicioPaciente.listarTodos()) {
                        System.out.println("ID: " + p.getId() + " | Nombre: " + p.getNombre() + " " + p.getApellido() + " | DNI: " + p.getDni());
                    }
                    break;

                case 3:
                    System.out.println("\n-- Datos del Odontólogo --");
                    System.out.print("Nombre: ");
                    String nombreOdon = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellidoOdon = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();

                    // Creamos uno General por defecto para simplificar el ingreso de datos
                    OdontologoGeneral nuevoOdon = new OdontologoGeneral(null, nombreOdon, apellidoOdon, matricula, 50000.0, true, 1);
                    servicioOdontologo.registrar(nuevoOdon);
                    break;

                case 4:
                    System.out.println("\n--- Lista de Odontólogos ---");
                    for (Odontologo o : servicioOdontologo.listarTodos()) {
                        System.out.println("ID: " + o.getId() + " | Nombre: " + o.getNombre() + " " + o.getApellido() + " | Matrícula: " + o.getMatricula());
                    }
                    break;

                case 5:
                    System.out.println("\n-- Asignar Turno --");
                    System.out.print("Ingrese el ID del Paciente: ");
                    Long idPac = scanner.nextLong();
                    System.out.print("Ingrese el ID del Odontólogo: ");
                    Long idOdon = scanner.nextLong();
                    scanner.nextLine(); // Limpiar el buffer

                    // Buscamos si esos IDs realmente existen en el sistema
                    Paciente pTurno = servicioPaciente.buscarPorId(idPac);
                    Odontologo oTurno = servicioOdontologo.buscarPorId(idOdon);

                    if (pTurno != null && oTurno != null) {
                        System.out.print("Motivo de la urgencia: ");
                        String motivo = scanner.nextLine();

                        // Creamos un turno urgente con fecha de hoy y hora fija para agilizar
                        TurnoUrgente nuevoTurno = new TurnoUrgente(null, pTurno, oTurno,
                                LocalDate.now(), LocalTime.now(), "PENDIENTE", 15000.0, motivo);

                        servicioTurno.registrar(nuevoTurno);
                    } else {
                        System.out.println("Error: No se encontró al Paciente o al Odontólogo con los IDs ingresados. Intente nuevamente.");
                    }
                    break;

                case 0:
                    System.out.println("Guardando datos y saliendo del sistema. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción incorrecta. Por favor ingrese un número del 0 al 5.");
            }
        }

        scanner.close(); // Siempre es buena práctica cerrar el scanner al final
    }
}

