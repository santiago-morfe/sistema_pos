package models;

public class Cliente {
    private String identificacion;
    private String tipoIdentificacion;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correoElectronico;

    public Cliente(String identificacion, String tipoIdentificacion, String nombres, 
                  String apellidos, String telefono, String correoElectronico) {
        this.identificacion = identificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    // Getters and Setters
    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public String toString() {
        return identificacion + "," + tipoIdentificacion + "," + nombres + "," + 
               apellidos + "," + telefono + "," + correoElectronico;
    }

    public static Cliente fromString(String line) {
        String[] parts = line.split(",");
        return new Cliente(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
} 