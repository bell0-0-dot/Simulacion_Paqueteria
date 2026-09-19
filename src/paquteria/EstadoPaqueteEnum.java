/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public enum EstadoPaqueteEnum {
    RECIBIDO,
    ALMACENADO,
    CLASIFICANDO,
    CLASIFICADO,
    EMPAQUETANDO,
    EMPAQUETADO,
    EN_EXPEDICION,
    EN_REPARTO,
    NUEVO_INTENTO,
    ENTREGADO,
    DEVUELTO;
    
    public boolean moverPedido(EstadoPaqueteEnum nuevoEstado) {
        switch (this) {
            case RECIBIDO:
                return nuevoEstado == ALMACENADO;
            case ALMACENADO:
                return nuevoEstado == CLASIFICANDO;
            case CLASIFICANDO:
                return nuevoEstado == CLASIFICADO;
            case CLASIFICADO:
                return nuevoEstado == EMPAQUETANDO;
            case EMPAQUETANDO:
                return nuevoEstado == EMPAQUETADO;
            case EMPAQUETADO:
                return nuevoEstado == EN_EXPEDICION;
            case EN_EXPEDICION:
                return nuevoEstado == EN_REPARTO;
            case EN_REPARTO:
                return nuevoEstado == ENTREGADO || nuevoEstado == NUEVO_INTENTO;
            case NUEVO_INTENTO:
                return nuevoEstado == EN_REPARTO || nuevoEstado == DEVUELTO;
            case ENTREGADO:
                return false;
            case DEVUELTO:
                return false;
            default:
                return false;
        }
    }
}
