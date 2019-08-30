package com.grapher2d;

import javax.swing.*;
import java.awt.*;

public class Grapher2D extends JFrame {
    private static final float DEFABSSHIFT      = 0.0F;     // Сдвиг по оси абсцисс по умолчанию
    private static final float DEFORDSHIFT      = 0.0F;     // Сдвиг по оси ординат по умолчанию
    private static final float DEFABSMEASURE    = 1.0F;     // Количество единиц измерения в одном делении оси абсцисс по умолчанию
    private static final float DEFORDMEASURE    = 1.0F;     // Количество единиц измерения в одном делении оси ординат по умолчанию
    private static final float DEFABSEXPAND     = 1.0F;     // Растяжение оси абсцисс по умолчанию
    private static final float DEFORDEXPAND     = 1.0F;     // Растяжение оси ординат по умолчанию

    private static final int DEFZOOMVALUE       = 10;       // Количество пикселей в одном делении по умолчанию
    private static final int DEFZOOMMIN         = 1;        // Нижняя граница зумирования по умолчанию
    private static final int DEFZOOMMAX         = 1000;     // Верхняя граница зумирования по умолчанию
    private static final int DEFZOOMSTEP        = 4;        // Шаг изменения зумирования по умолчанию

    private CoordinateSystem coordSystem = new CoordinateSystem();  // Системы координат
    private Mouse mouse = new Mouse();                              // Курсор мыщи

    // public

    public Grapher2D() {
    }

    // protected

    // ПАРАМЕТРЫ СИСТЕМЫ КООРДИНАТ

    // Смещение системы координат

    protected float getCSAbsTranslate() {
        return getWidth() / 2.0F + coordSystem.abs.shift;
    }

    protected float getCSOrdTranslate() {
        return getHeight() / 2.0F + coordSystem.abs.shift;
    }

    // Масштаб системы координат

    protected float getCSAbsScale() {
        return coordSystem.zoom.value * coordSystem.abs.expansion / coordSystem.abs.measure;
    }

    protected float getCSOrdScale() {
        return (- 1.0F) * coordSystem.zoom.value * coordSystem.ord.expansion / coordSystem.ord.measure;
    }

    /// Отображаемые координатные углы

    protected boolean setCSAngles(boolean _first, boolean _second,
                        boolean _third, boolean _fourth) {
        boolean[] temp = new boolean[4];
        temp[0] = _first;
        temp[1] = _second;
        temp[2] = _third;
        temp[3] = _fourth;

        int count = 0;
        for(int i = 0; i < 4; ++i)
            if(temp[i]) ++count;

        switch(count)
        {
            case 2:
                if((temp[0] && temp[2]) ||
                        (temp[1] && temp[3]))
                {
                    for(int i = 0; i < 4; ++i)
                        coordSystem.angles[i] = true;
                    return false;
                }
            case 4:
            case 1:
                for(int i = 0; i < 4; ++i)
                    coordSystem.angles[i] = temp[i];
                return true;
            case 3:
            default:
                for(int i = 0; i < 4; ++i)
                    coordSystem.angles[i] = true;
                return false;
        }
    }

    protected boolean[] getCSAngles() {
        return coordSystem.angles;
    }

    // ПАРАМЕТРЫ ОСИ АБСЦИСС

    // Сдвиг относительно центра окна

    protected void setCSAbsShift() {
        setCSAbsShift(DEFABSSHIFT);
    }

    protected void setCSAbsShift(float _shift) {
        coordSystem.abs.shift = _shift;
    }

    protected float getCSAbsShift() {
        return coordSystem.abs.shift;
    }

    // Количество единиц измерения в одном делении

    protected boolean setCSAbsMeasure() {
        return setCSAbsMeasure(DEFABSMEASURE);
    }

    protected boolean setCSAbsMeasure(float _measure) {
        if(_measure < 1.0)
            return false;

        coordSystem.abs.measure = _measure;
        return true;
    }

    protected float getCSAbsMeasure() {
        return coordSystem.abs.measure;
    }

    // Растяжение осей

    protected boolean setCSAbsExpansion() {
        return setCSAbsExpansion(DEFABSEXPAND);
    }

    protected boolean setCSAbsExpansion(float _expansion) {
        if(_expansion < 1.0)
            return false;

        coordSystem.abs.expansion = _expansion;
        return true;
    }

    protected float getCSAbsExpansion() {
        return coordSystem.abs.expansion;
    }

    // Отображение цифровых значений

    protected void setCSOrdValues(boolean _values) {
        coordSystem.ord.values = _values;
    }

    protected boolean isCSAbsValues() {
        return coordSystem.abs.values;
    }

    // Отображение пунктирных линий

    protected void setCSAbsDashLines(boolean _dashLines) {
        coordSystem.abs.dashLines = _dashLines;
    }

    protected boolean isCSAbsDashLines() {
        return coordSystem.abs.dashLines;
    }

    // ПАРАМЕТРЫ ОСИ ОРДИНАТ

    /// Сдвиг относительно центра окна

    protected void setCSOrdShift() {
        setCSOrdShift(DEFORDSHIFT);
    }

    protected void setCSOrdShift(float _shift) {
        coordSystem.ord.shift = _shift;
    }



    // private

    private void translocationCoorinateSystem() {

    }

    private class CoordinateSystem {    // Структура координатной оси
        public boolean[] angles = new boolean[4];   // Координатные углы
        public Axis abs = new Axis();               // Ось абсцисс
        public Axis ord = new Axis();               // Ось ординат

        public Zoom zoom = new Zoom();  // Зуммирование

        private class Axis {            // Структура координатной оси
            public float shift;         // Сдвиг относительно центра окна
            public float measure;       // Количество единиц измерения в одном делении
            public float expansion;     // Коэффициентр растяжения
            public boolean values;      // Флаг отображения цифровых значений
            public boolean dashLines;   // Флаг отображения пунктирных линий
        }

        private class Zoom {        // Структура зумирования
            public int value;       // Количество пикселей в одном делении
            public int min;         // Нижняя граница зумирования
            public int max;         // Верхняя граница зумирования
            public int step;        // Шаг изменения зума
            public boolean center;  // Зумирование относительно центра СК
        }
    }

    private class Mouse {   /// Структура курсора мыши
        //Qt::CursorShape cursorShape;    // Текущий тип курсора
        public Pos pos = new Pos();     // Последняя позиция мыши

        private class Pos                           // Структура последней позиции курсора
        {
            public Point click = new Point();       // Позиция курсора при нажатии
            public Point noClick = new Point();     // Позиция курсора без нажатия
        }
    }
}
