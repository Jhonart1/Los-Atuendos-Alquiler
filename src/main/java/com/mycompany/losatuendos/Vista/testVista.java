
package com.mycompany.losatuendos.Vista;
import javax.swing.*;
import java.awt.*;

public class testVista extends JFrame{

    private JPanel panelCentral;
    private CardLayout cardLayout;

    public testVista() {
        setTitle("Ejemplo de Paneles Dinámicos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Menú lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(3, 1));
        JButton btnPanel1 = new JButton("Panel 1");
        JButton btnPanel2 = new JButton("Panel 2");
        JButton btnPanel3 = new JButton("Panel 3");
        panelMenu.add(btnPanel1);
        panelMenu.add(btnPanel2);
        panelMenu.add(btnPanel3);

        // Panel central con CardLayout
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(Color.red);

        // Crear paneles de contenido
        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Contenido del Panel 1"));
        panel1.setBackground(Color.red);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Contenido del Panel 2"));

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("Contenido del Panel 3"));

        // Agregar paneles al contenedor central
        panelCentral.add(panel1, "panel1");
        panelCentral.add(panel2, "panel2");
        panelCentral.add(panel3, "panel3");

        // Eventos de los botones
        btnPanel1.addActionListener(e -> cardLayout.show(panelCentral, "panel1"));
        btnPanel2.addActionListener(e -> cardLayout.show(panelCentral, "panel2"));
        btnPanel3.addActionListener(e -> cardLayout.show(panelCentral, "panel3"));

        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelMenu, BorderLayout.WEST);
        getContentPane().add(panelCentral, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new testVista().setVisible(true);
        });
    }

}
