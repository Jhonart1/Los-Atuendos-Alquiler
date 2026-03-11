package DAO;

import model.Prenda;
import model.PrendaBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrendaDAO {

    // =========================
    // INSERTAR
    // =========================
    public boolean insertar(Connection conn, Prenda prenda) {

        String sql = """
            INSERT INTO prendas 
            (ref, color, marca, talla, valor_alquiler, tipo)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prenda.getRef());
            stmt.setString(2, prenda.getColor());
            stmt.setString(3, prenda.getMarca());
            stmt.setString(4, prenda.getTalla());
            stmt.setDouble(5, prenda.getValorAlquiler());
            stmt.setString(6, prenda.getTipo());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // BUSCAR POR REF
    // =========================
    public Prenda buscarPorRef(Connection conn, String ref) {

        String sql = "SELECT * FROM prendas WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new PrendaBase(
                            rs.getString("ref"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("talla"),
                            rs.getDouble("valor_alquiler"),
                            rs.getString("tipo")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Prenda> buscarVariasRef(Connection conn, List<String> referencias) {

        List<Prenda> lista = new ArrayList<>();

        // Si la lista viene vacía no tiene sentido consultar
        if (referencias == null || referencias.isEmpty()) {
            return lista;
        }

        // Construir los ? dinámicamente
        String placeholders = referencias.stream()
                .map(r -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT * FROM prendas WHERE ref IN (" + placeholders + ")";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Asignar los parámetros
            for (int i = 0; i < referencias.size(); i++) {
                stmt.setString(i + 1, referencias.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Prenda prenda = new PrendaBase(
                            rs.getString("ref"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("talla"),
                            rs.getDouble("valor_alquiler"),
                            rs.getString("tipo")
                    );

                    lista.add(prenda);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Prenda> buscarTalla(Connection conn, String talla) {

        String sql = "SELECT * FROM prendas WHERE talla = ?";

        List<Prenda> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, talla);
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(new PrendaBase(
                            rs.getString("ref"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("talla"),
                            rs.getDouble("valor_alquiler"),
                            rs.getString("tipo")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Prenda> filtrar(Connection conn, String ref, String talla, String tipo) {
        // Base de la consulta
        StringBuilder sql = new StringBuilder("SELECT * FROM prendas WHERE 1=1");
        //Lista
        List<Prenda> lista = new ArrayList<>();
        //Mapa con datos
        Map<String, String> filtros = new HashMap<>();
        filtros.put("ref", ref);
        filtros.put("talla", talla);
        filtros.put("tipo", tipo);

        //Iterar mapa para revisar nulos
        for (Map.Entry<String, String> entry : filtros.entrySet()) {

            String columna = entry.getKey();
            String valor = entry.getValue();

            // Validar null, vacío o "0"
            if (valor != null && !valor.trim().isEmpty() && !valor.equals("0")) {

                sql.append(" AND ");
                sql.append(columna);
                sql.append(" = '");
                sql.append(valor);
                sql.append("'");

            }
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(new PrendaBase(
                            rs.getString("ref"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("talla"),
                            rs.getDouble("valor_alquiler"),
                            rs.getString("tipo")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // =========================
    // LISTAR TODAS
    // =========================
    public List<Prenda> listarTodas(Connection conn) {

        List<Prenda> lista = new ArrayList<>();

        String sql = "SELECT * FROM prendas";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(new PrendaBase(
                        rs.getString("ref"),
                        rs.getString("color"),
                        rs.getString("marca"),
                        rs.getString("talla"),
                        rs.getDouble("valor_alquiler"),
                        rs.getString("tipo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // LISTAR TODAS menos ref
    // =========================
    public List<Prenda> listarTodasDisponibles(Connection conn, List<String> refsExcluir) {

        List<Prenda> lista = new ArrayList<>();

        String sql;

        if (refsExcluir == null || refsExcluir.isEmpty()) {
            System.out.println("No existe referencias a excluir ingreso por default");
            sql = "SELECT * FROM prendas";
        } else {
            System.out.println("Con referencias a Excluir");
            String placeholders = String.join(",",
                    Collections.nCopies(refsExcluir.size(), "?"));

            sql = "SELECT * FROM prendas WHERE ref NOT IN (" + placeholders + ")";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (refsExcluir != null && !refsExcluir.isEmpty()) {
                for (int i = 0; i < refsExcluir.size(); i++) {
                    stmt.setString(i + 1, refsExcluir.get(i));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    lista.add(new PrendaBase(
                            rs.getString("ref"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("talla"),
                            rs.getDouble("valor_alquiler"),
                            rs.getString("tipo")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("El DAO retorna lista excluida: " + lista.toString());
        return lista;
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(Connection conn, String ref) {

        String sql = "DELETE FROM prendas WHERE ref = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // LISTAR TODAS
    // =========================
    public int contar(Connection conn) {
        String sql = "SELECT COUNT(*) FROM prendas";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al contar las filas: " + e.getMessage());
        }
        return 0;
    }

}
