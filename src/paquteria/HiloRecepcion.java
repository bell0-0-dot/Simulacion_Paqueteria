/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

import java.util.Random;

/**
 *
 * @author vasqu
 */
public class HiloRecepcion extends Thread {
    private final ListaEnlazada<Paquete> listaRecepcion;
    private final ListaEnlazada<Paquete> listaAlmacen;
    private final Control control;
    private final RegistroEventos registro;
    private final Random random = new Random();

    private static final String[] CLIENTES = {"Carlos López", "Ana Ruiz", "Marta Soler", "Pedro Gómez", "Laura Ibáñez"};
    private static final String[] CIUDADES = {"Tegucigalpa -SPS", "Santa Rosa", "Cortes", "Santa Barbara", "Intibuca"};
 
    public HiloRecepcion(ListaEnlazada<Paquete> listaRecepcion, ListaEnlazada<Paquete> listaAlmacen,Control control, RegistroEventos registro) {
        this.listaRecepcion = listaRecepcion;
        this.listaAlmacen = listaAlmacen;
        this.control = control;
        this.registro = registro;
        setName("HiloRecepcion");
    }
    
    public void run() {
        try {
            while (control.isCorriendo()) {
                control.esperarSiPausado();
                if (!control.isCorriendo()){
                    break;
                }

                Paquete p = generarPaqueteAleatorio();
                listaRecepcion.agregarEsperando(p);
                registro.log(p.getCodigo() + " recibido");

                
                
                Thread traslado = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500 + random.nextInt(2000));
                            p.cambiarEstado(EstadoPaqueteEnum.ALMACENADO);
                            listaRecepcion.eliminar(p);
                            listaAlmacen.agregarEsperando(p);
                            registro.log(p.getCodigo() + " almacenado");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                traslado.start();

                Thread.sleep(400 + random.nextInt(400)); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private Paquete generarPaqueteAleatorio() {
        String cliente = CLIENTES[random.nextInt(CLIENTES.length)];
        String ciudad = CIUDADES[random.nextInt(CIUDADES.length)];
        double peso = Math.round((0.5 + random.nextDouble() * 9.5) * 10) / 10.0;
        PrioridadEnum[] prioridades = PrioridadEnum.values();
        PrioridadEnum prioridad = prioridades[random.nextInt(prioridades.length)];
        String direccion = "Calle " + (random.nextInt(200) + 1);
        return new Paquete(cliente, direccion, ciudad, peso, prioridad);
    }
    
    
}
