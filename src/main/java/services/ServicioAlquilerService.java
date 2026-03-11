package services;

import DAO.ServicioAlquilerDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ServicioAlquiler;

/**
 *
 * @author jhona
 */
public class ServicioAlquilerService {

    private ServicioAlquilerDAO servicioAlquilerDAO = new ServicioAlquilerDAO();

    public boolean insertar(ServicioAlquiler servicioAlquiler) {

        try (Connection conn = SupabaseConnection.getConnection()) {
            //Se revisa si existe ya un cliente con ese ID

            conn.setAutoCommit(false);

            //insertar 
            boolean okBase = servicioAlquilerDAO.insertar(conn, servicioAlquiler);

            if (!okBase) {
                conn.rollback();
                System.out.println("Error en insercion rollback");
                return false;
            }
            conn.commit();
            System.out.println("Insertado exitosamente en ServicioAlquiler: " + servicioAlquiler.getId());
            return true;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarMultiple(
            long idBase,
            OffsetDateTime fechaSolicitud,
            OffsetDateTime fechaAlquiler,
            long idEmpleado,
            long idCliente,
            List<String> referencias) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            long idGenerado = idBase;

            for (String ref : referencias) {

                ServicioAlquiler servicio = new ServicioAlquiler(
                        idGenerado++,
                        fechaSolicitud,
                        fechaAlquiler,
                        idEmpleado,
                        idCliente,
                        ref
                );

                boolean ok = servicioAlquilerDAO.insertar(conn, servicio);

                if (!ok) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ServicioAlquiler> buscarPorIdCliente(long id) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<ServicioAlquiler> alquileres = servicioAlquilerDAO.buscarPorIdCliente(conn, id);

            if (alquileres == null) {
                return null;
            }

            return alquileres;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return null;
    }

    public List<ServicioAlquiler> listarTodas() {
        try (Connection conn = SupabaseConnection.getConnection()) {
            List<ServicioAlquiler> lista = servicioAlquilerDAO.listarTodas(conn);

            if (lista == null) {
                return null;
            }
            return lista;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return null;
    }

    public List<String> buscarRefAlquiladas() {

        try (Connection conn = SupabaseConnection.getConnection()) {
            List<String> lista = servicioAlquilerDAO.buscarRefAlquiladas(conn);

            if (lista == null) {
                return null;
            }
            return lista;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return null;

    }
    
    public ServicioAlquiler buscarPorRef(String ref) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            ServicioAlquiler servicioAlquiler = servicioAlquilerDAO.buscarPorRef(conn, ref);
            return servicioAlquiler;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }

        return null;
    }

    public List<ServicioAlquiler> filtrar(Date desde, Date hasta, long idCliente, long idServicio) {

        List<ServicioAlquiler> resultado = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            resultado = servicioAlquilerDAO.filtrar(conn, desde, hasta, idCliente, idServicio);

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return resultado;
    }
    
     public boolean eliminar(String refPrenda) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            boolean ok = servicioAlquilerDAO.eliminar(conn, refPrenda);

            if (!ok) {

                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }
}
