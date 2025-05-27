package utils;

public class Validators {
    
    public static boolean isValidIdentificacion(String identificacion) {
        return identificacion != null && 
               identificacion.matches("\\d{8,10}");
    }

    public static boolean isValidTipoIdentificacion(String tipo) {
        return tipo != null && 
               (tipo.equals("CC") || tipo.equals("CE"));
    }

    public static boolean isValidNombres(String nombres) {
        return nombres != null && 
               nombres.length() >= 10 && 
               nombres.length() <= 30;
    }

    public static boolean isValidApellidos(String apellidos) {
        return apellidos != null && 
               apellidos.length() >= 10 && 
               apellidos.length() <= 30;
    }

    public static boolean isValidTelefono(String telefono) {
        return telefono != null && 
               telefono.matches("\\d{10}");
    }

    public static boolean isValidCorreo(String correo) {
        return correo != null && 
               correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidCodigoProducto(String codigo) {
        return codigo != null && 
               codigo.length() == 5 && 
               codigo.substring(0, 2).matches("[a-zA-Z]+") && 
               codigo.substring(2).matches("\\d+");
    }

    public static boolean isValidNombreProducto(String nombre) {
        return nombre != null && 
               nombre.length() <= 20;
    }

    public static boolean isValidPrecio(float precio) {
        return precio > 0;
    }
} 