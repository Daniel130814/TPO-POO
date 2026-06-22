package presentacion.gui;

import Exceptions.DatoInvalidoException;
import Exceptions.TurnoNoEncontradoException;
import Exceptions.TurnoYaReservadoException;
import controller.OdontologoController;
import controller.PacienteController;
import controller.TurnoController;
import modelo.EstadoTurno;
import modelo.Odontologo;
import modelo.Paciente;
import modelo.Turno;
import modelo.TurnoControl;
import modelo.TurnoUrgente;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelTurnos extends JPanel {
    private PacienteController pacienteController;
    private OdontologoController odontologoController;
    private TurnoController turnoController;

    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;

    private JComboBox<Paciente> comboPacientes;
    private JComboBox<Odontologo> comboOdontologos;
    private JTextField campoFecha;
    private JComboBox<String> comboHora;
    private JComboBox<EstadoTurno> comboEstado;

    private JTextField campoFiltroFecha;
    private JComboBox<EstadoTurno> comboFiltroEstado;

    private Long turnoSeleccionadoId;

    public PanelTurnos(
            PacienteController pacienteController,
            OdontologoController odontologoController,
            TurnoController turnoController) {

        this.pacienteController = pacienteController;
        this.odontologoController = odontologoController;
        this.turnoController = turnoController;
        this.turnoSeleccionadoId = null;

        inicializarPanel();
        refrescarDatos();
    }

    private void inicializarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del turno"));
        panelFormulario.setPreferredSize(new Dimension(360, 0));

        comboPacientes = new JComboBox<Paciente>();
        comboOdontologos = new JComboBox<Odontologo>();
        campoFecha = new JTextField(18);
        campoFecha.setEditable(false);
        comboHora = new JComboBox<String>();
        comboEstado = new JComboBox<EstadoTurno>(EstadoTurno.values());
        comboEstado.setSelectedItem(EstadoTurno.PENDIENTE);

        comboPacientes.setRenderer(new PacienteRenderer());
        comboOdontologos.setRenderer(new OdontologoRenderer());
        cargarHorarios();

        agregarComboPaciente(panelFormulario, "Paciente", comboPacientes, 0);
        agregarComboOdontologo(panelFormulario, "Odontologo", comboOdontologos, 1);
        agregarFechaConCalendario(panelFormulario, "Fecha", 2);
        agregarComboHora(panelFormulario, "Hora", comboHora, 3);
        agregarComboEstado(panelFormulario, "Estado", comboEstado, 4);

        return panelFormulario;
    }

    private JPanel crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(crearPanelFiltros(), BorderLayout.NORTH);
        panelCentral.add(crearPanelTabla(), BorderLayout.CENTER);
        return panelCentral;
    }

    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = new JPanel();
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        campoFiltroFecha = new JTextField(10);
        campoFiltroFecha.setEditable(false);
        comboFiltroEstado = new JComboBox<EstadoTurno>(EstadoTurno.values());

        JButton botonCalendarioFiltro = new JButton("Calendario");
        JButton botonFiltrarFecha = new JButton("Filtrar fecha");
        JButton botonFiltrarEstado = new JButton("Filtrar estado");
        JButton botonLimpiarFiltros = new JButton("Limpiar filtros");

        botonCalendarioFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate fecha = CalendarioDialog.seleccionarFecha(PanelTurnos.this);

                if (fecha != null) {
                    campoFiltroFecha.setText(fecha.toString());
                    campoFiltroFecha.setBackground(Color.WHITE);
                }
            }
        });

        botonFiltrarFecha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorFecha();
            }
        });

        botonFiltrarEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorEstado();
            }
        });

        botonLimpiarFiltros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campoFiltroFecha.setText("");
                cargarTurnosEnTabla(turnoController.listarTodos());
            }
        });

        panelFiltros.add(new JLabel("Fecha"));
        panelFiltros.add(campoFiltroFecha);
        panelFiltros.add(botonCalendarioFiltro);
        panelFiltros.add(botonFiltrarFecha);
        panelFiltros.add(new JLabel("Estado"));
        panelFiltros.add(comboFiltroEstado);
        panelFiltros.add(botonFiltrarEstado);
        panelFiltros.add(botonLimpiarFiltros);

        return panelFiltros;
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Paciente");
        modeloTabla.addColumn("Odontologo");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Hora");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Precio");

        tablaTurnos = new JTable(modeloTabla);
        tablaTurnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTurnos.getTableHeader().setReorderingAllowed(false);

        tablaTurnos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarTurnoSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaTurnos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Turnos registrados"));
        return scrollPane;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();

        JButton botonReservar = new JButton("Reservar turno");
        JButton botonModificar = new JButton("Modificar turno");
        JButton botonCancelar = new JButton("Cancelar turno");
        JButton botonConfirmar = new JButton("Confirmar turno");
        JButton botonCompletar = new JButton("Marcar completado");
        JButton botonLimpiar = new JButton("Limpiar");
        JButton botonRefrescar = new JButton("Refrescar listado");

        botonReservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reservarTurno();
            }
        });

        botonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarTurno();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoTurno(EstadoTurno.CANCELADO);
            }
        });

        botonConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoTurno(EstadoTurno.CONFIRMADO);
            }
        });

        botonCompletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoTurno(EstadoTurno.COMPLETADO);
            }
        });

        botonLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        botonRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarDatos();
            }
        });

        panelBotones.add(botonReservar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonCancelar);
        panelBotones.add(botonConfirmar);
        panelBotones.add(botonCompletar);
        panelBotones.add(botonLimpiar);
        panelBotones.add(botonRefrescar);

        return panelBotones;
    }

    private void reservarTurno() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Paciente paciente = (Paciente) comboPacientes.getSelectedItem();
            Odontologo odontologo = (Odontologo) comboOdontologos.getSelectedItem();
            LocalDate fecha = obtenerFechaFormulario();
            LocalTime hora = obtenerHoraFormulario();
            EstadoTurno estado = (EstadoTurno) comboEstado.getSelectedItem();

            TurnoUrgente turno = new TurnoUrgente(
                    null,
                    paciente,
                    odontologo,
                    fecha,
                    hora,
                    estado,
                    15000.0,
                    false,
                    0.0
            );

            turnoController.registrar(turno);
            mostrarMensaje("Turno reservado correctamente.");
            limpiarFormulario();
            cargarTurnosEnTabla(turnoController.listarTodos());
        } catch (TurnoYaReservadoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void modificarTurno() {
        if (turnoSeleccionadoId == null) {
            mostrarError("Seleccione un turno de la tabla para modificar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Turno actual = turnoController.buscarPorId(turnoSeleccionadoId);
            Turno modificado = crearTurnoModificado(actual);
            turnoController.actualizar(modificado);
            mostrarMensaje("Turno modificado correctamente.");
            limpiarFormulario();
            cargarTurnosEnTabla(turnoController.listarTodos());
        } catch (TurnoYaReservadoException e) {
            mostrarError(e.getMessage());
        } catch (TurnoNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private Turno crearTurnoModificado(Turno actual) {
        Paciente paciente = (Paciente) comboPacientes.getSelectedItem();
        Odontologo odontologo = (Odontologo) comboOdontologos.getSelectedItem();
        LocalDate fecha = obtenerFechaFormulario();
        LocalTime hora = obtenerHoraFormulario();
        EstadoTurno estado = (EstadoTurno) comboEstado.getSelectedItem();

        if (actual instanceof TurnoControl) {
            TurnoControl control = (TurnoControl) actual;
            return new TurnoControl(
                    actual.getId(),
                    paciente,
                    odontologo,
                    fecha,
                    hora,
                    estado,
                    actual.getPrecioBase(),
                    control.getDuracion(),
                    control.getTipoConsulta(),
                    control.isRequiereRadiografia(),
                    control.isTieneObraSocial()
            );
        }

        TurnoUrgente urgente = (TurnoUrgente) actual;
        return new TurnoUrgente(
                actual.getId(),
                paciente,
                odontologo,
                fecha,
                hora,
                estado,
                actual.getPrecioBase(),
                urgente.isRequiereIntervencion(),
                urgente.getDuracion()
        );
    }

    private void cambiarEstadoTurno(EstadoTurno estado) {
        if (turnoSeleccionadoId == null) {
            mostrarError("Seleccione un turno de la tabla.");
            return;
        }

        try {
            turnoController.cambiarEstado(turnoSeleccionadoId, estado);
            mostrarMensaje("Estado actualizado correctamente.");
            limpiarFormulario();
            cargarTurnosEnTabla(turnoController.listarTodos());
        } catch (TurnoNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorFecha() {
        if (campoFiltroFecha.getText().trim().isEmpty()) {
            campoFiltroFecha.setBackground(new Color(255, 230, 230));
            mostrarError("Seleccione una fecha para filtrar.");
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(campoFiltroFecha.getText().trim());
            cargarTurnosEnTabla(turnoController.buscarPorFecha(fecha));
            campoFiltroFecha.setBackground(Color.WHITE);
        } catch (DateTimeParseException e) {
            campoFiltroFecha.setBackground(new Color(255, 230, 230));
            mostrarError("Formato de fecha invalido. Use yyyy-MM-dd.");
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void filtrarPorEstado() {
        try {
            EstadoTurno estado = (EstadoTurno) comboFiltroEstado.getSelectedItem();
            cargarTurnosEnTabla(turnoController.buscarPorEstado(estado));
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarTurnoSeleccionado() {
        int fila = tablaTurnos.getSelectedRow();

        if (fila < 0) {
            return;
        }

        Long id = Long.parseLong(modeloTabla.getValueAt(fila, 0).toString());

        try {
            Turno turno = turnoController.buscarPorId(id);
            cargarTurnoEnFormulario(turno);
        } catch (TurnoNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarTurnoEnFormulario(Turno turno) {
        turnoSeleccionadoId = turno.getId();
        seleccionarPaciente(turno.getPaciente());
        seleccionarOdontologo(turno.getOdontologo());
        campoFecha.setText(turno.getFecha().toString());
        comboHora.setSelectedItem(turno.getHora().toString());
        comboEstado.setSelectedItem(turno.getEstado());
    }

    private void seleccionarPaciente(Paciente paciente) {
        for (int i = 0; i < comboPacientes.getItemCount(); i++) {
            Paciente item = comboPacientes.getItemAt(i);

            if (item != null && item.getId().equals(paciente.getId())) {
                comboPacientes.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarOdontologo(Odontologo odontologo) {
        for (int i = 0; i < comboOdontologos.getItemCount(); i++) {
            Odontologo item = comboOdontologos.getItemAt(i);

            if (item != null && item.getId().equals(odontologo.getId())) {
                comboOdontologos.setSelectedIndex(i);
                return;
            }
        }
    }

    private boolean validarFormulario() {
        limpiarColorCampos();

        if (comboPacientes.getSelectedItem() == null) {
            mostrarError("Debe seleccionar un paciente.");
            return false;
        }

        if (comboOdontologos.getSelectedItem() == null) {
            mostrarError("Debe seleccionar un odontologo.");
            return false;
        }

        if (campoFecha.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoFecha, "La fecha es obligatoria.");
            return false;
        }

        try {
            obtenerFechaFormulario();
        } catch (DateTimeParseException e) {
            marcarCampoInvalido(campoFecha, "Formato de fecha invalido. Use yyyy-MM-dd.");
            return false;
        }

        if (comboHora.getSelectedItem() == null) {
            mostrarError("La hora es obligatoria.");
            return false;
        }

        try {
            obtenerHoraFormulario();
        } catch (DateTimeParseException e) {
            mostrarError("Formato de hora invalido. Use HH:mm.");
            return false;
        }

        return true;
    }

    private LocalDate obtenerFechaFormulario() {
        return LocalDate.parse(campoFecha.getText().trim());
    }

    private LocalTime obtenerHoraFormulario() {
        return LocalTime.parse(comboHora.getSelectedItem().toString());
    }

    private void marcarCampoInvalido(JTextField campo, String mensaje) {
        campo.setBackground(new Color(255, 230, 230));
        campo.requestFocus();
        mostrarError(mensaje);
    }

    private void limpiarColorCampos() {
        campoFecha.setBackground(Color.WHITE);
    }

    private void limpiarFormulario() {
        turnoSeleccionadoId = null;
        if (comboPacientes.getItemCount() > 0) {
            comboPacientes.setSelectedIndex(0);
        }
        if (comboOdontologos.getItemCount() > 0) {
            comboOdontologos.setSelectedIndex(0);
        }
        campoFecha.setText("");
        if (comboHora.getItemCount() > 0) {
            comboHora.setSelectedIndex(0);
        }
        comboEstado.setSelectedItem(EstadoTurno.PENDIENTE);
        tablaTurnos.clearSelection();
        limpiarColorCampos();
    }

    private void refrescarDatos() {
        cargarPacientesEnCombo();
        cargarOdontologosEnCombo();
        cargarTurnosEnTabla(turnoController.listarTodos());
    }

    private void cargarPacientesEnCombo() {
        DefaultComboBoxModel<Paciente> modelo = new DefaultComboBoxModel<Paciente>();
        List<Paciente> pacientes = pacienteController.listarOrdenadosPorApellido();

        for (Paciente paciente : pacientes) {
            modelo.addElement(paciente);
        }

        comboPacientes.setModel(modelo);
    }

    private void cargarOdontologosEnCombo() {
        DefaultComboBoxModel<Odontologo> modelo = new DefaultComboBoxModel<Odontologo>();
        List<Odontologo> odontologos = odontologoController.listarTodos();

        for (Odontologo odontologo : odontologos) {
            modelo.addElement(odontologo);
        }

        comboOdontologos.setModel(modelo);
    }

    private void cargarTurnosEnTabla(List<Turno> turnos) {
        modeloTabla.setRowCount(0);

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

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void agregarFechaConCalendario(JPanel panel, String etiqueta, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        JPanel panelFecha = new JPanel(new BorderLayout(4, 0));
        JButton botonCalendario = new JButton("Calendario");

        botonCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate fecha = CalendarioDialog.seleccionarFecha(PanelTurnos.this);

                if (fecha != null) {
                    campoFecha.setText(fecha.toString());
                    campoFecha.setBackground(Color.WHITE);
                }
            }
        });

        panelFecha.add(campoFecha, BorderLayout.CENTER);
        panelFecha.add(botonCalendario, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(panelFecha, gbc);
    }

    private void agregarComboPaciente(JPanel panel, String etiqueta, JComboBox<Paciente> combo, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(combo, gbc);
    }

    private void agregarComboOdontologo(JPanel panel, String etiqueta, JComboBox<Odontologo> combo, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(combo, gbc);
    }

    private void agregarComboEstado(JPanel panel, String etiqueta, JComboBox<EstadoTurno> combo, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(combo, gbc);
    }

    private void agregarComboHora(JPanel panel, String etiqueta, JComboBox<String> combo, int fila) {
        GridBagConstraints gbc = crearGbc(fila);
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(combo, gbc);
    }

    private void cargarHorarios() {
        comboHora.removeAllItems();

        LocalTime hora = LocalTime.of(7, 0);
        LocalTime horaFin = LocalTime.of(20, 0);

        while (!hora.isAfter(horaFin)) {
            comboHora.addItem(hora.toString());
            hora = hora.plusMinutes(30);
        }
    }

    private GridBagConstraints crearGbc(int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = fila;
        gbc.weightx = 0;
        return gbc;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class PacienteRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Paciente) {
                Paciente paciente = (Paciente) value;
                setText(paciente.getApellido() + ", " + paciente.getNombre() + " - DNI " + paciente.getDni());
            }

            return this;
        }
    }

    private class OdontologoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Odontologo) {
                Odontologo odontologo = (Odontologo) value;
                setText(odontologo.getApellido() + ", " + odontologo.getNombre() + " - " + odontologo.getMatricula());
            }

            return this;
        }
    }
}
