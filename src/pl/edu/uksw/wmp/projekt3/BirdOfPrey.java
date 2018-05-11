package pl.edu.uksw.wmp.projekt3;

/**
 *
 * @author Konrad Chrząszcz
 */
public class BirdOfPrey extends Mandelbrot{
    
    @Override
    public int calcValue() {
        int it = 0;
        double tmp_re, tmp_im, ztmp_re, ztmp_im;
        double zRe2 = zRe * zRe, zIm2 = zIm * zIm;
        double zM2 = 0.0;
        
        while ((zRe2 + zIm2) < 4 && it < maxIterations)
        {
            zM2 = zRe2 + zIm2;
            zRe2 = zRe * zRe;
            zIm2 = zIm * zIm;
            
            ztmp_re = zRe;
            ztmp_im = zIm;
            for (int i=1; i<powerLevel; i++){
                tmp_re = zRe*ztmp_re - zIm*ztmp_im;
                tmp_im = zRe*ztmp_im + ztmp_re*zIm;
                zRe = tmp_re;
                zIm = tmp_im;
            }
            zRe += pRe;
            zIm += pIm;
            zRe = Math.abs(zRe);
            zIm = Math.abs(zIm);
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
