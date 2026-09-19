/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class SistemaPaqueteria {
    private ListaEnlazada<Paquete> listaRecepcion;
    private ListaEnlazada<Paquete> listaAlmacen;
   private ListaEnlazada<Paquete> listaClasificacion; 
    private ListaEnlazada<Paquete> listaEmpaquetado;
    private ListaEnlazada<Paquete> listaExpedicion;
    private ListaEnlazada<Paquete> listaEntregados;
    private ListaEnlazada<Paquete> listaDevueltos;
    
    private Control control;
    private RegistroEventos registro;
    
    private HiloRecepcion hiloRecepcion;
    private ListaEnlazada<HiloClasificador> clasificadores;
    private ListaEnlazada<HiloEmpaquetador> empaquetadores;
    private ListaEnlazada<HiloRepartidor> hilosRepartidores;
    private ListaEnlazada<Repartidor> repartidores;
    
    private static final int capacidad_recepcion = 10;
    private static final int capacidad_almacen = 20;
    private static final int capacidad_clasificacion = 10;
    private static final int capacidad_empaquetado = 8;
    private static final int capacidad_expedicion = 15;

    public SistemaPaqueteria() {
        inicializarEstructuras();
    }
    


    private void inicializarEstructuras() {
        listaRecepcion = new ListaEnlazada<>(capacidad_recepcion);
        listaAlmacen = new ListaEnlazada<>(capacidad_almacen);
        listaEmpaquetado = new ListaEnlazada<>(capacidad_empaquetado);
        listaExpedicion = new ListaEnlazada<>(capacidad_expedicion);
        listaEntregados = new ListaEnlazada<>();
        listaDevueltos = new ListaEnlazada<>();
        control = new Control();
        registro = new RegistroEventos();

        clasificadores = new ListaEnlazada<>();
        empaquetadores = new ListaEnlazada<>();
        hilosRepartidores = new ListaEnlazada<>();
        repartidores = new ListaEnlazada<>();

        hiloRecepcion = new HiloRecepcion(listaRecepcion, listaAlmacen, control, registro);

        for (int i = 1; i <= 3; i++) {
            clasificadores.agregar(new HiloClasificador(i, listaAlmacen, listaEmpaquetado, control, registro));
        }

        for (int i = 1; i <= 2; i++) {
            empaquetadores.agregar(new HiloEmpaquetador(i, listaEmpaquetado, listaExpedicion, control, registro));
        }

        String[] rutas = {"Ruta 1", "Ruta 2", "Ruta 3", "Ruta 4"};
        int[] capacidades = {5, 4, 6, 5};
        for (int i = 0; i < rutas.length; i++) {
            Repartidor r = new Repartidor(i + 1, "Repartidor " + (i + 1), capacidades[i], rutas[i]);
            repartidores.agregar(r);
            hilosRepartidores.agregar(new HiloRepartidor(r, listaExpedicion, listaEntregados, listaDevueltos, control, registro));
        }
    }

    public void iniciar() {
        hiloRecepcion.start();

        for (int i = 0; i < clasificadores.size(); i++) {
            clasificadores.obtener(i).start();
        }
        for (int i = 0; i < empaquetadores.size(); i++) {
            empaquetadores.obtener(i).start();
        }
        for (int i = 0; i < hilosRepartidores.size(); i++) {
            hilosRepartidores.obtener(i).start();
        }

        registro.log("Procesos iniciados");
    }
     public void pausar() {
        control.pausar();
        registro.log("Procesos pausados");
    }

    public void reanudar() {
        control.reanudar();
        registro.log("Procesos reanudados");
    }

    public void detener() {
       control.detener();
        hiloRecepcion.interrupt();
        for (int i = 0; i < clasificadores.size(); i++) {
            clasificadores.obtener(i).interrupt();}
        for (int i = 0; i < empaquetadores.size(); i++){
            empaquetadores.obtener(i).interrupt();
        }
        for (int i = 0; i < hilosRepartidores.size(); i++) {
            hilosRepartidores.obtener(i).interrupt();
        }
        registro.log("Procesos detenidos");
    }
    
     public void reiniciar() throws InterruptedException {
        detener();

        hiloRecepcion.join();
        for (int i = 0; i < clasificadores.size(); i++) {
            clasificadores.obtener(i).join();
        }
        for (int i = 0; i < empaquetadores.size(); i++){
            empaquetadores.obtener(i).join();
        }
        for (int i = 0; i < hilosRepartidores.size(); i++) {
            hilosRepartidores.obtener(i).join();
        }

        inicializarEstructuras();
        registro.log("Procesos reiniciados");
    }

  public Paquete[] getRecepcion() { 
      return listaRecepcion.aArreglo(new Paquete[0]); 
  }
    public Paquete[] getAlmacen() { 
        return listaAlmacen.aArreglo(new Paquete[0]); 
    }
    public Paquete[] getEmpaquetado() {
        return listaEmpaquetado.aArreglo(new Paquete[0]);
    }
    public Paquete[] getExpedicion() { 
        return listaExpedicion.aArreglo(new Paquete[0]);
    }
    public Paquete[] getEntregados() {
        return listaEntregados.aArreglo(new Paquete[0]); 
    }
    public Paquete[] getDevueltos() { 
        return listaDevueltos.aArreglo(new Paquete[0]); 
    }
    public String[] getEventos() { 
        return registro.obtenerEventos(); 
    }
    public Repartidor[] getRepartidores() { 
        return repartidores.aArreglo(new Repartidor[0]);
    }
     
    public int getTotalGenerados() {
        return listaEntregados.size() + listaDevueltos.size() + listaRecepcion.size() + listaAlmacen.size() + listaEmpaquetado.size() + listaExpedicion.size();
    }

    public int getTotalEntregados() { 
        return listaEntregados.size();
    }
    public int getTotalDevueltos() {
        return listaDevueltos.size(); 
    }
    public int getTotalEnProceso() {
        return listaRecepcion.size() + listaAlmacen.size() + listaEmpaquetado.size() + listaExpedicion.size();
    }



}
