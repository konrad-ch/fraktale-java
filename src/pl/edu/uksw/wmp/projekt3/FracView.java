package pl.edu.uksw.wmp.projekt3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import pl.edu.uksw.wmp.Colors.ColorTypes;
import pl.edu.uksw.wmp.projekt3.FractalTypes;

/**
 * GUI
 * @author Konrad Chrząszcz
 */

public class FracView extends JFrame {
    
    FracModel fModel;
    BufferedImage fractalImage;
    
    boolean isMouseDragging; //czy uzytkownik korzysta z obszarowego zooma
    Dimension zoomRectangleCoords;
    Dimension zoomRectangleSizes;
    
  //komponenty
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("Plik");
    JMenuItem menuItemReset = new JMenuItem("Resetuj ustawienia");
    JMenuItem menuItemClose = new JMenuItem("Zakończ");
    JMenu menuFractal = new JMenu("Fraktal");
    JMenuItem menuItemDraw = new JMenuItem("Rysuj fraktal");
    JMenuItem menuItemCancelDrawing = new JMenuItem("Anuluj rysowanie");
    JMenu menuHelp = new JMenu("Pomoc");
    JMenuItem menuItemHelpContents = new JMenuItem("Pomoc dla programu");
    JMenuItem menuItemAbout = new JMenuItem("O programie...");
    
    JPanel panelContent = new JPanel(); //glowny panel - rodzic
    JPanel panelSettings = new JPanel(); //panel ustawien uzytkownika
    JPanel panelSettingsTop = new JPanel();
    JPanel panelSettingsBottom = new JPanel();
    JPanel panelSettingsCommon = new JPanel(); //ustawienia podstawowe
    JPanel panelSettingsOther = new JPanel(); //inne ustawienia
    PanelFractal panelFractal = new PanelFractal(); //panel z obiektem fraktala
    
    JLabel labelFractalSelect = new JLabel("Wybierz fraktal:");
    JComboBox comboFractalSelect = new JComboBox();
    JLabel labelFractalColor = new JLabel("Wybierz kolor:");
    JComboBox comboFractalColor = new JComboBox();
    
    JCheckBox checkSmoothedImage = new JCheckBox("Wygładź obraz");
    JCheckBox checkRedrawAfterResize = new JCheckBox("Przerysowuj po zmianie wielkości okna");
    
    JLabel labelZoomLevel = new JLabel("Zoom:");
    JTextField textZoomLevel = new JTextField();
    
    JLabel labelMaxIterations = new JLabel("Maksymalna liczba iteracji:");
    SpinnerModel smmIterations = new SpinnerNumberModel(128, 2, 10000, 1); //start, min, max, step
    JSpinner spinnerMaxIterations = new JSpinner(smmIterations);
   
    JLabel labelPowerLevel = new JLabel("Potęga:");
    SpinnerModel smmPower = new SpinnerNumberModel(2, 2, 50, 1); //start, min, max, step
    JSpinner spinnerPowerLevel = new JSpinner(smmPower);
    
    JLabel labelPosX0 = new JLabel("Przesunięcie po osi x:");
    JTextField textPosX0 = new JTextField("0.0");
    JLabel labelPosY0 = new JLabel("Przesunięcie po osi y:");
    JTextField textPosY0 = new JTextField("0.0");
    
    JLabel labelSelectedX = new JLabel("Wybrana współrzędna x:");
    JTextField textSelectedX = new JTextField("0.0");
    JLabel labelSelectedY = new JLabel("Wybrana współrzędna y:");
    JTextField textSelectedY = new JTextField("0.0");
    
    JButton buttonDrawFractal = new JButton("Rysuj fraktal");
    JButton buttonCancelDrawing = new JButton("Anuluj rysowanie");
    JButton buttonResetSettings = new JButton("Resetuj ustawienia");
    
    private class PanelFractal extends JPanel{
        @Override
        public void paint(Graphics g){ 
            g.drawImage(fractalImage, 0, 0, getPanelFractalSize().width, getPanelFractalSize().height, null);
            if(isMouseDragging){
                g.setXORMode(Color.red);
                g.drawRect(zoomRectangleCoords.width, zoomRectangleCoords.height, zoomRectangleSizes.width, zoomRectangleSizes.height);
            }
        }
    }

