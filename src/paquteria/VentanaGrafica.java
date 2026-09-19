/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author vasqu
 */
public class VentanaGrafica extends JFrame {
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnReanudar;
    private JButton btnDetener;
    private JButton btnReiniciar;
    private JButton btnEstadisticas;
    private JTextArea areaRecepcion, areaAlmacen, areaEmpaquetado, areaExpedicion;
    private JLabel lblCapRecepcion, lblCapAlmacen, lblCapEmpaquetado, lblCapExpedicion;

  
    private JLabel[] lblEstadoRepartidor;
    private JLabel[] lblCargaRepartidor;
    private final SistemaPaqueteria sistema;
    private JTextArea areaLog;
    private Timer timerActualizacion;
 
    public VentanaGrafica() {
        sistema = new SistemaPaqueteria();

        setTitle("Sistema de Paquetería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(construirPanelControles(), BorderLayout.NORTH);
        add(construirPanelCentral(), BorderLayout.CENTER);
        add(construirPanelInferior(), BorderLayout.SOUTH);

        configurarTimer();
    }
    
    private JPanel construirPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnIniciar = new JButton("INICIAR");
        btnPausar = new JButton("PAUSAR");
        btnReanudar = new JButton("REANUDAR");
        btnDetener = new JButton("DETENER");
        btnReiniciar = new JButton("REINICIAR");
        btnEstadisticas = new JButton("ESTADÍSTICAS");

        btnIniciar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Iniciar(e);
        }
        });

        btnPausar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pausar(e);
            }
        });

        btnReanudar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reanudar(e);
            }
        });

        btnDetener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Detener(e);
            }
        });

        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reiniciar(e);
            }
        });

        btnEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Estadisticas(e);
            }
        });

        panel.add(btnIniciar);
        panel.add(btnPausar);
        panel.add(btnReanudar);
        panel.add(btnDetener);
        panel.add(btnReiniciar);
        panel.add(btnEstadisticas);

        return panel;
    }
    
    private JPanel construirPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());

      
        JPanel filaSuperior = new JPanel(new GridLayout(1, 2, 5, 5));
        filaSuperior.add(crearZona("RECEPCIÓN", areaRecepcion = new JTextArea(),lblCapRecepcion = new JLabel("0")));
        filaSuperior.add(crearZona("ALMACÉN", areaAlmacen = new JTextArea(),lblCapAlmacen = new JLabel("0")));

        JPanel filaMedia = new JPanel(new GridLayout(1, 2, 5, 5));
        filaMedia.add(crearZona("EMPAQUETADO", areaEmpaquetado = new JTextArea(), lblCapEmpaquetado = new JLabel("0")));
        filaMedia.add(crearZona("EXPEDICIÓN", areaExpedicion = new JTextArea(),lblCapExpedicion = new JLabel("0")));

        JPanel filasSuperiores = new JPanel(new GridLayout(2, 1, 5, 5));
        filasSuperiores.add(filaSuperior);
        filasSuperiores.add(filaMedia);

        panelCentral.add(filasSuperiores, BorderLayout.CENTER);
        panelCentral.add(construirPanelRepartidores(), BorderLayout.SOUTH);

        return panelCentral;
    }
    private JPanel crearZona(String titulo, JTextArea area, JLabel labelCapacidad) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        encabezado.add(new JLabel("Capacidad: "));
        encabezado.add(labelCapacidad);

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
    private JPanel construirPanelRepartidores() {
        Repartidor[] repartidores = sistema.getRepartidores();

        JPanel panel = new JPanel(new GridLayout(1, repartidores.length, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("REPARTIDORES"));

        lblEstadoRepartidor = new JLabel[repartidores.length];
        lblCargaRepartidor = new JLabel[repartidores.length];

        for (int i = 0; i < repartidores.length; i++) {
            JPanel tarjeta = new JPanel(new GridLayout(3, 1));
            tarjeta.setBorder(BorderFactory.createEtchedBorder());

            tarjeta.add(new JLabel(i+1 + repartidores[i].getNombre() + " (" + repartidores[i].getRuta() + ")"));

            lblEstadoRepartidor[i] = new JLabel("Estado: " + repartidores[i].getEstado());
            tarjeta.add(lblEstadoRepartidor[i]);

            lblCargaRepartidor[i] = new JLabel("Entregados: 0");
            tarjeta.add(lblCargaRepartidor[i]);

            panel.add(tarjeta);
        }
        return panel;
    }
    
     private JPanel construirPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("REGISTRO DEL SISTEMA"));

        areaLog = new JTextArea(8, 100);
        areaLog.setEditable(false);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(1100, 180));

        return panel;
    }
     
     private void configurarTimer() {
        timerActualizacion = new Timer(700, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVista();
            }
        });
        timerActualizacion.start();
    }
     
     private void actualizarVista() {
        actualizarZona(areaRecepcion, lblCapRecepcion, sistema.getRecepcion(), 10);
        actualizarZona(areaAlmacen, lblCapAlmacen, sistema.getAlmacen(), 20);
        actualizarZona(areaEmpaquetado, lblCapEmpaquetado, sistema.getEmpaquetado(), 8);
        actualizarZona(areaExpedicion, lblCapExpedicion, sistema.getExpedicion(), 15);

        Repartidor[] repartidores = sistema.getRepartidores();
       for (int i = 0; i < repartidores.length; i++) {
        String estadoTexto = "Estado: " + repartidores[i].getEstado();
        if (repartidores[i].getEstado() == EstadoRepartidor.CARGANDO) {
            estadoTexto += " (" + repartidores[i].getCargaActual().size()
                    + "/" + repartidores[i].getCapacidadMaxima() + ")";
        }
        lblEstadoRepartidor[i].setText(estadoTexto);
        lblCargaRepartidor[i].setText("Entregados: " + repartidores[i].getPaquetesEntregados());
    }

        String[] eventos = sistema.getEventos();
        StringBuilder sb = new StringBuilder();
        int inicio = Math.max(0, eventos.length - 30); 
        for (int i = inicio; i < eventos.length; i++) {
            sb.append(eventos[i]).append("\n");
        }
        areaLog.setText(sb.toString());
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
     
      private void actualizarZona(JTextArea area, JLabel labelCapacidad, Paquete[] paquetes, int capacidadMaxima) {
        StringBuilder sb = new StringBuilder();
        for (Paquete p : paquetes) {
            sb.append(p.getCodigo()).append("\n");
        }
        area.setText(sb.toString());
        labelCapacidad.setText(paquetes.length + " / " + capacidadMaxima);
    }
      private void Iniciar(ActionEvent e) {
    sistema.iniciar();
}

    private void Pausar(ActionEvent e) {
        sistema.pausar();
    }

    private void Reanudar(ActionEvent e) {
        sistema.reanudar();
    }

    private void Detener(ActionEvent e) {
        sistema.detener();
        
    }

    private void Reiniciar(ActionEvent e) {
        btnReiniciar.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sistema.reiniciar();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            btnReiniciar.setEnabled(true);
                        }
                    });
                }
            }
        }).start();
    }

    private void Estadisticas(ActionEvent e) {
        String mensaje = String.format(
                "Paquetes generados: %d%nEntregados: %d%nDevueltos: %d%nEn proceso: %d",
                sistema.getTotalGenerados(),
                sistema.getTotalEntregados(),
                sistema.getTotalDevueltos(),
                sistema.getTotalEnProceso()
        );
        JOptionPane.showMessageDialog(this, mensaje, "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
}
      
      
}
