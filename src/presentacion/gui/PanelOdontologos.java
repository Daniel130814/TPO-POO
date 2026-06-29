package presentacion.gui;

import exceptions.DatoInvalidoException;
import exceptions.MatriculaDuplicadaException;
import exceptions.OdontologoNoEncontradoException;
import controller.OdontologoController;
import modelo.Endodoncista;
import modelo.Odontologo;
import modelo.OdontologoGeneral;
import modelo.Ortodoncista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class PanelOdontologos extends JPanel {
    private static final String TIPO_GENERAL = "Odontologo General";
    private static final String TIPO_ORTODONCISTA = "Ortodoncista";
    private static final String TIPO_ENDODONCISTA = "Endodoncista";

    private OdontologoController odontologoController;

    private JTable tablaOdontologos;
    private DefaultTableModel modeloTabla;

    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoMatricula;
    private JComboBox<String> comboTipo;

    private Long odontologoSeleccionadoId;

    public PanelOdontologos(OdontologoController odontologoController) {
        this.odontologoController = odontologoController;
        this.odontologoSeleccionadoId = null;

        inicializarPanel();
        cargarOdontologosEnTabla();
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
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del odontologo"));
        panelFormulario.setPreferredSize(new Dimension(340, 0));

        campoNombre = new JTextField(18);
        campoApellido = new JTextField(18);
        campoMatricula = new JTextField(18);
        comboTipo = new JComboBox<String>();
        comboTipo.addItem(TIPO_GENERAL);
        comboTipo.addItem(TIPO_ORTODONCISTA);
        comboTipo.addItem(TIPO_ENDODONCISTA);

        agregarCampo(panelFormulario, "Nombre", campoNombre, 0);
        agregarCampo(panelFormulario, "Apellido", campoApellido, 1);
        agregarCampo(panelFormulario, "Matricula", campoMatricula, 2);
        agregarCombo(panelFormulario, "Tipo", comboTipo, 3);

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

    private void agregarCombo(JPanel panel, String etiqueta, JComboBox<String> combo, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(combo, gbc);
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Matricula");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Honorarios");
        modeloTabla.addColumn("Urgencias");

        tablaOdontologos = new JTable(modeloTabla);
        tablaOdontologos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaOdontologos.getTableHeader().setReorderingAllowed(false);

        tablaOdontologos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarOdontologoSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaOdontologos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Odontologos registrados"));
        return scrollPane;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();

        JButton botonNuevo = new JButton("Nuevo / Limpiar");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonModificar = new JButton("Modificar");
        JButton botonEliminar = new JButton("Eliminar");
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
                guardarOdontologo();
            }
        });

        botonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarOdontologo();
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarOdontologo();
            }
        });

        botonRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarOdontologosEnTabla();
            }
        });

        panelBotones.add(botonNuevo);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRefrescar);

        return panelBotones;
    }

    private void guardarOdontologo() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Odontologo odontologo = crearOdontologoDesdeFormulario(null, 50000.0);
            odontologoController.registrar(odontologo);
            mostrarMensaje("Odontologo registrado correctamente.");
            limpiarFormulario();
            cargarOdontologosEnTabla();
        } catch (MatriculaDuplicadaException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void modificarOdontologo() {
        String matricula = pedirMatricula("Ingrese matricula a modificar");

        if (matricula == null) {
            return;
        }

        try {
            Odontologo odontologo = odontologoController.buscarPorMatricula(matricula);
            mostrarVentanaModificarDato(odontologo);
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarVentanaModificarDato(Odontologo odontologo) {
        String[] opciones = {
                "Nombre",
                "Apellido",
                "Matricula",
                "Tipo"
        };

        JComboBox<String> comboCampo = new JComboBox<String>(opciones);
        JTextField campoValor = new JTextField(18);
        JComboBox<String> comboTipoNuevo = new JComboBox<String>();
        comboTipoNuevo.addItem(TIPO_GENERAL);
        comboTipoNuevo.addItem(TIPO_ORTODONCISTA);
        comboTipoNuevo.addItem(TIPO_ENDODONCISTA);
        comboTipoNuevo.setSelectedItem(obtenerTipoOdontologo(odontologo));

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

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nuevo tipo"), gbc);

        gbc.gridx = 1;
        panel.add(comboTipoNuevo, gbc);

        comboTipoNuevo.setEnabled(false);

        comboCampo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean modificaTipo = comboCampo.getSelectedItem().toString().equals("Tipo");
                campoValor.setEnabled(!modificaTipo);
                comboTipoNuevo.setEnabled(modificaTipo);
            }
        });

        int resultado = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Modificar odontologo matricula " + odontologo.getMatricula(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String campo = comboCampo.getSelectedItem().toString();
        String nuevoValor = campoValor.getText().trim();

        try {
            Odontologo modificado = crearOdontologoModificado(
                    odontologo,
                    campo,
                    nuevoValor,
                    comboTipoNuevo.getSelectedItem().toString()
            );

            odontologoController.actualizar(modificado);
            mostrarMensaje("Odontologo modificado correctamente.");
            cargarOdontologoEnFormulario(modificado);
            cargarOdontologosEnTabla();
            seleccionarOdontologoEnTabla(modificado.getId());
        } catch (MatriculaDuplicadaException e) {
            mostrarError(e.getMessage());
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private Odontologo crearOdontologoModificado(
            Odontologo odontologo,
            String campo,
            String nuevoValor,
            String nuevoTipo) {

        String nombre = odontologo.getNombre();
        String apellido = odontologo.getApellido();
        String matricula = odontologo.getMatricula();
        String tipo = obtenerTipoOdontologo(odontologo);

        if (campo.equals("Nombre")) {
            validarNuevoValor(nuevoValor);
            nombre = nuevoValor;
        } else if (campo.equals("Apellido")) {
            validarNuevoValor(nuevoValor);
            apellido = nuevoValor;
        } else if (campo.equals("Matricula")) {
            validarNuevoValor(nuevoValor);
            matricula = nuevoValor;
        } else if (campo.equals("Tipo")) {
            tipo = nuevoTipo;
        }

        return crearOdontologo(tipo, odontologo.getId(), nombre, apellido, matricula, odontologo.getSalarioBase());
    }

    private void validarNuevoValor(String nuevoValor) {
        if (nuevoValor == null || nuevoValor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo valor no puede estar vacio.");
        }
    }

    private void eliminarOdontologo() {
        String matricula = pedirMatricula("Ingrese matricula a eliminar");

        if (matricula == null) {
            return;
        }

        Odontologo odontologo;

        try {
            odontologo = odontologoController.buscarPorMatricula(matricula);
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Desea eliminar a " + odontologo.getNombre() + " " + odontologo.getApellido() + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            odontologoController.eliminarPorId(odontologo.getId());
            mostrarMensaje("Odontologo eliminado correctamente.");
            limpiarFormulario();
            cargarOdontologosEnTabla();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private String pedirMatricula(String mensaje) {
        String valorInicial = "";

        if (odontologoSeleccionadoId != null) {
            valorInicial = campoMatricula.getText().trim();
        }

        String matricula = (String) JOptionPane.showInputDialog(
                this,
                mensaje,
                "Matricula",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                valorInicial
        );

        if (matricula == null) {
            return null;
        }

        matricula = matricula.trim();

        if (matricula.isEmpty()) {
            mostrarError("Debe ingresar una matricula.");
            return null;
        }

        return matricula;
    }

    private void cargarOdontologoSeleccionado() {
        int fila = tablaOdontologos.getSelectedRow();

        if (fila < 0) {
            return;
        }

        Long id = Long.parseLong(modeloTabla.getValueAt(fila, 0).toString());

        try {
            Odontologo odontologo = odontologoController.buscarPorId(id);
            cargarOdontologoEnFormulario(odontologo);
        } catch (OdontologoNoEncontradoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cargarOdontologoEnFormulario(Odontologo odontologo) {
        odontologoSeleccionadoId = odontologo.getId();
        campoNombre.setText(odontologo.getNombre());
        campoApellido.setText(odontologo.getApellido());
        campoMatricula.setText(odontologo.getMatricula());
        comboTipo.setSelectedItem(obtenerTipoOdontologo(odontologo));
    }

    private Odontologo crearOdontologoDesdeFormulario(Long id, double salarioBase) {
        String tipo = comboTipo.getSelectedItem().toString();
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String matricula = campoMatricula.getText().trim();

        return crearOdontologo(tipo, id, nombre, apellido, matricula, salarioBase);
    }

    private Odontologo crearOdontologo(String tipo, Long id, String nombre, String apellido, String matricula, double salarioBase) {
        if (tipo.equals(TIPO_GENERAL)) {
            return new OdontologoGeneral(id, nombre, apellido, matricula, salarioBase, true, 1);
        }

        if (tipo.equals(TIPO_ORTODONCISTA)) {
            return new Ortodoncista(id, nombre, apellido, matricula, salarioBase, "Brackets");
        }

        return new Endodoncista(id, nombre, apellido, matricula, salarioBase, true);
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

        if (campoMatricula.getText().trim().isEmpty()) {
            marcarCampoInvalido(campoMatricula, "La matricula es obligatoria.");
            return false;
        }

        return true;
    }

    private void marcarCampoInvalido(JTextField campo, String mensaje) {
        campo.setBackground(new Color(255, 230, 230));
        campo.requestFocus();
        mostrarError(mensaje);
    }

    private void limpiarColorCampos() {
        campoNombre.setBackground(Color.WHITE);
        campoApellido.setBackground(Color.WHITE);
        campoMatricula.setBackground(Color.WHITE);
    }

    private void limpiarFormulario() {
        odontologoSeleccionadoId = null;
        campoNombre.setText("");
        campoApellido.setText("");
        campoMatricula.setText("");
        comboTipo.setSelectedItem(TIPO_GENERAL);
        tablaOdontologos.clearSelection();
        limpiarColorCampos();
    }

    private void cargarOdontologosEnTabla() {
        modeloTabla.setRowCount(0);

        List<Odontologo> odontologos = odontologoController.listarTodos();

        for (Odontologo odontologo : odontologos) {
            modeloTabla.addRow(new Object[]{
                    odontologo.getId(),
                    odontologo.getNombre(),
                    odontologo.getApellido(),
                    odontologo.getMatricula(),
                    obtenerTipoOdontologo(odontologo),
                    odontologo.calcularHonorarios(),
                    odontologo.atiendeUrgencias() ? "Si" : "No"
            });
        }
    }

    private void seleccionarOdontologoEnTabla(Long id) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Long idTabla = Long.parseLong(modeloTabla.getValueAt(i, 0).toString());

            if (idTabla.equals(id)) {
                tablaOdontologos.setRowSelectionInterval(i, i);
                tablaOdontologos.scrollRectToVisible(tablaOdontologos.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private String obtenerTipoOdontologo(Odontologo odontologo) {
        if (odontologo instanceof OdontologoGeneral) {
            return TIPO_GENERAL;
        }

        if (odontologo instanceof Ortodoncista) {
            return TIPO_ORTODONCISTA;
        }

        if (odontologo instanceof Endodoncista) {
            return TIPO_ENDODONCISTA;
        }

        return "Desconocido";
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
