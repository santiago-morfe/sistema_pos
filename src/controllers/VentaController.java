package controllers;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import models.Venta;
import models.Cliente;
import models.Producto;

public class VentaController {
    private static final String VENTAS_DIR = "Ventas";
    private static final String VENTAS_FILE = "data/ventas.txt";
    private static int contadorVentas = 0;

    static {
        // Crear directorio de ventas si no existe
        try {
            Files.createDirectories(Paths.get(VENTAS_DIR));
            Files.createDirectories(Paths.get("data"));
            // Contar ventas existentes
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(VENTAS_DIR), "VEN*.txt")) {
                stream.forEach(path -> contadorVentas++);
            }
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

        // Guardar información de la venta en data/ventas.txt
        guardarVentaEnArchivo(venta);

        return nombreArchivo;
    }

    private static void guardarVentaEnArchivo(Venta venta) throws IOException {
        try (FileWriter fw = new FileWriter(VENTAS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            StringBuilder ventaData = new StringBuilder();
            ventaData.append(venta.getNumeroVenta()).append("*");
            ventaData.append(venta.getFechaHora()).append("*");
            ventaData.append(venta.getCliente().getIdentificacion()).append("*");
            ventaData.append(venta.getSubtotal()).append("*");
            ventaData.append(venta.getTotalIva()).append("*");
            ventaData.append(venta.getTotal()).append("*");
            
            // Agregar detalles de productos
            List<String> detalles = new ArrayList<>();
            for (var detalle : venta.getDetalles()) {
                detalles.add(String.format("%s,%d", 
                    detalle.getProducto().getCodigo(), 
                    detalle.getCantidad()));
            }
            ventaData.append(String.join(";", detalles));
            
            out.println(ventaData.toString());
        }
    }

    public static List<Venta> listarVentas() throws IOException {
        List<Venta> ventas = new ArrayList<>();
        File file = new File(VENTAS_FILE);
        if (!file.exists()) {
            return ventas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split("\\*");
                if (partes.length >= 7) {
                    Venta venta = new Venta(ClienteController.consultarCliente(partes[2]));
                    venta.setNumeroVenta(partes[0]);
                    // Aquí podrías cargar más detalles si es necesario
                    ventas.add(venta);
                }
            }
        }
        return ventas;
    }

    public static String obtenerContenidoVenta(String nombreArchivo) throws IOException {
        Path rutaArchivo = Paths.get(VENTAS_DIR, nombreArchivo);
        return Files.readString(rutaArchivo);
    }
} 