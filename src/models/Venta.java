package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private static final String EMPRESA_NOMBRE = "Mi Empresa S.A.S";
    private static final String EMPRESA_NIT = "900123456-7";
    private static final String EMPRESA_SOFTWARE = "Sistema POS v1.0";
    private static final float IVA = 0.19f;

    private String numeroVenta;
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private float subtotal;
    private float totalIva;
    private float total;

    public Venta(Cliente cliente) {
        this.fechaHora = LocalDateTime.now();
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.subtotal = 0;
        this.totalIva = 0;
        this.total = 0;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        detalles.add(detalle);
        actualizarTotales();
    }

    private void actualizarTotales() {
        subtotal = 0;
        for (DetalleVenta detalle : detalles) {
            subtotal += detalle.getSubtotal();
        }
        totalIva = subtotal * IVA;
        total = subtotal + totalIva;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public float getTotalIva() {
        return totalIva;
    }

    public float getTotal() {
        return total;
    }

    public String generarTicket() {
        StringBuilder ticket = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Sección Empresa
        ticket.append("EMPRESA\n");
        ticket.append("Nombre: ").append(EMPRESA_NOMBRE).append("\n");
        ticket.append("NIT: ").append(EMPRESA_NIT).append("\n\n");

        // Datos de venta
        ticket.append("DATOS DE VENTA\n");
        ticket.append("Número: ").append(numeroVenta).append("\n");
        ticket.append("Fecha y Hora: ").append(fechaHora.format(formatter)).append("\n\n");

        // Datos del cliente
        ticket.append("DATOS DEL CLIENTE\n");
        ticket.append("Nombre Completo: ").append(cliente.getNombres()).append(" ").append(cliente.getApellidos()).append("\n");
        ticket.append("Identificación: ").append(cliente.getTipoIdentificacion()).append(" ").append(cliente.getIdentificacion()).append("\n");
        ticket.append("Teléfono: ").append(cliente.getTelefono()).append("\n");
        ticket.append("Correo: ").append(cliente.getCorreoElectronico()).append("\n\n");

        // Productos
        ticket.append("PRODUCTOS\n");
        ticket.append("Código\tNombre\t\tPrecio\tCantidad\tSubtotal\n");
        for (DetalleVenta detalle : detalles) {
            Producto p = detalle.getProducto();
            ticket.append(String.format("%s\t%s\t%.2f\t%d\t\t%.2f\n",
                p.getCodigo(), p.getNombre(), p.getPrecioVenta(),
                detalle.getCantidad(), detalle.getSubtotal()));
        }
        ticket.append("\n");

        // Totales
        ticket.append("TOTALES\n");
        ticket.append(String.format("Subtotal: $%.2f\n", subtotal));
        ticket.append(String.format("IVA (19%%): $%.2f\n", totalIva));
        ticket.append(String.format("Total: $%.2f\n\n", total));

        // Datos del programa
        ticket.append("DATOS DEL PROGRAMA\n");
        ticket.append(EMPRESA_SOFTWARE).append("\n");

        return ticket.toString();
    }
} 