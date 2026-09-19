/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

import java.util.Arrays;

/**
 *
 * @author vasqu
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int size;
    
    private final int capacidadMaxima;
    
    
    public ListaEnlazada(int capacidadMaxima) {
        this.cabeza = null;
        this.cola = null;
        this.size = 0;
        this.capacidadMaxima = capacidadMaxima;
    }
    public ListaEnlazada() {
        this(0);
    }
    
    public synchronized boolean agregar(T dato) {
        if (capacidadMaxima > 0 && size >= capacidadMaxima) {
            return false;
        }
        agregarInterno(dato);
        notifyAll(); 
        return true;
    }

    public synchronized T removerPrimero() {
        if (cabeza == null) return null;
        T dato = removerInterno();
        notifyAll(); 
        return dato;
    }

   
    public synchronized void agregarEsperando(T dato) throws InterruptedException {
        while (capacidadMaxima > 0 && size >= capacidadMaxima) {
            wait();
        }
        agregarInterno(dato);
        notifyAll(); 
    }

   
    public synchronized T removerEsperando() throws InterruptedException {
        while (cabeza == null) {
            wait();
        }
        T dato = removerInterno();
        notifyAll();
        return dato;
    }

    private void agregarInterno(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            cola = nuevo;
        }
        size++;
    }

    private T removerInterno() {
        T dato = cabeza.getDato();
        cabeza = cabeza.getSiguiente();
        if (cabeza == null) cola = null;
        size--;
        return dato;
    }

   

    public synchronized boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            if (cabeza == null) cola = null;
            size--;
            notifyAll();
            return true;
        }

        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                Nodo<T> aEliminar = actual.getSiguiente();
                actual.setSiguiente(aEliminar.getSiguiente());
                if (aEliminar == cola) cola = actual;
                size--;
                notifyAll();
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public synchronized boolean buscar(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(dato)) return true;
            actual = actual.getSiguiente();
        }
        return false;
    }

    public synchronized T obtener(int indice) {
        if (indice < 0 || indice >= size) return null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

  
    public synchronized T[] aArreglo(T[] tipo) {
        T[] resultado = Arrays.copyOf(tipo, size);
        Nodo<T> actual = cabeza;
        int i = 0;
        while (actual != null) {
            resultado[i++] = actual.getDato();
            actual = actual.getSiguiente();
        }
        return resultado;
    }

     
       public synchronized int size() {
        return size;
    }

    public synchronized boolean estaVacia() {
        return size == 0;
    }

    public synchronized boolean estaLlena() {
        return capacidadMaxima > 0 && size>= capacidadMaxima;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public synchronized T removerPrimeroSegun(Condicion<T> condicion) {
        if (cabeza == null) return null;

        if (condicion.cumple(cabeza.getDato())) {
            T dato = removerInterno();
            notifyAll();
            return dato;
        }

        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != null) {
            if (condicion.cumple(actual.getDato())) {
                anterior.setSiguiente(actual.getSiguiente());
                if (actual == cola) cola = anterior;
                size--;
                notifyAll();
                return actual.getDato();
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
    return null;
}
    
    public synchronized T removerPrimeroSegunEsperando(Condicion<T> condicion) throws InterruptedException {
        T resultado;
        while ((resultado = removerPrimeroSegun(condicion)) == null) {
            wait();
        }
        notifyAll();
        return resultado;
}
    
    
     
}
