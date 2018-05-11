package pl.edu.uksw.wmp.Colors;

/**
 *
 * @author Konrad Chrząszcz
 */
public enum ColorTypes {
    NIEBIESKI(0), ZIELONY(1), CZERWONY(2), NOIR(3), FAJNY(4);
    
    private int colorCode;

    ColorTypes(int cc){
        colorCode = cc;
    }
    
    /**
     * Zwraca kod koloru
     * @return kod koloru.
     */
    public int getColorCode(){
        return colorCode;
    }
    
    /**
     * Zwraca tekstową reprezentację zmiennych enum.
     * @return tekstowa reprezentacja zmiennej enum.
     */
    @Override
    public String toString() {
        String output = name().toString();
        output = output.charAt(0) + output.substring(1).toLowerCase();
        return output;
    }
}
