package services;

import DAO.PrendaDAO;
import database.SupabaseConnection;
import iterators.ListaPrendasIterator;
import iterators.PrendaIterator;
import model.Prenda;
import model.Disfraz;
import model.TrajeCaballero;
import model.VestidoDama;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrendasService {

    private PrendaDAO prendaDAO;

    // usar services específicos
    private VestidoDamaService vestidoService;
    private TrajeCaballeroService trajeService;
    private DisfrazService disfrazService;

    public PrendasService() {

        this.prendaDAO = new PrendaDAO();

        this.vestidoService = new VestidoDamaService();
        this.trajeService = new TrajeCaballeroService();
        this.disfrazService = new DisfrazService();
    }

    // ==========================================
    // LISTAR TODAS CON DECORADORES COMPLETOS
    // ==========================================
    public List<Prenda> obtenerTodas() {

        List<Prenda> resultado = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<Prenda> listaBase = prendaDAO.listarTodas(conn);

            PrendaIterator iterator = new ListaPrendasIterator(listaBase);
            while (iterator.hasNext()) {
                Prenda base = iterator.next();
                Prenda completa = construirPrendaCompleta(conn, base);
                if (completa != null) {
                    resultado.add(completa);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }

        return resultado;
    }

    public List<Prenda> obtenerTodasDisponibles(List<String> refsExcluir ) {

        List<Prenda> resultado = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<Prenda> listaBase = prendaDAO.listarTodasDisponibles(conn, refsExcluir);
            
            System.out.println("Listado de prendas en el servicio ya excluidas: " + listaBase.toString());

            PrendaIterator iterator = new ListaPrendasIterator(listaBase);
            while (iterator.hasNext()) {
                Prenda base = iterator.next();
                Prenda completa = construirPrendaCompleta(conn, base);
                if (completa != null) {
                    resultado.add(completa);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }

        return resultado;
    }

    // ==========================================
    // BUSCAR POR REF COMPLETO
    // ==========================================
    public Prenda buscarPorRef(String ref) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            Prenda base = prendaDAO.buscarPorRef(conn, ref);

            if (base == null) {
                return null;
            }

            return construirPrendaCompleta(conn, base);

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }

        return null;
    }

    public List<Prenda> buscarTalla(String talla) {

        List<Prenda> resultado = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<Prenda> listaBase = prendaDAO.buscarTalla(conn, talla);

            PrendaIterator iterator = new ListaPrendasIterator(listaBase);
            while (iterator.hasNext()) {
                Prenda base = iterator.next();
                Prenda completa = construirPrendaCompleta(conn, base);
                if (completa != null) {
                    resultado.add(completa);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }

        return resultado;
    }

    public List<Prenda> filtrar(String ref, String talla, String tipo) {

        List<Prenda> resultado = new ArrayList<>();

        try (Connection conn = SupabaseConnection.getConnection()) {

            List<Prenda> listaBase = prendaDAO.filtrar(conn, ref, talla, tipo);

            //Se llama a Decorator para decorar cada prenda base
            PrendaIterator iterator = new ListaPrendasIterator(listaBase);
            while (iterator.hasNext()) {
                Prenda base = iterator.next();
                Prenda completa = construirPrendaCompleta(conn, base);
                if (completa != null) {
                    resultado.add(completa);
                }
            }

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return resultado;
    }

    // ==========================================
    // FACTORY METHOD REAL
    // ==========================================
    private Prenda construirPrendaCompleta(Connection conn, Prenda base) {

        switch (base.getTipo().toLowerCase()) {

            case "vestido_dama":
                return vestidoService.buscarPorRef(conn, base.getRef());

            case "traje_caballero":
                return trajeService.buscarPorRef(conn, base.getRef());

            case "disfraz":
                return disfrazService.buscarPorRef(conn, base.getRef());

            default:
                return base;
        }
    }

    // ==========================================
    // INSERTAR
    // ==========================================
    public boolean insertar(Prenda prenda) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 1. insertar base
            boolean okBase = prendaDAO.insertar(conn, prenda);

            if (!okBase) {
                conn.rollback();
                System.out.println("Error en insercion rollback");
                return false;
            }

            // 2. insertar decorador según tipo
            boolean okDecorador = true;

            if (prenda instanceof VestidoDama vestido) {
                System.out.println("Pedreria 7 esta en: " + vestido.isPedreria());
                okDecorador = vestidoService.insertar(conn, vestido);
                System.out.println("Insertado correctamente en vestidosDama");

            } else if (prenda instanceof TrajeCaballero traje) {

                okDecorador = trajeService.insertar(conn, traje);
                System.out.println("Insertado correctamente en TrajeSErvice");

            } else if (prenda instanceof Disfraz disfraz) {

                okDecorador = disfrazService.insertar(conn, disfraz);
                System.out.println("Insertado correctamente en Disfraz");
            }

            if (!okDecorador) {

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

    // ==========================================
    // ELIMINAR
    // ==========================================
    public boolean eliminar(String ref) {

        try (Connection conn = SupabaseConnection.getConnection()) {

            conn.setAutoCommit(false);

            boolean ok = prendaDAO.eliminar(conn, ref);

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

    public int contar() {
        int ok = 0;
        try (Connection conn = SupabaseConnection.getConnection()) {

            ok = prendaDAO.contar(conn);
            return ok;

        } catch (SQLException e) {

            System.out.println("Error SQL: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error general: " + e.getMessage());
        }
        return ok;
    }
}
