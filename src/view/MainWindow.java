package view;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    private JTabbedPane tabbedPane;
    private ClienteView clienteView;
    private ProductoView productoView;
    private VentaView ventaView;

    public MainWindow() {
        setTitle("Sistema POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        // Crear las vistas
        ventaView = new VentaView();
        clienteView = new ClienteView();
        productoView = new ProductoView();

        // Agregar las vistas al tabbedPane
        tabbedPane.addTab("Ventas", new ImageIcon(), ventaView, "Gestión de Ventas");
        tabbedPane.addTab("Clientes", new ImageIcon(), clienteView, "Gestión de Clientes");
        tabbedPane.addTab("Productos", new ImageIcon(), productoView, "Gestión de Productos");

        // Agregar el tabbedPane al frame
        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            }
            new MainWindow().setVisible(true);
        });
    }
} 