package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Konrad Chrząszcz
 */
public class MandelbrotPhoenix extends Mandelbrot {

    @Override
    public int calcValue(){
        double zp_re = 0;
        double zp_im = 0;
        int it = 0;
        double tmp_re, tmp_im;
        
        double zRe2 = zRe * zRe, zIm2 = zIm * zIm;
        double zM2 = 0.0;
        
        while ((zRe2 + zIm2) < 4 && it < maxIterations) {
            zM2 = zRe2 + zIm2;
            zRe2 = zRe * zRe;
            zIm2 = zIm * zIm;
            
            tmp_re = zRe * zRe - zIm * zIm + pRe + pIm * zp_re;
            tmp_im = 2 * zRe * zIm + pIm * zp_im;
            zp_re = zRe;
            zp_im = zIm;
            zRe = tmp_re;
            zIm = tmp_im;
            
            it++;
        }
  
        if (it == maxIterations || it == 0) {
            return 0;
        }

        double result = 256 * it;
        
        zM2 += 0.0000001;
        result += 255. * Math.log(4. / zM2) / Math.log((zRe2 + zIm2) / zM2); //do wygladzania    
        
        return (int)result;
    }
}
