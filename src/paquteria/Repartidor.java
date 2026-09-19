/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class Repartidor {
    private final int id;
    private final String nombre;
    private final int capacidadMaxima;
    private final String ruta;

    private volatile EstadoRepartidor estado;
    private volatile int paquetesEntregados;
    private final ListaEnlazada<Paquete> cargaActual;
    
     public Repartidor(int id, String nombre, int capacidadMaxima, String ruta) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.ruta = ruta;
        this.estado = EstadoRepartidor.DISPONIBLE;
        this.paquetesEntregados = 0;
        this.cargaActual = new ListaEnlazada<>(capacidadMaxima);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getRuta() {
        return ruta;
    }

    public ListaEnlazada<Paquete> getCargaActual() {
        return cargaActual;
    }
     

    public EstadoRepartidor getEstado() {
        return estado;
    }

    public void setEstado(EstadoRepartidor estado) {
        this.estado = estado;
    }

    public int getPaquetesEntregados() {
        return paquetesEntregados;
    }

    public void setPaquetesEntregados(int paquetesEntregados) {
        this.paquetesEntregados = paquetesEntregados;
    }
     
    public synchronized void incrementarEntregados() {
        paquetesEntregados++;
    }

    public boolean estaLleno() {
        return cargaActual.size() >= capacidadMaxima;
    }
     
}
