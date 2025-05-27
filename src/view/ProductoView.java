package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import models.Producto;
import controllers.ProductoController;

public class ProductoView extends JPanel {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTable tblProductos;
    private DefaultTableModel tableModel;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public ProductoView() {
        setLayout(new BorderLayout());
        initComponents();
        cargarProductos();
    }

    private void initComponents() {
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        formPanel.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        formPanel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(20);
        formPanel.add(txtPrecio, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        btnCrear = new JButton("Crear");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);

        // Tabla de productos
        String[] columnNames = {"Código", "Nombre", "Precio"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblProductos);

        // Agregar componentes al panel principal
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar listeners
        btnCrear.addActionListener(e -> crearProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProducto();
            }
        });
    }

    private void crearProducto() {
        try {
            float precio = Float.parseFloat(txtPrecio.getText());
            ProductoController.crearProducto(
                txtCodigo.getText(),
                txtNombre.getText(),
                precio
            );
            JOptionPane.showMessageDialog(this, "Producto creado exitosamente");
            limpiarFormulario();
            cargarProductos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        try {
            float precio = Float.parseFloat(txtPrecio.getText());
            ProductoController.actualizarProducto(
                txtCodigo.getText(),
                txtNombre.getText(),
                precio
            );
            JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente");
            limpiarFormulario();
            cargarProductos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                ProductoController.eliminarProducto(txtCodigo.getText());
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente");
                limpiarFormulario();
                cargarProductos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarProductos() {
        try {
            tableModel.setRowCount(0);
            List<Producto> productos = ProductoController.listarProductos();
            for (Producto producto : productos) {
                Object[] row = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecioVenta()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarProducto() {
        int selectedRow = tblProductos.getSelectedRow();
        if (selectedRow >= 0) {
            txtCodigo.setText((String) tableModel.getValueAt(selectedRow, 0));
            txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtPrecio.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        tblProductos.clearSelection();
    }
} 