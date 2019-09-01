package com.jgrapher2d.coordinatesystem;

public class Zoom {     // Увеличение

    private int value;          // Количество пикселей в одном делении
    private int min;            // Нижняя граница увеличения
    private int max;            // Верхняя граница увеличения
    private int step;           // Шаг изменения увеличения
    private boolean center;     // Увеличение относительно центра системы координат

    public boolean setValue(int value) {
        if(value < min + 1 || value > max - 1)
            return false;

        this.value = value;

        return true;
    }

    public boolean setMin(int min) {
        if(min < 1 || min > 100 || min > max)
            return false;

        this.min = min;

        return true;
    }

    public boolean setMax(int max) {
        if(max < 10 || max > 1000 || max < min)
            return false;

        this.max = max;

        return true;
    }

    public boolean setStep(int step) {
        if(step < 1 || step > 10)
            return false;

        this.step = step;

        return true;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }

    public boolean isCenter() {
        return center;
    }
}
