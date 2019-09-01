package com.jgrapher2d.coordinatesystem;

public class Axis {     // Координатная ось

    private float shift;        // Сдвиг относительно центра окна
    private float measure;      // Количество единиц измерения в одном делении
    private float expansion;    // Коэффициентр растяжения
    private boolean values;     // Флаг отображения цифровых значений
    private boolean dashLines;  // Флаг отображения пунктирных линий

    public void setShift(float shift) {
        this.shift = shift;
    }

    public boolean setMeasure(float measure) {
        if(measure < 1.0)
            return false;

        this.measure = measure;

        return true;
    }

    public boolean setExpansion(float expansion) {
        if(expansion < 1.0)
            return false;

        this.expansion = expansion;

        return true;
    }

    public void setValues(boolean values) {
        this.values = values;
    }

    public void setDashLines(boolean dashLines) {
        this.dashLines = dashLines;
    }

    public float getShift() {
        return shift;
    }

    public float getMeasure() {
        return measure;
    }

    public float getExpansion() {
        return expansion;
    }

    public boolean isValues() {
        return values;
    }

    public boolean isDashLines() {
        return dashLines;
    }
}
