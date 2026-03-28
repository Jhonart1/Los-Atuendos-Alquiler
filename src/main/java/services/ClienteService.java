package services;

import DAO.ClienteDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import model.Cliente;

/**
 *
 * @author jhona
 */
public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public boolean insertar(Cliente cliente) {

        try (Connection conn = SupabaseConnection.getConnection()) {
            //Se revisa si existe ya un cliente con ese ID
            
            conn.setAutoCommit(false);

            //insertar 
            boolean okBase = clienteDAO.insertar(conn, cliente);

            if (!okBase) {
                conn.rollback();
                System.out.println("Error en insercion rollback");
                return false;
            }
            conn.commit();
            System.out.println("Insertado exitosamente en Clientes: " + cliente.getId());
            return true;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());
            return false;

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
            return false;
        }
    }

    public Cliente buscarPorId(long id) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            Cliente cliente = clienteDAO.buscarPorId(conn, id);

            if (cliente == null) {
                return null;
            }
            return cliente;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return null;

    }

    public int contar() {
        try (Connection conn = SupabaseConnection.getConnection()) {
            return clienteDAO.contar(conn);
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
        return 0;
    }
}
