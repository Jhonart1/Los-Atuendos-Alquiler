package com.mycompany.losatuendos.Vista;

import SistemaFacade.SistemaFacade;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhona
 */
public class Dashboard extends javax.swing.JFrame {

    //Facade
    private SistemaFacade facade;
    //Cap Datos Prendas
    private String tipoPrenda;
    private List<String> selectPrendas = new ArrayList();

    public Dashboard(SistemaFacade facade) {
        this.facade = facade;
        tipoPrenda = "vestido_dama";
        initComponents();
        cargarDatosConBarra(jTableInventarioPrendas, 1);
        iniciarReloj();
        conteoPrendas();
        //Asigna el nombre del empleado logueado en la interfaz
        facade.setNombreEmpleado(jLabel_NombreEmpleado, jTextFieldIdEmpleado);
    }

    public void iniciar() {
        setVisible(true);
    }

    public void registrarPrenda() {

        String ref = jTextFieldRegistroReferencia.getText();
        String color = jTextFieldRegistroColor.getText();
        String marca = jTextFieldRegistroMarca.getText();
        String talla = jComboBoxTalla.getSelectedItem().toString();
        int valorAlquiler = Integer.parseInt(jTextFieldRegistroValor.getText());
        String tipo = tipoPrenda;
        boolean pedreria = jCheckBoxPedreria.isSelected();
        String altura = jTextFieldLongitud.getText();
        String cantPiezas = jTextFieldCantPiezas.getText();
        String tipoTraje = jComboBoxTipoTraje.getSelectedItem().toString();
        String aderezo = jComboBoxAderezo.getSelectedItem().toString();
        String nombre = jTextFieldDisfrazNombre.getText();

        System.out.println("Pedreria 1 esta en: " + pedreria);
        //Revisar si hay campos sin rellenar
        if (ref.isBlank() || color.isBlank() || marca.isBlank() || talla.isBlank()
                || valorAlquiler == 0 || tipo.isBlank()) {

            JOptionPane.showMessageDialog(null, "Faltan Campos iniciales rellenarlos todos");
            return;
        } else {
            //Datos dinamicos
            if (altura.isBlank() || cantPiezas.isBlank() || tipoTraje.isBlank() || aderezo.isBlank() || nombre.isBlank()) {

            }
            Map<String, Object> datos = new HashMap<>();
            switch (tipo) {
                case "vestido_dama":
                    if (altura.isBlank() || cantPiezas.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Faltan Campos en vestido rellenarlos todos");
                    } else {
                        System.out.println("Pedreria 3 esta en: " + pedreria);
                        datos.put("pedreria", pedreria);
                        datos.put("altura", altura);
                        datos.put("cantPiezas", cantPiezas);
                    }
                    break;
                case "traje_caballero":
                    if (tipoTraje.isBlank() || aderezo.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Faltan Campos en traje caballero rellenarlos todos");
                    } else {
                        datos.put("tipo_traje", tipoTraje);
                        datos.put("aderezo", aderezo);
                    }
                    break;
                case "disfraz":
                    if (nombre.isBlank()) {
                        JOptionPane.showMessageDialog(null, "Faltan Campos en disfraz rellenarlos todos");
                    } else {
                        datos.put("nombre", nombre);
                    }
                    break;
                default:
                    throw new AssertionError();
            }
            System.out.println("Pedreria 4 esta en: " + pedreria);
            facade.insertarNuevoVestido(ref, color, marca, talla, valorAlquiler, tipo, datos);
            conteoPrendas();
            JOptionPane.showMessageDialog(null, "Añadido exitosamente ref: " + ref);
            refrescarTabla();
            limpiarPanelPrendas();
        }

    }

    public void iniciarReloj() {
        //actualiza la fecha y hora del sistema en tiempo real
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Timer timer = new Timer(1000, e -> {
            String fechaHora = LocalDateTime.now().format(formato);
            jLabelFecha.setText("Fecha: " + fechaHora);
            jTextFieldFechaSolicitud.setText(fechaHora);
        });
        timer.start();
    }

    public void conteoPrendas() {
        jLabelTotalPrendas.setText(facade.conteoPrendas() + "");
    }

    private void limpiarPanelPrendas() {
        jTextFieldRegistroReferencia.setText("");
        jTextFieldRegistroColor.setText("");
        jTextFieldRegistroMarca.setText("");
        jTextFieldRegistroValor.setText("");
        jTextFieldLongitud.setText("");
        jTextFieldCantPiezas.setText("");
        jTextFieldDisfrazNombre.setText("");
    }

    private DefaultTableModel asignarModeloPrendas() {
        //obtiene todas las prendas de la bd
        facade.refrescarDatos(jTableInventarioPrendas);
        return (DefaultTableModel) jTableInventarioPrendas.getModel();
    }

    private DefaultTableModel prendasDisponibles() {
        //solo obtiene las prendas disponibles
        facade.obtenerPrendasDisponibles(jTableInventarioPrendas);
        return (DefaultTableModel) jTableInventarioPrendas.getModel();
    }

    private void refrescarTabla() {
        facade.refrescarDatos(jTableInventarioPrendas);
    }

