package vista;

import java.util.Scanner;

public class VistaClinica {
    private Scanner scanner;

    public VistaClinica() {
        this.scanner = new Scanner(System.in);
    }

    // ==========================================
    //            MENÚS DE LA APLICACIÓN
    // ==========================================

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE GESTIÓN CLÍNICA ===");
        System.out.println("1. Gestión de Pacientes");
        System.out.println("2. Gestión de Odontólogos");
        System.out.println("3. Gestión de Turnos");
        System.out.println("0. Salir del sistema");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public int mostrarMenuPacientes() {
        System.out.println("\n--- GESTIÓN DE PACIENTES ---");
        System.out.println("1. Registrar nuevo Paciente");
        System.out.println("2. Buscar Paciente por ID");
        System.out.println("3. Buscar Paciente por DNI");
        System.out.println("4. Listar todos los Pacientes");
        System.out.println("5. Eliminar Paciente");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public int mostrarMenuOdontologos() {
        System.out.println("\n--- GESTIÓN DE ODONTÓLOGOS ---");
        System.out.println("1. Registrar nuevo Odontólogo");
        System.out.println("2. Buscar Odontólogo por ID");
        System.out.println("3. Buscar Odontólogo por Matrícula");
        System.out.println("4. Listar todos los Odontólogos");
        System.out.println("5. Eliminar Odontólogo");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public int mostrarMenuTurnos() {
        System.out.println("\n--- GESTIÓN DE TURNOS ---");
        System.out.println("1. Reservar Turno (Urgencia)");
        System.out.println("2. Buscar Turno por ID");
        System.out.println("3. Listar todos los Turnos");
        System.out.println("4. Cancelar Turno");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    // ==========================================
    //     MÉTODOS UTILITARIOS (Herramientas)
    // ==========================================

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public String pedirDatoString(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine();
    }

    public Long pedirDatoLong(String mensaje) {
        System.out.print(mensaje + ": ");
        while (!scanner.hasNextLong()) {
            System.out.print("Error. Ingrese un número válido: ");
            scanner.next();
        }
        Long dato = scanner.nextLong();
        scanner.nextLine();
        return dato;
    }

    public int pedirDatoInt(String mensaje) {
        System.out.print(mensaje + ": ");
        while (!scanner.hasNextInt()) {
            System.out.print("Error. Ingrese un número válido: ");
            scanner.next();
        }
        int dato = scanner.nextInt();
        scanner.nextLine();
        return dato;
    }

    public void pausar() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    public void cerrar() {
        scanner.close();
    }
}