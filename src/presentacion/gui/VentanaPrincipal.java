package presentacion.gui;

import controller.PacienteController;
import controller.OdontologoController;
import controller.TurnoController;
import servicio.ServicioOdontologo;
import servicio.ServicioPaciente;
import servicio.ServicioTurno;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {
    private static final String ARCHIVO_PACIENTES = "data/pacientes.csv";
    private static final String ARCHIVO_ODONTOLOGOS = "data/odontologos.csv";
    private static final String ARCHIVO_TURNOS = "data/turnos.csv";

    private static final String CARD_PACIENTES = "PACIENTES";
    private static final String CARD_ODONTOLOGOS = "ODONTOLOGOS";
    private static final String CARD_TURNOS = "TURNOS";
    private static final String CARD_BUSQUEDAS = "BUSQUEDAS";

    private ServicioPaciente servicioPaciente;
    private ServicioOdontologo servicioOdontologo;
    private ServicioTurno servicioTurno;
    private PacienteController pacienteController;
    private OdontologoController odontologoController;
    private TurnoController turnoController;

    private JPanel panelContenido;
    private CardLayout cardLayout;

    public VentanaPrincipal(
            ServicioPaciente servicioPaciente,
            ServicioOdontologo servicioOdontologo,
            ServicioTurno servicioTurno) {

        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
        this.servicioTurno = servicioTurno;
        this.pacienteController = new PacienteController(servicioPaciente);
        this.odontologoController = new OdontologoController(servicioOdontologo);
        this.turnoController = new TurnoController(servicioTurno);

        inicializarVentana();
        inicializarNavegacion();
        inicializarPaneles();
        inicializarCierre();
    }

    private void inicializarVentana() {
        setTitle("Cl\u00ednica Odontol\u00f3gica Sonrisa Feliz");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarNavegacion() {
        JPanel panelNavegacion = new JPanel(new GridLayout(1, 5, 8, 8));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton botonPacientes = new JButton("Pacientes");
        JButton botonOdontologos = new JButton("Odontologos");
        JButton botonTurnos = new JButton("Turnos");
        JButton botonBusquedas = new JButton("Busquedas");
        JButton botonSalir = new JButton("Salir");

        botonPacientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanel(CARD_PACIENTES);
            }
        });

        botonOdontologos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanel(CARD_ODONTOLOGOS);
            }
        });

        botonTurnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanel(CARD_TURNOS);
            }
        });

        botonBusquedas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPanel(CARD_BUSQUEDAS);
            }
        });

        botonSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });

        panelNavegacion.add(botonPacientes);
        panelNavegacion.add(botonOdontologos);
        panelNavegacion.add(botonTurnos);
        panelNavegacion.add(botonBusquedas);
        panelNavegacion.add(botonSalir);

        add(panelNavegacion, BorderLayout.NORTH);
    }

    private void inicializarPaneles() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        panelContenido.add(new PanelPacientes(pacienteController), CARD_PACIENTES);
        panelContenido.add(new PanelOdontologos(odontologoController), CARD_ODONTOLOGOS);
        panelContenido.add(new PanelTurnos(pacienteController, odontologoController, turnoController), CARD_TURNOS);
        panelContenido.add(new PanelBusquedas(pacienteController, odontologoController, turnoController), CARD_BUSQUEDAS);

        add(panelContenido, BorderLayout.CENTER);
        mostrarPanel(CARD_PACIENTES);
    }

    private void inicializarCierre() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });
    }

    private void mostrarPanel(String nombrePanel) {
        cardLayout.show(panelContenido, nombrePanel);
    }

    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Desea guardar los datos y salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (guardarDatos()) {
            dispose();
            System.exit(0);
        }
    }

    private boolean guardarDatos() {
        try {
            servicioPaciente.guardarEnArchivo(ARCHIVO_PACIENTES);
            servicioOdontologo.guardarEnArchivo(ARCHIVO_ODONTOLOGOS);
            servicioTurno.guardarEnArchivo(ARCHIVO_TURNOS);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron guardar los datos: " + e.getMessage(),
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}
