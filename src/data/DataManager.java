package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import models.Producto;
import utils.Validators;

/**
 * Clase DataManager
 * 
 * Esta clase se encarga de la gestión de datos del sistema, proporcionando métodos
 * para guardar, cargar, actualizar y eliminar registros de clientes y productos.
 * Los datos se almacenan en archivos de texto en la carpeta 'data/'.
 * 
 * Estructura de archivos:
 * - data/clientes.txt: Almacena los datos de los clientes
 * - data/productos.txt: Almacena los datos de los productos
 * 
 * Formato de almacenamiento:
 * - Clientes: identificacion*tipoIdentificacion*nombres*apellidos*telefono*correo
 * - Productos: codigo*nombre*precio
 */
public class DataManager {
    // Rutas de los archivos de datos
    private static final String CLIENTES_FILE = "data/clientes.txt";
    private static final String PRODUCTOS_FILE = "data/productos.txt";
    private static final String SEPARATOR = "*";

    /**
     * Guarda un nuevo cliente en el archivo de clientes.
     * El cliente se agrega al final del archivo.
     * 
     * @param cliente El objeto Cliente a guardar
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    public static void guardarCliente(Cliente cliente) throws IOException {
        try (FileWriter fw = new FileWriter(CLIENTES_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(cliente.toString().replace(",", SEPARATOR));
        }
    }

    /**
     * Carga todos los clientes desde el archivo.
     * Si el archivo no existe, lo crea y retorna una lista vacía.
     * Valida los datos de cada cliente antes de agregarlo a la lista.
     * Si algún cliente no cumple con las validaciones, detiene la ejecución del programa.
     * 
     * @return Lista de objetos Cliente
     * @throws RuntimeException Si algún cliente no cumple con las validaciones
     */
    public static List<Cliente> cargarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        File file = new File(CLIENTES_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error al crear el archivo de clientes: " + e.getMessage());
            }
            return clientes;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    Cliente cliente = Cliente.fromString(line.replace(SEPARATOR, ","));
                    
                    // Validar identificación
                    if (!Validators.isValidIdentificacion(cliente.getIdentificacion())) {
                        System.err.println("Error en línea " + lineNumber + ": Identificación inválida - " + cliente.getIdentificacion());
                        System.exit(1);
                    }
                    
                    // Validar tipo de identificación
                    if (!Validators.isValidTipoIdentificacion(cliente.getTipoIdentificacion())) {
                        System.err.println("Error en línea " + lineNumber + ": Tipo de identificación inválido - " + cliente.getTipoIdentificacion());
                        System.exit(1);
                    }
                    
                    // Validar nombres
                    if (!Validators.isValidNombres(cliente.getNombres())) {
                        System.err.println("Error en línea " + lineNumber + ": Nombres inválidos - " + cliente.getNombres());
                        System.exit(1);
                    }
                    
                    // Validar apellidos
                    if (!Validators.isValidApellidos(cliente.getApellidos())) {
                        System.err.println("Error en línea " + lineNumber + ": Apellidos inválidos - " + cliente.getApellidos());
                        System.exit(1);
                    }
                    
                    // Validar teléfono
                    if (!Validators.isValidTelefono(cliente.getTelefono())) {
                        System.err.println("Error en línea " + lineNumber + ": Teléfono inválido - " + cliente.getTelefono());
                        System.exit(1);
                    }
                    
                    // Validar correo
                    if (!Validators.isValidCorreo(cliente.getCorreoElectronico())) {
                        System.err.println("Error en línea " + lineNumber + ": Correo inválido - " + cliente.getCorreoElectronico());
                        System.exit(1);
                    }
                    
