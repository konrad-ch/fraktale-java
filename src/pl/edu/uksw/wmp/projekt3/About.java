package pl.edu.uksw.wmp.projekt3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.*;

/**
 * Klasa wyświetlająca informacje o programie.
 * @author Konrad Chrząszcz
 */

public final class About extends JFrame {
    
    private JPanel panelContent = new JPanel();
    private JPanel panelTop = new JPanel();
    private JPanel panelBottom = new JPanel();
    
    private JLabel labelTitle = new JLabel("Program rysujący fraktale");
    private JLabel labelTitle2 = new JLabel(":: projekt 3 ::");
    private JLabel labelAuthor = new JLabel("Autor:  Konrad Chrząszcz");
    private JLabel labelUKSW = new JLabel("Uniwersytet Kardynała Stefana Wyszyńskiego");
    private JLabel labelUKSW2 = new JLabel("w Warszawie");
    private JButton buttonOK = new JButton("   SUPER !   ");
    
    private JLabel labelGif1 = new JLabel();
    private JLabel labelGif2 = new JLabel();
    
    /**
     * Pobiera obrazek z pliku
     * @param path ścieżka do obrazka
     * @return obrazek.
     */
    private ImageIcon createImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
 
    About(){
        String defaultFont = UIManager.getDefaults().getFont("TabbedPane.font").getName();
        this.setTitle("O programie");
        this.setResizable(false);
        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.Y_AXIS));
        panelTop.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        this.setContentPane(panelContent);
        
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelBottom.setLayout(new BorderLayout());
        
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitle.setFont(new Font(defaultFont, Font.BOLD, 16));
        labelTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelUKSW.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelUKSW2.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonOK.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonOK.setMargin(new Insets(5,10,5,10));
        buttonOK.setFont(new Font(defaultFont, Font.BOLD, 12));
        panelTop.add(Box.createVerticalStrut(20));
        panelTop.add(labelTitle);
        panelTop.add(Box.createVerticalStrut(10));
        panelTop.add(labelTitle2);
        panelTop.add(Box.createVerticalStrut(30));
        panelTop.add(labelAuthor);
        panelTop.add(Box.createVerticalStrut(5));
        panelTop.add(labelUKSW);
        panelTop.add(labelUKSW2);
        panelTop.add(Box.createVerticalStrut(50));
        panelTop.add(buttonOK);
        
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent me) {
                setVisible(false);
            }
        });
        
        final ImageIcon img1b = createImageIcon("img/1b.gif");
        final ImageIcon img1g = createImageIcon("img/1g.gif"); 
        final ImageIcon img2b = createImageIcon("img/2b.gif");
        final ImageIcon img2g = createImageIcon("img/2g.gif"); 
        labelGif1.setIcon(img1b);
        labelGif2.setIcon(img2b);
        labelGif2.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        labelGif1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                labelGif1.setIcon(img1g);
            }
        });
        
        labelGif2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                labelGif2.setIcon(img2g);        
            }
            @Override
            public void mouseExited(MouseEvent me) {
                labelGif2.setIcon(img2b);
            }
        });
        
        panelContent.add(panelTop);
        panelContent.add(panelBottom);
        panelBottom.add(labelGif1, BorderLayout.LINE_END);
        panelBottom.add(labelGif2, BorderLayout.LINE_START);
        
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
}
