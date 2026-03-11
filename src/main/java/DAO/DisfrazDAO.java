package DAO;

import model.Disfraz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DisfrazDAO {

    // ============================================
    // INSERTAR (modo transaccional)
    // ============================================
    public boolean insertar(Connection conn, Disfraz disfraz) {

        String sql = "INSERT INTO disfraz (ref, nombre) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disfraz.getRef());
            stmt.setString(2, disfraz.getNombre());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("error en dao: " + e);
            return false;
        }
    }

    // ============================================
    // BUSCAR POR REF (modo transaccional)
    // ============================================
    public Disfraz buscarPorRef(Connection conn, String ref) {

        String sql = "SELECT nombre FROM disfraz WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Disfraz datos = new Disfraz(null);

                    datos.setNombre(rs.getString("nombre"));

                    return datos;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================================
    // LISTAR TODOS (modo transaccional)
    // ============================================
    public List<Disfraz> listarTodos(Connection conn) {

        List<Disfraz> lista = new ArrayList<>();

        String sql = "SELECT ref, nombre FROM disfraz";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Disfraz disfraz = new Disfraz(null);

                disfraz.setRefTemporal(rs.getString("ref"));
                disfraz.setNombre(rs.getString("nombre"));

                lista.add(disfraz);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ============================================
    // ELIMINAR (modo transaccional)
    // ============================================
    public boolean eliminar(Connection conn, String ref) {

        String sql = "DELETE FROM disfraz WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }

}
