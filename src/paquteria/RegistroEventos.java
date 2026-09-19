/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author vasqu
 */
public class RegistroEventos {
     private final ListaEnlazada<String> eventos = new ListaEnlazada<>();
    private final SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");

    public void log(String mensaje) {
        String hora = formato.format(new Date());
        eventos.agregar(hora + " - " + mensaje);
    }

    public String[] obtenerEventos() {
        return eventos.aArreglo(new String[0]);
    }
}
