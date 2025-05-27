package utils;

import models.Venta;
import models.Cliente;
import models.Producto;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BinaryTree {
    private Node root;

    private class Node {
        Venta venta;
        Node left;
        Node right;

        Node(Venta venta) {
            this.venta = venta;
            this.left = null;
            this.right = null;
        }
    }

    public void insert(Venta venta) {
        root = insertRec(root, venta);
    }

    private Node insertRec(Node root, Venta venta) {
        if (root == null) {
            root = new Node(venta);
            return root;
        }

        if (venta.getTotal() < root.venta.getTotal()) {
            root.left = insertRec(root.left, venta);
        } else if (venta.getTotal() > root.venta.getTotal()) {
            root.right = insertRec(root.right, venta);
        }

        return root;
    }

    public List<Venta> inorderTraversal() {
        List<Venta> ventas = new ArrayList<>();
        inorderRec(root, ventas);
        return ventas;
    }

    private void inorderRec(Node root, List<Venta> ventas) {
        if (root != null) {
            inorderRec(root.left, ventas);
            ventas.add(root.venta);
            inorderRec(root.right, ventas);
        }
    }

    public void loadFromDirectory(String directoryPath) throws IOException {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del directorio no puede ser nula o vacía");
        }

        Path dirPath = Paths.get(directoryPath);
        if (!Files.exists(dirPath)) {
            throw new IOException("El directorio de ventas no existe: " + directoryPath);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "VEN*.txt")) {
            for (Path path : stream) {
                try {
                    String content = Files.readString(path);
                    String[] lines = content.split("\n");
                    
                    // Crear nueva venta
                    Venta venta = new Venta(null);
                    
                    // Procesar cada sección del archivo
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim();
                        
                        // Número de venta
                        if (line.startsWith("Número:")) {
                            String numeroVenta = line.split(":")[1].trim();
                            venta.setNumeroVenta(numeroVenta);
                        }
                        
                        // Fecha y hora
                        else if (line.startsWith("Fecha y Hora:")) {
                            String fechaStr = line.split(":")[1].trim();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
                            try {
                                LocalDateTime.parse(fechaStr, formatter); // Solo validar el formato
                            } catch (Exception e) {
                                System.err.println("Error al procesar fecha en línea " + (i + 1) + ": " + e.getMessage());
                            }
                        }
                        
                        // Datos del cliente
                        else if (line.startsWith("Nombre Completo:")) {
                            String nombreCompleto = line.split(":")[1].trim();
                            String[] nombres = nombreCompleto.split(" ", 2);
                            Cliente cliente = new Cliente("1234567890", "CC", nombres[0], 
                                nombres.length > 1 ? nombres[1] : "", "1234567890", "cliente@email.com");
                            venta.setCliente(cliente);
                        }
                        
                        // Productos
                        else if (line.startsWith("Código")) {
                            // Saltar la línea de encabezado
                            i++;
                            while (i < lines.length && !lines[i].trim().startsWith("TOTALES")) {
                                String[] productoData = lines[i].trim().split("\\s+");
                                if (productoData.length >= 5) {
                                    try {
                                        String codigo = productoData[0];
                                        String nombre = productoData[1];
                                        double precio = Double.parseDouble(productoData[2].replace(",", "."));
                                        int cantidad = Integer.parseInt(productoData[3]);
                                        
                                        Producto producto = new Producto(codigo, nombre, (float)precio);
                                        venta.agregarProducto(producto, cantidad);
                                    } catch (NumberFormatException e) {
                                        System.err.println("Error al procesar producto en línea " + (i + 1) + ": " + e.getMessage());
                                    }
                                }
                                i++;
                            }
                        }
                        
                        // Total
                        else if (line.startsWith("Total:")) {
                            String totalStr = line.split(":")[1].trim().replace("$", "").replace(",", ".");
                            try {
                                Double.parseDouble(totalStr); // Solo validar el formato
                            } catch (NumberFormatException e) {
                                System.err.println("Error al procesar el total en línea " + (i + 1) + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    insert(venta);
                    
                } catch (Exception e) {
                    System.err.println("Error al procesar archivo " + path.getFileName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new IOException("Error al leer el directorio de ventas: " + e.getMessage(), e);
        }
    }
} 