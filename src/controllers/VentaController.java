package controllers;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import models.Cliente;
import models.Producto;
import models.Venta;
import utils.BinaryTree;

public class VentaController {
    private static final String VENTAS_DIR = "Ventas";
    private static int contadorVentas = 0;
    private static BinaryTree ventasTree;

    static {
        inicializarVentas();
    }

    /**
     * Inicializa el sistema de ventas:
     * 1. Crea una nueva instancia del árbol binario de ventas.
     * 2. Crea el directorio de ventas si no existe.
     * 3. Cuenta la cantidad de archivos de ventas existentes (archivos que cumplen el patrón "VEN*.txt")
     *    para mantener actualizado el contador de ventas.
     * 4. Carga todas las ventas existentes desde los archivos del directorio al árbol binario.
     * 5. Si ocurre algún error, imprime el mensaje y termina la aplicación.
     */
    private static void inicializarVentas() {
        ventasTree = new BinaryTree();
        // Crear directorio de ventas si no existe
        try {
            Files.createDirectories(Paths.get(VENTAS_DIR));
            // Contar ventas existentes
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(VENTAS_DIR), "VEN*.txt")) {
                stream.forEach(path -> contadorVentas++);
            }
            // Cargar ventas existentes en el árbol binario
            ventasTree.loadFromDirectory(VENTAS_DIR);
        } catch (IOException e) {
            System.err.println("Error al inicializar el directorio de ventas: " + e.getMessage());
            System.exit(1);
        }
    }

    public static Venta iniciarVenta() {
        return new Venta(null); // Iniciar sin cliente
    }

    public static void asociarCliente(Venta venta, String identificacionCliente) throws Exception {
        Cliente cliente = ClienteController.consultarCliente(identificacionCliente);
        if (cliente == null) {
            throw new Exception("Cliente no encontrado");
        }
        venta.setCliente(cliente);
    }

    public static void agregarProductoAVenta(Venta venta, String codigoProducto, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor que 0");
        }

        Producto producto = ProductoController.consultarProducto(codigoProducto);
        if (producto == null) {
            throw new Exception("Producto no encontrado");
        }

        venta.agregarProducto(producto, cantidad);
    }

    public static String finalizarVenta(Venta venta) throws IOException {
        if (venta.getCliente() == null) {
            throw new IOException("Debe asociar un cliente a la venta");
        }

        contadorVentas++;
        String numeroVenta = String.format("VEN%03d", contadorVentas);
        venta.setNumeroVenta(numeroVenta);

        // Generar y guardar ticket
        String ticket = venta.generarTicket();
        String nombreArchivo = numeroVenta + ".txt";
        Path rutaArchivo = Paths.get(VENTAS_DIR, nombreArchivo);

        try (BufferedWriter writer = Files.newBufferedWriter(rutaArchivo)) {
            writer.write(ticket);
        }

        // Agregar la venta al árbol binario
        ventasTree.insert(venta);

        return nombreArchivo;
    }

    public static List<Venta> listarVentas() {
        return ventasTree.inorderTraversal();
    }

    public static String obtenerContenidoVenta(String nombreArchivo) throws IOException {
        Path rutaArchivo = Paths.get(VENTAS_DIR, nombreArchivo);
        return Files.readString(rutaArchivo);
    }
}