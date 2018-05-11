/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Konrad Chrząszcz
 */
public class JuliaPhoenix extends Julia {
         
    public void changePRe(double pre){
        pRe = pre;
    }
    
    public void changePIm(double pim){ 
        pIm = pim;
    }
    
    @Override
    public int calcValue(){
        int it = 0;
        double zRe2 = zRe * zRe, zIm2 = zIm * zIm;
        double sRe = 0.0, sIm = 0.0;
        double zM2 = 0.0;
        while ((zRe2 + zIm2) < 4.0 && it < maxIterations) {
            zM2 = zRe2 + zIm2;
            zRe2 = zRe2 - zIm2 + pIm * sRe + pRe;
            zIm2 = 2.0 * zRe * zIm + pIm * sIm;
            sRe = zRe;
            sIm = zIm;
            zRe = zRe2;
            zIm = zIm2;
            zRe2 = zRe * zRe;
            zIm2 = zIm * zIm;
            it++;
        }
        
        if (it == maxIterations || it == 0) {
            return 0;
        }
        
        double result = 256 * it;
        
        zM2 += 0.0000001;
        result += 255. * Math.log(4. / zM2) / Math.log((zRe2 + zIm2) / zM2); //do wygladzania       
        return (int) result;
    }
    
}
