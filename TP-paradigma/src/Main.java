import Modelo.Paciente;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {


        Paciente paciente = new Paciente(1L, "Carlos", "Gonzáles", "11111", "carlos@gmail.com", LocalDate.of(2025, 2, 10));
        System.out.println(paciente.getNombre());
        System.out.println("-------------Bienvenido al sistema de pacientes-------------");
        //El sout("...")con las comilla funciona como un print con tod0 lo que se escriba adentro
        System.out.println("Los datos cargados son:"+paciente);
        //Ejemplo si quiero editar el nombre de un paciente
        System.out.println("Nombre actual del paciente: "+paciente.getNombre());
        paciente.setNombre("Luis");
        System.out.println("nombre del paciente actualizado: "+paciente.getNombre());
    }
}
