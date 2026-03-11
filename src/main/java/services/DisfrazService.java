package services;

import DAO.DisfrazDAO;
import DAO.PrendaDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.Disfraz;
import model.Prenda;

public class DisfrazService {

    private final PrendaDAO prendaDAO = new PrendaDAO();
    private final DisfrazDAO disfrazDAO = new DisfrazDAO();

    public boolean insertar(Connection conn, Disfraz disfraz) {

        return disfrazDAO.insertar(conn, disfraz);
    }

    public Disfraz buscarPorRef(Connection conn, String ref) {

        // 1. obtener base
        Prenda base = prendaDAO.buscarPorRef(conn, ref);

        if (base == null) {
            return null;
        }

        // 2. obtener atributos decorador
        Disfraz datos = disfrazDAO.buscarPorRef(conn, ref);

        if (datos == null) {
            return null;
        }

        // 3. construir objeto completo
        return new Disfraz(
                base,
                datos.getNombre()
        );
    }

    public List<Disfraz> listarTodos() {

        List<Disfraz> listaFinal = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<Disfraz> datos = disfrazDAO.listarTodos(conn);

            for (Disfraz d : datos) {

                Prenda base = prendaDAO.buscarPorRef(conn, d.getRef());

                listaFinal.add(
                        new Disfraz(
                                base,
                                d.getNombre()
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFinal;
    }
}