    private void filtrarTabla(String filtro) {
        facade.filtrarTablaPrendas(jTableInventarioPrendas, filtro);
    }

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
    }

    public class CargandoDialog extends JDialog {

        public CargandoDialog(Frame padre) {
            super(padre, "Espere por favor", true); // true = modal

            // Configuración de la barra de progreso
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true); // Movimiento infinito (estilo carga)

            // Panel y Texto
            JPanel panelCarga = new JPanel(new BorderLayout(10, 10));
            panelCarga.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelCarga.add(new JLabel("Cargando inventario de prendas..."), BorderLayout.NORTH);
            panelCarga.add(progressBar, BorderLayout.CENTER);

            this.add(panelCarga);
            this.setUndecorated(true); // Quita los bordes de ventana (X, minimizar)
            this.pack();
            this.setLocationRelativeTo(padre); // Centra respecto a la ventana principal
        }
    }

    private void cargarDatosConBarra(JTable tabla, int modo) {
        CargandoDialog cargando = new CargandoDialog(this);

        //Crear el trabajador en segundo plano
        SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                // Esto no congela la pantalla
                if (modo == 1) { //Se carga modelo completo
                    facade.cargarAlquileres(jTableConsultaAlquiler, jLabelAlquileresActivos);
                    return asignarModeloPrendas();
                } else if (modo == 2) { //se carga solo el filtrado
                    System.out.println("Entra en modo 2");
                    return prendasDisponibles();
                } else {
                    return asignarModeloPrendas();
                }

            }

            @Override
            protected void done() {
                try {
                    // Cuando termina el proceso:
                    tabla.setModel(get()); // Seteamos los datos
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cargando.dispose(); // CERRAMOS la barra de carga al finalizar
                }
            }
        };

        //Ejecutar el proceso y MOSTRAR el diálogo
        worker.execute();
        cargando.setVisible(true); // Al ser modal, el código se "pausa" aquí visualmente
    }

    private void cargarIconos() {
        
    }

    private void registrarCliente() {
        String idTexto = jTextFieldClienteId.getText().trim();
        String nombre = jTextFieldClienteNombre.getText().trim();
        String direccion = jTextFieldClienteDireccion.getText().trim();
        String telefonoTexto = jTextFieldClienteTelefono.getText().trim();
        String correo = jTextFieldClienteCorreo.getText().trim();

// Validar campos vacíos
        if (idTexto.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || telefonoTexto.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        long id;
        long telefono;

// Validar números
        try {
            id = Long.parseLong(idTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número válido");
            return;
        }

        try {
            telefono = Long.parseLong(telefonoTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido");
            return;
        }

// Validación básica de correo
        if (!correo.contains("@") || !correo.contains(".")) {
            JOptionPane.showMessageDialog(this, "Correo no válido");
            return;
        }

// Si todo está correcto, llamar facade
        boolean ok = facade.insertarNuevoCliente(
                id,
                nombre,
                direccion,
                telefono,
                correo
        );

// Resultado
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cliente insertado correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al insertar cliente, Cliente ya existe o faltan datos");
        }
    }

    private void prendasSelectorDialog() {

        //control de selecciones
        final String[] referenciaSeleccionada = {null};
        long valorPrendas = 0;

        JDialog modal = new JDialog(this, "Selección de prendas", true);
        modal.setUndecorated(true);
        modal.setSize(700, 500);
        modal.setLocationRelativeTo(this);
        modal.setLayout(new BorderLayout());
        ((JPanel) modal.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        JTable tabla = new JTable();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabla);
        modal.add(scrollPane, BorderLayout.CENTER);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //insertar datos a la tabla
        cargarDatosConBarra(tabla, 2);

        // Boton confirmar
        JButton btnSeleccionar = new JButton("Confirmar Selección");
        btnSeleccionar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                referenciaSeleccionada[0] = tabla.getValueAt(fila, 0).toString();
                //Revisar que no se seleccione la misma referencia
                if (selectPrendas.contains(referenciaSeleccionada[0])) {
                    JOptionPane.showMessageDialog(modal, "La referencia ya esta seleccionada");
                } else {
                    selectPrendas.add(referenciaSeleccionada[0]);
                    System.out.println(selectPrendas);
                    modal.dispose();
                    jLabelAlqTotalPrendas.setText(selectPrendas.size() + "");
                }
            } else {
                JOptionPane.showMessageDialog(modal, "Por favor, selecciona una fila primero.");
            }
        });

        // Boton para Salir
        JButton btnCancelar = new JButton("Cancelar / Salir");
        btnCancelar.addActionListener(e -> {
            referenciaSeleccionada[0] = null;
            modal.dispose();
        });

        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);
        modal.add(panelBotones, BorderLayout.SOUTH);

        modal.setVisible(true);

        // Uso de seleccion
        if (referenciaSeleccionada[0] != null) {
            System.out.println("Has seleccionado la referencia: " + referenciaSeleccionada[0]);
            //Referencia seleccionada la inserta en la tabla de carro de prendas
            facade.tablaCarroPrendas(jTableCarroPrendas, referenciaSeleccionada[0], jLabelAlqTotalValor);
        }
    }

    private void crearNuevoAlquiler() {
        //obtiene fecha seleccionada de alquiler 
        Date date = jDateChooserFechaAlquiler.getDate();
        OffsetDateTime FechaAlquiler = null;
        if (date != null) {
            FechaAlquiler = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime();
        }

        //auto genera id
        long id = ThreadLocalRandom.current().nextLong(100000, 1000000);

        //revisar si el cliente esta registrado
        boolean okCliente = facade.buscarClientePorID(Long.parseLong(jTextFieldAlquilerIdCliente.getText()));

        //si existe el cliente proceder con la creacion porque el cliente debe estar registrado previamente
        if (okCliente) {
            boolean ok = facade.nuevoServicioAlquiler(
                    id,
                    OffsetDateTime.now(),
                    FechaAlquiler,
                    Long.parseLong(jTextFieldIdEmpleado.getText()),
                    Long.parseLong(jTextFieldAlquilerIdCliente.getText()),
                    selectPrendas);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Se genero la orden de alquier exitosamente: " + id);
                selectPrendas.clear();
                jTextFieldAlquilerIdCliente.setText("");
                DefaultTableModel model = (DefaultTableModel) jTableCarroPrendas.getModel();
                model.setRowCount(0);
                jDateChooserFechaAlquiler.setDate(null);

            } else {
                JOptionPane.showMessageDialog(this, "Error en la creacion de la orden, revise los datos y reintente");
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Cliente no regsitrado: " + jTextFieldAlquilerIdCliente.getText()
                    + "\nPara registrar el cliente ingrese al modulo de clientes primero.");
        }

    }

    public void cargarLavanderia() {
        JTable table = jTableListaLavanderia;
        facade.listarLavanderia(table);

        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    }

    public void enviarLavanderia(JTable table) {
        List<String> referencias = new ArrayList<>();
        //obtener lista de selecciones 
        int[] filasSeleccionadas = table.getSelectedRows();

        for (int fila : filasSeleccionadas) {

            String ref = table.getValueAt(fila, 1).toString();
            referencias.add(ref);

        }
        System.out.println("referencias: " + referencias);
        //si ha seleccionado prendas
        if (referencias.size() > 0) {
            int option = JOptionPane.showConfirmDialog(null, "¿Enviar a la lavanderia las " 
                    +referencias.size()+ " Prendas con Referencias: " + 
                    referencias, "Confirmar", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                //Abrir jdialog 

            }
        }else{
            JOptionPane.showConfirmDialog(null, "Seleccione prendas a enviar a lavanderia" );
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_absoluto = new javax.swing.JPanel();
        jPanel_izquierdo = new javax.swing.JPanel();
        jLabel_titulo = new javax.swing.JLabel();
        jLabel_resume = new javax.swing.JLabel();
        jLabel_icon = new javax.swing.JLabel();
        jButtonMenuInventario = new javax.swing.JButton();
        jButtonMenuClientes = new javax.swing.JButton();
        jButtonMenuAlquileres1 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanelHeader = new javax.swing.JPanel();
        jLabel_NombreEmpleado = new javax.swing.JLabel();
        jLabelFecha = new javax.swing.JLabel();
        jButtonNewAlquiler = new javax.swing.JButton();
        jPanel_contenido = new javax.swing.JPanel();
        jPanelGestionInv = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanelRegistroInv = new javax.swing.JPanel();
        iconPlusPrenda = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanelConsultaInv = new javax.swing.JPanel();
        icon1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanelResumeInv = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelTotalPrendas = new javax.swing.JLabel();
        jLabel1TotalDisp = new javax.swing.JLabel();
        jLabel1TotalLavanderia = new javax.swing.JLabel();
        jPanel_Clientes = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanelConsultaCli = new javax.swing.JPanel();
        icon5 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanelRegistroCli = new javax.swing.JPanel();
        icon6 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanelLavanderia = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jPanelAlquileres = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jPanel_RegistroPrenda = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldRegistroReferencia = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldRegistroColor = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldRegistroMarca = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxTalla = new javax.swing.JComboBox<>();
        jTextFieldRegistroValor = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButtonCatDisfraz = new javax.swing.JButton();
        jButtonCatVestido = new javax.swing.JButton();
        jButtonCatTraje = new javax.swing.JButton();
        jPanelContenidoCat = new javax.swing.JPanel();
        jPanelCatVestido = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jCheckBoxPedreria = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldLongitud = new javax.swing.JTextField();
        jButtonGuardarVestido = new javax.swing.JButton();
        jTextFieldCantPiezas = new javax.swing.JTextField();
        jPanelCatDisfraz = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldDisfrazNombre = new javax.swing.JTextField();
        jButtonGuardarDisfraz = new javax.swing.JButton();
        jPanelCatTraje = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxTipoTraje = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jComboBoxAderezo = new javax.swing.JComboBox<>();
        jButtonGuardarTraje = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanelConsultaInventario = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jButtonFiltrarInventario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableInventarioPrendas = new javax.swing.JTable();
        jComboBoxTipo = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboFiltroTalla = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jTextFieldFiltroRef = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jPanelFormularioCliente = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextFieldClienteId = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextFieldClienteNombre = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextFieldClienteTelefono = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextFieldClienteCorreo = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldClienteDireccion = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanelConsultaCliente = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextFieldBuscarPorId = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextFieldResultNombre = new javax.swing.JTextField();
        jTextFieldResultTel = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jTextFieldResultId = new javax.swing.JTextField();
        jTextFieldResultDireccion = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextFieldResultCorreo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableAlquilerCliente = new javax.swing.JTable();
        jLabel49 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanelNuevoAlquiler = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jTextFieldIdEmpleado = new javax.swing.JTextField();
        jTextFieldAlquilerIdCliente = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableCarroPrendas = new javax.swing.JTable();
        jTextFieldFechaSolicitud = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        jLabelAlqTotalValor = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabelAlqTotalPrendas = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jDateChooserFechaAlquiler = new com.toedter.calendar.JDateChooser();
        jPanelConsultaAlquiler = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jTextFieldAlqFiltroIdServicio = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableConsultaAlquiler = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        jLabelAlquileresActivos = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jTextFieldAlqFiltroIDcliente = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jDateChooserDesde = new com.toedter.calendar.JDateChooser();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jDateChooserHasta = new com.toedter.calendar.JDateChooser();
        jButton14 = new javax.swing.JButton();
        jPanel_RegistroLavanderia = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jTextFieldLavanderiaRef = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableDatosPrendaLav = new javax.swing.JTable();
        jLabel84 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadioMedia = new javax.swing.JRadioButton();
        jRadioAlta = new javax.swing.JRadioButton();
        jRadioBaja = new javax.swing.JRadioButton();
        jLabel85 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPaneObservaciones = new javax.swing.JTextPane();
        jPanel_ConsultaLavanderia = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableListaLavanderia = new javax.swing.JTable();
        jLabel87 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel_absoluto.setBackground(new java.awt.Color(204, 0, 51));
        jPanel_absoluto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel_izquierdo.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_izquierdo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel_titulo.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_titulo.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        jLabel_titulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabel_titulo.setText("Los Atuendos");

        jLabel_resume.setFont(new java.awt.Font("Roboto", 0, 10)); // NOI18N
        jLabel_resume.setForeground(new java.awt.Color(153, 153, 153));
        jLabel_resume.setText("Gestion de inventario y alquiler");

        jLabel_icon.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/atuendo64x64.png"))); // NOI18N
        jLabel_icon.setText("icon");

        jButtonMenuInventario.setBackground(new java.awt.Color(255, 255, 255));
        jButtonMenuInventario.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButtonMenuInventario.setForeground(new java.awt.Color(0, 102, 255));
        jButtonMenuInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/anadir24.png"))); // NOI18N
        jButtonMenuInventario.setText("Inventario");
        jButtonMenuInventario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButtonMenuInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonMenuInventario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonMenuInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMenuInventarioMouseClicked(evt);
            }
        });
        jButtonMenuInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuInventarioActionPerformed(evt);
            }
        });

        jButtonMenuClientes.setBackground(new java.awt.Color(255, 255, 255));
        jButtonMenuClientes.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButtonMenuClientes.setForeground(new java.awt.Color(0, 102, 255));
        jButtonMenuClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/nueva-cuenta24.png"))); // NOI18N
        jButtonMenuClientes.setText("Clientes");
        jButtonMenuClientes.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButtonMenuClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonMenuClientes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonMenuClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMenuClientesMouseClicked(evt);
            }
        });
        jButtonMenuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuClientesActionPerformed(evt);
            }
        });

        jButtonMenuAlquileres1.setBackground(new java.awt.Color(255, 255, 255));
        jButtonMenuAlquileres1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButtonMenuAlquileres1.setForeground(new java.awt.Color(0, 102, 255));
        jButtonMenuAlquileres1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/apreton-de-manos.png"))); // NOI18N
        jButtonMenuAlquileres1.setText("Alquileres");
        jButtonMenuAlquileres1.setToolTipText("");
        jButtonMenuAlquileres1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButtonMenuAlquileres1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonMenuAlquileres1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonMenuAlquileres1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonMenuAlquileres1MouseClicked(evt);
            }
        });
        jButtonMenuAlquileres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuAlquileres1ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(255, 255, 255));
        jButton15.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton15.setForeground(new java.awt.Color(0, 102, 255));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lavadora-inteligente.png"))); // NOI18N
        jButton15.setText("Lavanderia");
        jButton15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.setPreferredSize(new java.awt.Dimension(81, 35));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/boton-de-encendido.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_izquierdoLayout = new javax.swing.GroupLayout(jPanel_izquierdo);
        jPanel_izquierdo.setLayout(jPanel_izquierdoLayout);
        jPanel_izquierdoLayout.setHorizontalGroup(
            jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_izquierdoLayout.createSequentialGroup()
                .addGroup(jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_izquierdoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_titulo)
                            .addGroup(jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1)
                                .addComponent(jLabel_resume))))
                    .addGroup(jPanel_izquierdoLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonMenuInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonMenuClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonMenuAlquileres1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel_izquierdoLayout.setVerticalGroup(
            jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_izquierdoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_izquierdoLayout.createSequentialGroup()
                        .addComponent(jLabel_titulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel_resume))
                    .addComponent(jLabel_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addComponent(jButtonMenuInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonMenuClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonMenuAlquileres1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 305, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(24, 24, 24))
        );

        jPanel_absoluto.add(jPanel_izquierdo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, 760));

        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel_NombreEmpleado.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel_NombreEmpleado.setForeground(new java.awt.Color(51, 51, 51));
        jLabel_NombreEmpleado.setText("Nombre");

        jLabelFecha.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(153, 153, 153));
        jLabelFecha.setText("Fecha");

        jButtonNewAlquiler.setBackground(new java.awt.Color(51, 102, 255));
        jButtonNewAlquiler.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButtonNewAlquiler.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNewAlquiler.setText("+ Nuevo Alquiler");
        jButtonNewAlquiler.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));
        jButtonNewAlquiler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonNewAlquiler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonNewAlquilerMouseClicked(evt);
            }
        });
        jButtonNewAlquiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewAlquilerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_NombreEmpleado)
                    .addComponent(jLabelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                .addComponent(jButtonNewAlquiler, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonNewAlquiler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelHeaderLayout.createSequentialGroup()
                        .addComponent(jLabel_NombreEmpleado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFecha)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel_absoluto.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, -1, 90));

        jPanel_contenido.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_contenido.setLayout(new java.awt.CardLayout());

        jPanelGestionInv.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel2.setText("Gestion de inventario");

        jPanelRegistroInv.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRegistroInv.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanelRegistroInv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelRegistroInv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelRegistroInvMouseClicked(evt);
            }
        });

        iconPlusPrenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mas64x64.png"))); // NOI18N
        iconPlusPrenda.setText("icon");

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel3.setText("Registrar Nueva Prenda");

        jLabel4.setBackground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Añade nuevos vestidos, trajes o disfraces.");

        javax.swing.GroupLayout jPanelRegistroInvLayout = new javax.swing.GroupLayout(jPanelRegistroInv);
        jPanelRegistroInv.setLayout(jPanelRegistroInvLayout);
        jPanelRegistroInvLayout.setHorizontalGroup(
            jPanelRegistroInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroInvLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(iconPlusPrenda, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanelRegistroInvLayout.setVerticalGroup(
            jPanelRegistroInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroInvLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(iconPlusPrenda, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanelConsultaInv.setBackground(new java.awt.Color(255, 255, 255));
        jPanelConsultaInv.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanelConsultaInv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelConsultaInv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelConsultaInvMouseClicked(evt);
            }
        });

        icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lupa64x64.png"))); // NOI18N
        icon1.setText("icon");

        jLabel5.setBackground(new java.awt.Color(51, 51, 51));
        jLabel5.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel5.setText("Consultar Inventario");

        jLabel6.setBackground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Busca y filtra prendas existentes.");

        javax.swing.GroupLayout jPanelConsultaInvLayout = new javax.swing.GroupLayout(jPanelConsultaInv);
        jPanelConsultaInv.setLayout(jPanelConsultaInvLayout);
        jPanelConsultaInvLayout.setHorizontalGroup(
            jPanelConsultaInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaInvLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(icon1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanelConsultaInvLayout.setVerticalGroup(
            jPanelConsultaInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaInvLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(icon1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanelResumeInv.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel7.setText("Resumen de Inventarios");

        jLabel8.setBackground(new java.awt.Color(153, 153, 153));
        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("DISPONIBLES");

        jLabel9.setBackground(new java.awt.Color(153, 153, 153));
        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("TOTAL PRENDAS");

        jLabel10.setBackground(new java.awt.Color(153, 153, 153));
        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("EN LAVANDERIA");

        jLabelTotalPrendas.setBackground(new java.awt.Color(204, 204, 204));
        jLabelTotalPrendas.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabelTotalPrendas.setText("0");

        jLabel1TotalDisp.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel1TotalDisp.setText("0");

        jLabel1TotalLavanderia.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel1TotalLavanderia.setText("0");

        javax.swing.GroupLayout jPanelResumeInvLayout = new javax.swing.GroupLayout(jPanelResumeInv);
        jPanelResumeInv.setLayout(jPanelResumeInvLayout);
        jPanelResumeInvLayout.setHorizontalGroup(
            jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResumeInvLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelResumeInvLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelResumeInvLayout.createSequentialGroup()
                        .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabelTotalPrendas))
                        .addGap(143, 143, 143)
                        .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel1TotalDisp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1TotalLavanderia)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18))))
        );
        jPanelResumeInvLayout.setVerticalGroup(
            jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResumeInvLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addGap(38, 38, 38)
                .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(36, 36, 36)
                .addGroup(jPanelResumeInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalPrendas)
                    .addComponent(jLabel1TotalDisp)
                    .addComponent(jLabel1TotalLavanderia))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jLabelTotalPrendas.getAccessibleContext().setAccessibleName("LabelTotalPrendas");
        jLabel1TotalDisp.getAccessibleContext().setAccessibleName("LabelTotalDisp");
        jLabel1TotalLavanderia.getAccessibleContext().setAccessibleName("LabelTotalLavanderia");

        javax.swing.GroupLayout jPanelGestionInvLayout = new javax.swing.GroupLayout(jPanelGestionInv);
        jPanelGestionInv.setLayout(jPanelGestionInvLayout);
        jPanelGestionInvLayout.setHorizontalGroup(
            jPanelGestionInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionInvLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelGestionInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelGestionInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanelGestionInvLayout.createSequentialGroup()
                            .addComponent(jPanelRegistroInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(65, 65, 65)
                            .addComponent(jPanelConsultaInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanelResumeInv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelGestionInvLayout.setVerticalGroup(
            jPanelGestionInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGestionInvLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addGap(36, 36, 36)
                .addGroup(jPanelGestionInvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelConsultaInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelRegistroInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jPanelResumeInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        jPanelRegistroInv.getAccessibleContext().setAccessibleName("");
        jPanelResumeInv.getAccessibleContext().setAccessibleName("jPanelGestionInv");

        jPanel_contenido.add(jPanelGestionInv, "panelGestionInv");

        jPanel_Clientes.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel30.setText("Panel de Clientes");

        jPanelConsultaCli.setBackground(new java.awt.Color(255, 255, 255));
        jPanelConsultaCli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanelConsultaCli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelConsultaCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelConsultaCliMouseClicked(evt);
            }
        });

        icon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lupa64x64.png"))); // NOI18N
        icon5.setText("icon");

        jLabel38.setBackground(new java.awt.Color(51, 51, 51));
        jLabel38.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel38.setText("Buscar/Consultar clientes");

        javax.swing.GroupLayout jPanelConsultaCliLayout = new javax.swing.GroupLayout(jPanelConsultaCli);
        jPanelConsultaCli.setLayout(jPanelConsultaCliLayout);
        jPanelConsultaCliLayout.setHorizontalGroup(
            jPanelConsultaCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaCliLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(icon5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanelConsultaCliLayout.setVerticalGroup(
            jPanelConsultaCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaCliLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(icon5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel38)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jLabel40.setBackground(new java.awt.Color(204, 204, 204));
        jLabel40.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel40.setText("Administre el registro y la busqueda de perfiles de clientes de forma centralizada");

        jPanelRegistroCli.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRegistroCli.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanelRegistroCli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelRegistroCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelRegistroCliMouseClicked(evt);
            }
        });

        icon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mas64x64.png"))); // NOI18N
        icon6.setText("icon");

        jLabel41.setBackground(new java.awt.Color(51, 51, 51));
        jLabel41.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel41.setText("Registrar Nuevo Cliente");

        javax.swing.GroupLayout jPanelRegistroCliLayout = new javax.swing.GroupLayout(jPanelRegistroCli);
        jPanelRegistroCli.setLayout(jPanelRegistroCliLayout);
        jPanelRegistroCliLayout.setHorizontalGroup(
            jPanelRegistroCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroCliLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(icon6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanelRegistroCliLayout.setVerticalGroup(
            jPanelRegistroCliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroCliLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(icon6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel41)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_ClientesLayout = new javax.swing.GroupLayout(jPanel_Clientes);
        jPanel_Clientes.setLayout(jPanel_ClientesLayout);
        jPanel_ClientesLayout.setHorizontalGroup(
            jPanel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ClientesLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(jPanel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ClientesLayout.createSequentialGroup()
                        .addComponent(jPanelRegistroCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanelConsultaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel40)
                    .addComponent(jLabel30))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel_ClientesLayout.setVerticalGroup(
            jPanel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ClientesLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel40)
                .addGap(28, 28, 28)
                .addGroup(jPanel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelConsultaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelRegistroCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanel_Clientes, "panelClientes");

        jPanelLavanderia.setBackground(new java.awt.Color(255, 255, 255));

        jLabel61.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 0));
        jLabel61.setText("Gestión de Lavanderia");

        jLabel62.setText("Gestione el envio de prendas a lavado");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        jLabel63.setBackground(new java.awt.Color(255, 255, 255));
        jLabel63.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 0));
        jLabel63.setText("Consultar  o enviar a lavanderia");

        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lupa64x64.png"))); // NOI18N
        jLabel64.setText("Icon");

        jLabel65.setText("Consulte y envie prendas lavanderia ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        jLabel66.setBackground(new java.awt.Color(255, 255, 255));
        jLabel66.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Registrar Prenda para lavanderia");

        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mas64x64.png"))); // NOI18N
        jLabel67.setText("Icon");

        jLabel68.setText("Registre prendas devueltas para lavar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel68)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelLavanderiaLayout = new javax.swing.GroupLayout(jPanelLavanderia);
        jPanelLavanderia.setLayout(jPanelLavanderiaLayout);
        jPanelLavanderiaLayout.setHorizontalGroup(
            jPanelLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLavanderiaLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLavanderiaLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel62)
                    .addComponent(jLabel61))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanelLavanderiaLayout.setVerticalGroup(
            jPanelLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLavanderiaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel61)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel62)
                .addGap(53, 53, 53)
                .addGroup(jPanelLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanelLavanderia, "panelLavanderia");

        jPanelAlquileres.setBackground(new java.awt.Color(255, 255, 255));

        jLabel74.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(0, 0, 0));
        jLabel74.setText("Gestión de Servicios de Alquiler");

        jLabel76.setText("Bienvenido al panel central. Seleccione una acción para comenzar a gestionar sus trajes y disfraces");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel77.setBackground(new java.awt.Color(255, 255, 255));
        jLabel77.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(0, 0, 0));
        jLabel77.setText("Consultar Alquileres");

        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lupa64x64.png"))); // NOI18N
        jLabel78.setText("Icon");

        jLabel79.setText("Busque alquileres activos");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jLabel77)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel79)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.setPreferredSize(new java.awt.Dimension(250, 250));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel80.setBackground(new java.awt.Color(255, 255, 255));
        jLabel80.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(0, 0, 0));
        jLabel80.setText("Registrar Nuevo Servicio");

        jLabel81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mas64x64.png"))); // NOI18N
        jLabel81.setText("Icon");

        jLabel82.setText("Cree una nueva reservacion");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel82)
                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jLabel80)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel82)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelAlquileresLayout = new javax.swing.GroupLayout(jPanelAlquileres);
        jPanelAlquileres.setLayout(jPanelAlquileresLayout);
        jPanelAlquileresLayout.setHorizontalGroup(
            jPanelAlquileresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlquileresLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelAlquileresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAlquileresLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel76)
                    .addComponent(jLabel74))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanelAlquileresLayout.setVerticalGroup(
            jPanelAlquileresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlquileresLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel74)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76)
                .addGap(53, 53, 53)
                .addGroup(jPanelAlquileresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(266, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanelAlquileres, "panelAlquileres");

        jPanel_RegistroPrenda.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel11.setText("Registro de Prenda");

        jLabel12.setBackground(new java.awt.Color(153, 153, 153));
        jLabel12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Referencia");

        jTextFieldRegistroReferencia.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldRegistroReferencia.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldRegistroReferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegistroReferenciaActionPerformed(evt);
            }
        });

        jLabel13.setBackground(new java.awt.Color(153, 153, 153));
        jLabel13.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Color");

        jTextFieldRegistroColor.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldRegistroColor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldRegistroColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegistroColorActionPerformed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(153, 153, 153));
        jLabel14.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("Marca");

        jTextFieldRegistroMarca.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldRegistroMarca.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldRegistroMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegistroMarcaActionPerformed(evt);
            }
        });

        jLabel15.setBackground(new java.awt.Color(153, 153, 153));
        jLabel15.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Talla");

        jComboBoxTalla.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxTalla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S", "M", "L", "XL" }));
        jComboBoxTalla.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jTextFieldRegistroValor.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldRegistroValor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldRegistroValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegistroValorActionPerformed(evt);
            }
        });

        jLabel16.setBackground(new java.awt.Color(153, 153, 153));
        jLabel16.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Valor Alquiler");

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel17.setText("Categoria de la prenda");

        jButtonCatDisfraz.setBackground(new java.awt.Color(0, 102, 255));
        jButtonCatDisfraz.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonCatDisfraz.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCatDisfraz.setText("Disfraz");
        jButtonCatDisfraz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCatDisfrazMouseClicked(evt);
            }
        });
        jButtonCatDisfraz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCatDisfrazActionPerformed(evt);
            }
        });

        jButtonCatVestido.setBackground(new java.awt.Color(0, 102, 255));
        jButtonCatVestido.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonCatVestido.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCatVestido.setText("Vestido");
        jButtonCatVestido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCatVestidoMouseClicked(evt);
            }
        });
        jButtonCatVestido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCatVestidoActionPerformed(evt);
            }
        });

        jButtonCatTraje.setBackground(new java.awt.Color(0, 102, 255));
        jButtonCatTraje.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonCatTraje.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCatTraje.setText("Traje");
        jButtonCatTraje.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCatTrajeMouseClicked(evt);
            }
        });
        jButtonCatTraje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCatTrajeActionPerformed(evt);
            }
        });

        jPanelContenidoCat.setBackground(new java.awt.Color(255, 255, 255));
        jPanelContenidoCat.setLayout(new java.awt.CardLayout());

        jPanelCatVestido.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel18.setText("Vestido");

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel19.setText("Detalle de Confección");

        jCheckBoxPedreria.setText("Tiene Predreria");
        jCheckBoxPedreria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxPedreriaMouseClicked(evt);
            }
        });
        jCheckBoxPedreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPedreriaActionPerformed(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel20.setText("Longitud");

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel21.setText("Cantidad de piezas");

        jTextFieldLongitud.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldLongitud.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldLongitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLongitudActionPerformed(evt);
            }
        });

        jButtonGuardarVestido.setBackground(new java.awt.Color(0, 102, 255));
        jButtonGuardarVestido.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonGuardarVestido.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarVestido.setText("Guardar");
        jButtonGuardarVestido.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButtonGuardarVestido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarVestidoActionPerformed(evt);
            }
        });

        jTextFieldCantPiezas.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldCantPiezas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldCantPiezas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCantPiezasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCatVestidoLayout = new javax.swing.GroupLayout(jPanelCatVestido);
        jPanelCatVestido.setLayout(jPanelCatVestidoLayout);
        jPanelCatVestidoLayout.setHorizontalGroup(
            jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCatVestidoLayout.createSequentialGroup()
                .addGroup(jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxPedreria)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(44, 44, 44)
                .addGroup(jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jTextFieldLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCantPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(91, 91, 91))
            .addGroup(jPanelCatVestidoLayout.createSequentialGroup()
                .addGap(276, 276, 276)
                .addComponent(jButtonGuardarVestido, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCatVestidoLayout.setVerticalGroup(
            jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatVestidoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCatVestidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxPedreria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldLongitud, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCantPiezas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jButtonGuardarVestido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelContenidoCat.add(jPanelCatVestido, "panelVestido");

        jPanelCatDisfraz.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel25.setText("Disfraz");

        jLabel26.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel26.setText("Nombre");

        jTextFieldDisfrazNombre.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldDisfrazNombre.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldDisfrazNombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jButtonGuardarDisfraz.setBackground(new java.awt.Color(0, 102, 255));
        jButtonGuardarDisfraz.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonGuardarDisfraz.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarDisfraz.setText("Guardar");
        jButtonGuardarDisfraz.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButtonGuardarDisfraz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarDisfrazActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCatDisfrazLayout = new javax.swing.GroupLayout(jPanelCatDisfraz);
        jPanelCatDisfraz.setLayout(jPanelCatDisfrazLayout);
        jPanelCatDisfrazLayout.setHorizontalGroup(
            jPanelCatDisfrazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatDisfrazLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCatDisfrazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jTextFieldDisfrazNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCatDisfrazLayout.createSequentialGroup()
                .addContainerGap(276, Short.MAX_VALUE)
                .addComponent(jButtonGuardarDisfraz, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(276, 276, 276))
        );
        jPanelCatDisfrazLayout.setVerticalGroup(
            jPanelCatDisfrazLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatDisfrazLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDisfrazNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jButtonGuardarDisfraz, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelContenidoCat.add(jPanelCatDisfraz, "panelDisfraz");

        jPanelCatTraje.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel22.setText("Traje");

        jComboBoxTipoTraje.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxTipoTraje.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jComboBoxTipoTraje.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Convencional", "Frac", "Sacoleva", "Otro" }));
        jComboBoxTipoTraje.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jComboBoxTipoTraje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoTrajeActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel23.setText("Accesorio");

        jLabel24.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel24.setText("Tipo");

        jComboBoxAderezo.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxAderezo.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jComboBoxAderezo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Corbata", "Corbatín", "Plastrón", "Ninguno" }));
        jComboBoxAderezo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jButtonGuardarTraje.setBackground(new java.awt.Color(0, 102, 255));
        jButtonGuardarTraje.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButtonGuardarTraje.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarTraje.setText("Guardar");
        jButtonGuardarTraje.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButtonGuardarTraje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarTrajeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCatTrajeLayout = new javax.swing.GroupLayout(jPanelCatTraje);
        jPanelCatTraje.setLayout(jPanelCatTrajeLayout);
        jPanelCatTrajeLayout.setHorizontalGroup(
            jPanelCatTrajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatTrajeLayout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addGap(201, 201, 201)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(326, 326, 326))
            .addGroup(jPanelCatTrajeLayout.createSequentialGroup()
                .addGroup(jPanelCatTrajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCatTrajeLayout.createSequentialGroup()
                        .addComponent(jComboBoxTipoTraje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161)
                        .addComponent(jComboBoxAderezo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCatTrajeLayout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jButtonGuardarTraje, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCatTrajeLayout.setVerticalGroup(
            jPanelCatTrajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCatTrajeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCatTrajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCatTrajeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTipoTraje, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxAderezo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jButtonGuardarTraje, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanelContenidoCat.add(jPanelCatTraje, "panelTraje");

        jButton2.setBackground(new java.awt.Color(0, 102, 255));
        jButton2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Inventario");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_RegistroPrendaLayout = new javax.swing.GroupLayout(jPanel_RegistroPrenda);
        jPanel_RegistroPrenda.setLayout(jPanel_RegistroPrendaLayout);
        jPanel_RegistroPrendaLayout.setHorizontalGroup(
            jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                        .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldRegistroReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jComboBoxTalla, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCatVestido, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel16)
                                .addComponent(jTextFieldRegistroValor)
                                .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                                    .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldRegistroColor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))
                                    .addGap(50, 50, 50)
                                    .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel14)
                                        .addComponent(jTextFieldRegistroMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RegistroPrendaLayout.createSequentialGroup()
                                .addComponent(jButtonCatTraje, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(jButtonCatDisfraz, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanelContenidoCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(69, 69, 69))
        );
        jPanel_RegistroPrendaLayout.setVerticalGroup(
            jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldRegistroColor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                        .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldRegistroReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RegistroPrendaLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldRegistroMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(35, 35, 35)
                .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxTalla, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_RegistroPrendaLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldRegistroValor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(jLabel17)
                .addGap(43, 43, 43)
                .addGroup(jPanel_RegistroPrendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCatDisfraz, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCatTraje, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCatVestido, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addComponent(jPanelContenidoCat, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanel_RegistroPrenda, "panelRegTraje");

        jPanelConsultaInventario.setBackground(new java.awt.Color(255, 255, 255));

        jLabel27.setBackground(new java.awt.Color(0, 0, 0));
        jLabel27.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Consulta de inventario");

        jButtonFiltrarInventario.setBackground(new java.awt.Color(51, 102, 255));
        jButtonFiltrarInventario.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButtonFiltrarInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonFiltrarInventario.setText("Buscar");
        jButtonFiltrarInventario.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButtonFiltrarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFiltrarInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonFiltrarInventarioMouseClicked(evt);
            }
        });
        jButtonFiltrarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFiltrarInventarioActionPerformed(evt);
            }
        });

        jTableInventarioPrendas.setBackground(new java.awt.Color(255, 255, 255));
        jTableInventarioPrendas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTableInventarioPrendas.setFont(new java.awt.Font("Roboto", 0, 10)); // NOI18N
        jTableInventarioPrendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableInventarioPrendas.setGridColor(new java.awt.Color(204, 204, 204));
        jTableInventarioPrendas.setSelectionBackground(new java.awt.Color(0, 153, 255));
        jTableInventarioPrendas.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTableInventarioPrendas.getTableHeader().setReorderingAllowed(false);
        jTableInventarioPrendas.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(jTableInventarioPrendas);

        jComboBoxTipo.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "vestido_dama", "traje_caballero", "disfraz", " " }));

        jLabel28.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel28.setText("Talla");

        jLabel29.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel29.setText("Tipo");

        jComboFiltroTalla.setBackground(new java.awt.Color(255, 255, 255));
        jComboFiltroTalla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S", "M", "L", "XL", " " }));

        jButton3.setBackground(new java.awt.Color(0, 102, 255));
        jButton3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText(" + Prenda");
        jButton3.setToolTipText("");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 102, 255));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("R");
        jButton9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTextFieldFiltroRef.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldFiltroRef.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldFiltroRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFiltroRefActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel69.setText("Referencia");

        javax.swing.GroupLayout jPanelConsultaInventarioLayout = new javax.swing.GroupLayout(jPanelConsultaInventario);
        jPanelConsultaInventario.setLayout(jPanelConsultaInventarioLayout);
        jPanelConsultaInventarioLayout.setHorizontalGroup(
            jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaInventarioLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelConsultaInventarioLayout.createSequentialGroup()
                        .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanelConsultaInventarioLayout.createSequentialGroup()
                                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jComboFiltroTalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29))
                                .addGap(39, 39, 39)
                                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel69)
                                    .addComponent(jTextFieldFiltroRef, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelConsultaInventarioLayout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(37, 37, 37)
                        .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonFiltrarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanelConsultaInventarioLayout.setVerticalGroup(
            jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaInventarioLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel69))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelConsultaInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboFiltroTalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFiltrarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldFiltroRef, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        jPanel_contenido.add(jPanelConsultaInventario, "panelConsultaInv");

        jPanelFormularioCliente.setBackground(new java.awt.Color(255, 255, 255));

        jLabel31.setBackground(new java.awt.Color(0, 0, 0));
        jLabel31.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Registrar nuevo cliente");

        jLabel39.setBackground(new java.awt.Color(204, 204, 204));
        jLabel39.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel39.setText("Numero de Identificación");

        jTextFieldClienteId.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldClienteId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel42.setBackground(new java.awt.Color(204, 204, 204));
        jLabel42.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel42.setText("Nombre Completo");

        jTextFieldClienteNombre.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldClienteNombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel43.setBackground(new java.awt.Color(204, 204, 204));
        jLabel43.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel43.setText("Teléfono de contacto");

        jTextFieldClienteTelefono.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldClienteTelefono.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel44.setBackground(new java.awt.Color(204, 204, 204));
        jLabel44.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel44.setText("Correo Electrónico");

        jTextFieldClienteCorreo.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldClienteCorreo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel45.setBackground(new java.awt.Color(204, 204, 204));
        jLabel45.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel45.setText("Dirección de Residencia");

        jTextFieldClienteDireccion.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldClienteDireccion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel46.setBackground(new java.awt.Color(255, 255, 255));
        jLabel46.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 153, 255));
        jLabel46.setText("Nota: Este Cliente podrá alquilar multiples prendas una vez registrado en el sistema central");

        jButton4.setBackground(new java.awt.Color(0, 102, 255));
        jButton4.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Registrar Cliente");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFormularioClienteLayout = new javax.swing.GroupLayout(jPanelFormularioCliente);
        jPanelFormularioCliente.setLayout(jPanelFormularioClienteLayout);
        jPanelFormularioClienteLayout.setHorizontalGroup(
            jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                    .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                        .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addComponent(jLabel31))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormularioClienteLayout.createSequentialGroup()
                        .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldClienteDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                                        .addComponent(jTextFieldClienteTelefono)
                                        .addGap(52, 52, 52))
                                    .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                                        .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextFieldClienteId, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel43))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel44)
                                    .addComponent(jTextFieldClienteCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel42)
                                    .addComponent(jTextFieldClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(69, 69, 69))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFormularioClienteLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );
        jPanelFormularioClienteLayout.setVerticalGroup(
            jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldClienteNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelFormularioClienteLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldClienteId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelFormularioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldClienteTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldClienteCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldClienteDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(219, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanelFormularioCliente, "panelRegistroCliente");

        jPanelConsultaCliente.setBackground(new java.awt.Color(255, 255, 255));
        jPanelConsultaCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanelConsultaCliente.setEnabled(false);

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Consultar clientes");

        jLabel33.setBackground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Administra el directorio de clientes y sus Alquileres.");

        jTextFieldBuscarPorId.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldBuscarPorId.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldBuscarPorId.setText("   Buscar por ID");
        jTextFieldBuscarPorId.setToolTipText("   Buscar por ID");
        jTextFieldBuscarPorId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jButton5.setBackground(new java.awt.Color(0, 102, 255));
        jButton5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Buscar");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel34.setBackground(new java.awt.Color(255, 255, 255));
        jLabel34.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("Datos Del Cliente");

        jLabel35.setBackground(new java.awt.Color(255, 255, 255));
        jLabel35.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel35.setText("IDENTIFICACIÓN");

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel36.setText("NOMBRE COMPLETO");

        jTextFieldResultNombre.setEditable(false);
        jTextFieldResultNombre.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldResultNombre.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldResultNombre.setForeground(new java.awt.Color(0, 0, 0));
        jTextFieldResultNombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldResultNombre.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jTextFieldResultTel.setEditable(false);
        jTextFieldResultTel.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldResultTel.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldResultTel.setForeground(new java.awt.Color(0, 0, 0));
        jTextFieldResultTel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldResultTel.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextFieldResultTel.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel37.setBackground(new java.awt.Color(255, 255, 255));
        jLabel37.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel37.setText("CORREO ELECTRÓNICO");

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel47.setText("TELÉFONO DE CONTACTO");

        jTextFieldResultId.setEditable(false);
        jTextFieldResultId.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldResultId.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldResultId.setForeground(new java.awt.Color(0, 0, 0));
        jTextFieldResultId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldResultId.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jTextFieldResultId.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jTextFieldResultId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldResultIdActionPerformed(evt);
            }
        });

        jTextFieldResultDireccion.setEditable(false);
        jTextFieldResultDireccion.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldResultDireccion.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldResultDireccion.setForeground(new java.awt.Color(0, 0, 0));
        jTextFieldResultDireccion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldResultDireccion.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        jLabel48.setBackground(new java.awt.Color(255, 255, 255));
        jLabel48.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel48.setText("DIRECCIÓN");

        jTextFieldResultCorreo.setEditable(false);
        jTextFieldResultCorreo.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldResultCorreo.setFont(new java.awt.Font("Roboto", 0, 10)); // NOI18N
        jTextFieldResultCorreo.setForeground(new java.awt.Color(0, 0, 0));
        jTextFieldResultCorreo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldResultCorreo.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jTextFieldResultCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldResultCorreoActionPerformed(evt);
            }
        });

        jTableAlquilerCliente.setBackground(new java.awt.Color(255, 255, 255));
        jTableAlquilerCliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTableAlquilerCliente.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTableAlquilerCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Ref Prenda", "Fecha Solicitud", "Fecha Alquiler", "ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAlquilerCliente.setGridColor(new java.awt.Color(153, 153, 153));
        jTableAlquilerCliente.setSelectionBackground(new java.awt.Color(0, 102, 255));
        jScrollPane2.setViewportView(jTableAlquilerCliente);

        jLabel49.setBackground(new java.awt.Color(255, 255, 255));
        jLabel49.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Servicios de Alquiler Vigentes del cliente");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel1.setText("BUSCAR POR IDENTIFICACIÓN");

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator3.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanelConsultaClienteLayout = new javax.swing.GroupLayout(jPanelConsultaCliente);
        jPanelConsultaCliente.setLayout(jPanelConsultaClienteLayout);
        jPanelConsultaClienteLayout.setHorizontalGroup(
            jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaClienteLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addComponent(jLabel49)
                    .addComponent(jLabel34)
                    .addComponent(jLabel33)
                    .addComponent(jLabel32)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConsultaClienteLayout.createSequentialGroup()
                        .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldResultTel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(jTextFieldResultId, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldResultNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelConsultaClienteLayout.createSequentialGroup()
                                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jTextFieldResultCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48)
                                    .addComponent(jTextFieldResultDireccion)))))
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelConsultaClienteLayout.createSequentialGroup()
                        .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel1))
                        .addGap(52, 52, 52)
                        .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelConsultaClienteLayout.createSequentialGroup()
                                .addComponent(jTextFieldBuscarPorId, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel36)))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanelConsultaClienteLayout.setVerticalGroup(
            jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaClienteLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldBuscarPorId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldResultNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldResultId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel47)
                        .addComponent(jLabel37)))
                .addGap(18, 18, 18)
                .addGroup(jPanelConsultaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldResultTel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldResultDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldResultCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel49)
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanelConsultaCliente, "panelConsultaCliente");

        jPanelNuevoAlquiler.setBackground(new java.awt.Color(255, 255, 255));
        jPanelNuevoAlquiler.setForeground(new java.awt.Color(0, 0, 0));

        jLabel50.setBackground(new java.awt.Color(255, 255, 255));
        jLabel50.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setText("Nuevo Alquiler");

        jLabel51.setBackground(new java.awt.Color(255, 255, 255));
        jLabel51.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel51.setText("Datos de Resgistro");

        jLabel52.setBackground(new java.awt.Color(51, 51, 51));
        jLabel52.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(51, 51, 51));
        jLabel52.setText("ID del Cliente");

        jTextFieldIdEmpleado.setEditable(false);
        jTextFieldIdEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldIdEmpleado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldIdEmpleado.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jTextFieldAlquilerIdCliente.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldAlquilerIdCliente.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldAlquilerIdCliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel53.setBackground(new java.awt.Color(51, 51, 51));
        jLabel53.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(51, 51, 51));
        jLabel53.setText("ID del empleado");

        jLabel54.setBackground(new java.awt.Color(51, 51, 51));
        jLabel54.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(51, 51, 51));
        jLabel54.setText("Fecha de Alquiler");

        jLabel55.setBackground(new java.awt.Color(51, 51, 51));
        jLabel55.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(51, 51, 51));
        jLabel55.setText("Fecha de Solicitud");

        jLabel56.setBackground(new java.awt.Color(255, 255, 255));
        jLabel56.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("Seleccion de Prendas");

        jTableCarroPrendas.setBackground(new java.awt.Color(255, 255, 255));
        jTableCarroPrendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "REF", "TIPO", "COLOR", "MARCA", "VALOR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCarroPrendas.setGridColor(new java.awt.Color(255, 255, 255));
        jTableCarroPrendas.setSelectionBackground(new java.awt.Color(0, 102, 255));
        jScrollPane3.setViewportView(jTableCarroPrendas);

        jTextFieldFechaSolicitud.setEditable(false);
        jTextFieldFechaSolicitud.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldFechaSolicitud.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldFechaSolicitud.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jButton6.setBackground(new java.awt.Color(0, 102, 255));
        jButton6.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Añadir");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel57.setText("TOTAL PRENDAS");

        jLabelAlqTotalValor.setBackground(new java.awt.Color(255, 255, 255));
        jLabelAlqTotalValor.setFont(new java.awt.Font("Roboto", 0, 36)); // NOI18N
        jLabelAlqTotalValor.setForeground(new java.awt.Color(0, 102, 255));
        jLabelAlqTotalValor.setText("0");

        jLabel59.setText("TOTAL ALQUILER");

        jLabelAlqTotalPrendas.setBackground(new java.awt.Color(255, 255, 255));
        jLabelAlqTotalPrendas.setFont(new java.awt.Font("Roboto", 0, 36)); // NOI18N
        jLabelAlqTotalPrendas.setForeground(new java.awt.Color(0, 0, 0));
        jLabelAlqTotalPrendas.setText("0");

        jButton7.setBackground(new java.awt.Color(0, 102, 255));
        jButton7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Confirmar y Registrar Alquiler");
        jButton7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton8.setText("Limpiar");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelNuevoAlquilerLayout = new javax.swing.GroupLayout(jPanelNuevoAlquiler);
        jPanelNuevoAlquiler.setLayout(jPanelNuevoAlquilerLayout);
        jPanelNuevoAlquilerLayout.setHorizontalGroup(
            jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(55, 55, 55)
                        .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 462, Short.MAX_VALUE))
                            .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                                .addComponent(jLabelAlqTotalValor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNuevoAlquilerLayout.createSequentialGroup()
                        .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAlquilerIdCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                            .addComponent(jLabel52)
                            .addComponent(jLabel51)
                            .addComponent(jLabel50)
                            .addComponent(jLabel54)
                            .addComponent(jLabel56)
                            .addComponent(jDateChooserFechaAlquiler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                                .addGap(255, 255, 255)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53)
                            .addComponent(jLabel55)
                            .addComponent(jTextFieldFechaSolicitud, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(70, 70, 70))
            .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(jLabelAlqTotalPrendas)
                    .addContainerGap(741, Short.MAX_VALUE)))
        );
        jPanelNuevoAlquilerLayout.setVerticalGroup(
            jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel51)
                .addGap(27, 27, 27)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel53))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIdEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAlquilerIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldFechaSolicitud, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(jDateChooserFechaAlquiler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNuevoAlquilerLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel56))
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jLabel59))
                .addGap(29, 29, 29)
                .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelAlqTotalValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(jPanelNuevoAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNuevoAlquilerLayout.createSequentialGroup()
                    .addContainerGap(576, Short.MAX_VALUE)
                    .addComponent(jLabelAlqTotalPrendas)
                    .addGap(51, 51, 51)))
        );

        jPanel_contenido.add(jPanelNuevoAlquiler, "panelNuevoAlquiler");
        jPanelNuevoAlquiler.getAccessibleContext().setAccessibleName("");

        jPanelConsultaAlquiler.setBackground(new java.awt.Color(255, 255, 255));
        jPanelConsultaAlquiler.setForeground(new java.awt.Color(0, 0, 0));

        jLabel58.setBackground(new java.awt.Color(255, 255, 255));
        jLabel58.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Consulta de servicios de Alquiler");

        jLabel60.setBackground(new java.awt.Color(255, 255, 255));
        jLabel60.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel60.setText("Busque y gestione los servicios registrados en el sistema.");

        jLabel70.setBackground(new java.awt.Color(51, 51, 51));
        jLabel70.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(51, 51, 51));
        jLabel70.setText("Numero del servicio");

        jTextFieldAlqFiltroIdServicio.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldAlqFiltroIdServicio.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldAlqFiltroIdServicio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jTableConsultaAlquiler.setBackground(new java.awt.Color(255, 255, 255));
        jTableConsultaAlquiler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha solicitud", "Fecha Alquiler", "ID empleado", "ID cliente", "Ref Prenda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableConsultaAlquiler.setGridColor(new java.awt.Color(255, 255, 255));
        jTableConsultaAlquiler.setSelectionBackground(new java.awt.Color(0, 102, 255));
        jScrollPane4.setViewportView(jTableConsultaAlquiler);

        jButton10.setBackground(new java.awt.Color(0, 102, 255));
        jButton10.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Nuevo Alquiler");
        jButton10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel75.setText("Alquileres Activos");

        jLabelAlquileresActivos.setBackground(new java.awt.Color(255, 255, 255));
        jLabelAlquileresActivos.setFont(new java.awt.Font("Roboto", 0, 36)); // NOI18N
        jLabelAlquileresActivos.setForeground(new java.awt.Color(0, 0, 0));
        jLabelAlquileresActivos.setText("0");

        jButton13.setBackground(new java.awt.Color(0, 102, 255));
        jButton13.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Buscar");
        jButton13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTextFieldAlqFiltroIDcliente.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldAlqFiltroIDcliente.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jTextFieldAlqFiltroIDcliente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel71.setBackground(new java.awt.Color(51, 51, 51));
        jLabel71.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(51, 51, 51));
        jLabel71.setText("Desde");

        jDateChooserDesde.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N

        jLabel72.setBackground(new java.awt.Color(51, 51, 51));
        jLabel72.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(51, 51, 51));
        jLabel72.setText("Id de cliente");

        jLabel73.setBackground(new java.awt.Color(51, 51, 51));
        jLabel73.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(51, 51, 51));
        jLabel73.setText("Hasta");

        jDateChooserHasta.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N

        jButton14.setBackground(new java.awt.Color(0, 102, 255));
        jButton14.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("R");
        jButton14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 255), 1, true));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConsultaAlquilerLayout = new javax.swing.GroupLayout(jPanelConsultaAlquiler);
        jPanelConsultaAlquiler.setLayout(jPanelConsultaAlquilerLayout);
        jPanelConsultaAlquilerLayout.setHorizontalGroup(
            jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConsultaAlquilerLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelConsultaAlquilerLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                        .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel71)
                                    .addComponent(jDateChooserDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel73)
                                        .addGap(77, 77, 77))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConsultaAlquilerLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jDateChooserHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldAlqFiltroIDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel72))
                                .addGap(33, 33, 33))
                            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel60)
                                    .addComponent(jLabel58))
                                .addGap(109, 109, 109)))
                        .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                .addComponent(jLabel70)
                                .addGap(102, 120, Short.MAX_VALUE))
                            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                                .addComponent(jTextFieldAlqFiltroIdServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(70, 70, 70))
            .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(jLabelAlquileresActivos)
                    .addContainerGap(741, Short.MAX_VALUE)))
        );
        jPanelConsultaAlquilerLayout.setVerticalGroup(
            jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel60)
                .addGap(21, 21, 21)
                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooserHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelConsultaAlquilerLayout.createSequentialGroup()
                            .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel70)
                                    .addComponent(jLabel71))
                                .addComponent(jLabel72))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextFieldAlqFiltroIdServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldAlqFiltroIDcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jDateChooserDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel75)
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(jPanelConsultaAlquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConsultaAlquilerLayout.createSequentialGroup()
                    .addContainerGap(576, Short.MAX_VALUE)
                    .addComponent(jLabelAlquileresActivos)
                    .addGap(51, 51, 51)))
        );

        jPanel_contenido.add(jPanelConsultaAlquiler, "panelConsultaAlquiler");

        jPanel_RegistroLavanderia.setBackground(new java.awt.Color(255, 255, 255));

        jLabel83.setBackground(new java.awt.Color(0, 0, 0));
        jLabel83.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel83.setText("Registro de Prenda para Lavanderia");

        jTextFieldLavanderiaRef.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldLavanderiaRef.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextFieldLavanderiaRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLavanderiaRefActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(0, 102, 255));
        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Buscar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTableDatosPrendaLav.setBackground(new java.awt.Color(255, 255, 255));
        jTableDatosPrendaLav.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Ref", "Color", "Marca", "Talla", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTableDatosPrendaLav);

        jLabel84.setText("Detalles de la prenda");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jRadioMedia.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jRadioMedia.setForeground(new java.awt.Color(255, 204, 0));
        jRadioMedia.setText("MEDIA");
        jRadioMedia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioMediaActionPerformed(evt);
            }
        });

        jRadioAlta.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jRadioAlta.setForeground(new java.awt.Color(255, 153, 0));
        jRadioAlta.setText("ALTA");
        jRadioAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioAltaActionPerformed(evt);
            }
        });

        jRadioBaja.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jRadioBaja.setForeground(new java.awt.Color(153, 255, 0));
        jRadioBaja.setSelected(true);
        jRadioBaja.setText("BAJA");
        jRadioBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioBajaActionPerformed(evt);
            }
        });

        jLabel85.setBackground(new java.awt.Color(0, 0, 0));
        jLabel85.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel85.setText("PRIORIDAD DE LAVADO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jRadioAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jRadioBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jRadioMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jLabel85)
                .addGap(35, 35, 35)
                .addComponent(jRadioAlta)
                .addGap(18, 18, 18)
                .addComponent(jRadioMedia)
                .addGap(18, 18, 18)
                .addComponent(jRadioBaja)
                .addGap(61, 61, 61))
        );

        jButton11.setBackground(new java.awt.Color(0, 102, 255));
        jButton11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Enviar");
        jButton11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextPaneObservaciones.setBackground(new java.awt.Color(255, 255, 255));
        jTextPaneObservaciones.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jTextPaneObservaciones.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jTextPaneObservaciones.setText("Observaciones");
        jTextPaneObservaciones.setCaretColor(new java.awt.Color(153, 153, 153));
        jScrollPane6.setViewportView(jTextPaneObservaciones);

        javax.swing.GroupLayout jPanel_RegistroLavanderiaLayout = new javax.swing.GroupLayout(jPanel_RegistroLavanderia);
        jPanel_RegistroLavanderia.setLayout(jPanel_RegistroLavanderiaLayout);
        jPanel_RegistroLavanderiaLayout.setHorizontalGroup(
            jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                        .addGroup(jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                                        .addGap(62, 62, 62)
                                        .addComponent(jScrollPane6))))
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                                .addComponent(jLabel83)
                                .addGap(32, 32, 32)
                                .addComponent(jTextFieldLavanderiaRef, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 71, Short.MAX_VALUE)))
                        .addGap(69, 69, 69))))
        );
        jPanel_RegistroLavanderiaLayout.setVerticalGroup(
            jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldLavanderiaRef, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel84)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_RegistroLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel_RegistroLavanderiaLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanel_RegistroLavanderia, "panelRegLavanderia");

        jPanel_ConsultaLavanderia.setBackground(new java.awt.Color(255, 255, 255));

        jLabel86.setBackground(new java.awt.Color(0, 0, 0));
        jLabel86.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel86.setText("Prendas Pendienrtes de Lavanderia");

        jTableListaLavanderia.setBackground(new java.awt.Color(255, 255, 255));
        jTableListaLavanderia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Ref Prenda", "Prioridad", "Observacion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTableListaLavanderia);

        jLabel87.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jLabel87.setText("Gestión de limpieza para trajes, vestidos, disfraces retornados.   Use CTRL + Click para seleccionar mas de una fila");

        jButton17.setBackground(new java.awt.Color(0, 102, 255));
        jButton17.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Enviar a Lavanderia");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_ConsultaLavanderiaLayout = new javax.swing.GroupLayout(jPanel_ConsultaLavanderia);
        jPanel_ConsultaLavanderia.setLayout(jPanel_ConsultaLavanderiaLayout);
        jPanel_ConsultaLavanderiaLayout.setHorizontalGroup(
            jPanel_ConsultaLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConsultaLavanderiaLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel_ConsultaLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_ConsultaLavanderiaLayout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel_ConsultaLavanderiaLayout.createSequentialGroup()
                        .addGroup(jPanel_ConsultaLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane7)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_ConsultaLavanderiaLayout.createSequentialGroup()
                                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 158, Short.MAX_VALUE)))
                        .addGap(69, 69, 69))))
        );
        jPanel_ConsultaLavanderiaLayout.setVerticalGroup(
            jPanel_ConsultaLavanderiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_ConsultaLavanderiaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel87)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jPanel_contenido.add(jPanel_ConsultaLavanderia, "panelConsultaLavanderia");

        jPanel_absoluto.add(jPanel_contenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 840, 670));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_absoluto, javax.swing.GroupLayout.PREFERRED_SIZE, 1095, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_absoluto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewAlquilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewAlquilerActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelNuevoAlquiler");
    }//GEN-LAST:event_jButtonNewAlquilerActionPerformed

    private void jButtonNewAlquilerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNewAlquilerMouseClicked

    }//GEN-LAST:event_jButtonNewAlquilerMouseClicked

    private void jPanelRegistroInvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelRegistroInvMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelRegTraje");
        tipoPrenda = "vestido_dama";
    }//GEN-LAST:event_jPanelRegistroInvMouseClicked

    private void jButtonMenuInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMenuInventarioMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelGestionInv");
    }//GEN-LAST:event_jButtonMenuInventarioMouseClicked

    private void jButtonMenuInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonMenuInventarioActionPerformed

    private void jButtonMenuClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMenuClientesMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelClientes");
    }//GEN-LAST:event_jButtonMenuClientesMouseClicked

    private void jButtonMenuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonMenuClientesActionPerformed

    private void jButtonCatDisfrazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCatDisfrazActionPerformed
        tipoPrenda = "disfraz";
    }//GEN-LAST:event_jButtonCatDisfrazActionPerformed

    private void jButtonCatDisfrazMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCatDisfrazMouseClicked
        tipoPrenda = "disfraz";
        CardLayout cl = (CardLayout) jPanelContenidoCat.getLayout();
        cl.show(jPanelContenidoCat, "panelDisfraz");
    }//GEN-LAST:event_jButtonCatDisfrazMouseClicked

    private void jButtonFiltrarInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFiltrarInventarioMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonFiltrarInventarioMouseClicked

    private void jButtonFiltrarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFiltrarInventarioActionPerformed
        filtrarTabla(jComboFiltroTalla.getSelectedItem().toString());
        facade.filtroCompletoPrendas(jTableInventarioPrendas, jTextFieldFiltroRef.getText(), jComboFiltroTalla.getSelectedItem().toString(), jComboBoxTipo.getSelectedItem().toString());
    }//GEN-LAST:event_jButtonFiltrarInventarioActionPerformed

    private void jPanelConsultaInvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelConsultaInvMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelConsultaInv");
    }//GEN-LAST:event_jPanelConsultaInvMouseClicked

    private void jPanelRegistroInv1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelRegistroInv1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelRegistroInv1MouseClicked

    private void jPanelConsultaCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelConsultaCliMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelConsultaCliente");
    }//GEN-LAST:event_jPanelConsultaCliMouseClicked

    private void jPanelRegistroCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelRegistroCliMouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelRegistroCliente");
    }//GEN-LAST:event_jPanelRegistroCliMouseClicked

    private void jTextFieldResultIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldResultIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldResultIdActionPerformed

    private void jTextFieldResultCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldResultCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldResultCorreoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        prendasSelectorDialog();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        crearNuevoAlquiler();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        selectPrendas.clear();
        jTextFieldAlquilerIdCliente.setText("");
        DefaultTableModel model = (DefaultTableModel) jTableCarroPrendas.getModel();
        model.setRowCount(0);
        jDateChooserFechaAlquiler.setDate(null);
        jLabelAlqTotalPrendas.setText("0");
        jLabelAlqTotalValor.setText("0");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelRegLavanderia");
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jTextFieldRegistroReferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegistroReferenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRegistroReferenciaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelRegTraje");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refrescarTabla();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextFieldFiltroRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFiltroRefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFiltroRefActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        registrarCliente();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTextFieldResultId.setText("Pulsado");
        boolean ok = facade.mostrarClientePorId(
                jTextFieldResultId,
                jTextFieldResultNombre,
                jTextFieldResultTel,
                jTextFieldResultCorreo,
                jTextFieldResultDireccion,
                Long.parseLong(jTextFieldBuscarPorId.getText()),
                jTableAlquilerCliente);
        if (!ok)
            JOptionPane.showMessageDialog(this, "Datos del cliente no encontrados, no existe id");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelNuevoAlquiler");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        long filtroIDcliente = 0;
        long filtroIdServicio = 0;
        if (!jTextFieldAlqFiltroIDcliente.getText().isEmpty()) {
            filtroIDcliente = Long.parseLong(jTextFieldAlqFiltroIDcliente.getText());
        }
        if (!jTextFieldAlqFiltroIdServicio.getText().isEmpty()) {
            filtroIdServicio = Long.parseLong(jTextFieldAlqFiltroIdServicio.getText());
        }
        facade.filtroCompletoAlquiler(
                jTableConsultaAlquiler,
                jDateChooserDesde.getDate(),
                jDateChooserHasta.getDate(),
                filtroIDcliente,
                filtroIdServicio);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelConsultaLavanderia");
        cargarLavanderia();

    }//GEN-LAST:event_jPanel2MouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        facade.cargarAlquileres(jTableConsultaAlquiler, jLabelAlquileresActivos);
        jDateChooserDesde.setDate(null);
        jDateChooserHasta.setDate(null);
        jTextFieldAlqFiltroIDcliente.setText("");
        jTextFieldAlqFiltroIdServicio.setText("");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButtonMenuAlquileres1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMenuAlquileres1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonMenuAlquileres1MouseClicked

    private void jButtonMenuAlquileres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuAlquileres1ActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelAlquileres");
    }//GEN-LAST:event_jButtonMenuAlquileres1ActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelConsultaAlquiler");
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelNuevoAlquiler");
        DefaultTableModel model = (DefaultTableModel) jTableCarroPrendas.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jButtonGuardarTrajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarTrajeActionPerformed
        registrarPrenda();
        System.out.println("Presiona guardar en traje");
    }//GEN-LAST:event_jButtonGuardarTrajeActionPerformed

    private void jComboBoxTipoTrajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoTrajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxTipoTrajeActionPerformed

    private void jButtonGuardarDisfrazActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarDisfrazActionPerformed
        registrarPrenda();
        System.out.println("Presiona guardar en disfraz");
    }//GEN-LAST:event_jButtonGuardarDisfrazActionPerformed

    private void jTextFieldCantPiezasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCantPiezasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldCantPiezasActionPerformed

    private void jButtonGuardarVestidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarVestidoActionPerformed
        registrarPrenda();
        System.out.println("Presiona guardar en vestido");
    }//GEN-LAST:event_jButtonGuardarVestidoActionPerformed

    private void jTextFieldLongitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLongitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLongitudActionPerformed

    private void jCheckBoxPedreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPedreriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxPedreriaActionPerformed

    private void jCheckBoxPedreriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxPedreriaMouseClicked
        System.out.println("Check pedreria esta en: " + jCheckBoxPedreria.isSelected());
    }//GEN-LAST:event_jCheckBoxPedreriaMouseClicked

    private void jButtonCatTrajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCatTrajeActionPerformed
        tipoPrenda = "traje_caballero";
    }//GEN-LAST:event_jButtonCatTrajeActionPerformed

    private void jButtonCatTrajeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCatTrajeMouseClicked
        tipoPrenda = "traje_caballero";
        CardLayout cl = (CardLayout) jPanelContenidoCat.getLayout();
        cl.show(jPanelContenidoCat, "panelTraje");
    }//GEN-LAST:event_jButtonCatTrajeMouseClicked

    private void jButtonCatVestidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCatVestidoActionPerformed
        tipoPrenda = "vestido_dama";
    }//GEN-LAST:event_jButtonCatVestidoActionPerformed

    private void jButtonCatVestidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCatVestidoMouseClicked
        tipoPrenda = "vestido_dama";
        CardLayout cl = (CardLayout) jPanelContenidoCat.getLayout();
        cl.show(jPanelContenidoCat, "panelVestido");
    }//GEN-LAST:event_jButtonCatVestidoMouseClicked

    private void jTextFieldRegistroValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegistroValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRegistroValorActionPerformed

    private void jTextFieldRegistroMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegistroMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRegistroMarcaActionPerformed

    private void jTextFieldRegistroColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegistroColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRegistroColorActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelConsultaInv");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextFieldLavanderiaRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLavanderiaRefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLavanderiaRefActionPerformed

    private void jRadioBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioBajaActionPerformed
        jRadioMedia.setSelected(false);
        jRadioAlta.setSelected(false);
    }//GEN-LAST:event_jRadioBajaActionPerformed

    private void jRadioAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioAltaActionPerformed
        jRadioMedia.setSelected(false);
        jRadioBaja.setSelected(false);
    }//GEN-LAST:event_jRadioAltaActionPerformed

    private void jRadioMediaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioMediaActionPerformed
        jRadioBaja.setSelected(false);
        jRadioAlta.setSelected(false);
    }//GEN-LAST:event_jRadioMediaActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        enviarLavanderia(jTableListaLavanderia);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        CardLayout cl = (CardLayout) jPanel_contenido.getLayout();
        cl.show(jPanel_contenido, "panelLavanderia");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (!facade.datosPrendaLavanderia(jTableDatosPrendaLav, jTextFieldLavanderiaRef.getText())) {
            JOptionPane.showMessageDialog(this, "La prenda no esta alquilada");
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int id = ThreadLocalRandom.current().nextInt(0, 1000000);
        boolean baja = jRadioBaja.isSelected();
        boolean media = jRadioMedia.isSelected();
        boolean alta = jRadioAlta.isSelected();
        String ref = jTextFieldLavanderiaRef.getText();
        String prioridad;
        if (baja) {
            prioridad = "baja";
        } else if (media) {
            prioridad = "media";
        } else {
            prioridad = "alta";
        }

        boolean eliminarAlquiler = facade.eliminarAlquiler(jTextFieldLavanderiaRef.getText());
        if (eliminarAlquiler && !ref.isBlank()) {
            boolean ok = facade.insertarNuevaLavanderia(
                    id,
                    jTextFieldLavanderiaRef.getText(),
                    prioridad,
                    jTextPaneObservaciones.getText());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Se añadio nueva lavanderia ref: " + ref + "\nPrioridad : " + prioridad);
                //limpia datos añadidos previamente
                jTextFieldLavanderiaRef.setText("");
                DefaultTableModel tabla = (DefaultTableModel) jTableDatosPrendaLav.getModel();
                tabla.setRowCount(0);
                jRadioBaja.setSelected(true);
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrio error reintentar");
            }
        }


    }//GEN-LAST:event_jButton11ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel icon1;
    private javax.swing.JLabel icon5;
    private javax.swing.JLabel icon6;
    private javax.swing.JLabel iconPlusPrenda;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonCatDisfraz;
    private javax.swing.JButton jButtonCatTraje;
    private javax.swing.JButton jButtonCatVestido;
    private javax.swing.JButton jButtonFiltrarInventario;
    private javax.swing.JButton jButtonGuardarDisfraz;
    private javax.swing.JButton jButtonGuardarTraje;
    private javax.swing.JButton jButtonGuardarVestido;
    private javax.swing.JButton jButtonMenuAlquileres1;
    private javax.swing.JButton jButtonMenuClientes;
    private javax.swing.JButton jButtonMenuInventario;
    private javax.swing.JButton jButtonNewAlquiler;
    private javax.swing.JCheckBox jCheckBoxPedreria;
    private javax.swing.JComboBox<String> jComboBoxAderezo;
    private javax.swing.JComboBox<String> jComboBoxTalla;
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JComboBox<String> jComboBoxTipoTraje;
    private javax.swing.JComboBox<String> jComboFiltroTalla;
    private com.toedter.calendar.JDateChooser jDateChooserDesde;
    private com.toedter.calendar.JDateChooser jDateChooserFechaAlquiler;
    private com.toedter.calendar.JDateChooser jDateChooserHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel1TotalDisp;
    private javax.swing.JLabel jLabel1TotalLavanderia;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAlqTotalPrendas;
    private javax.swing.JLabel jLabelAlqTotalValor;
    private javax.swing.JLabel jLabelAlquileresActivos;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelTotalPrendas;
    private javax.swing.JLabel jLabel_NombreEmpleado;
    private javax.swing.JLabel jLabel_icon;
    private javax.swing.JLabel jLabel_resume;
    private javax.swing.JLabel jLabel_titulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelAlquileres;
    private javax.swing.JPanel jPanelCatDisfraz;
    private javax.swing.JPanel jPanelCatTraje;
    private javax.swing.JPanel jPanelCatVestido;
    private javax.swing.JPanel jPanelConsultaAlquiler;
    private javax.swing.JPanel jPanelConsultaCli;
    private javax.swing.JPanel jPanelConsultaCliente;
    private javax.swing.JPanel jPanelConsultaInv;
    private javax.swing.JPanel jPanelConsultaInventario;
    private javax.swing.JPanel jPanelContenidoCat;
    private javax.swing.JPanel jPanelFormularioCliente;
    private javax.swing.JPanel jPanelGestionInv;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLavanderia;
    private javax.swing.JPanel jPanelNuevoAlquiler;
    private javax.swing.JPanel jPanelRegistroCli;
    private javax.swing.JPanel jPanelRegistroInv;
    private javax.swing.JPanel jPanelResumeInv;
    private javax.swing.JPanel jPanel_Clientes;
    private javax.swing.JPanel jPanel_ConsultaLavanderia;
    private javax.swing.JPanel jPanel_RegistroLavanderia;
    private javax.swing.JPanel jPanel_RegistroPrenda;
    private javax.swing.JPanel jPanel_absoluto;
    private javax.swing.JPanel jPanel_contenido;
    private javax.swing.JPanel jPanel_izquierdo;
    private javax.swing.JRadioButton jRadioAlta;
    private javax.swing.JRadioButton jRadioBaja;
    private javax.swing.JRadioButton jRadioMedia;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableAlquilerCliente;
    private javax.swing.JTable jTableCarroPrendas;
    private javax.swing.JTable jTableConsultaAlquiler;
    private javax.swing.JTable jTableDatosPrendaLav;
    private javax.swing.JTable jTableInventarioPrendas;
    private javax.swing.JTable jTableListaLavanderia;
    private javax.swing.JTextField jTextFieldAlqFiltroIDcliente;
    private javax.swing.JTextField jTextFieldAlqFiltroIdServicio;
    private javax.swing.JTextField jTextFieldAlquilerIdCliente;
    private javax.swing.JTextField jTextFieldBuscarPorId;
    private javax.swing.JTextField jTextFieldCantPiezas;
    private javax.swing.JTextField jTextFieldClienteCorreo;
    private javax.swing.JTextField jTextFieldClienteDireccion;
    private javax.swing.JTextField jTextFieldClienteId;
    private javax.swing.JTextField jTextFieldClienteNombre;
    private javax.swing.JTextField jTextFieldClienteTelefono;
    private javax.swing.JTextField jTextFieldDisfrazNombre;
    private javax.swing.JTextField jTextFieldFechaSolicitud;
    private javax.swing.JTextField jTextFieldFiltroRef;
    private javax.swing.JTextField jTextFieldIdEmpleado;
    private javax.swing.JTextField jTextFieldLavanderiaRef;
    private javax.swing.JTextField jTextFieldLongitud;
    private javax.swing.JTextField jTextFieldRegistroColor;
    private javax.swing.JTextField jTextFieldRegistroMarca;
    private javax.swing.JTextField jTextFieldRegistroReferencia;
    private javax.swing.JTextField jTextFieldRegistroValor;
    private javax.swing.JTextField jTextFieldResultCorreo;
    private javax.swing.JTextField jTextFieldResultDireccion;
    private javax.swing.JTextField jTextFieldResultId;
    private javax.swing.JTextField jTextFieldResultNombre;
    private javax.swing.JTextField jTextFieldResultTel;
    private javax.swing.JTextPane jTextPaneObservaciones;
    // End of variables declaration//GEN-END:variables

}
