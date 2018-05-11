package pl.edu.uksw.wmp.projekt3;

import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.MouseInputAdapter;

/**
 * Klasa kontroler - zapewnia komunikację pomiędzy GUI a logiką.
 * @author Konrad Chrząszcz
 */
public final class FracController {
    
    private FracModel fModel;
    private FracView fView;
    private About about;
    private boolean isDrawing; //okresla czy program jest w trakcie rysowania
    private int selectedX; //wybrana wspolrzedna x
    private int selectedY; //wybtana wspolrzedna y
    
    FracController(FracModel fracModel, FracView fracView){  
        fModel = fracModel;
        fView = fracView;
        about = new About();
        isDrawing = false;

        //listeners
        fView.addMenuItemResetListener(new menuItemResetListener());
        fView.addMenuItemCloseListener(new menuItemCloseListener());
        fView.addMenuItemDrawListener(new menuItemDrawListener());
        fView.addMenuItemCancelDrawingListener(new menuItemCancelDrawingListener());
        fView.addMenuItemHelpContentsListener(new menuItemHelpContentsListener());
        fView.addMenuItemAboutListener(new menuItemAboutListener());
        
        fView.addButtonDrawListener(new buttonDrawListener());
        fView.addButtonCancelListener(new buttonCancelDrawingListener());
        fView.addButtonResetSettingsListener(new buttonSetDefaultsListener());
        fView.addMouseActionListener(new mouseActionListener());
        fView.addMouseMovementListener(new mouseMovementListener());
        fView.addFractalPanelChangeListener(new fractalPanelChangeListener());
        
        //rysowanie domyslnego fraktala
        if(parseSettings(false))
            createFractalImage();
    }

