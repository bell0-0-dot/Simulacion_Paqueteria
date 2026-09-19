/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class HiloClasificador extends Thread{
    
    private final int id;
    private final ListaEnlazada<Paquete> listaAlmacen;
    private final ListaEnlazada<Paquete> listaEmpaquetado;
    private final Control control;
    private final RegistroEventos registro;
    
    
    public HiloClasificador(int id, ListaEnlazada<Paquete> listaAlmacen, ListaEnlazada<Paquete> listaEmpaquetado, Control control, RegistroEventos registro) {
        this.id = id;
        this.listaAlmacen = listaAlmacen;
        this.listaEmpaquetado = listaEmpaquetado;
        this.control = control;
        this.registro = registro;
        setName("Clasificador-" + id);
    }
    
    public void run() {
        try {
            while (control.isCorriendo()) {
                control.esperarSiPausado();
                if (!control.isCorriendo()){
                    break;
                }

                Paquete p = listaAlmacen.removerEsperando();
                
                p.cambiarEstado(EstadoPaqueteEnum.CLASIFICANDO);
                registro.log(p.getCodigo() + "tomado para Clasificador-" + id);

                Thread.sleep(800);

                String ruta = determinarRuta(p.getCiudad());
                p.setRuta(ruta);
                p.cambiarEstado(EstadoPaqueteEnum.CLASIFICADO);
                registro.log(p.getCodigo() + " clasificado -> " + ruta);

                listaEmpaquetado.agregarEsperando(p);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private String determinarRuta(String ciudad) {
        switch (ciudad) {
            case "Tegucigalpa-SPS":
                return "Ruta 1";
            case "Santa Rosa ":        
                return "Ruta 1";
            case "Cortes":           
                return "Ruta 2";
            case "Santa Barbara":      
                return "Ruta 3";
            case "Intibuca":         
                return "Ruta 4";
            default:                 
                return "Ruta 1";
        }
    }
    
}
