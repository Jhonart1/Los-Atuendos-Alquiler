package DAO;

import model.ServicioAlquiler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jhona
 */
public class ServicioAlquilerDAO {

    public boolean insertar(Connection conn, ServicioAlquiler servicioAlquiler) {

        String sql = """
            INSERT INTO servicio_alquiler 
            (id, fecha_solicitud, fecha_alquiler, id_empleado, id_cliente, ref_prenda)
            VALUES (?, ?, ?, ?, ?,?)""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, servicioAlquiler.getId());
            stmt.setObject(2, OffsetDateTime.now());
            stmt.setObject(3, servicioAlquiler.getFechaAlquiler());
            stmt.setLong(4, servicioAlquiler.getIdEmpleado());
            stmt.setLong(5, servicioAlquiler.getIdCliente());
            stmt.setString(6, servicioAlquiler.getRefPrenda());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }

    public int contar(Connection conn) {
        String sql = "SELECT COUNT(*) FROM servicio_alquiler";
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

    public List<ServicioAlquiler> buscarPorIdCliente(Connection conn, long id) {

        List<ServicioAlquiler> lista = new ArrayList<>();

        String sql = "SELECT * FROM servicio_alquiler WHERE id_cliente = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(new ServicioAlquiler(
                            rs.getLong("id"),
                            rs.getObject("fecha_solicitud", OffsetDateTime.class),
                            rs.getObject("fecha_alquiler", OffsetDateTime.class),
                            rs.getLong("id_empleado"),
                            rs.getLong("id_cliente"),
                            rs.getString("ref_prenda")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<ServicioAlquiler> listarTodas(Connection conn) {

        List<ServicioAlquiler> lista = new ArrayList<>();

        String sql = "SELECT * FROM servicio_alquiler";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                OffsetDateTime fechaSolicitud
                        = rs.getObject("fecha_solicitud", OffsetDateTime.class);

                OffsetDateTime fechaAlquiler
                        = rs.getObject("fecha_alquiler", OffsetDateTime.class);

                lista.add(new ServicioAlquiler(
                        rs.getLong("id"),
                        fechaSolicitud,
                        fechaAlquiler,
                        rs.getLong("id_empleado"),
                        rs.getLong("id_cliente"),
                        rs.getString("ref_prenda"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<String> buscarRefAlquiladas(Connection conn) {
        List<String> lista = new ArrayList<>();

        String sql = "SELECT ref_prenda FROM servicio_alquiler";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(rs.getString("ref_prenda"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
    public ServicioAlquiler buscarPorRef(Connection conn, String ref) {

        String sql = "SELECT * FROM servicio_alquiler WHERE ref_prenda = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ref);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new ServicioAlquiler(
                            rs.getLong("id"),
                            rs.getObject("fecha_solicitud", OffsetDateTime.class),
                            rs.getObject("fecha_alquiler", OffsetDateTime.class),
                            rs.getLong("id_empleado"),
                            rs.getLong("id_cliente"),
                            rs.getString("ref_prenda")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ServicioAlquiler> filtrar(Connection conn, Date desde, Date hasta, long idCliente, long idServicio) {

        // Base de la consulta
        StringBuilder sql = new StringBuilder("""
        SELECT *
        FROM servicio_alquiler
        WHERE fecha_alquiler >= ?
        AND fecha_alquiler <= ?
        """);

        if (idCliente > 0) {
            sql.append(" AND id_cliente = ?");
        }

        if (idServicio > 0) {
            sql.append(" AND id = ?");
        }
        //Lista
        List<ServicioAlquiler> lista = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int index = 1;

            // Fechas obligatorias
            stmt.setTimestamp(index++, new Timestamp(desde.getTime()));
            stmt.setTimestamp(index++, new Timestamp(hasta.getTime()));

            // Filtros opcionales
            if (idCliente > 0) {
                stmt.setLong(index++, idCliente);
            }

            if (idServicio > 0) {
                stmt.setLong(index++, idServicio);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    ServicioAlquiler servicio = new ServicioAlquiler(
                            rs.getLong("id"),
                            rs.getObject("fecha_solicitud", OffsetDateTime.class),
                            rs.getObject("fecha_alquiler", OffsetDateTime.class),
                            rs.getLong("id_empleado"),
                            rs.getLong("id_cliente"),
                            rs.getString("ref_prenda")
                    );

                    lista.add(servicio);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
     public boolean eliminar(Connection conn, String refPrenda) {

        String sql = "DELETE FROM servicio_alquiler WHERE ref_prenda = ?";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, refPrenda);

            int filas = stmt.executeUpdate();

            return filas > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return false;
    }
}
