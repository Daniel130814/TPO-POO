import presentacion.gui.VentanaPrincipal;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    private static final String ARCHIVO_PACIENTES = "data/pacientes.txt";
    private static final String ARCHIVO_ODONTOLOGOS = "data/odontologos.txt";
    private static final String ARCHIVO_TURNOS = "data/turnos.txt";

    public static void main(String[] args) {
        final ServicioPaciente servicioPaciente = new ServicioPaciente();
        final ServicioOdontologo servicioOdontologo = new ServicioOdontologo();
        final ServicioTurno servicioTurno = new ServicioTurno();

        cargarDatos(servicioPaciente, servicioOdontologo, servicioTurno);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(
                        servicioPaciente,
                        servicioOdontologo,
                        servicioTurno
                );
                ventanaPrincipal.setVisible(true);
            }
        });
    }

    private static void cargarDatos(
            ServicioPaciente servicioPaciente,
            ServicioOdontologo servicioOdontologo,
            ServicioTurno servicioTurno) {

        try {
            servicioPaciente.cargarDesdeArchivo(ARCHIVO_PACIENTES);
            servicioOdontologo.cargarDesdeArchivo(ARCHIVO_ODONTOLOGOS);
            servicioTurno.cargarDesdeArchivo(ARCHIVO_TURNOS, servicioPaciente, servicioOdontologo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudieron cargar los datos guardados: " + e.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
