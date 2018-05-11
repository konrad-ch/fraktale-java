/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

import java.awt.Dimension;

/**
 *
 * @author Elenthar
 */

public class Fractals {

    //glowne atrybuty
    protected static int maxIterations; //maksymalna liczba iteracji
    protected static int powerLevel; //potega
    protected static double relPixSize; //wzgledny rozmiar pikseli
    protected double zoomLevel; //poziom zoom
    protected boolean isSmoothed; //okresla czy obraz ma byc wygladzany
    protected static double visibleX; //wzgledny rozmiar obrazka, szerokosc
    protected static double visibleY; //wzgledny rozmiar obrazka, wysokosc
    protected static double mX; //przesuniecie x
    protected static double mY; //przesuniecie y

    //glowne atrybuty - redo
    private int redoMaxIterations;
    private int redoPowerLevel;
    private double redoRelPixSize;
    private double redoZoomLevel;
    private boolean redoIsSmoothed;
    private double redoViewX;
    private double redoViewY;
    private double redoSx;
    private double redoSy;

    public Fractals(){
        maxIterations = 128;
        redoMaxIterations = maxIterations;
        powerLevel = 2;
        redoPowerLevel = powerLevel;
        relPixSize = 1.;
        redoRelPixSize = relPixSize;
        zoomLevel = 1.;
        redoZoomLevel = zoomLevel;
        isSmoothed = false;
        redoIsSmoothed = isSmoothed;
        visibleX = 0.;
        visibleY = 0.;
        redoViewX = visibleX;
        redoViewY = visibleY;
        mX = 0.;
        mY = 0.;
        redoSx = mX;
        redoSy = mY;

    }

    public void setDefaultSettings(){
        maxIterations = 128;
        powerLevel = 2;
        relPixSize = 0.;
        zoomLevel = 1.;
        isSmoothed = false;
        visibleX = 0.;
        visibleY = 0.;
        mX = 0.;
        mY = 0.;
    }

    public void saveCurrentSettings(){
        redoMaxIterations = maxIterations;
        redoPowerLevel = powerLevel;
        redoRelPixSize = relPixSize;
        redoZoomLevel = zoomLevel;
        redoIsSmoothed = isSmoothed;
        redoViewX = visibleX;
        redoViewY = visibleY;
        redoSx = mX;
        redoSy = mY;
    }

    public void redoViewChanges(){
        maxIterations = redoMaxIterations;
        powerLevel = redoPowerLevel;
        relPixSize = redoRelPixSize;
        zoomLevel = redoZoomLevel;
        isSmoothed = redoIsSmoothed;
        visibleX = redoViewX;
        visibleY = redoViewY;
        mX = redoSx;
        mY = redoSy;
    }

    public void incOneClickZoomLevel(int currentMouseX, int currentMouseY, Dimension fracSize){
        visibleX = visibleX + (0.5 * (currentMouseX) * relPixSize);
        visibleY = visibleY + (0.5 * (currentMouseY) * relPixSize);
        zoomLevel = zoomLevel * 0.5;
    }

    public void decOneClickZoomLevel(int currentMouseX, int currentMouseY, Dimension fracSize){
        visibleX = visibleX - (0.5 * (currentMouseX) * relPixSize);
        visibleY = visibleY - (0.5 * (currentMouseY) * relPixSize);
        zoomLevel = zoomLevel * 1.5;
    }

    public void incAreaZoomLevel(int currentMouseX, int currentMouseY, int startMouseX, int startMouseY, Dimension fracSize){
        int msX = Math.min(currentMouseX, startMouseX);
        int msY = Math.min(currentMouseY, startMouseY);
        visibleX += msX * relPixSize;
        visibleY += msY * relPixSize;
        double w = currentMouseX + startMouseX - 2. * msX;
        double h = currentMouseY + startMouseY - 2. * msY;
        zoomLevel *= Math.max(w / fracSize.getWidth(), h / fracSize.getHeight());
    }

    public void changeZoomLevel(double zoom){
        zoomLevel = (1./zoom);
    }

    public double convertRealToRelativeX(int realX){
        return 4. * (realX * relPixSize + visibleX) - 2. - mX;
    }

    public double convertRealToRelativeY(int realY){
        return  -4. * (realY * relPixSize + visibleY) + 2. + mY;
    }

    public Dimension[] calcRectangleSizes(int startMouseX, int startMouseY, int currentMouseX, int currentMouseY, Dimension fracSize){
        int x = Math.min(startMouseX, currentMouseX);
        int y = Math.min(startMouseY, currentMouseY);
        double w = startMouseX + currentMouseX - 2. * x;
        double h = startMouseY + currentMouseY - 2. * y;
        double r = Math.max(w / fracSize.width, h / fracSize.height);
        Dimension[] result = {new Dimension(x,y),new Dimension((int)(fracSize.width * r),(int)(fracSize.height * r))};

        return result;
    }

    public void changeRelativePixelSize(Dimension dim){
        relPixSize = zoomLevel / Math.min(dim.width, dim.height);
    }

    //settery
    public void setMaxIterations(int it){
        maxIterations = it;
    }

    public void setPowerLevel(int power){
        powerLevel = power;
    }

    public void setIsSmoothed(boolean sm){
        isSmoothed = sm;
    }

    public void setCurrentVisibleAreaX(double x){
        visibleX = x;
    }

    public void setCurrentVisibleAreaY(double y){
        visibleY = y;
    }

    public void setSX(double x){
        mX = x;
    }

    public void setSY(double y){
        mY = y;
    }

    //gettery
    public double getZoomLevel(){
        return zoomLevel;
    }

    public double getRelPixSize(){
        return relPixSize;
    }

    public boolean getIsSmoothed(){
        return isSmoothed;
    }

    public double getCurrentVisibleAreaX(){
        return visibleX;
    }

    public double getCurrentVisibleAreaY(){
        return visibleY;
    }

}