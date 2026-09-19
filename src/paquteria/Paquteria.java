/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paquteria;

import javax.swing.SwingUtilities;

/**
 *
 * @author vasqu
 */
public class Paquteria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
            VentanaGrafica ventana = new VentanaGrafica();
            ventana.setVisible(true);
        });
    }
    
}