    FracView(FracModel fracModel){
        fModel = fracModel;
        isMouseDragging = false;
        //parametry komponentow
        this.setTitle("Projekt 3 :: Program rysujący fraktale");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setMinimumSize(new Dimension(480, 320)); //minimalny rozmiar okienkat
        menuFile.setMnemonic(KeyEvent.VK_P);
        menuFractal.setMnemonic(KeyEvent.VK_F);
        menuItemHelpContents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));

        menuHelp.setMnemonic(KeyEvent.VK_C);
        menuBar.add(menuFile);
        menuBar.add(menuFractal);
        menuBar.add(menuHelp);
        menuItemReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menuItemClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menuFile.add(menuItemReset);
        menuFile.add(menuItemClose);
        menuItemDraw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        menuItemCancelDrawing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuFractal.add(menuItemDraw);
        menuFractal.add(menuItemCancelDrawing);
        
        menuHelp.add(menuItemHelpContents);
        menuHelp.add(menuItemAbout);
        this.setJMenuBar(menuBar);
        panelFractal.setPreferredSize(new Dimension(480,340)); //domyslny rozmiar fraktala
        panelSettings.setMaximumSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 250)); //maksymalny rozmiar panelu z ustawieniami
        panelSettings.setMinimumSize(new Dimension(0, 240)); //minimalny rozmiar panelu z ustawieniami
        panelSettingsCommon.setBorder(BorderFactory.createTitledBorder("Ustawienia podstawowe"));
        panelSettingsOther.setBorder(BorderFactory.createTitledBorder("Inne ustawienia"));
        panelSettingsBottom.setBorder(BorderFactory.createEtchedBorder());
        panelSettingsBottom.setMaximumSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 50));
        for(FractalTypes ftype : FractalTypes.values()) //typy fraktali
            comboFractalSelect.addItem(ftype.toString());
        for(ColorTypes ctype : ColorTypes.values()) //dostepny kolory
            comboFractalColor.addItem(ctype.toString());
        textZoomLevel.setText(fModel.getZoomLevel()+""); //domyslny zoom
        buttonCancelDrawing.setEnabled(false); 
        checkRedrawAfterResize.setSelected(true);
        //uklad komponentow  
        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.PAGE_AXIS));
        panelSettings.setLayout(new BoxLayout(panelSettings, BoxLayout.PAGE_AXIS));
        panelSettingsTop.setLayout(new GridLayout(0, 2));
        panelSettingsCommon.setLayout(new GridBagLayout());
        panelSettingsOther.setLayout(new GridBagLayout());
        panelContent.add(panelFractal);
        panelContent.add(panelSettings);
        panelSettings.add(panelSettingsTop);
        panelSettings.add(panelSettingsBottom);
        panelSettingsTop.add(panelSettingsCommon);
        panelSettingsTop.add(panelSettingsOther);
        panelSettingsBottom.add(buttonDrawFractal);
        panelSettingsBottom.add(buttonCancelDrawing);
        panelSettingsBottom.add(buttonResetSettings);
        panelSettingsCommon.add(labelFractalSelect, setComponentPos(0,0,5,1));
        panelSettingsCommon.add(comboFractalSelect, setComponentPos(1,0,5,1));
        panelSettingsCommon.add(labelFractalColor, setComponentPos(0,1,5,1));
        panelSettingsCommon.add(comboFractalColor, setComponentPos(1,1,5,1));
        panelSettingsCommon.add(textZoomLevel, setComponentPos(1,2,5,1));
        panelSettingsCommon.add(labelZoomLevel, setComponentPos(0,2,5,1));
        panelSettingsCommon.add(checkSmoothedImage, setComponentPos(0,3,5,1));
        panelSettingsCommon.add(checkRedrawAfterResize, setComponentPos(0,4,5,2));
        panelSettingsOther.add(labelMaxIterations, setComponentPos(0,0,5,1));
        panelSettingsOther.add(spinnerMaxIterations, setComponentPos(1,0,5,1));
        panelSettingsOther.add(labelPowerLevel, setComponentPos(0,1,5,1));
        panelSettingsOther.add(spinnerPowerLevel, setComponentPos(1,1,5,1));
        panelSettingsOther.add(labelSelectedX,setComponentPos(0,2,5,1));
        panelSettingsOther.add(textSelectedX,setComponentPos(1,2,5,1));
        panelSettingsOther.add(labelSelectedY,setComponentPos(0,3,5,1));
        panelSettingsOther.add(textSelectedY,setComponentPos(1,3,5,1));
        panelSettingsOther.add(labelPosX0, setComponentPos(0,4,5,1));
        panelSettingsOther.add(textPosX0, setComponentPos(1,4,5,1));
        panelSettingsOther.add(labelPosY0, setComponentPos(0,5,5,1));
        panelSettingsOther.add(textPosY0, setComponentPos(1,5,5,1));
        this.setContentPane(panelContent);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
