package vista;

import java.util.Scanner;

public class VistaClinica {
    private Scanner scanner;

    public VistaClinica() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE GESTION CLINICA ===");
        System.out.println("1. Gestion de Pacientes");
        System.out.println("2. Gestion de Odontologos");
        System.out.println("3. Gestion de Turnos");
        System.out.println("0. Salir del sistema");
        return pedirDatoInt("Ingrese una opcion");
    }

    public int mostrarMenuPacientes() {
        System.out.println("\n--- GESTION DE PACIENTES ---");
        System.out.println("1. Registrar nuevo Paciente");
        System.out.println("2. Buscar Paciente por ID");
        System.out.println("3. Buscar Paciente por DNI");
        System.out.println("4. Listar todos los Pacientes");
        System.out.println("5. Eliminar Paciente");
        System.out.println("0. Volver al menu principal");
        return pedirDatoInt("Ingrese una opcion");
    }

    public int mostrarMenuOdontologos() {
        System.out.println("\n--- GESTION DE ODONTOLOGOS ---");
        System.out.println("1. Registrar nuevo Odontologo");
        System.out.println("2. Buscar Odontologo por ID");
        System.out.println("3. Buscar Odontologo por Matricula");
        System.out.println("4. Listar todos los Odontologos");
        System.out.println("5. Eliminar Odontologo");
        System.out.println("0. Volver al menu principal");
        return pedirDatoInt("Ingrese una opcion");
    }

    public int mostrarMenuTurnos() {
        System.out.println("\n--- GESTION DE TURNOS ---");
        System.out.println("1. Reservar Turno (Urgencia)");
        System.out.println("2. Buscar Turno por ID");
        System.out.println("3. Listar todos los Turnos");
        System.out.println("4. Cancelar Turno");
        System.out.println("5. Buscar Turnos por Fecha");
        System.out.println("6. Buscar Turnos por Estado");
        System.out.println("7. Buscar Turnos por Paciente");
        System.out.println("8. Buscar Turnos por Odontologo");
        System.out.println("9. Buscar Turnos por Rango de Fechas");
        System.out.println("0. Volver al menu principal");
        return pedirDatoInt("Ingrese una opcion");
    }

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
            if (!scanner.hasNext()) {
                throw new IllegalStateException("No se ingreso un numero valido.");
            }
            System.out.print("Error. Ingrese un numero valido: ");
            scanner.next();
        }
        Long dato = scanner.nextLong();
        scanner.nextLine();
        return dato;
    }

    public int pedirDatoInt(String mensaje) {
        System.out.print(mensaje + ": ");
        while (!scanner.hasNextInt()) {
            if (!scanner.hasNext()) {
                throw new IllegalStateException("No se ingreso un numero valido.");
            }
            System.out.print("Error. Ingrese un numero valido: ");
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
