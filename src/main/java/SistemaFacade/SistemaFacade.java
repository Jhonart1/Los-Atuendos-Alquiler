package SistemaFacade;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import iterators.IteratorGenerico;
import iterators.ListaIterator;
import iterators.ListaPrendasIterator;
import iterators.PrendaIterator;
import model.*;
import services.PrendasService;
import services.ClienteService;
import services.EmpleadoService;
import services.ServicioAlquilerService;
import services.LavanderiaService;

/**
 *
 * @author jhona Sistema Fachada - Facade que se usara para acceso al sistema de
 * la UI para evitar conocimiento de datos inncesarios por parte de la UI
 */
public class SistemaFacade {

    // Columnas completas del inventario (incluye atributos específicos según el tipo de prenda)
    private static final String[] COLUMNAS_INVENTARIO = {
        "REF", "COLOR", "MARCA", "TALLA", "V.ALQUILER", "TIPO",
        "PEDRERÍA", "ALTURA", "PIEZAS", "TIPO TRAJE", "ADEREZO", "NOMBRE"
    };

    //Atributo estático privado que guardará la única instancia del Singleton
    private static SistemaFacade instancia;
    //Estado de sesion
    private Empleado empleadoLogueado;
    //Servicios
    private PrendasService prendasService;
    private ClienteService clienteService;
    private EmpleadoService empleadoService;
    private ServicioAlquilerService servicioAlquilerService;
    private LavanderiaService lavanderiaService;
    //Persistencia de datos Carro compras
    private double valorAlquiler;

    /**
     * Singleton de Facade
     */
    public SistemaFacade() {
        System.out.println("Sistema Facade Iniciado por única vez");
        //inicializa servicios
        this.prendasService = new PrendasService();
        this.clienteService = new ClienteService();
        this.empleadoService = new EmpleadoService();
        this.servicioAlquilerService = new ServicioAlquilerService();
        this.lavanderiaService = new LavanderiaService();
    }

    //Método público estático para obtener la instancia
    public static SistemaFacade getInstancia() {
        if (instancia == null) {
            instancia = new SistemaFacade();
        }
        return instancia;
    }

    /**
     *
     * Facade para Prendas
     */
    public void insertarNuevoVestido(String ref, String color, String marca, String talla, int valorAlquiler, String tipo, Map<String, Object> datos) {
        System.out.println("Pedreria 5 esta en: " + datos.get("pedreria"));

        //Se crea nueva prenda Base
        PrendaBase prendaBase = new PrendaBase(ref, color, marca, talla, valorAlquiler, tipo);

        switch (tipo) {
            case "vestido_dama":
                int piezas = Integer.parseInt(datos.get("cantPiezas").toString());
                VestidoDama vestido = new VestidoDama(prendaBase, (boolean) datos.get("pedreria"), (String) datos.get("altura"), piezas);
                System.out.println("Pedreria 6 esta en: " + vestido.isPedreria());
                boolean insertado = prendasService.insertar(vestido);
                System.out.println("insertado: " + insertado);
                break;
            case "traje_caballero":
                TrajeCaballero traje = new TrajeCaballero(prendaBase, (String) datos.get("tipo_traje"), (String) datos.get("aderezo"));
                boolean inserta = prendasService.insertar(traje);
                System.out.println("insertado: " + inserta);
                break;
            case "disfraz":
                Disfraz disfraz = new Disfraz(prendaBase, (String) datos.get("nombre"));
                boolean inser = prendasService.insertar(disfraz);
                System.out.println("insertado: " + inser);
                break;
            default:
                throw new AssertionError();
        }
    }

    public int conteoPrendas() {
        return prendasService.contar();
    }

    public int conteoClientes() {
        return clienteService.contar();
    }

    public int conteoAlquileres() {
        return servicioAlquilerService.contar();
    }

    public int conteoLavanderia() {
        return lavanderiaService.contar();
    }

    public List<Prenda> obtenerPrendas() {
        List<Prenda> lista = prendasService.obtenerTodas();
        return lista;
    }

