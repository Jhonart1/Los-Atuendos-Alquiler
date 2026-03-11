
package database;

/**
 *
 * @author jhona
 */
public class DatabaseConfig {

    public static final String HOST = "aws-0-us-west-2.pooler.supabase.com";
    public static final String PORT = "5432";
    public static final String DATABASE = "postgres";
    public static final String USER = "postgres.zfwlzagjvzcyxbwzmrov";
    public static final String PASSWORD = "1033791308jh";

    public static final String URL
            = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE + "?sslmode=require";
}
