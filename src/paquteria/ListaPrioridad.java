/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class ListaPrioridad {
    private final ListaEnlazada<Paquete> lista;

    public ListaPrioridad(ListaEnlazada<Paquete> lista) {
        this.lista = lista;
    }
    public Paquete tomarSiguiente() throws InterruptedException {
        synchronized (lista) {
            Paquete resultado;
            while ((resultado = buscarYRemoverMasUrgente()) == null) {
                lista.wait();
            }
            lista.notifyAll();
            return resultado;
        }
    }
    
    private Paquete buscarYRemoverMasUrgente() {
        Paquete p = lista.removerPrimeroSegun(pkt -> pkt.getPrioridad() == PrioridadEnum.URGENTE);
        if (p != null) return p;

        p = lista.removerPrimeroSegun(pkt -> pkt.getPrioridad() == PrioridadEnum.ALTA);
        if (p != null) return p;

        p = lista.removerPrimeroSegun(pkt -> pkt.getPrioridad() == PrioridadEnum.NORMAL);
        if (p != null) return p;

        return lista.removerPrimeroSegun(pkt -> pkt.getPrioridad() == PrioridadEnum.BAJA);
    }
    
}
