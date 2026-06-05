package pl.forestfire.application.view;

import pl.forestfire.model.Forest;

public interface Renderer {
    public void drawForest(Forest forest);
    public void drawCell(Forest forest, int x, int y);
    public void showStatistics(Forest forest, double speed, double angle, long delay);

    //określa symbol kierunku wiatru
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
