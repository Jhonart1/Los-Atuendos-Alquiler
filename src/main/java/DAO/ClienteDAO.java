package DAO;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author jhona
 */
public class ClienteDAO {

    public boolean insertar(Connection conn, Cliente cliente) {

        String sql = """
            INSERT INTO clientes 
            (id, nombre, direccion, telefono, correo)
            VALUES (?, ?, ?, ?, ?)""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cliente.getId());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getDireccion());
            stmt.setLong(4, cliente.getTelefono());
            stmt.setString(5, cliente.getCorreo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }
    
    public Cliente buscarPorId(Connection conn, long id) {

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Cliente(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getLong("telefono"),
                            rs.getString("correo")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
