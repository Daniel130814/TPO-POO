package vista;

import java.util.Scanner;

public class VistaClinica {
    private Scanner scanner;

    public VistaClinica() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public int mostrarMenuInicial() {
        System.out.println("\n=== MENÚ PRINCIPAL DE LA CLÍNICA ===");
        System.out.println("1. Registrar nuevo Paciente");
        System.out.println("2. Listar todos los Pacientes");
        System.out.println("3. Registrar nuevo Odontólogo");
        System.out.println("4. Listar todos los Odontólogos");
        System.out.println("5. Asignar Turno");
        System.out.println("0. Salir del sistema");
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        return opcion;
    }

    public String pedirDatoString(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine();
    }

    public Long pedirDatoLong(String mensaje) {
        System.out.print(mensaje + ": ");
        Long dato = scanner.nextLong();
        scanner.nextLine(); // Limpiar el buffer
        return dato;
    }

    public int pedirDatoInt(String mensaje) {
        System.out.print(mensaje + ": ");
        int dato = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
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