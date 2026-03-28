package services;

import DAO.PrendaDAO;
import DAO.VestidoDamaDAO;
import database.SupabaseConnection;
import iterators.IteratorGenerico;
import iterators.ListaIterator;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.Prenda;
import model.VestidoDama;

public class VestidoDamaService {

    private final PrendaDAO prendaDAO = new PrendaDAO();
    private final VestidoDamaDAO vestidoDAO = new VestidoDamaDAO();

    // INSERTAR
    public boolean insertar(Connection conn, VestidoDama vestido) {
        System.out.println("Pedreria 8 esta en: " + vestido.isPedreria());
        return vestidoDAO.insertar(conn, vestido);
    }

    public VestidoDama buscarPorRef(Connection conn, String ref) {

        // 1. obtener base
        Prenda base = prendaDAO.buscarPorRef(conn, ref);

        if (base == null) {
            return null;
        }

        // 2. obtener atributos decorador
        VestidoDama datos = vestidoDAO.buscarPorRef(conn, ref);

        if (datos == null) {
            return null;
        }

        // 3. construir objeto completo
        return new VestidoDama(
                base,
                datos.isPedreria(),
                datos.getAltura(),
                datos.getCantPiezas()
        );
    }

    // LISTAR
    public List<VestidoDama> listarTodos() {

        List<VestidoDama> listaFinal = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<VestidoDama> datos = vestidoDAO.listarTodos(conn);

            IteratorGenerico<VestidoDama> iterator = new ListaIterator<>(datos);
            while (iterator.hasNext()) {
                VestidoDama d = iterator.next();

                Prenda base = prendaDAO.buscarPorRef(conn, d.getRef());

                listaFinal.add(
                        new VestidoDama(
                                base,
                                d.isPedreria(),
                                d.getAltura(),
                                d.getCantPiezas()
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaFinal;
    }
}
