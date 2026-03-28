/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Lavanderia;

/**
 *
 * @author jhona
 */
public class LavanderiaDAO {
    
    public boolean insertar(Connection conn, Lavanderia lavanderia) {

        String sql = """
            INSERT INTO lavanderia 
            (id, ref_prenda, prioridad, observacion)
            VALUES (?, ?, ?, ?)""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, lavanderia.getId());
            stmt.setString(2, lavanderia.getRefPrenda());
            stmt.setString(3, lavanderia.getPrioridad());
            stmt.setString(4, lavanderia.getObservacion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }
    
    public List<Lavanderia> listarTodas(Connection conn) {

        List<Lavanderia> lista = new ArrayList<>();

        String sql = "SELECT * FROM lavanderia";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(new Lavanderia(
                        rs.getLong("id"),
                        rs.getString("ref_prenda"),
                        rs.getString("prioridad"),
                        rs.getString("observacion"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contar(Connection conn) {
        String sql = "SELECT COUNT(*) FROM lavanderia";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
        return 0;
    }
}
