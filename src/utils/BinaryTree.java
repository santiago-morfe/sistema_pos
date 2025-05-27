/**
 * Clase BinaryTree
 * ----------------
 * Implementa un árbol binario de búsqueda para almacenar objetos Venta, permitiendo
 * inserción y recorrido en orden. Además, proporciona funcionalidad para cargar ventas
 * desde archivos de texto en un directorio, procesando la información de clientes y productos.
 *
 * Mejoras:
 * - Documentación Javadoc agregada a la clase y métodos principales.
 * - Mejora en la carga de productos: ahora el nombre del producto puede contener espacios.
 *
 * Autor: [Tu Nombre]
 * Fecha: 2025-05-27
 */
package utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import models.Producto;
import models.Venta;

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

    /**
     * Inserta una venta en el árbol binario.
     * @param venta Venta a insertar
     */
    public void insert(Venta venta) {
        root = insertRec(root, venta);
    }

    /**
     * Inserta recursivamente una venta en el árbol.
     * @param root Nodo actual
     * @param venta Venta a insertar
     * @return Nodo actualizado
     */
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

    /**
     * Realiza un recorrido en orden del árbol y retorna la lista de ventas.
     * @return Lista de ventas en orden
     */
    public List<Venta> inorderTraversal() {
        List<Venta> ventas = new ArrayList<>();
        inorderRec(root, ventas);
        return ventas;
    }

    /**
     * Recorrido en orden recursivo.
     * @param root Nodo actual
     * @param ventas Lista acumulada de ventas
     */
    private void inorderRec(Node root, List<Venta> ventas) {
        if (root != null) {
            inorderRec(root.left, ventas);
            ventas.add(root.venta);
            inorderRec(root.right, ventas);
        }
    }

    /**
     * Carga ventas desde archivos en un directorio. Cada archivo debe tener el formato esperado.
     * El nombre del producto puede contener espacios.
     * @param directoryPath Ruta del directorio
     * @throws IOException Si ocurre un error de lectura
     */
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
                                        // --- Mejora: nombre con espacios ---
                                        // El nombre es todo lo que está entre el código y el precio
                                        int idxPrecio = productoData.length - 3;
                                        StringBuilder nombreBuilder = new StringBuilder();
                                        for (int j = 1; j < idxPrecio; j++) {
                                            if (j > 1) nombreBuilder.append(" ");
                                            nombreBuilder.append(productoData[j]);
                                        }
                                        String nombre = nombreBuilder.toString();
                                        double precio = Double.parseDouble(productoData[idxPrecio].replace(",", "."));
                                        int cantidad = Integer.parseInt(productoData[idxPrecio + 1]);
                                        
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
                                Double.valueOf(totalStr); // Solo validar el formato
                            } catch (NumberFormatException e) {
                                System.err.println("Error al procesar el total en línea " + (i + 1) + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    insert(venta);
                    
                } catch (IOException e) {
                    System.err.println("Error al procesar archivo " + path.getFileName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IOException("Error al leer el directorio de ventas: " + e.getMessage(), e);
        }
    }
}