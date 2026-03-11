package com.mycompany.losatuendos;

import SistemaFacade.SistemaFacade;
import com.mycompany.losatuendos.Vista.Dashboard;
import com.mycompany.losatuendos.Vista.Login;

public class LosAtuendos {

    public static void main(String[] args) {
        //Iniciar UI
        java.awt.EventQueue.invokeLater(() -> {
            SistemaFacade facade = SistemaFacade.getInstancia();
//            Dashboard vistaDashboard = new Dashboard(f);
            Login vistaLogin = new Login(facade);
            vistaLogin.pack(); // Ajusta la ventana al contenido
            vistaLogin.setLocationRelativeTo(null); // La centra en pantalla
            vistaLogin.setVisible(true); // LA MUESTRA
        });
    }
}
