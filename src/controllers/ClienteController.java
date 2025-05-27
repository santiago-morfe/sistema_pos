package controllers;

import java.io.IOException;
import java.util.List;
import models.Cliente;
import data.DataManager;

public class ClienteController {

    public static void crearCliente(String identificacion, String tipoIdentificacion,
            String nombres, String apellidos, String telefono,
            String correoElectronico) throws Exception {

        // Validaciones
        if (consultarCliente(identificacion) != null) {
            throw new Exception("El cliente ya existe");
        }

        if (identificacion.length() < 8 || identificacion.length() > 10) {
            throw new Exception("La identificación debe tener entre 8 y 10 dígitos");
        }

        if (!tipoIdentificacion.equals("CC") && !tipoIdentificacion.equals("CE")) {
            throw new Exception("Tipo de identificación inválido. Use CC o CE");
        }

        if (nombres.length() < 10 || nombres.length() > 30) {
            throw new Exception("Los nombres deben tener entre 10 y 30 caracteres");
        }

        if (apellidos.length() < 10 || apellidos.length() > 30) {
            throw new Exception("Los apellidos deben tener entre 10 y 30 caracteres");
        }

        if (telefono.length() != 10) {
            throw new Exception("El teléfono debe tener 10 dígitos");
        }

        if (!correoElectronico.contains("@")) {
            throw new Exception("Correo electrónico inválido");
        }

        Cliente cliente = new Cliente(identificacion, tipoIdentificacion, nombres,
                apellidos, telefono, correoElectronico);
        DataManager.guardarCliente(cliente);
    }

    public static Cliente consultarCliente(String identificacion) throws IOException {
        List<Cliente> clientes = DataManager.cargarClientes();
        return clientes.stream()
                .filter(c -> c.getIdentificacion().equals(identificacion))
                .findFirst()
                .orElse(null);
    }

    public static void actualizarCliente(
            String identificacion, String tipoIdentificacion,
            String nombres, String apellidos, String telefono,
            String correoElectronico
        ) throws Exception {
        Cliente cliente = consultarCliente(identificacion);
        if (cliente == null) {
            throw new Exception("Cliente no encontrado");
        }

        // Validaciones
        if (!tipoIdentificacion.equals("CC") && !tipoIdentificacion.equals("CE")) {
            throw new Exception("Tipo de identificación inválido. Use CC o CE");
        }

        if (nombres.length() < 10 || nombres.length() > 30) {
            throw new Exception("Los nombres deben tener entre 10 y 30 caracteres");
        }

        if (apellidos.length() < 10 || apellidos.length() > 30) {
            throw new Exception("Los apellidos deben tener entre 10 y 30 caracteres");
        }

        if (telefono.length() != 10) {
            throw new Exception("El teléfono debe tener 10 dígitos");
        }

        if (!correoElectronico.contains("@")) {
            throw new Exception("Correo electrónico inválido");
        }

        cliente.setTipoIdentificacion(tipoIdentificacion);
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setTelefono(telefono);
        cliente.setCorreoElectronico(correoElectronico);

        DataManager.actualizarCliente(cliente);
    }

    public static void eliminarCliente(String identificacion) throws IOException {
        DataManager.eliminarCliente(identificacion);
    }

    public static List<Cliente> listarClientes() throws IOException {
        return DataManager.cargarClientes();
    }
}