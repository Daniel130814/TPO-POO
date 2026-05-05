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
        scanner.nextLine(); // Limpiar el buffer
        return opcion;
    }

    public int mostrarMenuPacientes() {
        System.out.println("\n--- GESTIÓN DE PACIENTES ---");
        System.out.println("1. Registrar nuevo Paciente");
        System.out.println("2. Listar todos los Pacientes");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public int mostrarMenuOdontologos() {
        System.out.println("\n--- GESTIÓN DE ODONTÓLOGOS ---");
        System.out.println("1. Registrar nuevo Odontólogo");
        System.out.println("2. Listar todos los Odontólogos");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    public int mostrarMenuTurnos() {
        System.out.println("\n--- GESTIÓN DE TURNOS ---");
        System.out.println("1. Asignar nuevo Turno");
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
        Long dato = scanner.nextLong();
        scanner.nextLine();
        return dato;
    }

    public int pedirDatoInt(String mensaje) {
        System.out.print(mensaje + ": ");
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