package presentacion.gui;

import Exceptions.DatoInvalidoException;
import Exceptions.DniDuplicadoException;
import Exceptions.PacienteNoEncontradoException;
import controller.PacienteController;
import modelo.Domicilio;
import modelo.Paciente;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelPacientes extends JPanel {
    private PacienteController pacienteController;

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;

    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoDni;
    private JTextField campoEmail;
    private JTextField campoCalle;
    private JTextField campoNumero;
    private JTextField campoLocalidad;
    private JTextField campoProvincia;

    private Long pacienteSeleccionadoId;

    public PanelPacientes(PacienteController pacienteController) {
        this.pacienteController = pacienteController;
        this.pacienteSeleccionadoId = null;

        inicializarPanel();
        cargarPacientesEnTabla();
    }

    private void inicializarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        panelFormulario.setPreferredSize(new Dimension(340, 0));

        campoNombre = new JTextField(18);
        campoApellido = new JTextField(18);
        campoDni = new JTextField(18);
        campoEmail = new JTextField(18);
        campoCalle = new JTextField(18);
        campoNumero = new JTextField(18);
        campoLocalidad = new JTextField(18);
        campoProvincia = new JTextField(18);

        agregarCampo(panelFormulario, "Nombre", campoNombre, 0);
        agregarCampo(panelFormulario, "Apellido", campoApellido, 1);
        agregarCampo(panelFormulario, "DNI", campoDni, 2);
        agregarCampo(panelFormulario, "Email", campoEmail, 3);
        agregarCampo(panelFormulario, "Calle", campoCalle, 4);
        agregarCampo(panelFormulario, "Numero", campoNumero, 5);
        agregarCampo(panelFormulario, "Localidad", campoLocalidad, 6);
        agregarCampo(panelFormulario, "Provincia", campoProvincia, 7);

        return panelFormulario;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Localidad");

        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPacientes.getTableHeader().setReorderingAllowed(false);

        tablaPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarPacienteSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pacientes registrados"));
        return scrollPane;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();

        JButton botonNuevo = new JButton("Nuevo / Limpiar");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonModificar = new JButton("Modificar");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonBuscarDni = new JButton("Buscar por DNI");
        JButton botonRefrescar = new JButton("Refrescar listado");

        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPaciente();
            }
        });

        botonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarPaciente();
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
            }
        });

        botonBuscarDni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPorDni();
            }
        });

        botonRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPacientesEnTabla();
            }
        });

        panelBotones.add(botonNuevo);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonBuscarDni);
        panelBotones.add(botonRefrescar);

        return panelBotones;
    }

    private void guardarPaciente() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Paciente paciente = crearPacienteDesdeFormulario();
            pacienteController.registrar(paciente);
            mostrarMensaje("Paciente registrado correctamente.");
            limpiarFormulario();
            cargarPacientesEnTabla();
        } catch (DniDuplicadoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void modificarPaciente() {
        String dni = pedirDni("Ingrese DNI a modificar");

        if (dni == null) {
            return;
        }

        try {
            Paciente paciente = pacienteController.buscarPorDni(dni);
            mostrarVentanaModificarDato(paciente);
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarVentanaModificarDato(Paciente paciente) {
        String[] opciones = {
                "Nombre",
                "Apellido",
                "DNI",
                "Email",
                "Calle",
                "Numero",
                "Localidad",
                "Provincia"
        };

        javax.swing.JComboBox<String> comboCampo = new javax.swing.JComboBox<String>(opciones);
        JTextField campoValor = new JTextField(18);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Dato a modificar"), gbc);

        gbc.gridx = 1;
        panel.add(comboCampo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nuevo valor"), gbc);

        gbc.gridx = 1;
        panel.add(campoValor, gbc);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Modificar paciente DNI " + paciente.getDni(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String nuevoValor = campoValor.getText().trim();

        if (nuevoValor.isEmpty()) {
            mostrarError("El nuevo valor no puede estar vacio.");
            return;
        }

        try {
            aplicarCambioPaciente(paciente, comboCampo.getSelectedItem().toString(), nuevoValor);
            pacienteController.actualizar(paciente);
            mostrarMensaje("Paciente modificado correctamente.");
            cargarPacienteEnFormulario(paciente);
            cargarPacientesEnTabla();
            seleccionarPacienteEnTabla(paciente.getId());
        } catch (NumberFormatException e) {
            mostrarError("El numero debe ser un valor entero.");
        } catch (DniDuplicadoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void aplicarCambioPaciente(Paciente paciente, String campo, String nuevoValor) {
        if (campo.equals("Nombre")) {
            paciente.setNombre(nuevoValor);
        } else if (campo.equals("Apellido")) {
            paciente.setApellido(nuevoValor);
        } else if (campo.equals("DNI")) {
            paciente.setDni(nuevoValor);
        } else if (campo.equals("Email")) {
            paciente.setEmail(nuevoValor);
        } else if (campo.equals("Calle")) {
            paciente.getDomicilio().setCalle(nuevoValor);
        } else if (campo.equals("Numero")) {
            paciente.getDomicilio().setNumero(Integer.parseInt(nuevoValor));
        } else if (campo.equals("Localidad")) {
            paciente.getDomicilio().setLocalidad(nuevoValor);
        } else if (campo.equals("Provincia")) {
            paciente.getDomicilio().setProvincia(nuevoValor);
        }
    }

    private void eliminarPaciente() {
        String dni = pedirDni("Ingrese DNI a eliminar");

        if (dni == null) {
            return;
        }

        Paciente paciente;

        try {
            paciente = pacienteController.buscarPorDni(dni);
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Desea eliminar a " + paciente.getNombre() + " " + paciente.getApellido() + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            pacienteController.eliminarPorId(paciente.getId());
            mostrarMensaje("Paciente eliminado correctamente.");
            limpiarFormulario();
            cargarPacientesEnTabla();
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private String pedirDni(String mensaje) {
        String valorInicial = "";

        if (pacienteSeleccionadoId != null) {
            valorInicial = campoDni.getText().trim();
        }

        String dni = (String) JOptionPane.showInputDialog(
                this,
                mensaje,
                "DNI",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                valorInicial
        );

        if (dni == null) {
            return null;
        }

        dni = dni.trim();

        if (dni.isEmpty()) {
            mostrarError("Debe ingresar un DNI.");
            return null;
        }

        return dni;
    }

    private void buscarPorDni() {
        String dni = pedirDni("Ingrese DNI a buscar");

        if (dni == null) {
            return;
        }

        try {
            Paciente paciente = pacienteController.buscarPorDni(dni);
            cargarPacienteEnFormulario(paciente);
            seleccionarPacienteEnTabla(paciente.getId());
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();

        if (fila < 0) {
            return;
        }

        Long id = Long.parseLong(modeloTabla.getValueAt(fila, 0).toString());

        try {
            Paciente paciente = pacienteController.buscarPorId(id);
            cargarPacienteEnFormulario(paciente);
        } catch (PacienteNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarPacienteEnFormulario(Paciente paciente) {
        pacienteSeleccionadoId = paciente.getId();
        campoNombre.setText(paciente.getNombre());
        campoApellido.setText(paciente.getApellido());
        campoDni.setText(paciente.getDni());
        campoEmail.setText(paciente.getEmail());

        if (paciente.getDomicilio() != null) {
            campoCalle.setText(paciente.getDomicilio().getCalle());
            campoNumero.setText(String.valueOf(paciente.getDomicilio().getNumero()));
            campoLocalidad.setText(paciente.getDomicilio().getLocalidad());
            campoProvincia.setText(paciente.getDomicilio().getProvincia());
        }
    }

    private Paciente crearPacienteDesdeFormulario() {
        Domicilio domicilio = new Domicilio(
                campoCalle.getText().trim(),
                Integer.parseInt(campoNumero.getText().trim()),
                campoLocalidad.getText().trim(),
                campoProvincia.getText().trim()
        );

        return new Paciente(
                campoNombre.getText().trim(),
                campoApellido.getText().trim(),
                campoDni.getText().trim(),
                campoEmail.getText().trim(),
                domicilio
        );
    }

    private boolean validarFormulario() {
        limpiarColorCampos();

        if (campoNombre.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoNombre, "El nombre es obligatorio.");
            return false;
        }

        if (campoApellido.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoApellido, "El apellido es obligatorio.");
            return false;
        }

        if (campoDni.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoDni, "El DNI es obligatorio.");
            return false;
        }

        if (campoEmail.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoEmail, "El email es obligatorio.");
            return false;
        }

        if (campoCalle.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoCalle, "La calle es obligatoria.");
            return false;
        }

        if (!validarNumero()) {
            return false;
        }

        if (campoLocalidad.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoLocalidad, "La localidad es obligatoria.");
            return false;
        }

        if (campoProvincia.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoProvincia, "La provincia es obligatoria.");
            return false;
        }

        return true;
    }

    private boolean validarNumero() {
        String numero = campoNumero.getText().trim();

        if (numero.isEmpty()) {
            marcarCampoInvalido(campoNumero, "El numero es obligatorio.");
            return false;
        }

        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            marcarCampoInvalido(campoNumero, "El numero debe ser un valor entero.");
            return false;
        }
    }

    private void marcarCampoInvalido(JTextField campo, String mensaje) {
        campo.setBackground(new Color(255, 230, 230));
        campo.requestFocus();
        mostrarError(mensaje);
    }

    private void limpiarColorCampos() {
        campoNombre.setBackground(Color.WHITE);
        campoApellido.setBackground(Color.WHITE);
        campoDni.setBackground(Color.WHITE);
        campoEmail.setBackground(Color.WHITE);
        campoCalle.setBackground(Color.WHITE);
        campoNumero.setBackground(Color.WHITE);
        campoLocalidad.setBackground(Color.WHITE);
        campoProvincia.setBackground(Color.WHITE);
    }

    private void limpiarFormulario() {
        pacienteSeleccionadoId = null;
        campoNombre.setText("");
        campoApellido.setText("");
        campoDni.setText("");
        campoEmail.setText("");
        campoCalle.setText("");
        campoNumero.setText("");
        campoLocalidad.setText("");
        campoProvincia.setText("");
        tablaPacientes.clearSelection();
        limpiarColorCampos();
    }

    private void cargarPacientesEnTabla() {
        modeloTabla.setRowCount(0);

        List<Paciente> pacientes = pacienteController.listarOrdenadosPorApellido();

        for (Paciente paciente : pacientes) {
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
    }

    private void seleccionarPacienteEnTabla(Long id) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Long idTabla = Long.parseLong(modeloTabla.getValueAt(i, 0).toString());

            if (idTabla.equals(id)) {
                tablaPacientes.setRowSelectionInterval(i, i);
                tablaPacientes.scrollRectToVisible(tablaPacientes.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