//metody  
    private GridBagConstraints setComponentPos(int x, int y, int insets, int gridwidth) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(insets, insets, insets, insets);
        c.gridx = x;
        c.gridy = y;
        c.weighty = 1;
        c.gridwidth = gridwidth;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }
    
    
//gettery
    
    /**
     * Zwraca maksymalną ilość iteracji
     * @return maksymalna ilość iteracji.
     */
    int getMaxIterations(){
        return (int)spinnerMaxIterations.getValue();
    }
    
    /**
     * Zwraca przesunięcie po osi x
     * @return przesunięcie po osi x.
     */
    String getPosX0(){
        return textPosX0.getText().replace(',', '.');
    }
     
    /**
     * Zwraca przesunięcie po osi y
     * @return przesunięcie po osi y.
     */
    String getPosY0(){
        return textPosY0.getText().replace(',', '.');
    }
    
    /**
     * Zwraca wybrany punkt x
     * @return wybrany punkt x.
     */
    String getTextSelectedX(){
        return textSelectedX.getText().replace(',', '.');
    }

    /**
     * Zwraca wybrany punkt y
     * @return wybrany punkt y.
     */
    String getTextSelectedY(){
        return textSelectedY.getText().replace(',', '.');
    }
    
    /**
     * Zwraca wartość potęgi
     * @return wartość potęgi.
     */
    int getPowerLevel(){
        return (int)spinnerPowerLevel.getValue();
    }
    
    /**
     * Zwraca wybrany kolor
     * @return wybrany kolor.
     */
    String getSelectedColor(){
        return comboFractalColor.getSelectedItem().toString();
    }
    
    /**
     * Zwraca wybrany fraktal
     * @return wybrany fraktal.
     */
    String getSelectedFractal(){
        return comboFractalSelect.getSelectedItem().toString();
    }
    
    /**
     * Zwraca czy rysunek fraktala ma być poddany wygładzeniu kolorów
     * @return true jeżeli zaznaczono opcję, false w przeciwnym wypadku.
     */
    boolean getIsSmoothedImage(){
        return checkSmoothedImage.isSelected();  
    }
    
    /**
     * Zwraca czy przerysowywać fraktal po zmianie rozmiaru okna aplikacji
     * @return true jeżeli zaznaczono opcję, false w przeciwnym wypadku.
     */
    boolean getIsRedrawAfterResize(){
        return checkRedrawAfterResize.isSelected();
    }
    
    /**
     * Zwraca poziom przybliżenia obrazu
     * @return poziom zoom
     */
    String getZoomLevel(){
        return textZoomLevel.getText().replace(',', '.');
    }
    
    /**
     * Zwraca rozmiar panelu z rysunkiem fraktala
     * @return rozmiar panelu
     */
    Dimension getPanelFractalSize(){
        return panelFractal.getSize();
    }
       
