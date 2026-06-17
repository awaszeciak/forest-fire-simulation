package pl.forestfire.application.view;

import pl.forestfire.model.Forest;

/**
 * Interfejs klas, mających za zadanie wyświetlanie symulacji na ekranie.
 */
public interface Renderer {
    /**
     * Metoda odpowiadająca za wyświetlenie lasu na ekranie.
     * @param forest las, który ma zostać wyświetlony.
     */
    public void drawForest(Forest forest);

    /**
     * Metoda odpowiadająca za wyświetlenie jednej komórki lasu na ekranie.
     * @param forest las z którego pochodzi komórka.
     * @param x położenie komórki na osi X.
     * @param y położenie komórki na osi Y.
     *
     */
    public void drawCell(Forest forest, int x, int y);

    /**
     * Metoda odpowiadająca za wyświetlenie na ekranie statysty symulacji.
     * @param forest las, którego statystyki mają zostać wyświetlone.
     * @param speed prędkość wiatru.
     * @param angle kąt kierunku wiatru względem wschodu
     * @param delay opóżnienie między krokami symulacji.
     *
     */
    public void showStatistics(Forest forest, double speed, double angle, long delay);

    /**
     * Metoda odpowiadająca za określenie symbolu kierunku wiatru.
     * @param angle kąt kierunku wiatru względem wschodu.
     * @return String z kierunkiem wiatru (np. N, NE itd.).
     *
     */
    default public String directionString(double angle) {
        String direction=new String();
        if(angle>180.0)
            direction+="N";

        if(angle>0&&angle<180.0)
            direction+="S";

        if(angle>90.0&&angle<270.0)
            direction+="W";

        if((angle<90.0&&angle>=0.0)||(angle>270.0&&angle<360.0))
            direction+="E";

        return direction;
    }

}
