/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.uksw.wmp.projekt3;

import java.awt.Color;


/**
 *
 * @author Konrad Chrząszcz
 */

public class FracColors {
       
    private Color[][] colorSet; //przechowuje poszczegolne palety kolorów
    
    //zbior palet kolorow {liczba kolorw, r, g, b}
    
    private int[][][] colorSets = {
        //niebieski + inne kolory teczy :)
        {{12, 0, 0, 102}, {12, 0, 153, 255}, {12, 255, 0, 0},{12, 255, 153, 0}, {12, 255, 255, 0}, {12, 51, 255, 0}, {12, 102, 0, 255}, {12, 204, 102, 255}},
        
        //zielony
        {{8, 0, 51, 0}, {8, 51, 102, 0}, {16, 51, 153, 0}, {24, 51, 204, 0}, {32, 51, 255, 0}, {40, 102, 255, 0}},
        
        //czerwony
        {{16, 102, 0, 0}, {16, 255, 0, 0}, {16, 255, 255, 0}, {16, 255, 153, 0}},
        
        //noir
        {{16, 0, 0, 0},{16, 128, 128, 128}, {16, 255, 255, 255}},
        
        //fajny
        {{12, 0, 0, 0}, {32, 255, 255, 0}}
    };

    FracColors(){      
       
        colorSet = new Color[colorSets.length][];
        for (int palNum = 0; palNum < colorSets.length; palNum++) { //przetworzenie kolejnych palet kolorów z tablicy
            int numColors = 0;
            for (int i = 0; i < colorSets[palNum].length; i++) //pobieranie liczby wszsytkich kolorów
            {
                numColors += colorSets[palNum][i][0];
            }
            colorSet[palNum] = new Color[numColors]; 
            numColors = 0;
            for (int i = 0; i < colorSets[palNum].length; i++) { // interpolacja kolorów
                int[] firstColor = colorSets[palNum][i]; // pierwszy kolor do interpolacji
                int[] secondColor = colorSets[palNum][(i + 1) % colorSets[palNum].length]; // drugi kolor do interpolacji

                for (int j = 0; j < firstColor[0]; j++)
                {
                    //interpolacja liniowa sąsiednich wartośći R,G,B
                    int R = (firstColor[1] * (firstColor[0] - 1 - j) + (secondColor[1] * j) ) / (firstColor[0] - 1);
                    int G = (firstColor[2] * (firstColor[0] - 1 - j) + (secondColor[2] * j) ) / (firstColor[0] - 1);
                    int B = (firstColor[3] * (firstColor[0] - 1 - j) + (secondColor[3] * j) ) / (firstColor[0] - 1);
                    colorSet[palNum][numColors + j] = new Color(R,G,B);
                }
                numColors += firstColor[0];
            }
        }
    }

  
    /**
     * Oblicza kolor dla danej wartosci ze zbioru fraktala
     * @param count dana wartość
     * @param selectedColorType wybrana paleta kolorów
     * @param isSmoothed czy wygladzać obraz
     * @return kolor
     */
    public Color computeColor(int count, ColorTypes selectedColorType, boolean isSmoothed) {
        int palSize = colorSet[selectedColorType.getColorCode()].length;
        Color firstColor = colorSet[selectedColorType.getColorCode()][count / 256 % palSize];

        //wygladzanie obrazu
        if (isSmoothed) {
            Color secondColor = colorSet[selectedColorType.getColorCode()][(count / 256 + palSize - 1) % palSize];
            int k1 = count % 256;
            int k2 = 255 - k1;
            int R = (k1 * firstColor.getRed() + k2 * secondColor.getRed()) / 255;
            int G = (k1 * firstColor.getGreen() + k2 * secondColor.getGreen()) / 255;
            int B = (k1 * firstColor.getBlue() + k2 * secondColor.getBlue()) / 255;
            firstColor = new Color(R, G, B);
        }
        return firstColor;
    }
    
    
    
    

}


