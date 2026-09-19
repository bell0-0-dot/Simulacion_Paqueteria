/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class HiloEmpaquetador extends Thread{
    private final int id;
    private final ListaEnlazada<Paquete> listaEmpaquetado;
    private final ListaEnlazada<Paquete> listaExpedicion;
    private final Control control;
    private final RegistroEventos registro;
    
    public HiloEmpaquetador(int id, ListaEnlazada<Paquete> listaEmpaquetado, ListaEnlazada<Paquete> listaExpedicion,Control control, RegistroEventos registro) {
        this.id = id;
        this.listaEmpaquetado = listaEmpaquetado;
        this.listaExpedicion = listaExpedicion;
        this.control = control;
        this.registro = registro;
        setName("Empaquetador-" + id);
    }
    public void run() {
        try {
            while (control.isCorriendo()) {
                control.esperarSiPausado();
                if (!control.isCorriendo()){
                    break;
                }

                Paquete p = listaEmpaquetado.removerEsperando();
                p.cambiarEstado(EstadoPaqueteEnum.EMPAQUETANDO);
                registro.log(p.getCodigo() + " en empaquetado (Empaquetador-" + id + ")");

                Thread.sleep(calcularTiempo(p.getPeso()));
                
                
                p.cambiarEstado(EstadoPaqueteEnum.EMPAQUETADO);
                listaEmpaquetado.eliminar(p);
                listaExpedicion.agregarEsperando(p);
                registro.log(p.getCodigo() + " empaquetado");
              
                p.cambiarEstado(EstadoPaqueteEnum.EN_EXPEDICION);
                listaExpedicion.agregarEsperando(p);
                registro.log(p.getCodigo() + " en expedición -> " + p.getRuta());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long calcularTiempo(double peso) {
        if (peso <= 2) {
            return 1000;
        }
        if (peso <= 5) {
            return 2000;
        }
        return 3000;
    }
}
