package controllers;

import java.io.IOException;
import java.util.List;
import models.Producto;
import data.DataManager;

public class ProductoController {
    
    public static void crearProducto(String codigo, String nombre, float precioVenta) throws Exception {
        // Validaciones

        if (consultarProducto(codigo) != null) {
            throw new Exception("El producto ya existe");
        }

        if (codigo.length() != 5) {
            throw new Exception("El código debe tener 5 caracteres");
        }
        
        if (!codigo.substring(0, 2).matches("[a-zA-Z]+")) {
            throw new Exception("Los dos primeros caracteres del código deben ser letras");
        }
        
        if (!codigo.substring(2).matches("\\d+")) {
            throw new Exception("Los últimos tres caracteres del código deben ser números");
        }
        
        if (nombre.length() > 10) {
            throw new Exception("El nombre no puede tener más de 10 caracteres");
        }
        
        if (precioVenta <= 0) {
            throw new Exception("El precio de venta debe ser mayor que 0");
        }

        Producto producto = new Producto(codigo, nombre, precioVenta);
        DataManager.guardarProducto(producto);
    }

    public static Producto consultarProducto(String codigo) throws IOException {
        List<Producto> productos = DataManager.cargarProductos();
        return productos.stream()
                       .filter(p -> p.getCodigo().equals(codigo))
                       .findFirst()
                       .orElse(null);
    }

    public static void actualizarProducto(String codigo, String nombre, float precioVenta) throws Exception {
        Producto producto = consultarProducto(codigo);
        if (producto == null) {
            throw new Exception("Producto no encontrado");
        }

        // Validaciones
        if (nombre.length() > 10) {
            throw new Exception("El nombre no puede tener más de 10 caracteres");
        }
        
        if (precioVenta <= 0) {
            throw new Exception("El precio de venta debe ser mayor que 0");
        }

        producto.setNombre(nombre);
        producto.setPrecioVenta(precioVenta);

        DataManager.actualizarProducto(producto);
    }

    public static void eliminarProducto(String codigo) throws IOException {
        DataManager.eliminarProducto(codigo);
    }

    public static List<Producto> listarProductos() throws IOException {
        return DataManager.cargarProductos();
    }
} 