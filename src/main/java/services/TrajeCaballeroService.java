package services;

import DAO.PrendaDAO;
import DAO.TrajeCaballeroDAO;
import database.SupabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.Prenda;
import model.TrajeCaballero;

public class TrajeCaballeroService {

    private final PrendaDAO prendaDAO = new PrendaDAO();
    private final TrajeCaballeroDAO trajeDAO = new TrajeCaballeroDAO();
    

    public boolean insertar(Connection conn, TrajeCaballero traje) {

        return trajeDAO.insertar(conn, traje);
    }

    public TrajeCaballero buscarPorRef(Connection conn, String ref) {

        // 1. obtener base
        Prenda base = prendaDAO.buscarPorRef(conn, ref);

        if (base == null) {
            return null;
        }

        // 2. obtener atributos decorador
        TrajeCaballero datos = trajeDAO.buscarPorRef(conn, ref);

        if (datos == null) {
            return null;
        }

        // 3. construir objeto completo
        return new TrajeCaballero(
                base,
                datos.getTipoTraje(),
                datos.getAderezo()
        );
    }

    public List<TrajeCaballero> listarTodos() {

        List<TrajeCaballero> listaFinal = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<TrajeCaballero> datos = trajeDAO.listarTodos(conn);

            for (TrajeCaballero t : datos) {

                Prenda base = prendaDAO.buscarPorRef(conn, t.getRef());

                listaFinal.add(
                        new TrajeCaballero(
                                base,
                                t.getTipoTraje(),
                                t.getAderezo()
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFinal;
    }
}
