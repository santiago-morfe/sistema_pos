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
     * 
     * Proceso detallado:
     * 1. Valida que la ruta del directorio no sea nula o vacía.
     * 2. Verifica que el directorio exista en el sistema de archivos.
     * 3. Utiliza un DirectoryStream para iterar sobre todos los archivos cuyo nombre comience con "VEN" y termine en ".txt".
     * 4. Para cada archivo:
     *    a. Lee el contenido completo del archivo como un String.
     *    b. Divide el contenido en líneas usando el salto de línea.
     *    c. Crea una nueva instancia de Venta.
     *    d. Procesa cada línea del archivo:
     *       - Si la línea comienza con "Número:", extrae el número de venta.
     *       - Si la línea comienza con "Fecha y Hora:", extrae y valida la fecha y hora.
     *       - Si la línea comienza con "Nombre Completo:", extrae los datos del cliente y busca en las siguientes líneas la identificación, teléfono y correo.
     *       - Si la línea comienza con "Código", identifica el inicio de la tabla de productos, salta la línea de encabezado y procesa cada producto hasta encontrar la línea que comienza con "TOTALES".
     *         Para cada producto, extrae código, nombre (puede contener espacios), precio y cantidad.
     *       - Si la línea comienza con "Total:", extrae y valida el total de la venta.
     *    e. Inserta la venta procesada en el árbol binario.
     * 5. Si ocurre un error al leer un archivo, lo reporta por consola y continúa con el siguiente archivo.
     * 6. Si ocurre un error al abrir el directorio, lanza una excepción.
     * 
     * @param directoryPath Ruta del directorio
     * @throws IOException Si ocurre un error de lectura
     */
    public void loadFromDirectory(String directoryPath) throws IOException {
        // 1. Validar que la ruta no sea nula o vacía
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del directorio no puede ser nula o vacía");
        }

        // 2. Verificar que el directorio exista
        Path dirPath = Paths.get(directoryPath);
        if (!Files.exists(dirPath)) {
            throw new IOException("El directorio de ventas no existe: " + directoryPath);
        }

        // 3. Iterar sobre los archivos que cumplen el patrón VEN*.txt
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "VEN*.txt")) {
            for (Path path : stream) {
                try {
                    // 4.a Leer el contenido completo del archivo
                    String content = Files.readString(path);
                    // 4.b Dividir el contenido en líneas
                    String[] lines = content.split("\n");
                    
                    // 4.c Crear nueva venta
                    Venta venta = new Venta(null);
                    
                    // 4.d Procesar cada línea del archivo
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim();
                        
                        // Número de venta
                        if (line.startsWith("Número:")) {
                            // Extrae el número de venta después de los dos puntos
                            String numeroVenta = line.split(":")[1].trim();
                            venta.setNumeroVenta(numeroVenta);
                        }
                        // Fecha y hora
                        else if (line.startsWith("Fecha y Hora:")) {
                            // Extrae la fecha y hora y valida el formato
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
                            // Extrae el nombre completo y busca los siguientes datos del cliente en las siguientes líneas
                            String nombreCompleto = line.split(":")[1].trim();
                            String identificacion = "";
                            String tipoIdentificacion = "";
                            String telefono = "";
                            String correo = "";
                            String[] nombres = nombreCompleto.split(" ", 2);
                            String apellidos = nombres.length > 1 ? nombres[1] : "";
                            String nombresCliente = nombres[0];
                            // Buscar los siguientes datos del cliente en las siguientes líneas
                            int j = i + 1;
                            while (j < lines.length) {
                                String nextLine = lines[j].trim();
                                if (nextLine.startsWith("Identificación:")) {
                                    // Extrae tipo y número de identificación
                                    String[] idParts = nextLine.split(":")[1].trim().split(" ", 2);
                                    if (idParts.length == 2) {
                                        tipoIdentificacion = idParts[0];
                                        identificacion = idParts[1];
                                    }
                                } else if (nextLine.startsWith("Teléfono:")) {
                                    telefono = nextLine.split(":")[1].trim();
                                } else if (nextLine.startsWith("Correo:")) {
                                    correo = nextLine.split(":")[1].trim();
                                    break; // Último dato del cliente
                                } else if (nextLine.isEmpty()) {
                                    break;
                                }
                                j++;
                            }
                            Cliente cliente = new Cliente(identificacion, tipoIdentificacion, nombresCliente, apellidos, telefono, correo);
                            venta.setCliente(cliente);
                        }
                        // Productos
                        else if (line.startsWith("Código")) {
                            // Identifica el inicio de la tabla de productos y salta la línea de encabezado
                            i++;
                            while (i < lines.length && !lines[i].trim().startsWith("TOTALES")) {
                                // Divide la línea de producto en partes separadas por espacios
                                String[] productoData = lines[i].trim().split("\\s+");
                                if (productoData.length >= 5) {
                                    try {
                                        String codigo = productoData[0];
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
                            // Extrae y valida el total de la venta
                            String totalStr = line.split(":")[1].trim().replace("$", "").replace(",", ".");
                            try {
                                Double.valueOf(totalStr); // Solo validar el formato
                            } catch (NumberFormatException e) {
                                System.err.println("Error al procesar el total en línea " + (i + 1) + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    // 4.e Inserta la venta procesada en el árbol binario
                    insert(venta);
                    
                } catch (IOException e) {
                    // 5. Reporta error de archivo y continúa
                    System.err.println("Error al procesar archivo " + path.getFileName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // 6. Error al abrir el directorio
            throw new IOException("Error al leer el directorio de ventas: " + e.getMessage(), e);
        }
    }
}