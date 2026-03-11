package services;

import DAO.EmpleadoDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import model.Empleado;

/**
 *
 * @author jhona
 */
public class EmpleadoService {

    private EmpleadoDAO empleadoDao = new EmpleadoDAO();

    public boolean insertar(Empleado empleado) {

        try (Connection conn = SupabaseConnection.getConnection()) {
            //Se revisa si existe ya un cliente con ese ID
            
            conn.setAutoCommit(false);

            //insertar 
            boolean okBase = empleadoDao.insertar(conn, empleado);

            if (!okBase) {
                conn.rollback();
                System.out.println("Error en insercion rollback");
                return false;
            }
            conn.commit();
            System.out.println("Insertado exitosamente en Empleados: " + empleado.getId());
            return true;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }

    public Empleado buscarPorId(long id) {
        try (Connection conn = SupabaseConnection.getConnection()) {
            return empleadoDao.buscarPorId(conn, id);
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
        return null;
    }

    /**
     * Autentica empleado por correo y contraseña
     * @return Empleado si las credenciales son correctas, null si no
     */
    public Empleado login(String correo, String password) {
        if (correo == null || correo.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        try (Connection conn = SupabaseConnection.getConnection()) {
            return empleadoDao.buscarPorCorreoYPassword(conn, correo.trim(), password);
        } catch (SQLException e) {
            System.out.println("Error SQL en login: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general en login: " + e.getMessage());
        }
        return null;
    }
}
