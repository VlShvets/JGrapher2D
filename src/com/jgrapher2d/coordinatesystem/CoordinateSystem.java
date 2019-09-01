package com.jgrapher2d.coordinatesystem;

public class CoordinateSystem {     // Система координат

    private boolean[] angles;   // Координатные углы
    private Axis abs;           // Ось абсцисс
    private Axis ord;           // Ось ординат
    private Zoom zoom;          // Увеличение

    public CoordinateSystem(){
        angles = new boolean[4];
        abs = new Axis();
        ord = new Axis();
        zoom = new Zoom();
    }

    public boolean setAngles(boolean first, boolean second, boolean third, boolean fourth) {
        boolean[] temp = new boolean[4];
        temp[0] = first;
        temp[1] = second;
        temp[2] = third;
        temp[3] = fourth;

        int count = 0;
        for(int i = 0; i < 4; ++i)
            if(temp[i]) ++count;

        switch(count)
        {
            case 2:
                if((temp[0] && temp[2]) || (temp[1] && temp[3]))
                {
                    for(int i = 0; i < 4; ++i)
                        angles[i] = true;
                    return false;
                }
            case 4:
            case 1:
                for(int i = 0; i < 4; ++i)
                    angles[i] = temp[i];
                return true;
            case 3:
            default:
                for(int i = 0; i < 4; ++i)
                    angles[i] = true;
                return false;
        }
    }

    public void setAbs(Axis abs) {
        this.abs = abs;
    }

    public void setOrd(Axis ord) {
        this.ord = ord;
    }

    public void setZoom(Zoom zoom) {
        this.zoom = zoom;
    }

    public boolean isFirst() {
        return angles[0];
    }

    public boolean isSecond() {
        return angles[1];
    }

    public boolean isThird() {
        return angles[2];
    }

    public boolean isFourth() {
        return angles[3];
    }

    public Axis getAbs() {
        return abs;
    }

    public Axis getOrd() {
        return ord;
    }

    public Zoom getZoom() {
        return zoom;
    }
}
