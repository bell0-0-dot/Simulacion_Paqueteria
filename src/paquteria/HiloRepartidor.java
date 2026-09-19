/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class HiloRepartidor extends Thread{
    private final Repartidor repartidor;
    private final ListaEnlazada<Paquete> listaExpedicion;
    private final ListaEnlazada<Paquete> listaEntregados;
    private final ListaEnlazada<Paquete> listaDevueltos;
    private final Control control;
    private final RegistroEventos registro;
    private final java.util.Random random = new java.util.Random();
    
    public HiloRepartidor(Repartidor repartidor, ListaEnlazada<Paquete> listaExpedicion,ListaEnlazada<Paquete> listaEntregados, ListaEnlazada<Paquete> listaDevueltos,
                           Control control, RegistroEventos registro) {
        this.repartidor = repartidor;
        this.listaExpedicion = listaExpedicion;
        this.listaEntregados = listaEntregados;
        this.listaDevueltos = listaDevueltos;
        this.control = control;
        this.registro = registro;
        setName("Repartidor-" + repartidor.getId());
    }
    
    public void run() {
        try {
            while (control.isCorriendo()) {
                control.esperarSiPausado();
                if (!control.isCorriendo()) break;

                cargarVehiculo();

                if (repartidor.getCargaActual().estaVacia()) {
                    continue; 
                }

                realizarRuta();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
     private void cargarVehiculo() throws InterruptedException {
        repartidor.setEstado(EstadoRepartidor.CARGANDO);

        while (!repartidor.estaLleno()) {
            control.esperarSiPausado();
            if (!control.isCorriendo()) return;

            Paquete p = listaExpedicion.removerPrimeroSegunEsperando(new Condicion<Paquete>() {
                @Override
                public boolean cumple(Paquete pkt) {
                    return pkt.getRuta() != null && pkt.getRuta().equals(repartidor.getRuta());
                }
            });

            repartidor.getCargaActual().agregar(p);
            registro.log(p.getCodigo() + " cargado en " + repartidor.getNombre()
                    + " (" + repartidor.getCargaActual().size() + "/" + repartidor.getCapacidadMaxima() + ")");
        }
    }
     
     private void realizarRuta() throws InterruptedException {
        repartidor.setEstado(EstadoRepartidor.EN_RUTA);
        registro.log(repartidor.getNombre() + " sale a " + repartidor.getRuta()+ " con " + repartidor.getCargaActual().size() + " paquetes");
        Thread.sleep(800); 

        Paquete p;
        while ((p = repartidor.getCargaActual().removerPrimero()) != null) {control.esperarSiPausado();
            if (!control.isCorriendo()){
                break;
            }

            entregarPaquete(p);
        }

        repartidor.setEstado(EstadoRepartidor.REGRESANDO);
        Thread.sleep(800);
        repartidor.setEstado(EstadoRepartidor.DISPONIBLE);
        registro.log(repartidor.getNombre() + " regresó al centro");
    }
     
     private void entregarPaquete(Paquete p) throws InterruptedException {
        repartidor.setEstado(EstadoRepartidor.ENTREGANDO);
        p.cambiarEstado(EstadoPaqueteEnum.EN_REPARTO);

        Thread.sleep(800);

        boolean clienteAusente = random.nextInt(100) < 20; 

        if (!clienteAusente) {
            p.cambiarEstado(EstadoPaqueteEnum.ENTREGADO);
            listaEntregados.agregar(p);
            repartidor.incrementarEntregados();
            registro.log(p.getCodigo() + " entregado");
        } else {
            p.registrarIntentoFallido();
            registro.log(p.getCodigo() + " intento " + p.getIntentos() + " cliente ausente");

            if (p.getIntentos() >= 3) {
                p.cambiarEstado(EstadoPaqueteEnum.DEVUELTO);
                listaDevueltos.agregar(p);
                registro.log(p.getCodigo() + " -> DEVUELTO");
            } else {
                p.cambiarEstado(EstadoPaqueteEnum.NUEVO_INTENTO);
                p.cambiarEstado(EstadoPaqueteEnum.EN_REPARTO); 
                repartidor.getCargaActual().agregar(p); 
            }
        }
    }
}
