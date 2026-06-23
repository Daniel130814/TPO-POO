package presentacion.gui;

import exceptions.DatoInvalidoException;
import exceptions.OdontologoNoEncontradoException;
import exceptions.PacienteNoEncontradoException;
import controller.OdontologoController;
import controller.PacienteController;
import controller.TurnoController;
import modelo.EstadoTurno;
import modelo.Odontologo;
import modelo.Paciente;
import modelo.Turno;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelBusquedas extends JPanel {
    private PacienteController pacienteController;
    private OdontologoController odontologoController;
    private TurnoController turnoController;

    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;

    private JTextField campoBusqueda;
    private JComboBox<EstadoTurno> comboEstado;

    public PanelBusquedas(
            PacienteController pacienteController,
            OdontologoController odontologoController,
            TurnoController turnoController) {

        this.pacienteController = pacienteController;
        this.odontologoController = odontologoController;
        this.turnoController = turnoController;

        inicializarPanel();
    }

    private void inicializarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelAcciones(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelAcciones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Busquedas"));

        campoBusqueda = new JTextField(16);
        comboEstado = new JComboBox<EstadoTurno>(EstadoTurno.values());

        JButton botonPacienteDni = new JButton("Paciente por DNI");
        JButton botonOdontologoMatricula = new JButton("Odontologo por matricula");
        JButton botonTurnosPaciente = new JButton("Turnos por paciente");
        JButton botonTurnosOdontologo = new JButton("Turnos por odontologo");
        JButton botonTurnosFecha = new JButton("Turnos por fecha");
        JButton botonTurnosEstado = new JButton("Turnos por estado");
        JButton botonLimpiar = new JButton("Limpiar");

        botonPacienteDni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPacientePorDni();
            }
        });

        botonOdontologoMatricula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarOdontologoPorMatricula();
            }
        });

        botonTurnosPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTurnosPorPaciente();
            }
        });

        botonTurnosOdontologo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTurnosPorOdontologo();
            }
        });

        botonTurnosFecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTurnosPorFecha();
            }
        });

        botonTurnosEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTurnosPorEstado();
            }
        });

        botonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campoBusqueda.setText("");
                limpiarTabla();
            }
        });

        panelAcciones.add(campoBusqueda);
        panelAcciones.add(botonPacienteDni);
        panelAcciones.add(botonOdontologoMatricula);
        panelAcciones.add(botonTurnosPaciente);
        panelAcciones.add(botonTurnosOdontologo);
        panelAcciones.add(botonTurnosFecha);
        panelAcciones.add(comboEstado);
        panelAcciones.add(botonTurnosEstado);
        panelAcciones.add(botonLimpiar);

        return panelAcciones;
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
        return scrollPane;
    }

    private void buscarPacientePorDni() {
        String dni = pedirDato("Ingrese DNI del paciente");

        if (dni == null) {
            return;
        }

        try {
            Paciente paciente = pacienteController.buscarPorDni(dni);
            mostrarPaciente(paciente);
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void buscarOdontologoPorMatricula() {
        String matricula = pedirDato("Ingrese matricula del odontologo");

        if (matricula == null) {
            return;
        }

        try {
            Odontologo odontologo = odontologoController.buscarPorMatricula(matricula);
            mostrarOdontologo(odontologo);
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void listarTurnosPorPaciente() {
        String dni = pedirDato("Ingrese DNI del paciente");

        if (dni == null) {
            return;
        }

        try {
            Paciente paciente = pacienteController.buscarPorDni(dni);
            List<Turno> turnos = turnoController.buscarPorPaciente(paciente.getId());
            mostrarTurnos(turnos);
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void listarTurnosPorOdontologo() {
        String matricula = pedirDato("Ingrese matricula del odontologo");

        if (matricula == null) {
            return;
        }

        try {
            Odontologo odontologo = odontologoController.buscarPorMatricula(matricula);
            List<Turno> turnos = turnoController.buscarPorOdontologo(odontologo.getId());
            mostrarTurnos(turnos);
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void listarTurnosPorFecha() {
        String fechaTexto = pedirDato("Ingrese fecha yyyy-MM-dd");

        if (fechaTexto == null) {
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);
            List<Turno> turnos = turnoController.buscarPorFecha(fecha);
            mostrarTurnos(turnos);
        } catch (DateTimeParseException e) {
            mostrarError("Formato de fecha invalido. Use yyyy-MM-dd.");
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void listarTurnosPorEstado() {
        try {
            EstadoTurno estado = (EstadoTurno) comboEstado.getSelectedItem();
            List<Turno> turnos = turnoController.buscarPorEstado(estado);
            mostrarTurnos(turnos);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarPaciente(Paciente paciente) {
        configurarColumnas(new String[]{"ID", "Nombre", "Apellido", "DNI", "Email", "Localidad"});

        String localidad = "";

        if (paciente.getDomicilio() != null) {
            localidad = paciente.getDomicilio().getLocalidad();
        }

        modeloTabla.addRow(new Object[]{
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getEmail(),
                localidad
        });
    }

    private void mostrarOdontologo(Odontologo odontologo) {
        configurarColumnas(new String[]{"ID", "Nombre", "Apellido", "Matricula", "Honorarios", "Urgencias"});
        modeloTabla.addRow(new Object[]{
                odontologo.getId(),
                odontologo.getNombre(),
                odontologo.getApellido(),
                odontologo.getMatricula(),
                odontologo.calcularHonorarios(),
                odontologo.atiendeUrgencias() ? "Si" : "No"
        });
    }

    private void mostrarTurnos(List<Turno> turnos) {
        configurarColumnas(new String[]{"ID", "Paciente", "Odontologo", "Fecha", "Hora", "Estado", "Precio"});

        for (Turno turno : turnos) {
            modeloTabla.addRow(new Object[]{
                    turno.getId(),
                    obtenerNombrePaciente(turno),
                    obtenerNombreOdontologo(turno),
                    turno.getFecha(),
                    turno.getHora(),
                    turno.getEstado(),
                    turno.calculaPrecioFinal()
            });
        }

        if (turnos.isEmpty()) {
            mostrarMensaje("No se encontraron turnos para la busqueda.");
        }
    }

    private void configurarColumnas(String[] columnas) {
        modeloTabla.setColumnCount(0);
        modeloTabla.setRowCount(0);

        for (int i = 0; i < columnas.length; i++) {
            modeloTabla.addColumn(columnas[i]);
        }
    }

    private void limpiarTabla() {
        modeloTabla.setColumnCount(0);
        modeloTabla.setRowCount(0);
    }

    private String pedirDato(String mensaje) {
        String valorInicial = campoBusqueda.getText().trim();

        String valor = (String) JOptionPane.showInputDialog(
                this,
                mensaje,
                "Busqueda",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                valorInicial
        );

        if (valor == null) {
            return null;
        }

        valor = valor.trim();

        if (valor.isEmpty()) {
            marcarBusquedaInvalida("Debe ingresar un valor para buscar.");
            return null;
        }

        campoBusqueda.setBackground(Color.WHITE);
        campoBusqueda.setText(valor);
        return valor;
    }

    private void marcarBusquedaInvalida(String mensaje) {
        campoBusqueda.setBackground(new Color(255, 230, 230));
        campoBusqueda.requestFocus();
        mostrarError(mensaje);
    }

    private String obtenerNombrePaciente(Turno turno) {
        if (turno.getPaciente() == null) {
            return "";
        }

        return turno.getPaciente().getApellido() + ", " + turno.getPaciente().getNombre();
    }

    private String obtenerNombreOdontologo(Turno turno) {
        if (turno.getOdontologo() == null) {
            return "";
        }

        return turno.getOdontologo().getApellido() + ", " + turno.getOdontologo().getNombre();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
