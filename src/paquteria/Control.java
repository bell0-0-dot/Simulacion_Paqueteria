/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public class Control {
    private volatile boolean corriendo = true;
    private volatile boolean pausado = false;
    
     public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notifyAll(); 
    }

    public void detener() {
        corriendo = false;
        synchronized (this) {
            notifyAll(); 
        }
    }

    public void reiniciar() {
        corriendo = true;
        pausado = false;
    }

    public boolean isCorriendo() {
        return corriendo;
    }

 
    public synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado && corriendo) {
            wait();
        }
    }
}
