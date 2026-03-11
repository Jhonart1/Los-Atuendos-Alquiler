
package services;

import DAO.LavanderiaDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Lavanderia;

/**
 *
 * @author jhona
 */
public class LavanderiaService {
    
    private LavanderiaDAO lavanderiaDAO = new LavanderiaDAO();
    
    public boolean insertar(Lavanderia lavanderia) {

        try (Connection conn = SupabaseConnection.getConnection()) {
            //Se revisa si existe ya un cliente con ese ID
            
            conn.setAutoCommit(false);

            //insertar 
            boolean okBase = lavanderiaDAO.insertar(conn, lavanderia);

            if (!okBase) {
                conn.rollback();
                System.out.println("Error en insercion rollback");
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
    
    public List<Lavanderia> listarTodas() {
        try (Connection conn = SupabaseConnection.getConnection()) {
            List<Lavanderia> lista = lavanderiaDAO.listarTodas(conn);

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
    
}
