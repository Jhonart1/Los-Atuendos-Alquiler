
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author jhona
 */
public class SupabaseConnection {
    public static Connection connection; // singleton
    public static Connection getConnection(){
        try {

            if (connection == null || connection.isClosed()) {

                connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

            }

        } catch (SQLException e) {

            System.out.println("Error conectando a Supabase");
            e.printStackTrace();

        }

        return connection;
        
    }
}
