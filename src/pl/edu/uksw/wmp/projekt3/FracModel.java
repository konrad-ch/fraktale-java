package pl.edu.uksw.wmp.projekt3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import pl.edu.uksw.wmp.Colors.ColorTypes;
import pl.edu.uksw.wmp.Colors.FracColors;
import pl.edu.uksw.wmp.projekt3.Fractals.*;

/**
 * Model logiczny aplikacji.
 * @author Konrad Chrząszcz
 */

public class FracModel {
    private Fractals fracEngine;
    private Mandelbrot mandelbrot;
    private Mandelbar mandelbar;
    private Julia julia;
    private JuliaPhoenix juliaPhoenix;
    private MandelbrotPhoenix mandelbrotPhoenix;
    private BirdOfPrey birdOfPrey;
    private FracColors fColors;
   
    private BufferedImage fractalImage; //przechowuje wygenerowaną grafike fraktala
    private boolean isDrawingInterrupted; //okresla czy rysowanie zostalo przerwane przez uzytkownika
    private FractalTypes selectedFractalType; //przechowuje wybrany typ fraktala
    private int currentMouseX; 
    private int currentMouseY; 
    private int startMouseX; 
    private int startMouseY;
    private Dimension fracSize; //rozmiar panelu z rysunkiem
    private boolean isZoomClick; //czy zostal uzyty klikany zoom
  
    private int coordX; //wspolrzedna x wybranego punktu
    private int coordY; //wspolrzedna y wybranego punktu
    private boolean isJuliaMode; 
    
    private ColorTypes selectedColorType; //przechowuje aktualnie wybrany kolor

    FracModel(){
        fracEngine = new Fractals();
        mandelbrot = new Mandelbrot();
        mandelbar = new Mandelbar();
        julia = new Julia();
        juliaPhoenix = new JuliaPhoenix();
        mandelbrotPhoenix = new MandelbrotPhoenix();
        birdOfPrey = new BirdOfPrey();
        fColors = new FracColors();
        isDrawingInterrupted = false;
        selectedFractalType = FractalTypes.MANDELBROT;
        isZoomClick = false;
        coordX = 0;
        coordY = 0;
        isJuliaMode = false;
        selectedColorType = ColorTypes.NIEBIESKI;
    }
    
    /**
     * Generuje graficzną reprezentację fraktala
     * @param dim wymiary generowanej grafiki
     * @return graficzna reprezentacja fraktala
     * @throws InterruptedException przerwanie rysowania przez użytkownika.
     */
    public BufferedImage getFractalGraphics(Dimension dim) throws InterruptedException{
        
        fractalImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB); 
        Graphics bimgGraphics = fractalImage.createGraphics();
        Color color;
        
        fracEngine.changeRelativePixelSize(dim);
        
        if(!isZoomClick)
        {
            //przesuniecie x/y
            fracEngine.setCurrentVisibleAreaX(-fracEngine.getCurrentVisibleAreaX()*fracEngine.getRelPixSize());
            fracEngine.setCurrentVisibleAreaY(fracEngine.getCurrentVisibleAreaY()*fracEngine.getRelPixSize());
            //polozenie fraktala (wysrodkowanie)
            double mX = dim.width > dim.height ? 2. * fracEngine.getRelPixSize() * (dim.width - dim.height) : 0.0;
            double mY = dim.height > dim.width ? 2. * fracEngine.getRelPixSize() * (dim.height - dim.width) : 0.0;  
            fracEngine.setSX(mX);
            fracEngine.setSY(mY);
        }
        
        if((selectedFractalType == FractalTypes.JULIA) && isJuliaMode){
            julia.changePRe(coordX);
            julia.changePIm(coordY);
            isJuliaMode = false;
        } else
        if((selectedFractalType == FractalTypes.JULIA_PHOENIX) && isJuliaMode){
            juliaPhoenix.changePRe(0.56667);
            juliaPhoenix.changePIm(-0.5);
            isJuliaMode = false;
        }

        for (int y = 0; y < dim.height; y++) {
            for (int x = 0; x < dim.width; x++) {
                if(isDrawingInterrupted){ //przerwanie rysowania przez uzytkownika
                    throw new InterruptedException();
                }

                if(selectedFractalType==FractalTypes.MANDELBROT){
                    mandelbrot.changePRe(x);
                    mandelbrot.changePIm(y);
                    mandelbrot.setZRe(0.);
                    mandelbrot.setZIm(0.);
                    color = fColors.computeColor(mandelbrot.calcValue(), selectedColorType, fracEngine.getIsSmoothed());             
                }
                else if(selectedFractalType==FractalTypes.MANDELBAR){
                    mandelbar.changePRe(x);
                    mandelbar.changePIm(y);
                    mandelbar.setZRe(0.);
                    mandelbar.setZIm(0.);
                    color = fColors.computeColor(mandelbar.calcValue(), selectedColorType, fracEngine.getIsSmoothed());             
                }
                else if(selectedFractalType==FractalTypes.JULIA){
                    julia.changeZRe(x);
                    julia.changeZIm(y);
                    color = fColors.computeColor(julia.calcValue(), selectedColorType, fracEngine.getIsSmoothed());
                }
                else if(selectedFractalType==FractalTypes.JULIA_PHOENIX){
                    juliaPhoenix.changeZRe(x);
                    juliaPhoenix.changeZIm(y);
                    color = fColors.computeColor(juliaPhoenix.calcValue(), selectedColorType, fracEngine.getIsSmoothed());
                }
                else if(selectedFractalType==FractalTypes.MANDELBROT_PHOENIX){
                    mandelbrotPhoenix.changePRe(x);
                    mandelbrotPhoenix.changePIm(y);
                    mandelbrotPhoenix.setZRe(0.);
                    mandelbrotPhoenix.setZIm(0.);
                    color = fColors.computeColor(mandelbrotPhoenix.calcValue(), selectedColorType, fracEngine.getIsSmoothed());             
                }
                else if(selectedFractalType==FractalTypes.BIRD_OF_PREY){
                    birdOfPrey.changePRe(x);
                    birdOfPrey.changePIm(y);
                    birdOfPrey.setZRe(0.);
                    birdOfPrey.setZIm(0.);
                    color = fColors.computeColor(birdOfPrey.calcValue(), selectedColorType, fracEngine.getIsSmoothed());             
                }
                else color = null;
                
                bimgGraphics.setColor(color);
                bimgGraphics.drawLine(x, y, x, y); 
            }
        }
 
