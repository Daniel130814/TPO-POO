package presentacion.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarioDialog extends JDialog {
    private YearMonth mesVisible;
    private LocalDate fechaSeleccionada;
    private JPanel panelDias;
    private JLabel etiquetaMes;

    public CalendarioDialog(Frame owner) {
        super(owner, "Seleccionar fecha", true);
        this.mesVisible = YearMonth.now();
        this.fechaSeleccionada = null;

        inicializar();
        cargarDias();
    }

    public static LocalDate seleccionarFecha(Component parent) {
        Frame frame = null;

        if (parent != null) {
            frame = (Frame) javax.swing.SwingUtilities.getWindowAncestor(parent);
        }

        CalendarioDialog dialog = new CalendarioDialog(frame);
        dialog.setVisible(true);
        return dialog.getFechaSeleccionada();
    }

    private void inicializar() {
        setSize(420, 340);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(8, 8));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JButton botonAnterior = new JButton("<");
        JButton botonSiguiente = new JButton(">");
        etiquetaMes = new JLabel("", SwingConstants.CENTER);

        botonAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mesVisible = mesVisible.minusMonths(1);
                cargarDias();
            }
        });

        botonSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mesVisible = mesVisible.plusMonths(1);
                cargarDias();
            }
        });

        panelSuperior.add(botonAnterior, BorderLayout.WEST);
        panelSuperior.add(etiquetaMes, BorderLayout.CENTER);
        panelSuperior.add(botonSiguiente, BorderLayout.EAST);

        panelDias = new JPanel(new GridLayout(0, 7, 4, 4));

        add(panelSuperior, BorderLayout.NORTH);
        add(panelDias, BorderLayout.CENTER);
    }

    private void cargarDias() {
        panelDias.removeAll();
        etiquetaMes.setText(mesVisible.getMonth().toString() + " " + mesVisible.getYear());

        agregarEncabezados();
        agregarEspaciosIniciales();
        agregarBotonesDias();

        panelDias.revalidate();
        panelDias.repaint();
    }

    private void agregarEncabezados() {
        String[] dias = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};

        for (int i = 0; i < dias.length; i++) {
            JLabel etiqueta = new JLabel(dias[i], SwingConstants.CENTER);
            panelDias.add(etiqueta);
        }
    }

    private void agregarEspaciosIniciales() {
        LocalDate primerDia = mesVisible.atDay(1);
        int espacios = primerDia.getDayOfWeek().getValue() - 1;

        for (int i = 0; i < espacios; i++) {
            panelDias.add(new JLabel(""));
        }
    }

    private void agregarBotonesDias() {
        int cantidadDias = mesVisible.lengthOfMonth();
        LocalDate hoy = LocalDate.now();

        for (int dia = 1; dia <= cantidadDias; dia++) {
            final LocalDate fecha = mesVisible.atDay(dia);
            JButton botonDia = new JButton(String.valueOf(dia));
            botonDia.setEnabled(!fecha.isBefore(hoy));

            botonDia.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fechaSeleccionada = fecha;
                    dispose();
                }
            });

            panelDias.add(botonDia);
        }
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }
}
