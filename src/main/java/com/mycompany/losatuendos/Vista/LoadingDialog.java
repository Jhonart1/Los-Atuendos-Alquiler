package com.mycompany.losatuendos.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Diálogo de carga reutilizable con estilo moderno.
 */
public class LoadingDialog extends JDialog {

    private final JLabel labelTitulo;
    private final JLabel labelMensaje;
    private final JProgressBar progressBar;

    public LoadingDialog(Frame padre, boolean modal) {
        super(padre, "Cargando", modal);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(new Color(0, 102, 255));
        progressBar.setBackground(new Color(229, 231, 235));

        JPanel panelCarga = new JPanel(new BorderLayout(10, 10));
        panelCarga.setBackground(Color.WHITE);
        panelCarga.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        labelTitulo = new JLabel("Cargando...");
        labelTitulo.setFont(new Font("Roboto", Font.BOLD, 14));
        labelTitulo.setForeground(new Color(17, 24, 39));

        labelMensaje = new JLabel("Por favor espere");
        labelMensaje.setFont(new Font("Roboto", Font.PLAIN, 12));
        labelMensaje.setForeground(new Color(107, 114, 128));

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);
        GridBagConstraints t = new GridBagConstraints();
        t.gridx = 0;
        t.gridy = 0;
        t.anchor = GridBagConstraints.WEST;
        top.add(labelTitulo, t);
        t.gridy = 1;
        t.insets = new Insets(4, 0, 0, 0);
        top.add(labelMensaje, t);

        panelCarga.add(top, BorderLayout.NORTH);
        panelCarga.add(progressBar, BorderLayout.CENTER);

        setUndecorated(true);
        add(panelCarga);
        pack();
        setLocationRelativeTo(padre);
    }

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    public void setMensaje(String mensaje) {
        labelMensaje.setText(mensaje);
    }
}

