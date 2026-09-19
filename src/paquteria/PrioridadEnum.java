/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package paquteria;

/**
 *
 * @author vasqu
 */
public enum PrioridadEnum {
    URGENTE(1),
    ALTA(2),
    NORMAL(3),
    BAJA(4);
    
    private final int nivel;

    private PrioridadEnum(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
    
    
    
}
