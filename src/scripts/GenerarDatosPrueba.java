package scripts;

import java.io.*;
import java.nio.file.*;

public class GenerarDatosPrueba {
    private static final String CLIENTES_FILE = "data/clientes.txt";
    private static final String PRODUCTOS_FILE = "data/productos.txt";

    public static void main(String[] args) {
        try {
            // Crear directorio data si no existe
            Files.createDirectories(Paths.get("data"));

            // Generar datos de clientes
            generarClientes();

            // Generar datos de productos
            generarProductos();

            System.out.println("Datos de prueba generados exitosamente!");
        } catch (IOException e) {
            System.err.println("Error al generar datos de prueba: " + e.getMessage());
        }
    }

    private static void generarClientes() throws IOException {
        String[] clientes = {
            "1234567890*CC*Juan Carlos*Pérez González*3101234567*juan.perez@email.com",
            "2345678901*CC*María Isabel*Rodríguez López*3202345678*maria.rodriguez@email.com",
            "3456789012*CC*Carlos Alberto*Martínez Silva*3303456789*carlos.martinez@email.com",
            "4567890123*CE*Ana Patricia*Gómez Torres*3404567890*ana.gomez@email.com",
            "5678901234*CC*Luis Fernando*Díaz Ramírez*3505678901*luis.diaz@email.com",
            "6789012345*CC*Laura Marcela*Hernández Vargas*3606789012*laura.hernandez@email.com",
            "7890123456*CE*Pedro José*Sánchez Mendoza*3707890123*pedro.sanchez@email.com",
            "8901234567*CC*Sofía Elena*Torres Jiménez*3808901234*sofia.torres@email.com",
            "9012345678*CC*Andrés Felipe*Ramírez Castro*3909012345*andres.ramirez@email.com",
            "0123456789*CE*Carolina Andrea*Vargas Rojas*3000123456*carolina.vargas@email.com"
        };

        try (PrintWriter writer = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (String cliente : clientes) {
                writer.println(cliente);
            }
        }
    }

    private static void generarProductos() throws IOException {
        String[] productos = {
            "AB001*Laptop HP*1200000",
            "AB002*Monitor LG*450000",
            "AB003*Teclado Mec*150000",
            "AB004*Mouse Gamer*80000",
            "AB005*Audífonos*120000",
            "CD001*Impresora HP*350000",
            "CD002*Scanner Epson*280000",
            "CD003*Webcam Logi*95000",
            "CD004*Micrófono*75000",
            "CD005*Parlantes*180000",
            "EF001*Tablet Samsung*850000",
            "EF002*Smartwatch*320000",
            "EF003*Cargador USB*25000",
            "EF004*Hub USB*45000",
            "EF005*Memoria USB*35000",
            "GH001*Disco SSD*280000",
            "GH002*Memoria RAM*180000",
            "GH003*Tarjeta SD*45000",
            "GH004*Adaptador HDMI*25000",
            "GH005*Cable USB-C*15000"
        };

        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCTOS_FILE))) {
            for (String producto : productos) {
                writer.println(producto);
            }
        }
    }
} 