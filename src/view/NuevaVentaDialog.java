package view;

import controllers.ClienteController;
import controllers.ProductoController;
import controllers.VentaController;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Cliente;
import models.Producto;
import models.Venta;

public class NuevaVentaDialog extends JDialog {
    private JTable tblProductos;
    private DefaultTableModel tableModelProductos;
    private JTable tblProductosVenta;
    private DefaultTableModel tableModelVenta;
    private JTextField txtCantidad;
    private JComboBox<Cliente> cmbClientes;
    private JButton btnAgregarProducto;
    private JButton btnAsociarCliente;
    private JButton btnFinalizarVenta;
    private final Venta ventaActual;

    public NuevaVentaDialog(Component parent) {
        super((Frame)SwingUtilities.getWindowAncestor(parent), "Nueva Venta", true);
        ventaActual = VentaController.iniciarVenta();
        initComponents();
        cargarProductos();
        cargarClientes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(getParent());

        // Panel izquierdo - Lista de productos disponibles
        JPanel leftPanel = new JPanel(new BorderLayout());
        String[] columnNamesProductos = {"Código", "Nombre", "Precio"};
        tableModelProductos = new DefaultTableModel(columnNamesProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos = new JTable(tableModelProductos);
        leftPanel.add(new JScrollPane(tblProductos), BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));

        // Panel derecho - Productos en la venta
        JPanel rightPanel = new JPanel(new BorderLayout());
        String[] columnNamesVenta = {"Código", "Nombre", "Precio", "Cantidad", "Subtotal"};
        tableModelVenta = new DefaultTableModel(columnNamesVenta, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductosVenta = new JTable(tableModelVenta);
        rightPanel.add(new JScrollPane(tblProductosVenta), BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Productos en la Venta"));

        // Panel central - Controles
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de cliente
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cmbClientes = new JComboBox<>();
        cmbClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente cliente) {
                    setText(cliente.getNombres() + " " + cliente.getApellidos() + " (" + cliente.getIdentificacion() + ")");
                }
                return this;
            }
        });
        centerPanel.add(cmbClientes, gbc);
        gbc.gridx = 2;
        btnAsociarCliente = new JButton("Asociar Cliente");
        centerPanel.add(btnAsociarCliente, gbc);

        // Campo de cantidad
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        txtCantidad = new JTextField(20);
        centerPanel.add(txtCantidad, gbc);
        gbc.gridx = 2;
        btnAgregarProducto = new JButton("Agregar Producto");
        centerPanel.add(btnAgregarProducto, gbc);

        // Botón finalizar
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        btnFinalizarVenta = new JButton("Finalizar Venta");
        centerPanel.add(btnFinalizarVenta, gbc);

        // Agregar listeners
        btnAsociarCliente.addActionListener(e -> asociarCliente());
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnFinalizarVenta.addActionListener(e -> finalizarVenta());

        // Organizar paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
    }

    private void cargarClientes() {
        try {
            cmbClientes.removeAllItems();
            List<Cliente> clientes = ClienteController.listarClientes();
            for (Cliente cliente : clientes) {
                cmbClientes.addItem(cliente);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarProductos() {
        try {
            tableModelProductos.setRowCount(0);
            List<Producto> productos = ProductoController.listarProductos();
            for (Producto producto : productos) {
                Object[] row = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecioVenta()
                };
                tableModelProductos.addRow(row);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociarCliente() {
        try {
            Cliente clienteSeleccionado = (Cliente) cmbClientes.getSelectedItem();
            if (clienteSeleccionado == null) {
                throw new Exception("Debe seleccionar un cliente");
            }
            VentaController.asociarCliente(ventaActual, clienteSeleccionado.getIdentificacion());
            JOptionPane.showMessageDialog(this, "Cliente asociado exitosamente");
            cmbClientes.setEnabled(false);
            btnAsociarCliente.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProducto() {
        int selectedRow = tblProductos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la lista",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String codigo = (String) tableModelProductos.getValueAt(selectedRow, 0);
            int cantidad = Integer.parseInt(txtCantidad.getText());
            VentaController.agregarProductoAVenta(ventaActual, codigo, cantidad);
            actualizarTablaVenta();
            txtCantidad.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaVenta() {
        tableModelVenta.setRowCount(0);
        for (var detalle : ventaActual.getDetalles()) {
            Producto p = detalle.getProducto();
            Object[] row = {
                p.getCodigo(),
                p.getNombre(),
                p.getPrecioVenta(),
                detalle.getCantidad(),
                detalle.getSubtotal()
            };
            tableModelVenta.addRow(row);
        }
    }

    private void finalizarVenta() {
        if (ventaActual.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la venta",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nombreArchivo = VentaController.finalizarVenta(ventaActual);
            JOptionPane.showMessageDialog(this, 
                "Venta finalizada exitosamente.\nArchivo generado: " + nombreArchivo);
            dispose();
        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} 