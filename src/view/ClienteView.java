package view;

import controllers.ClienteController;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Cliente;

public class ClienteView extends JPanel {
    private JTextField txtIdentificacion;
    private JComboBox<String> cmbTipoIdentificacion;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTable tblClientes;
    private DefaultTableModel tableModel;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public ClienteView() {
        setLayout(new BorderLayout());
        initComponents();
        cargarClientes();
    }

    private void initComponents() {
        // Panel izquierdo - Tabla de clientes
        JPanel leftPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Identificación", "Tipo ID", "Nombres", "Apellidos", "Teléfono", "Correo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblClientes);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        // Panel derecho - Formulario
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Identificación:"), gbc);
        gbc.gridx = 1;
        txtIdentificacion = new JTextField(20);
        formPanel.add(txtIdentificacion, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tipo ID:"), gbc);
        gbc.gridx = 1;
        cmbTipoIdentificacion = new JComboBox<>(new String[]{"CC", "CE"});
        formPanel.add(cmbTipoIdentificacion, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nombres:"), gbc);
        gbc.gridx = 1;
        txtNombres = new JTextField(20);
        formPanel.add(txtNombres, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 1;
        txtApellidos = new JTextField(20);
        formPanel.add(txtApellidos, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        formPanel.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        txtCorreo = new JTextField(20);
        formPanel.add(txtCorreo, gbc);

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

        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Formulario de Cliente"));

        // Agregar componentes al panel principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);

        // Agregar listeners
        btnCrear.addActionListener(e -> crearCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarCliente();
            }
        });
    }

    private void cargarClientes() {
        try {
            tableModel.setRowCount(0);
            List<Cliente> clientes = ClienteController.listarClientes();
            for (Cliente cliente : clientes) {
                Object[] row = {
                    cliente.getIdentificacion(),
                    cliente.getTipoIdentificacion(),
                    cliente.getNombres(),
                    cliente.getApellidos(),
                    cliente.getTelefono(),
                    cliente.getCorreoElectronico()
                };
                tableModel.addRow(row);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearCliente() {
        try {
            ClienteController.crearCliente(
                txtIdentificacion.getText(),
                (String) cmbTipoIdentificacion.getSelectedItem(),
                txtNombres.getText(),
                txtApellidos.getText(),
                txtTelefono.getText(),
                txtCorreo.getText()
            );
            JOptionPane.showMessageDialog(this, "Cliente creado exitosamente");
            limpiarFormulario();
            cargarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        try {
            ClienteController.actualizarCliente(
                txtIdentificacion.getText(),
                (String) cmbTipoIdentificacion.getSelectedItem(),
                txtNombres.getText(),
                txtApellidos.getText(),
                txtTelefono.getText(),
                txtCorreo.getText()
            );
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente");
            limpiarFormulario();
            cargarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                ClienteController.eliminarCliente(txtIdentificacion.getText());
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
                limpiarFormulario();
                cargarClientes();
            }
        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarCliente() {
        int selectedRow = tblClientes.getSelectedRow();
        if (selectedRow >= 0) {
            txtIdentificacion.setText((String) tableModel.getValueAt(selectedRow, 0));
            cmbTipoIdentificacion.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            txtNombres.setText((String) tableModel.getValueAt(selectedRow, 2));
            txtApellidos.setText((String) tableModel.getValueAt(selectedRow, 3));
            txtTelefono.setText((String) tableModel.getValueAt(selectedRow, 4));
            txtCorreo.setText((String) tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void limpiarFormulario() {
        txtIdentificacion.setText("");
        cmbTipoIdentificacion.setSelectedIndex(0);
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tblClientes.clearSelection();
    }
} 