                    clientes.add(cliente);
                } catch (Exception e) {
                    System.err.println("Error al procesar cliente en línea " + lineNumber + ": " + e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de clientes: " + e.getMessage());
            System.exit(1);
        }
        return clientes;
    }

    /**
     * Actualiza los datos de un cliente existente.
     * Busca el cliente por su identificación y actualiza sus datos.
     * 
     * @param cliente El objeto Cliente con los datos actualizados
     * @throws IOException Si ocurre un error al leer o escribir en el archivo
     */
    public static void actualizarCliente(Cliente cliente) throws IOException {
        List<Cliente> clientes = cargarClientes();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdentificacion().equals(cliente.getIdentificacion())) {
                clientes.set(i, cliente);
                break;
            }
        }
        guardarClientes(clientes);
    }

    /**
     * Elimina un cliente del archivo por su identificación.
     * 
     * @param identificacion La identificación del cliente a eliminar
     * @throws IOException Si ocurre un error al leer o escribir en el archivo
     */
    public static void eliminarCliente(String identificacion) throws IOException {
        List<Cliente> clientes = cargarClientes();
        clientes.removeIf(c -> c.getIdentificacion().equals(identificacion));
        guardarClientes(clientes);
    }

    /**
     * Guarda la lista completa de clientes en el archivo.
     * Sobrescribe el contenido actual del archivo.
     * 
     * @param clientes Lista de clientes a guardar
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    private static void guardarClientes(List<Cliente> clientes) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(CLIENTES_FILE))) {
            for (Cliente cliente : clientes) {
                out.println(cliente.toString().replace(",", SEPARATOR));
            }
        }
    }

    /**
     * Guarda un nuevo producto en el archivo de productos.
     * El producto se agrega al final del archivo.
     * 
     * @param producto El objeto Producto a guardar
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    public static void guardarProducto(Producto producto) throws IOException {
        try (FileWriter fw = new FileWriter(PRODUCTOS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(producto.toString().replace(",", SEPARATOR));
        }
    }

    /**
     * Carga todos los productos desde el archivo.
     * Si el archivo no existe, lo crea y retorna una lista vacía.
     * Valida los datos de cada producto antes de agregarlo a la lista.
     * Si algún producto no cumple con las validaciones, detiene la ejecución del programa.
     * 
     * @return Lista de objetos Producto
     * @throws RuntimeException Si algún producto no cumple con las validaciones
     */
    public static List<Producto> cargarProductos() {
        List<Producto> productos = new ArrayList<>();
        File file = new File(PRODUCTOS_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de productos: " + e.getMessage());
                System.exit(1);
            }
            return productos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    Producto producto = Producto.fromString(line.replace(SEPARATOR, ","));
                    
                    // Validar código
                    if (!Validators.isValidCodigoProducto(producto.getCodigo())) {
                        System.err.println("Error en línea " + lineNumber + ": Código inválido - " + producto.getCodigo());
                        System.exit(1);
                    }
                    
                    // Validar nombre
                    if (!Validators.isValidNombreProducto(producto.getNombre())) {
                        System.err.println("Error en línea " + lineNumber + ": Nombre inválido - " + producto.getNombre());
                        System.exit(1);
                    }
                    
                    // Validar precio
                    if (!Validators.isValidPrecio(producto.getPrecioVenta())) {
                        System.err.println("Error en línea " + lineNumber + ": Precio inválido - " + producto.getPrecioVenta());
                        System.exit(1);
                    }
                    
                    productos.add(producto);
                } catch (Exception e) {
                    System.err.println("Error al procesar producto en línea " + lineNumber + ": " + e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de productos: " + e.getMessage());
            System.exit(1);
        }
        return productos;
    }

    /**
     * Actualiza los datos de un producto existente.
     * Busca el producto por su código y actualiza sus datos.
     * 
     * @param producto El objeto Producto con los datos actualizados
     * @throws IOException Si ocurre un error al leer o escribir en el archivo
     */
    public static void actualizarProducto(Producto producto) throws IOException {
        List<Producto> productos = cargarProductos();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo().equals(producto.getCodigo())) {
                productos.set(i, producto);
                break;
            }
        }
        guardarProductos(productos);
    }

    /**
     * Elimina un producto del archivo por su código.
     * 
     * @param codigo El código del producto a eliminar
     * @throws IOException Si ocurre un error al leer o escribir en el archivo
     */
    public static void eliminarProducto(String codigo) throws IOException {
        List<Producto> productos = cargarProductos();
        productos.removeIf(p -> p.getCodigo().equals(codigo));
        guardarProductos(productos);
    }

    /**
     * Guarda la lista completa de productos en el archivo.
     * Sobrescribe el contenido actual del archivo.
     * 
     * @param productos Lista de productos a guardar
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    private static void guardarProductos(List<Producto> productos) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(PRODUCTOS_FILE))) {
            for (Producto producto : productos) {
                out.println(producto.toString().replace(",", SEPARATOR));
            }
        }
    }
} 