    class menuItemResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            fView.resetSettings();
        }
    }
    
    class menuItemCloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            System.exit(0);
        }       
    }
    
    class menuItemDrawListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if(parseSettings(true))
                createFractalImage();
        }
    }
    
    class menuItemCancelDrawingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            fModel.setIsDrawingInterrupted(true);
        }
    }
    
    class menuItemHelpContentsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            JOptionPane.showMessageDialog(fView,
                "Lewy przycisk myszki - przybliżenie względem punktu\n"
                    + "Prawy przycisk myszki - oddalenie względem punktu\n"
                    + "Przeciąganie myszki z wciśniętym LPM - przybliżenie obszaru\n"
                    + "Środkowy przycisk myszki / ctrl + LPM - pobranie współrzędnych danego punktu",
                "Pomoc",
                JOptionPane.PLAIN_MESSAGE);
        }
        
    }
    
    class menuItemAboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            about.setVisible(true);
        }
        
    }
    
    class fractalPanelChangeListener implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent ce){
            if(fView.getIsRedrawAfterResize() && !isDrawing){
                if(parseSettings(false))
                    createFractalImage();
            }
        }

        @Override
        public void componentMoved(ComponentEvent ce) {}

        @Override
        public void componentShown(ComponentEvent ce) {}

        @Override
        public void componentHidden(ComponentEvent ce) {}
    }

    class mouseMovementListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent me) {
            if(!isDrawing && fModel.getIsZoomClick()){
                fModel.setCurrentMouseX(me.getX());
                fModel.setCurrentMouseY(me.getY());
                fView.setZoomRectangleCoords(fModel.calcRectangleSizes()[0]);
                fView.setZoomRectangleSizes(fModel.calcRectangleSizes()[1]);
                fView.fractalImageRefresh(); //rysowanie granic obszaru do powiekszenia
            }
        }
        @Override
        public void mouseMoved(MouseEvent me) {}
    }
    
    class mouseActionListener extends MouseInputAdapter {
        @Override
        public void mousePressed(MouseEvent me) {
            fModel.setFracSize(fView.getPanelFractalSize());
            if(!isDrawing){
                if((me.getModifiers() & InputEvent.BUTTON1_MASK)!=0 || (me.getModifiers() & InputEvent.BUTTON3_MASK)!=0)
                {
                    fModel.setIsZoomClick(true);
                    fView.setIsMouseDragging(true);
                }
                fModel.setCurrentMouseX(me.getX());
                fModel.setCurrentMouseY(me.getY());
                fModel.setStartMouseX(me.getX());
                fModel.setStartMouseY(me.getY());
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent me){
            fView.setIsMouseDragging(false);
            if(me.getX() == fModel.getStartMouseX() && me.getY() == fModel.getStartMouseY()) 
            {
                if((me.getModifiers() & InputEvent.BUTTON2_MASK)!=0 || ((me.isControlDown()) && (me.getModifiers() & InputEvent.BUTTON1_MASK)!=0)) //ŚPM - pobranie wspolrzednych
                {
                    fView.setTextSelectedX(fModel.convertRealToRelativeX(me.getX())+"");
                    fView.setTextSelectedY(fModel.convertRealToRelativeY(me.getY())+"");
                    selectedX = me.getX();
                    selectedY = me.getY();
                }     
                else
                if((!isDrawing && fModel.getIsZoomClick()) && (me.getModifiers() & InputEvent.BUTTON1_MASK)!=0) //LPM - zoom[+] jednym kliknieciem
                {
                    fModel.incZoomLevelOneClick(); //przyblizenie wzgl. punktu
                    if(parseSettings(false))
                        createFractalImage();
                }
                else
                if((!isDrawing && fModel.getIsZoomClick()) && (me.getModifiers() & InputEvent.BUTTON3_MASK)!=0) //PPM - zoom[-] jednym kliknieciem
                {
                    fModel.decZoomLevelOneClick(); //oddalenie wzgl. punktu
                    if(parseSettings(false))
                        createFractalImage();
                }
            }
            else
            if(!isDrawing && fModel.getIsZoomClick())
            {
                fModel.incZoomLevelArea(); //przyblizenie obszaru
                if(parseSettings(false))
                    createFractalImage();
            }
        }
    }
    
    class buttonSetDefaultsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            fView.resetSettings();
        }
    }
    
    class buttonCancelDrawingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fModel.setIsDrawingInterrupted(true);
        }
    }
      
    class buttonDrawListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(parseSettings(true))
                createFractalImage();
        }
    }
    
    boolean parseSettings(boolean doParse){
        isDrawing = true;
        if(doParse){
            //usuniecie zmian
            fModel.undoViewChanges();

            //zoom
            try {
                String zoomS = fView.getZoomLevel();
                double zoomD = Double.parseDouble(zoomS);
                if (zoomD <= 0. || zoomD > 10.) {
                    JOptionPane.showMessageDialog(fView, "Podaj wartość zoom większą od 0 i nie większą od 10\nZmieniono na 1", "Błąd", JOptionPane.WARNING_MESSAGE);
                    fModel.changeZoomLevel(1.);
                } else {
                    fModel.changeZoomLevel(zoomD);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(fView, "Podano nieprawidłową wartość zoom\n\nTreść błędu:\n" + nfe.toString(), "Błąd", JOptionPane.ERROR_MESSAGE);
                fModel.redoViewChanges();
                return false;
            }
            
            //pozycja
            try {
                String posXs = fView.getPosX0();
                String posYs = fView.getPosY0();

                double posXd = Double.parseDouble(posXs);
                double posYd = Double.parseDouble(posYs);

                fModel.setCurrentVisibleAreaX(posXd);
                fModel.setCurrentVisibleAreaY(posYd);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(fView, "Podano nieprawidłową wartość pozycji początkowej\n\nTreść błędu:\n" + nfe.toString(), "Błąd", JOptionPane.ERROR_MESSAGE);
                fModel.redoViewChanges();
                return false;
            }

            //wybor fraktala
            String selectedFractal = fView.getSelectedFractal();
            fModel.setSelectedFractal(selectedFractal);
            
            //wspolrzedne
            fModel.setCoordX(selectedX);
            fModel.setCoordY(selectedY);
          
            //wygladzenie obrazu
            fModel.setIsSmoothed(fView.getIsSmoothedImage());

            //wybor koloru
            String selectedColor = fView.getSelectedColor();
            fModel.setSelectedColor(selectedColor);

            //iteracje
            fModel.setMaxIterations(fView.getMaxIterations());

            //pierwiastek
            fModel.setPowerLevel(fView.getPowerLevel());
        }
        //zmiana stanu buttonów i elementow menu
        fView.setMenuItemDrawStatus(false);
        fView.setMenuItemCancelDrawingStatus(true);
        fView.setButtonDrawStatus(false);
        fView.setButtonDrawText("Rysowanie...");
        fView.setButtonCancelStatus(true);
        fView.setButtonCancelTextColor(Color.red);
    
        return true;
    }
    
    void createFractalImage() {
        SwingWorker<BufferedImage, Void> sw =
                new SwingWorker<BufferedImage, Void>() {

                    @Override
                    protected BufferedImage doInBackground() throws InterruptedException {
                        BufferedImage fGraphics = fModel.getFractalGraphics(fView.getPanelFractalSize());
                        return fGraphics;
                    }                
                    
                    @Override
                    protected void done() {
                        //zmiana stanu buttonów i elementow menu
                        fView.setMenuItemDrawStatus(true);
                        fView.setMenuItemCancelDrawingStatus(false);
                        fView.setButtonDrawStatus(true);
                        fView.setButtonDrawText("Rysuj fraktal");
                        fView.setButtonCancelStatus(false);
                        fView.setButtonCancelTextColor(null);
                        isDrawing = false;
                        try {
                            fView.setFractalImage(get());
                        } catch (InterruptedException | ExecutionException ex) {
                                fModel.setIsDrawingInterrupted(false);
                                fModel.redoViewChanges();
                                fView.fractalImageRefresh();
                        }
                    }
                };
        sw.execute();
    }
}

