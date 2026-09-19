/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

import java.time.LocalTime;

/**
 *
 * @author vasqu
 */
public class Paquete {
    private static int contador = 1;
    
    private static synchronized String generarCodigo() {
        String codigo = String.format("PKG-%05d", contador);
        contador++;
        return codigo;
    }
    
    private final String codigo;
    private final String cliente;
    private final String direccion;
    private final String ciudad;
    private final double peso;
    private final PrioridadEnum prioridad;

    private volatile EstadoPaqueteEnum estado;
    private volatile String ruta;
    private volatile int intentos;
    private final LocalTime horaCreacion;
    
    public Paquete(String cliente, String direccion, String ciudad, double peso, PrioridadEnum prioridad) {
        this.codigo = generarCodigo();
        this.cliente = cliente;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.peso = peso;
        this.prioridad = prioridad;
        this.estado = EstadoPaqueteEnum.RECIBIDO;
        this.ruta = null;
        this.intentos = 0;
        this.horaCreacion = LocalTime.now();
    }
    
    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Paquete.contador = contador;
    }

    public EstadoPaqueteEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaqueteEnum estado) {
        this.estado = estado;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
    
    
    public String toString(){
        return "Codigo: "+codigo+" |"+"Cliente: "+cliente+"|"+"Ciudad: "+ciudad+"|"
                +"peso: "+peso+ "|"+"Prioridad: "+prioridad+"|"+" Estado: "+estado+"|"
                +" Ruta: "
                +ruta+"|"+" Intentos: "+intentos;
    }
    public synchronized boolean cambiarEstado(EstadoPaqueteEnum nuevoEstado) {
        if (estado.moverPedido(nuevoEstado)) {
            this.estado = nuevoEstado;
            return true;
        }
        return false;
    }

    public synchronized void registrarIntentoFallido() {
        this.intentos++;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getPeso() {
        return peso;
    }

    public PrioridadEnum getPrioridad() {
        return prioridad;
    }

    public LocalTime getHoraCreacion() {
        return horaCreacion;
    }
    

}
