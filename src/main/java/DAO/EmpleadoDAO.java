package DAO;

import database.SupabaseConnection;
import model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {


    /**
     * Busca un empleado por id
     */
    public Empleado buscarPorId(Connection conn, long id) {
        String sql = "SELECT * FROM empleados WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No existe id en empleados: " + id);
        }
        return null;
    }

    /**
     * Busca un empleado por correo y contraseña (para login)
     */
    public Empleado buscarPorCorreoYPassword(Connection conn, String correo, String password) {
        String sql = "SELECT * FROM empleados WHERE correo = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error en login: " + e.getMessage());
        }
        return null;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String cargo = rs.getString("cargo");
        String correo = rs.getString("correo");
        String direccion = rs.getString("direccion");
        long telefono = rs.getLong("telefono");
        String password = rs.getString("password");
        return new Empleado(id, nombre, cargo, correo, direccion, telefono, password);
    }

    /**
     * Insertar
     */
    public boolean insertar(Connection conn, Empleado empleado) {
        String sql = "INSERT INTO empleados (id, nombre, cargo, correo, direccion, telefono, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empleado.getId());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getCargo());
            stmt.setString(4, empleado.getCorreo());
            stmt.setString(5, empleado.getDireccion());
            stmt.setLong(6, empleado.getTelefono());
            stmt.setString(7, empleado.getPassword() != null ? empleado.getPassword() : "");

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Eliminar empleado
     */
    public boolean eliminar(Connection conn, int id) {

        String sql = "DELETE FROM empleados WHERE id = ?";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            int filas = stmt.executeUpdate();

            return filas > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return false;
    }

}
