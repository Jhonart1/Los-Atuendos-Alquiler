package DAO;

import model.TrajeCaballero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrajeCaballeroDAO {

    // ============================================
    // INSERTAR (modo transaccional)
    // ============================================
    public boolean insertar(Connection conn, TrajeCaballero traje) {

        String sql = "INSERT INTO traje_caballero (ref, tipo_traje, aderezo) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, traje.getRef());
            stmt.setString(2, traje.getTipoTraje());
            stmt.setString(3, traje.getAderezo());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }

    // ============================================
    // BUSCAR POR REF (modo transaccional)
    // ============================================
    public TrajeCaballero buscarPorRef(Connection conn, String ref) {

        String sql = "SELECT tipo_traje, aderezo FROM traje_caballero WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    TrajeCaballero datos = new TrajeCaballero(null);

                    datos.setTipoTraje(rs.getString("tipo_traje"));
                    datos.setAderezo(rs.getString("aderezo"));

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
    public List<TrajeCaballero> listarTodos(Connection conn) {

        List<TrajeCaballero> lista = new ArrayList<>();

        String sql = "SELECT ref, tipo_traje, aderezo FROM traje_caballero";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                TrajeCaballero traje = new TrajeCaballero(null);

                traje.setRefTemporal(rs.getString("ref"));
                traje.setTipoTraje(rs.getString("tipo_traje"));
                traje.setAderezo(rs.getString("aderezo"));

                lista.add(traje);
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

        String sql = "DELETE FROM traje_caballero WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }

}
