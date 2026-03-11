package DAO;

import model.VestidoDama;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VestidoDamaDAO {

    // ============================================
    // INSERTAR (modo transaccional)
    // ============================================
    public boolean insertar(Connection conn, VestidoDama vestido) {

        String sql = "INSERT INTO vestido_dama (ref, pedreria, altura, cantpiezas) VALUES (?, ?, ?, ?)";
        System.out.println("Pedreria 9  final esta en: " + vestido.isPedreria());
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vestido.getRef());
            stmt.setBoolean(2, vestido.isPedreria());
            stmt.setString(3, vestido.getAltura());
            stmt.setInt(4, vestido.getCantPiezas());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }

    // ============================================
    // BUSCAR POR REF (modo transaccional)
    // ============================================
    public VestidoDama buscarPorRef(Connection conn, String ref) {

        String sql = "SELECT pedreria, altura, cantpiezas FROM vestido_dama WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    VestidoDama datos = new VestidoDama(null);

                    datos.setAltura(rs.getString("altura"));
                    datos.setCantPiezas(rs.getInt("cantpiezas"));

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
    public List<VestidoDama> listarTodos(Connection conn) {

        List<VestidoDama> lista = new ArrayList<>();

        String sql = "SELECT ref, pedreria, altura, cantpiezas FROM vestido_dama";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                VestidoDama vestido = new VestidoDama(null);

                vestido.setRefTemporal(rs.getString("ref"));
                vestido.setPedreria(rs.getBoolean("pedreria"));
                vestido.setAltura(rs.getString("altura"));
                vestido.setCantPiezas(rs.getInt("cantpiezas"));

                lista.add(vestido);
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

        String sql = "DELETE FROM vestido_dama WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }
    }

}