        return fractalImage;
    }

//gettery
    public int getStartMouseX(){
        return startMouseX;
    }
    
    public int getStartMouseY(){
        return startMouseY;
    }
    
    public double getZoomLevel(){
        return fracEngine.getZoomLevel();
    }
    
    public boolean getIsZoomClick(){
        return isZoomClick;
    }
    
//settery
    public void setCurrentVisibleAreaX(double x){
        fracEngine.setCurrentVisibleAreaX(x);
    }
    
    public void setCurrentVisibleAreaY(double y){
        fracEngine.setCurrentVisibleAreaY(y);
    }
   
    public void setPowerLevel(int power){
        fracEngine.setPowerLevel(power);
    }
    
    public void setCurrentMouseX(int cmx){
        currentMouseX = cmx;
    }
    
    public void setCurrentMouseY(int cmy){
        currentMouseY = cmy;
    }
    
    public void setStartMouseX(int smx){
        startMouseX = smx;
    }
    
    public void setStartMouseY(int smy){
        startMouseY = smy;
    }
    
    public void setIsZoomClick(boolean zc){
        isZoomClick = zc;
    }
    
    public void setIsSmoothed(boolean sm){
        fracEngine.setIsSmoothed(sm);
    }
    
    public void setMaxIterations(int it){
        fracEngine.setMaxIterations(it);
    }
    
    public void setFracSize(Dimension dim){
        fracSize = dim;
    }
    
    /**
     * Ustawia wybrany kolor fraktala
     * @param color wybrany kolor.
     */
    public void setSelectedColor(String color){
        selectedColorType = ColorTypes.valueOf(color.toUpperCase());
    }
    
    /**
     * Ustawia wybrany fraktal
     * @param fractal wybrany fraktal.
     */
    public void setSelectedFractal(String fractal){     
        selectedFractalType = FractalTypes.valueOf(fractal.toUpperCase().replace(' ', '_'));

        if((selectedFractalType == FractalTypes.JULIA || selectedFractalType == FractalTypes.JULIA_PHOENIX))
            isJuliaMode = true;
    }
    
    /**
     * Ustala czy przerwano proces rysowania
     * @param status true - przerwano, false - nie przerwano.
     */
    public void setIsDrawingInterrupted(boolean status){
        isDrawingInterrupted = status;
    }
    
    /**
     * Ustawia wybraną współrzędną x
     * @param x wsp. x
     */
    public void setCoordX(int x){
        coordX = x ;
    }
    
    /**
     * Ustawia wybraną współrzędną y
     * @param x wsp. y
     */
    public void setCoordY(int y){
        coordY = y;
    }
    
//inne metody    
    
    /**
     * Zmienia poziom zoom
     * @param zoom poziom zoom.
     */
    public void changeZoomLevel(double zoom){
        fracEngine.changeZoomLevel(zoom);
    }

    /**
     * Powiększa wybrany obszar.
     */
    public void incZoomLevelArea(){
        fracEngine.saveCurrentSettings();
        isZoomClick = true;
        fracEngine.incAreaZoomLevel(currentMouseX, currentMouseY, startMouseX, startMouseY, fracSize);
    }
    
    /**
     * Przybliża obszar względem wybranego punktu.
     */
    public void incZoomLevelOneClick(){
        fracEngine.saveCurrentSettings();
        isZoomClick = true;
        fracEngine.incOneClickZoomLevel(currentMouseX, currentMouseY, fracSize);
    }
    
    /**
     * Oddala obszar względem wybranego punktu.
     */
    public void decZoomLevelOneClick(){
        fracEngine.saveCurrentSettings();
        isZoomClick = true;
        fracEngine.decOneClickZoomLevel(currentMouseX, currentMouseY, fracSize);
    }
    
    public double convertRealToRelativeX(int x){
        return fracEngine.convertRealToRelativeX(x);
    }
    
    public double convertRealToRelativeY(int y){
        return fracEngine.convertRealToRelativeX(y);
    }
   
    public void undoViewChanges(){
        isZoomClick = false;
        fracEngine.saveCurrentSettings();
        fracEngine.setDefaultSettings();
    }
    
    public void redoViewChanges(){
        fracEngine.redoViewChanges();
    }

    public Dimension[] calcRectangleSizes(){
        return fracEngine.calcRectangleSizes(startMouseX, startMouseY, currentMouseX, currentMouseY, fracSize);
    }
    
}
