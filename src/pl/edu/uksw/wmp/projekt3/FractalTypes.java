/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Konrad Chrząszcz
 */
public enum FractalTypes {
    MANDELBROT, MANDELBAR, JULIA, JULIA_PHOENIX, MANDELBROT_PHOENIX, BIRD_OF_PREY;
    
    FractalTypes(){
    }
    
    /**
     * Zwraca tekstową reprezentację zmiennych enum
     * @return tekstowa reprezentacja zmiennej enum.
     */
    @Override
    public String toString() {
        String output = name().toString();
        output = output.charAt(0) + output.substring(1).toLowerCase().replace('_', ' ');
        return output;
    }
}
