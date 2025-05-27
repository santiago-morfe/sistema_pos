package models;

public class Producto {
    private String codigo;
    private String nombre;
    private float precioVenta;

    public Producto(String codigo, String nombre, float precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
    }

    // Getters and Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    @Override
    public String toString() {
        return codigo + "," + nombre + "," + precioVenta;
    }

    public static Producto fromString(String line) {
        String[] parts = line.split(",");
        return new Producto(parts[0], parts[1], Float.parseFloat(parts[2]));
    }
} 