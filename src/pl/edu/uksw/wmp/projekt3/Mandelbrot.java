/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Elenthar
 */
public class Mandelbrot extends Fractals {
    
    protected double zRe;
    protected double zIm;
    protected double pRe;
    protected double pIm;
    
    Mandelbrot(){
        zRe = 0.;
        zIm = 0.;
        pRe = 0.;
        pIm = 0.; 
    }
    
    public void setZRe(double zre){
        zRe = zre;
    }
    
    public void setZIm(double zim){
        zIm = zim;
    }
    
    public void changePRe(int x){ 
        pRe = 4. * (x * relPixSize + visibleX) - 2. - mX;
    }
    
    public void changePIm(int y){ 
        pIm = -4. * (y * relPixSize + visibleY) + 2. + mY;
    }
   
    public int calcValue(){
        int it = 0;
        double tmp_re, tmp_im, ztmp_re, ztmp_im;
        double zRe2 = zRe * zRe;
        double zIm2 = zIm * zIm;
        double zM2 = 0.;
        
        while ((zRe2 + zIm2) < 4. && it < maxIterations) {
            zM2 = zRe2 + zIm2;
            ztmp_re = zRe;
            ztmp_im = zIm;
            for (int i = 1; i < powerLevel; i++) {
                tmp_re = zRe * ztmp_re - zIm * ztmp_im;
                tmp_im = zRe * ztmp_im + ztmp_re * zIm;
                zRe = tmp_re;
                zIm = tmp_im;
            }
            zRe += pRe;
            zIm += pIm;
            zRe2 = zRe * zRe;
            zIm2 = zIm * zIm;
            it++;
        }

        if (it == maxIterations || it == 0) {
            return 0;
        }

        double result = 256 * it;
        zM2 += 0.0000001; //brak dzielenia przez zero
        result += 255. * Math.log(4. / zM2) / Math.log((zRe2 + zIm2) / zM2); //do wygladzania
        return (int) result;
    }
}