    public void obtenerModeloTablaPrendas(JTable tabla) {
        //metodo que demora
        List<Prenda> listaPrendas = prendasService.obtenerTodas();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo = (DefaultTableModel) tabla.getModel();

        if (listaPrendas == null || listaPrendas.isEmpty()) {
            return;
        }

        // 1. Declarar TODAS las posibles columnas que aparecerán en la tabla
        String[] todasLasColumnas = {
            "REF", "COLOR", "MARCA", "TALLA", "V.ALQUILER", "TIPO",
            "PEDRERÍA", "ALTURA", "PIEZAS", "TIPO TRAJE", "ADEREZO", "NOMBRE"
        };
        modelo.setColumnIdentifiers(todasLasColumnas);

        PrendaIterator iterator = new ListaPrendasIterator(listaPrendas);
        while (iterator.hasNext()) {
            Prenda prenda = iterator.next();
            // 2. El tamaño del array 'fila' debe ser igual al total de columnas (11 en este caso)
            Object[] fila = new Object[todasLasColumnas.length];

            // 3. Atributos comunes (Índices 0 al 4)
            fila[0] = prenda.getRef();
            fila[1] = prenda.getColor();
            fila[2] = prenda.getMarca();
            fila[3] = prenda.getTalla();
            fila[4] = prenda.getValorAlquiler();
            fila[5] = prenda.getTipo();

            // 4. Lógica para llenar las columnas específicas según el tipo
            if (prenda instanceof VestidoDama) {
                VestidoDama v = (VestidoDama) prenda;
                fila[6] = v.isPedreria() ? "Sí" : "No";
                fila[7] = v.getAltura(); // Suponiendo que tienes estos getters
                fila[8] = v.getCantPiezas();
                // Las columnas 8, 9 y 10 quedarán nulas (vacías en la tabla)
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero t = (TrajeCaballero) prenda;
                fila[9] = t.getTipoTraje();
                fila[10] = t.getAderezo();
                // Las columnas 5, 6, 7 y 10 quedarán nulas
            } else {
                Disfraz d = (Disfraz) prenda;
                fila[11] = d.getNombre();
            }

            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        return;
    }

    public void obtenerPrendasDisponibles(JTable tabla) {
        //metodo que demora
        //Se obtienes referencias ya alquiladas a excluir
        List<String> refsExcluir = servicioAlquilerService.buscarRefAlquiladas();
        System.out.println("Ref a excluir : " + refsExcluir.toString());
        List<Prenda> listaPrendas = prendasService.obtenerTodasDisponibles(refsExcluir);
        System.out.println("Listado de prendas ya excluidas: " + listaPrendas.toString());
        DefaultTableModel modelo = new DefaultTableModel();

        // 1. Declarar TODAS las posibles columnas que aparecerán en la tabla
        String[] todasLasColumnas = {
            "REF", "COLOR", "MARCA", "TALLA", "V.ALQUILER", "TIPO",
            "PEDRERÍA", "ALTURA", "PIEZAS", "TIPO TRAJE", "ADEREZO", "NOMBRE"
        };
        modelo.setColumnIdentifiers(todasLasColumnas);

        PrendaIterator iterator = new ListaPrendasIterator(listaPrendas);
        while (iterator.hasNext()) {
            Prenda prenda = iterator.next();
            // 2. El tamaño del array 'fila' debe ser igual al total de columnas (11 en este caso)
            Object[] fila = new Object[todasLasColumnas.length];

            // 3. Atributos comunes (Índices 0 al 4)
            fila[0] = prenda.getRef();
            fila[1] = prenda.getColor();
            fila[2] = prenda.getMarca();
            fila[3] = prenda.getTalla();
            fila[4] = prenda.getValorAlquiler();
            fila[5] = prenda.getTipo();

            // 4. Lógica para llenar las columnas específicas según el tipo
            if (prenda instanceof VestidoDama) {
                VestidoDama v = (VestidoDama) prenda;
                fila[6] = v.isPedreria() ? "Sí" : "No";
                fila[7] = v.getAltura(); // Suponiendo que tienes estos getters
                fila[8] = v.getCantPiezas();
                // Las columnas 8, 9 y 10 quedarán nulas (vacías en la tabla)
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero t = (TrajeCaballero) prenda;
                fila[9] = t.getTipoTraje();
                fila[10] = t.getAderezo();
                // Las columnas 5, 6, 7 y 10 quedarán nulas
            } else {
                Disfraz d = (Disfraz) prenda;
                fila[11] = d.getNombre();
            }

            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        return;
    }

    public void refrescarDatos(JTable tabla) {
        System.out.println("Refrescando");
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tabla.getModel();
        //limpia datos del modelo pero sin columnas
        model.setRowCount(0);
        // Asegura que la tabla tenga las columnas correctas (base + atributos de decoradores)
        model.setColumnIdentifiers(COLUMNAS_INVENTARIO);
        //se obtienen los datos actuales
        List<Prenda> listaPrendas = prendasService.obtenerTodas();
        //Si es nulo anula el metodo
        if (listaPrendas == null || listaPrendas.isEmpty()) {
            return;
        }
        // Llenar con los nuevos datos (Patrón Iterator)
        PrendaIterator iterator = new ListaPrendasIterator(listaPrendas);
        while (iterator.hasNext()) {
            Prenda prenda = iterator.next();
            // El tamaño debe coincidir con el número de columnas que definiste al inicio
            Object[] fila = new Object[12];

            fila[0] = prenda.getRef();
            fila[1] = prenda.getColor();
            fila[2] = prenda.getMarca();
            fila[3] = prenda.getTalla();
            fila[4] = prenda.getValorAlquiler();
            fila[5] = prenda.getTipo();

            if (prenda instanceof VestidoDama) {
                VestidoDama v = (VestidoDama) prenda;
                fila[6] = v.isPedreria() ? "Sí" : "No";
                fila[7] = v.getAltura();
                fila[8] = v.getCantPiezas();
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero t = (TrajeCaballero) prenda;
                fila[9] = t.getTipoTraje();
                fila[10] = t.getAderezo();
            } else {
                Disfraz d = (Disfraz) prenda;
                fila[11] = d.getNombre();
            }

            model.addRow(fila);
        }
        System.out.println("Finaliza el refresco");
    }

    public void filtrarTablaPrendas(JTable tabla, String filtro) {
        System.out.println("Filtrando");
        List<Prenda> resultado = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        //limpia datos del modelo pero sin columnas
        model.setRowCount(0);
        // Asegura que la tabla tenga las columnas correctas (base + atributos de decoradores)
        model.setColumnIdentifiers(COLUMNAS_INVENTARIO);
        //se obtiene el objeto por la referencia
        resultado = prendasService.buscarTalla(filtro);
        PrendaIterator iterator = new ListaPrendasIterator(resultado);
        while (iterator.hasNext()) {
            Prenda prenda = iterator.next();
            // El tamaño debe coincidir con el número de columnas que definiste al inicio
            Object[] fila = new Object[12];

            fila[0] = prenda.getRef();
            fila[1] = prenda.getColor();
            fila[2] = prenda.getMarca();
            fila[3] = prenda.getTalla();
            fila[4] = prenda.getValorAlquiler();
            fila[5] = prenda.getTipo();

            if (prenda instanceof VestidoDama) {
                VestidoDama v = (VestidoDama) prenda;
                fila[6] = v.isPedreria() ? "Sí" : "No";
                fila[7] = v.getAltura();
                fila[8] = v.getCantPiezas();
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero t = (TrajeCaballero) prenda;
                fila[9] = t.getTipoTraje();
                fila[10] = t.getAderezo();
            } else {
                fila[11] = "Prenda General";
            }

            model.addRow(fila);
        }

    }

    public void tablaCarroPrendas(JTable tabla, String referencia, JLabel labelValor) {
        //se obtiene modelo
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        //Se obtiene la prenda
        Prenda prenda = prendasService.buscarPorRef(referencia);
        //Revisar si la prenda esta alquilada ya

        Object[] fila = new Object[5];
        if (prenda != null) {

            fila[0] = prenda.getRef();
            fila[1] = prenda.getTipo();
            fila[2] = prenda.getColor();
            fila[3] = prenda.getMarca();
            fila[4] = prenda.getValorAlquiler();
            valorAlquiler = valorAlquiler + prenda.getValorAlquiler();
        }
        model.addRow(fila);
        labelValor.setText(valorAlquiler + "");
    }

    public void filtroCompletoPrendas(JTable tabla, String ref, String talla, String tipo) {
        System.out.println("Filtrando");
        List<Prenda> resultado = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        //limpia datos del modelo pero sin columnas
        model.setRowCount(0);
        // Asegura que la tabla tenga las columnas correctas (base + atributos de decoradores)
        model.setColumnIdentifiers(COLUMNAS_INVENTARIO);

        //se obtiene las lista de objetos por los filtros
        resultado = prendasService.filtrar(ref, talla, tipo);

        //Se insertan datos en la Tabla.
        PrendaIterator iterator = new ListaPrendasIterator(resultado);
        while (iterator.hasNext()) {
            Prenda prenda = iterator.next();
            // El tamaño debe coincidir con el número de columnas
            Object[] fila = new Object[12];

            fila[0] = prenda.getRef();
            fila[1] = prenda.getColor();
            fila[2] = prenda.getMarca();
            fila[3] = prenda.getTalla();
            fila[4] = prenda.getValorAlquiler();
            fila[5] = prenda.getTipo();

            if (prenda instanceof VestidoDama) {
                VestidoDama v = (VestidoDama) prenda;
                fila[6] = v.isPedreria() ? "Sí" : "No";
                fila[7] = v.getAltura();
                fila[8] = v.getCantPiezas();
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero t = (TrajeCaballero) prenda;
                fila[9] = t.getTipoTraje();
                fila[10] = t.getAderezo();
            } else {
                fila[11] = "Prenda General";
            }
            model.addRow(fila);
        }

    }

    /**
     *
     * Facade para Clientes
     */
    public boolean insertarNuevoCliente(long id, String nombre, String direccion, long telefono, String correo) {
        //se crea cliente
        Cliente cliente = new Cliente(id, nombre, direccion, telefono, correo);
        //se llama al servicio para insertarlo con ayuda del DAO
        boolean ok = clienteService.insertar(cliente);
        return ok;
    }

    public boolean mostrarClientePorId(
            JTextField identificacion,
            JTextField nombre,
            JTextField telefono,
            JTextField correo,
            JTextField direccion,
            long id, JTable tablaAlquiler) {
        DefaultTableModel model = (DefaultTableModel) tablaAlquiler.getModel();
        model.setRowCount(0);
        //solicita los alquiles del cliente
        List<ServicioAlquiler> servicioAlquileres = servicioAlquilerService.buscarPorIdCliente(id);
        System.out.println("servicioAlquileres obtenidos: " + servicioAlquileres.toString());
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente != null) {
            identificacion.setText(cliente.getId() + "");
            nombre.setText(cliente.getNombre());
            telefono.setText(cliente.getTelefono() + "");
            correo.setText(cliente.getCorreo());
            direccion.setText(cliente.getDireccion());
            //solicitar alquileres del cliente por ID e insertarlos en la tabla
            if (servicioAlquileres != null) {
                //si existen alquiles tomados por el cliente
                IteratorGenerico<ServicioAlquiler> iterator = new ListaIterator<>(servicioAlquileres);
                while (iterator.hasNext()) {
                    ServicioAlquiler sa = iterator.next();
                    // El tamaño debe coincidir con el número de columnas que definiste al inicio
                    Object[] fila = new Object[12];

                    fila[0] = sa.getRefPrenda();
                    fila[1] = sa.getFechaSolicitud();
                    fila[2] = sa.getFechaAlquiler();
                    fila[3] = sa.getId();
                    model.addRow(fila);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean buscarClientePorID(long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            //si no existe el cliente
            return false;
        } else {
            //si existe el cliente 
            return true;
        }
    }

    /**
     *
     * Facade para Empleados
     */
    public boolean registrarEmpleado(long id, String nombre, String cargo, String correo, String direccion, long telefono, String password) {
        Empleado empleado = new Empleado(id, nombre, cargo, correo, direccion, telefono, password != null ? password : "");
        boolean ok = empleadoService.insertar(empleado);
        return ok;
    }

    /**
     *
     * Facade para Servicio de Alquiler
     */
    public boolean nuevoServicioAlquiler(
            long idBase,
            OffsetDateTime fechaSolicitud,
            OffsetDateTime fechaAlquiler,
            long idEmpleado,
            long idCliente,
            List<String> referencias) {

        return servicioAlquilerService.insertarMultiple(
                idBase,
                fechaSolicitud,
                fechaAlquiler,
                idEmpleado,
                idCliente,
                referencias
        );
    }

    public void filtroCompletoAlquiler(JTable tabla, Date desde, Date hasta, long idCliente, long idServicio) {
        System.out.println("Filtrando");
        List<ServicioAlquiler> resultado = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        //limpia datos del modelo pero sin columnas
        model.setRowCount(0);

        //se obtiene las lista de objetos por los filtros
        resultado = servicioAlquilerService.filtrar(desde, hasta, idCliente, idServicio);

        if (resultado == null || resultado.isEmpty()) {
            return;
        }
        //Se insertan datos en la Tabla.
        IteratorGenerico<ServicioAlquiler> iterator = new ListaIterator<>(resultado);
        while (iterator.hasNext()) {
            ServicioAlquiler sa = iterator.next();
            // El tamaño debe coincidir con el número de columnas
            Object[] fila = new Object[6];

            fila[0] = sa.getId();
            fila[1] = sa.getFechaSolicitud().toLocalDate();
            fila[2] = sa.getFechaAlquiler().toLocalDate();
            fila[3] = sa.getIdEmpleado();
            fila[4] = sa.getIdCliente();
            fila[5] = sa.getRefPrenda();

            model.addRow(fila);
        }

    }

    public void cargarAlquileres(JTable tabla, JLabel labelNumero) {
        System.out.println("Refrescando");
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tabla.getModel();
        //limpia datos del modelo pero sin columnas
        model.setRowCount(0);
        //se obtienen los datos actuales
        List<ServicioAlquiler> servicioAlquiler = servicioAlquilerService.listarTodas();
        //Si es nulo anula el metodo
        if (servicioAlquiler == null || servicioAlquiler.isEmpty()) {
            return;
        }
        labelNumero.setText(servicioAlquiler.size() + "");
        // Llenar con los nuevos datos
        IteratorGenerico<ServicioAlquiler> iterator = new ListaIterator<>(servicioAlquiler);
        while (iterator.hasNext()) {
            ServicioAlquiler sa = iterator.next();
            // El tamaño debe coincidir con el número de columnas
            Object[] fila = new Object[6];

            fila[0] = sa.getId();
            fila[1] = sa.getFechaSolicitud().toLocalDate();
            fila[2] = sa.getFechaAlquiler().toLocalDate();
            fila[3] = sa.getIdEmpleado();
            fila[4] = sa.getIdCliente();
            fila[5] = sa.getRefPrenda();

            model.addRow(fila);
        }
        System.out.println("Finaliza el refresco");
    }

    public ServicioAlquiler buscarAlquiler(String ref) {
        ServicioAlquiler servicioAlquiler = servicioAlquilerService.buscarPorRef(ref);
        return servicioAlquiler;
    }

    public boolean eliminarAlquiler(String refPrenda) {
        boolean ok = servicioAlquilerService.eliminar(refPrenda);
        return ok;
    }

    /**
     *
     * Facade para Lavanderia
     */
    public boolean insertarNuevaLavanderia(long id, String refPrenda, String prioridad, String observacion) {
        //se crea cliente
        Lavanderia lavanderia = new Lavanderia(id, refPrenda, prioridad, observacion);
        //se llama al servicio para insertarlo con ayuda del DAO
        boolean ok = lavanderiaService.insertar(lavanderia);
        return ok;
    }

    public boolean datosPrendaLavanderia(JTable tabla, String ref) {
        //busca si la prenda esta alquilada
        ServicioAlquiler servicioAlquiler = buscarAlquiler(ref);
        if (servicioAlquiler != null) {

            //Busca la prenda en la base de datos prenda
            Prenda prenda = prendasService.buscarPorRef(servicioAlquiler.getRefPrenda());
            if (prenda != null) {
                //inserta datos en la tabla
                System.out.println("Refrescando");
                DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                model.setRowCount(0);
                Object[] fila = new Object[5];

                fila[0] = prenda.getRef();
                fila[1] = prenda.getColor();
                fila[2] = prenda.getMarca();
                fila[3] = prenda.getTalla();
                fila[4] = prenda.getTipo();
                model.addRow(fila);
                tabla.setModel(model);
                return true;
            }
        }
        return false;
    }

    public void listarLavanderia(JTable tabla) {
        List<Lavanderia> lista = lavanderiaService.listarTodas();
        if (lista != null) {
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);

            IteratorGenerico<Lavanderia> iterator = new ListaIterator<>(lista);
            while (iterator.hasNext()) {
                Lavanderia la = iterator.next();
                // El tamaño debe coincidir con el número de columnas
                Object[] fila = new Object[6];

                fila[0] = la.getId();
                fila[1] = la.getRefPrenda();
                fila[2] = la.getPrioridad();
                fila[3] = la.getObservacion();
                model.addRow(fila);
            }
        }
    }

    /**
     *
     * Manejo de Login
     */
    /**
     * Intenta autenticar al empleado con correo y contraseña.
     * @param correo Correo electrónico del empleado
     * @param password Contraseña
     * @return true si el login fue exitoso, false si las credenciales son incorrectas
     */
    public boolean login(String correo, String password) {
        Empleado empleado = empleadoService.login(correo, password);
        if (empleado != null) {
            this.empleadoLogueado = empleado;
            return true;
        }
        return false;
    }

    public void setLogin(Empleado empleado) {
        this.empleadoLogueado = empleado;
    }

    public void logout() {
        this.empleadoLogueado = null;
    }

    public long getEmpleadoLogueado() {
        return empleadoLogueado != null ? empleadoLogueado.getId() : 0;
    }

    public void setNombreEmpleado(JLabel label, JTextField idEmpleado) {
        if (empleadoLogueado != null) {
            label.setText(empleadoLogueado.getNombre());
            idEmpleado.setText(String.valueOf(empleadoLogueado.getId()));
        }
    }

}
