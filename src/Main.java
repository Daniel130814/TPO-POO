import controlador.ClinicaController;
import vista.VistaClinica;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciamos la Vista (pantalla y teclado)
        VistaClinica vista = new VistaClinica();

        // 2. Instanciamos el Controlador (el cerebro) y lo conectamos con la Vista
        ClinicaController controller = new ClinicaController(vista);

        // 3. ¡Arrancamos el sistema!
        controller.run();

    }
}