//listenery
    
    //menu-->
    void addMenuItemResetListener(ActionListener al){
        menuItemReset.addActionListener(al);
    }
    
    void addMenuItemCloseListener(ActionListener al){
        menuItemClose.addActionListener(al);
    }
    
    void addMenuItemDrawListener(ActionListener al){
        menuItemDraw.addActionListener(al);
    }
    
    void addMenuItemCancelDrawingListener(ActionListener al){
        menuItemCancelDrawing.addActionListener(al);
    }
    
    void addMenuItemHelpContentsListener(ActionListener al){
        menuItemHelpContents.addActionListener(al);
    }
    
    void addMenuItemAboutListener(ActionListener al){
        menuItemAbout.addActionListener(al);
    }
    //<--menu
    
    void addButtonDrawListener(ActionListener al){
        buttonDrawFractal.addActionListener(al);     
    }
    
    void addButtonCancelListener(ActionListener al){
        buttonCancelDrawing.addActionListener(al);
    }
    
    void addButtonResetSettingsListener(ActionListener al){
        buttonResetSettings.addActionListener(al);
    }
    
    void addMouseActionListener(MouseInputAdapter mia){
        panelFractal.addMouseListener(mia);
    }
    
    void addMouseMovementListener(MouseMotionListener mml){
        panelFractal.addMouseMotionListener(mml);
    }
    /*
    void addSettingsChangeListener(ChangeListener cl){
        spinnerPowerLevel.addChangeListener(cl);
    }
    */
    void addFractalPanelChangeListener(ComponentListener cl){
        panelFractal.addComponentListener(cl);
    }

//settery
    
    
    void setMenuItemDrawStatus(boolean status){
        menuItemDraw.setEnabled(status);
    }
    
    void setMenuItemCancelDrawingStatus(boolean status){
        menuItemCancelDrawing.setEnabled(status);
    }
    
    /**
     * Wyświetla wybrany punkt x
     * @param x wybrany punkt x.
     */
    void setTextSelectedX(String x){
        textSelectedX.setText(String.format("%.6s", x));
    }
    
    /**
     * Wyświetla wybrany punkt y
     * @param y wybrany punkt y.
     */
    void setTextSelectedY(String y){ 
        textSelectedY.setText(String.format("%.6s", y));
    }
    
    /**
     * Ustawia tekst wyświetlany na przycisku rysowania
     * @param text tekst.
     */
    void setButtonDrawText(String text){
        buttonDrawFractal.setText(text);
    }
    
    /**
     * Ustawia status przycisku - aktywny lub nieaktywny
     * @param status status aktywnosci, true - aktywny, false - nieaktywny.
     */
    void setButtonDrawStatus(boolean status){
        buttonDrawFractal.setEnabled(status);
    }
    
    /**
     * Ustawia status przycisku - aktywny lub nieaktywny
     * @param status status aktywnosci, true - aktywny, false - nieaktywny.
     */
    void setButtonCancelStatus(boolean status){
        buttonCancelDrawing.setEnabled(status);
    }
    
    /**
     * Ustawia kolor tekstu przycisku
     * @param c wybrany kolor.
     */
    void setButtonCancelTextColor(Color c){
        buttonCancelDrawing.setForeground(c);
    }
    
    /**
     * Wyświetla obiekt z grafiką fraktala w panelu
     * @param g obiektt z grafiką 2d.
     */
    void setFractalImage(BufferedImage g){
        fractalImage = g;
        panelFractal.repaint();
    }
    
    /**
     * Ustala czy użytkownik korzysta obecnie z zaznaczania obszaru fraktala
     * @param isdrg true - korzysta, false - nie korzysta.
     */
    void setIsMouseDragging(boolean isdrg){
        isMouseDragging = isdrg;
    }
   
    /**
     * Ustawia współrzędne obszaru zaznaczania
     * @param xy współrzędne.
     */
    void setZoomRectangleCoords(Dimension xy){
        zoomRectangleCoords = xy;
    }
    
    /**
     * Ustawia rozmiary obszaru zaznaczania
     * @param wh rozmiary.
     */
    void setZoomRectangleSizes(Dimension wh){
        zoomRectangleSizes = wh;
    }
 
   
//inne metody
    
     /**
     * Resetuje kontrolki do ustawień początkowych.
     */
    void resetSettings(){
        comboFractalSelect.setSelectedIndex(0);
        comboFractalColor.setSelectedIndex(0);
        textZoomLevel.setText("1.0");
        checkSmoothedImage.setSelected(false);
        checkRedrawAfterResize.setSelected(true);
        spinnerMaxIterations.setValue(new Integer(128));
        spinnerPowerLevel.setValue(new Integer(2));
        textPosX0.setText("0.0");
        textPosY0.setText("0.0");
        textSelectedX.setText("0.0");
        textSelectedY.setText("0.0");
    }
    
    /**
     * Odświeża panel z fraktalem.
     */
    void fractalImageRefresh(){
        panelFractal.repaint();
    }
   
}