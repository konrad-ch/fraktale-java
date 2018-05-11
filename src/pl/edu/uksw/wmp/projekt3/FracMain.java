package pl.edu.uksw.wmp.projekt3;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Klada główna - tworzy i ustawia główne składowe programu. 
 * @author Konrad Chrząszcz
 */
public class FracMain {
  
    private static void createAndShowGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        FracModel model = new FracModel();
        FracView view = new FracView(model);
        FracController controller = new FracController(model, view);

        view.setVisible(true);  
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}
            }
        });
    }
}
