import controlador.OdontologoController;
import controlador.PacienteController;
import controlador.TurnoController;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;
import vista.VistaClinica;

public class Main {
    private static final String ARCHIVO_PACIENTES = "data/pacientes.txt";
    private static final String ARCHIVO_ODONTOLOGOS = "data/odontologos.txt";
    private static final String ARCHIVO_TURNOS = "data/turnos.txt";

    public static void main(String[] args) {
        VistaClinica vista = new VistaClinica();

        ServicioPaciente servicioPaciente = new ServicioPaciente();
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo();
        ServicioTurno servicioTurno = new ServicioTurno();

        try {
            servicioPaciente.cargarDesdeArchivo(ARCHIVO_PACIENTES);
            servicioOdontologo.cargarDesdeArchivo(ARCHIVO_ODONTOLOGOS);
            servicioTurno.cargarDesdeArchivo(ARCHIVO_TURNOS, servicioPaciente, servicioOdontologo);
        } catch (Exception e) {
            vista.mostrarMensaje("No se pudieron cargar los datos guardados: " + e.getMessage());
        }

        PacienteController pacienteController =
                new PacienteController(vista, servicioPaciente, servicioTurno);
        OdontologoController odontologoController =
                new OdontologoController(vista, servicioOdontologo, servicioTurno);
        TurnoController turnoController =
                new TurnoController(vista, servicioTurno, servicioPaciente, servicioOdontologo);

        boolean ejecutando = true;

        vista.mostrarMensaje("=== BIENVENIDO AL SISTEMA DE URGENCIAS ODONTOLOGICAS ===");

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
                    guardarDatos(vista, servicioPaciente, servicioOdontologo, servicioTurno);
                    break;
                default:
                    vista.mostrarMensaje("Opcion no valida. Intente nuevamente.");
            }
        }

        vista.cerrar();
    }

    private static void guardarDatos(
            VistaClinica vista,
            ServicioPaciente servicioPaciente,
            ServicioOdontologo servicioOdontologo,
            ServicioTurno servicioTurno) {

        try {
            servicioPaciente.guardarEnArchivo(ARCHIVO_PACIENTES);
            servicioOdontologo.guardarEnArchivo(ARCHIVO_ODONTOLOGOS);
            servicioTurno.guardarEnArchivo(ARCHIVO_TURNOS);
            vista.mostrarMensaje("Guardando cambios y saliendo del sistema... Hasta luego!");
        } catch (Exception e) {
            vista.mostrarMensaje("No se pudieron guardar los datos: " + e.getMessage());
        }
    }
}
