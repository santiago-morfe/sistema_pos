package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import models.Venta;
import controllers.VentaController;

public class VentaView extends JPanel {
    private JTable tblVentas;
    private DefaultTableModel tableModel;
    private JButton btnNuevaVenta;
    private JButton btnVerDetalle;

    public VentaView() {
        setLayout(new BorderLayout());
        initComponents();
        cargarVentas();
    }

    private void initComponents() {
        // Tabla de ventas
        String[] columnNames = {"Número", "Fecha", "Cliente", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblVentas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblVentas);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        btnNuevaVenta = new JButton("Nueva Venta");
        btnVerDetalle = new JButton("Ver Detalle");
        buttonPanel.add(btnNuevaVenta);
        buttonPanel.add(btnVerDetalle);

        // Agregar componentes al panel principal
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar listeners
        btnNuevaVenta.addActionListener(e -> abrirNuevaVenta());
        btnVerDetalle.addActionListener(e -> verDetalleVenta());
    }

    private void cargarVentas() {
        try {
            tableModel.setRowCount(0);
            List<Venta> ventas = VentaController.listarVentas();
            for (Venta venta : ventas) {
                Object[] row = {
                    venta.getNumeroVenta(),
                    venta.getFechaHora(),
                    venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos(),
                    venta.getTotal()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirNuevaVenta() {
        NuevaVentaDialog dialog = new NuevaVentaDialog(this);
        dialog.setVisible(true);
        cargarVentas(); // Recargar la lista después de crear una nueva venta
    }

    private void verDetalleVenta() {
        int selectedRow = tblVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroVenta = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                String contenido = VentaController.obtenerContenidoVenta(numeroVenta + ".txt");
                JTextArea textArea = new JTextArea(contenido);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, 
                    "Detalle de Venta " + numeroVenta, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el detalle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para ver su detalle",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
} 