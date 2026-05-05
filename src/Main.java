import controlador.OdontologoController;
import controlador.PacienteController;
import controlador.TurnoController;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;
import vista.VistaClinica;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciamos la Vista compartida
        VistaClinica vista = new VistaClinica();

        // 2. Instanciamos los Servicios (los motores de datos)
        ServicioPaciente servicioPaciente = new ServicioPaciente();
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo();
        ServicioTurno servicioTurno = new ServicioTurno();

        // 3. Instanciamos los Controladores
        // Importante: Pasamos servicioTurno a todos para las validaciones de bajas y disponibilidad
        PacienteController pacienteController = new PacienteController(vista, servicioPaciente, servicioTurno);
        OdontologoController odontologoController = new OdontologoController(vista, servicioOdontologo, servicioTurno);
        TurnoController turnoController = new TurnoController(vista, servicioTurno, servicioPaciente, servicioOdontologo);

        boolean ejecutando = true;

        // 4. Bucle del Menú Principal
        vista.mostrarMensaje("=== BIENVENIDO AL SISTEMA DE URGENCIAS ODONTOLÓGICAS ===");

        while (ejecutando) {
            int opcion = vista.mostrarMenuPrincipal();

            switch (opcion) {
                case 1:
                    pacienteController.iniciar();
                    break;
                case 2:
                    odontologoController.iniciar();
                    break;
                case 3:
                    turnoController.iniciar();
                    break;
                case 0:
                    ejecutando = false;
                    vista.mostrarMensaje("Guardando cambios y saliendo del sistema... ¡Hasta luego!");
                    break;
                default:
                    vista.mostrarMensaje("Opción no válida. Intente nuevamente.");
            }
        }

        vista.cerrar();
    }
}