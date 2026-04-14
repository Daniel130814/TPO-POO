import modelo.*;

import java.time.LocalTime;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // 1. Creamos el domicilio
        Domicilio domicilioCarlos = new Domicilio("Av. Rivadavia", 1234, "CABA", "Buenos Aires");
        // 2. Creamos el paciente
        Paciente paciente = new Paciente("Carlos", "Gonzáles", "11111", "carlos@gmail.com",domicilioCarlos);
        // 3. Creamos el odontólogo
        Odontologo odontologo= new Odontologo(1003L,"Dario","Gomez","MP-9876");
        // 4. Creamos el turno asociando a ambos
        Turno turno = new Turno(1001L, paciente, odontologo, LocalDate.of(2025, 5, 20), LocalTime.of(15, 30), EstadoTurno.PENDIENTE);
        // 5. Menú final
        System.out.println("=================================================");
        System.out.println("      CLÍNICA ODONTOLÓGICA 'SONRISA FELIZ'       ");
        System.out.println("=================================================");
        System.out.println(">>> COMPROBANTE DE TURNO GENERADO CON ÉXITO <<<");
        System.out.println(" ");
        System.out.println("   DATOS DEL PACIENTE");
        System.out.println("   Nombre: " + paciente.getNombre() +"   Apellido: "+ paciente.getApellido()+ " (DNI: " + paciente.getDni() + ")");
        System.out.println("   Domicilio: " + paciente.getDomicilio().getCalle() + " " + paciente.getDomicilio().getNumero());
        System.out.println("---------------------------------------");
        System.out.println(" ️   PROFESIONAL ASIGNADO");
        System.out.println("   Odontólogo: " + odontologo.getNombre()+" "+odontologo.getApellido() + " (Matrícula: " + odontologo.getMatricula() + ")");
        System.out.println("   DATOS DEL TURNO RESERVADO:");
        System.out.println("   ID Turno: " + turno.getId());
        System.out.println("   Fecha: " + turno.getFecha() + " | Hora: " + turno.getHora());
        System.out.println("   Estado actual: [" + turno.getEstado() + "]");
        System.out.println("=================================================");
        System.out.println(turno.toString());;
    }
}
