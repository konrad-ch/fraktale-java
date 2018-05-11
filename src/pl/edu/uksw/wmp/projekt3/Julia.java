/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Elenthar
 */
public class Julia extends Mandelbrot{
    
    Julia(){
        
    }

    public void changeZRe(int x){
        zRe = 4 * (x * relPixSize + visibleX) - 2. - mX;
    }
    
    public void changeZIm(int y){
        zIm = -4. * (y * relPixSize + visibleY) + 2. + mY;
    }
    
    @Override
    public void changePRe(int coordX){
        pRe = coordX == 0. ? 0. : 4. * (relPixSize * coordX + visibleX) - 2. - mX;
    }
    
    @Override
    public void changePIm(int coordY){ 
        pIm = coordY == 0. ? 0. : -4.0 * (relPixSize * (coordY) + visibleY) + 2. + mY;
    }
}
