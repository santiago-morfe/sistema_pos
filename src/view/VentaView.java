package view;

import controllers.VentaController;
import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Venta;

public class VentaView extends JPanel {
    private JTable tblVentas;
    private DefaultTableModel tableModel;
    private JButton btnNuevaVenta;
    private JButton btnVerDetalle;
    private JButton btnOrdenar;
    private boolean ordenAscendente = true;

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
        btnOrdenar = new JButton("Ordenar ↑");
        buttonPanel.add(btnNuevaVenta);
        buttonPanel.add(btnVerDetalle);
        buttonPanel.add(btnOrdenar);

        // Agregar componentes al panel principal
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar listeners
        btnNuevaVenta.addActionListener(e -> abrirNuevaVenta());
        btnVerDetalle.addActionListener(e -> verDetalleVenta());
        btnOrdenar.addActionListener(e -> ordenarVentas());
    }

    private void ordenarVentas() {
        try {
            List<Venta> ventas = VentaController.listarVentas();
            
            // Ordenar la lista
            if (ordenAscendente) {
                ventas.sort(Comparator.comparingDouble(Venta::getTotal));
                btnOrdenar.setText("Ordenar ↓");
            } else {
                ventas.sort(Comparator.comparingDouble(Venta::getTotal).reversed());
                btnOrdenar.setText("Ordenar ↑");
            }
            
            // Actualizar la tabla
            tableModel.setRowCount(0);
            for (Venta venta : ventas) {
                if (venta == null) continue;
                
                String clienteInfo = "Sin cliente";
                if (venta.getCliente() != null) {
                    clienteInfo = venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos();
                }
                
                Object[] row = {
                    venta.getNumeroVenta(),
                    venta.getFechaHora(),
                    clienteInfo,
                    String.format("%.2f", venta.getTotal())
                };
                tableModel.addRow(row);
            }
            
            ordenAscendente = !ordenAscendente;
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al ordenar ventas: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVentas() {
        try {
            tableModel.setRowCount(0);
            List<Venta> ventas = VentaController.listarVentas();
            
            if (ventas == null || ventas.isEmpty()) {
                return; // No hay ventas para mostrar
            }
            
            for (Venta venta : ventas) {
                if (venta == null) continue;
                
                String clienteInfo = "Sin cliente";
                if (venta.getCliente() != null) {
                    clienteInfo = venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos();
                }
                
                Object[] row = {
                    venta.getNumeroVenta(),
                    venta.getFechaHora(),
                    clienteInfo,
                    String.format("%.2f", venta.getTotal())
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar ventas: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar el detalle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para ver su detalle",
                "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
} 