package com.jgrapher2d;

import com.jgrapher2d.coordinatesystem.CoordinateSystem;
import com.jgrapher2d.mouse.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class JGrapher2D extends JComponent {
    private static final float DEFABSSHIFT      = 0.0F;     // Сдвиг по оси абсцисс по умолчанию
    private static final float DEFORDSHIFT      = 0.0F;     // Сдвиг по оси ординат по умолчанию
    private static final float DEFABSMEASURE    = 1.0F;     // Количество единиц измерения в одном делении оси абсцисс по умолчанию
    private static final float DEFORDMEASURE    = 1.0F;     // Количество единиц измерения в одном делении оси ординат по умолчанию
    private static final float DEFABSEXPANSION  = 1.0F;     // Растяжение оси абсцисс по умолчанию
    private static final float DEFORDEXPANSION  = 1.0F;     // Растяжение оси ординат по умолчанию

    private static final int DEFZOOMVALUE       = 10;       // Количество пикселей в одном делении по умолчанию
    private static final int DEFZOOMMIN         = 1;        // Нижняя граница увеличения по умолчанию
    private static final int DEFZOOMMAX         = 1000;     // Верхняя граница увеличения по умолчанию
    private static final int DEFZOOMSTEP        = 4;        // Шаг изменения увеличения по умолчанию

    private CoordinateSystem coordinateSystem;  // Системы координат
    private Mouse mouse;                        // Курсор мыщи

    // public

    public JGrapher2D() {
        coordinateSystem = new CoordinateSystem();
        mouse = new Mouse();

        // Априорная инициализация параметров системы координат
        coordinateSystem.setAngles(true, true, true, true);

        // Априорная инициализация параметров координатных осей
        coordinateSystem.getAbs().setShift(DEFABSSHIFT);
        coordinateSystem.getOrd().setShift(DEFORDSHIFT);
        coordinateSystem.getAbs().setMeasure(DEFABSMEASURE);
        coordinateSystem.getOrd().setMeasure(DEFORDMEASURE);
        coordinateSystem.getAbs().setExpansion(DEFABSEXPANSION);
        coordinateSystem.getOrd().setExpansion(DEFORDEXPANSION);
        coordinateSystem.getAbs().setValues(true);
        coordinateSystem.getOrd().setValues(true);
        coordinateSystem.getAbs().setDashLines(true);
        coordinateSystem.getOrd().setDashLines(true);

        // Априорная инициализация параметров увеличения
        coordinateSystem.getZoom().setMin(DEFZOOMMIN);
        coordinateSystem.getZoom().setMax(DEFZOOMMAX);
        coordinateSystem.getZoom().setValue(DEFZOOMVALUE);
        coordinateSystem.getZoom().setStep(DEFZOOMSTEP);
        coordinateSystem.getZoom().setCenter(true);

        // Априорная инициализация типа курсора мыши
        mouse.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        setCursor(mouse.getCursor());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e == null)
                    return;

                mouse.getPos().setClick(e.getPoint());

                mouse.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                setCursor(mouse.getCursor());

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e == null)
                    return;

                mouse.getPos().setNoClick(e.getPoint());

                mouse.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                setCursor(mouse.getCursor());

                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(e == null)
                    return;

                coordinateSystem.getAbs().setShift(coordinateSystem.getAbs().getShift() + (float) e.getX() - (float) mouse.getPos().getClick().getX());
                coordinateSystem.getOrd().setShift(coordinateSystem.getOrd().getShift() + (float) e.getY() - (float) mouse.getPos().getClick().getY());

                mouse.getPos().setClick(e.getPoint());

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(e == null)
                    return;

                mouse.getPos().setNoClick(e.getPoint());
            }
        });
        addMouseWheelListener(e -> {
            if(e == null || e.getPreciseWheelRotation() == 0)
                return;

            if(!coordinateSystem.getZoom().isCenter())
            {

                coordinateSystem.getAbs().setShift(coordinateSystem.getAbs().getShift() / coordinateSystem.getZoom().getValue());
                coordinateSystem.getOrd().setShift(coordinateSystem.getOrd().getShift() / coordinateSystem.getZoom().getValue());
            }

            if(e.getWheelRotation() > 0) {
                coordinateSystem.getZoom().setValue((int) ((coordinateSystem.getZoom().getValue() + 10) * Math.pow(Math.E, 0.1) - 10));
            } else if(e.getWheelRotation() < 0) {
                coordinateSystem.getZoom().setValue((int) ((coordinateSystem.getZoom().getValue() + 10) / Math.pow(Math.E, 0.05) - 10));
            }

            if(!coordinateSystem.getZoom().isCenter())
            {
                coordinateSystem.getAbs().setShift(coordinateSystem.getAbs().getShift() * coordinateSystem.getZoom().getValue());
                coordinateSystem.getOrd().setShift(coordinateSystem.getOrd().getShift() * coordinateSystem.getZoom().getValue());
            }

            repaint();
        });
    }

    public void setCoordSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    public Mouse getMouse() {
        return mouse;
    }

    protected void paintComponent(Graphics graphics) {
        if(graphics == null)
            return;

        translocationCoorinateSystem();

        Graphics2D g = (Graphics2D) graphics;
        g.translate(getWidth() / 2 + (int) coordinateSystem.getAbs().getShift(), getHeight() / 2 + (int) coordinateSystem.getOrd().getShift());

        List<Point2D.Float> points = new ArrayList<Point2D.Float>();

        // --------------------------------------------------

        // Построение координатных осей
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2.0F));

        // Построение линий координатный осей
        points.add(new Point2D.Float((-1.0F) * getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), 0.0F));
        points.add(new Point2D.Float(getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), 0.0F));
        points.add(new Point2D.Float(0.0F, (-1.0F) * getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
        points.add(new Point2D.Float(0.0F, getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
        g.draw(new Line2D.Float(points.get(0), points.get(1)));
        g.draw(new Line2D.Float(points.get(2), points.get(3)));
        points.clear();

        // Построение стрелок направлений координатных осей
        points.add(new Point2D.Float(0.0F, (-1.0F) * getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
        points.add(new Point2D.Float(-5.0F, (-1.0F) * getHeight() / 2.0F + 5.0F - coordinateSystem.getOrd().getShift()));
        points.add(new Point2D.Float(5.0F,(-1.0F) * getHeight() / 2.0F + 5.0F - coordinateSystem.getOrd().getShift()));
        points.add(new Point2D.Float(getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), 0.0F));
        points.add(new Point2D.Float(getWidth() / 2.0F - 5.0F - coordinateSystem.getAbs().getShift(), -5.0F));
        points.add(new Point2D.Float(getWidth() / 2.0F - 5.0F - coordinateSystem.getAbs().getShift(), 5.0F));
        g.draw(new Line2D.Float(points.get(0), points.get(1)));
        g.draw(new Line2D.Float(points.get(0), points.get(2)));
        g.draw(new Line2D.Float(points.get(3), points.get(4)));
        g.draw(new Line2D.Float(points.get(3), points.get(5)));
        points.clear();

        g.setStroke(new BasicStroke(1.0F));
        g.drawString(String.valueOf(0), -12, 15);

        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1.0F, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {10.0F, 10.0F}, 0.0F));

        // Ось абсцисс
        int absStep = 0;
        int absValue = 0;
        while(absStep <= getWidth() / 2.0F + Math.abs(coordinateSystem.getAbs().getShift())) {

            // Положительная
            if (coordinateSystem.isFirst() || coordinateSystem.isFourth()) {

                // Цифры
                if (coordinateSystem.getAbs().isValues() && absStep != 0) {
                    g.drawString(String.valueOf(absValue), absStep - 9, 15);
                    g.drawString(String.valueOf(absValue), absStep - 9, getHeight() / 2 - coordinateSystem.getOrd().getShift());
                }

                // Пунктирные линии
                if (coordinateSystem.getAbs().isDashLines()) {
                    points.add(new Point2D.Float(absStep, (-1.0F) * getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
                    points.add(new Point2D.Float(absStep, getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();
                }

                // Штрихи
                int dashStep = absStep;
                int dashValue = 0;
                int dashSize;
                while(isDrowAbsDash(dashStep, absStep)) {
                    dashSize = calcDashSize(dashValue);
                    points.add(new Point2D.Float(dashStep, (-1.0F) * dashSize));
                    points.add(new Point2D.Float(dashStep, dashSize));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();

                    dashStep += coordinateSystem.getAbs().getExpansion() * coordinateSystem.getZoom().getValue() * (coordinateSystem.getZoom().getValue() != 1 ? 1 : 10);
                    dashValue += 1;
                }
            }

            // Отрицательная
            if (coordinateSystem.isSecond() || coordinateSystem.isThird()) {

                // Цифры
                if (coordinateSystem.getAbs().isValues() && absStep != 0) {
                    g.drawString(String.valueOf((-1) * absValue), (-1) * absStep - 15, 15);
                    g.drawString(String.valueOf((-1) * absValue), (-1) * absStep - 15, getHeight() / 2.0F - coordinateSystem.getOrd().getShift());
                }

                // Пунктирные линии
                if (coordinateSystem.getAbs().isDashLines()) {
                    points.add(new Point2D.Float((-1.0F) * absStep, (-1.0F) * getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
                    points.add(new Point2D.Float((-1.0F) * absStep, getHeight() / 2.0F - coordinateSystem.getOrd().getShift()));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();
                }

                // Штрихи
                int dashStep = absStep;
                int dashValue = 0;
                int dashSize;
                while(isDrowAbsDash(dashStep, absStep)) {
                    dashSize = calcDashSize(dashValue);
                    points.add(new Point2D.Float((-1.0F) * dashStep, (-1.0F) * dashSize));
                    points.add(new Point2D.Float((-1.0F) * dashStep, dashSize));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();

                    dashStep += coordinateSystem.getAbs().getExpansion() * coordinateSystem.getZoom().getValue() * (coordinateSystem.getZoom().getValue() != 1 ? 1 : 10);
                    dashValue += 1;
                }
            }

            absStep += coordinateSystem.getAbs().getExpansion() * coordinateSystem.getZoom().getValue() * calcZoomCoef();
            absValue += calcZoomCoef();
        }

        // Ось ординат
        int ordStep = 0;
        int ordValue = 0;
        while(ordStep <= getHeight() / 2.0F + Math.abs(coordinateSystem.getOrd().getShift())) {

            // Положительная
            if (coordinateSystem.isFirst() || coordinateSystem.isSecond()) {

                // Цифры
                if (coordinateSystem.getOrd().isValues() && ordStep != 0) {
                    g.drawString(String.valueOf(ordValue), -24, (-1) * ordStep + 15);
                    g.drawString(String.valueOf(ordValue), (-1) * getWidth() / 2 - coordinateSystem.getAbs().getShift(), (-1) * ordStep + 15);
                }

                // Пунктирные линии
                if (coordinateSystem.getOrd().isDashLines()) {
                    points.add(new Point2D.Float((-1.0F) * getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), (-1.0F) * ordStep));
                    points.add(new Point2D.Float(getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), (-1.0F) * ordStep));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();
                }

                // Штрихи
                int dashStep = ordStep;
                int dashValue = 0;
                int dashSize;
                while(isDrowOrdDash(dashStep, ordStep)) {
                    dashSize = calcDashSize(dashValue);
                    points.add(new Point2D.Float((-1.0F) * dashSize, (-1.0F) * dashStep));
                    points.add(new Point2D.Float(dashSize, (-1.0F) * dashStep));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();

                    dashStep += coordinateSystem.getAbs().getExpansion() * coordinateSystem.getZoom().getValue() * (coordinateSystem.getZoom().getValue() != 1 ? 1 : 10);
                    dashValue += 1;
                }
            }

            // Отрицательная
            if(coordinateSystem.isThird() || coordinateSystem.isFourth()) {

                // Цифры
                if(coordinateSystem.getOrd().isValues() && ordStep != 0) {
                    g.drawString(String.valueOf((-1) * ordValue), -30, ordStep + 15);
                    g.drawString(String.valueOf((-1) * ordValue), (-1) * getWidth() / 2 - coordinateSystem.getAbs().getShift(), ordStep + 15);
                }

                // Пунктирные линии
                if(coordinateSystem.getOrd().isDashLines()) {
                    points.add(new Point2D.Float((-1.0F) * getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), ordStep));
                    points.add(new Point2D.Float(getWidth() / 2.0F - coordinateSystem.getAbs().getShift(), ordStep));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();
                }

                // Штрихи
                int dashStep = ordStep;
                int dashValue = 0;
                int dashSize;
                while(isDrowOrdDash(dashStep, ordStep)) {
                    dashSize = calcDashSize(dashValue);
                    points.add(new Point2D.Float((-1.0F) * dashSize, dashStep));
                    points.add(new Point2D.Float(dashSize, dashStep));
                    g.draw(new Line2D.Float(points.get(0), points.get(1)));
                    points.clear();

                    dashStep += coordinateSystem.getOrd().getExpansion() * coordinateSystem.getZoom().getValue() * (coordinateSystem.getZoom().getValue() != 1 ? 1 : 10);
                    dashValue += 1;
                }
            }

            ordStep += coordinateSystem.getOrd().getExpansion() * coordinateSystem.getZoom().getValue() * calcZoomCoef();
            ordValue += calcZoomCoef();
        }
    }

    // protected

    // Смещение системы координат

    protected void translocationCoorinateSystem() {
        if(!coordinateSystem.isFirst() && !coordinateSystem.isSecond()) {
            if(coordinateSystem.getOrd().getShift() > (-1.0F) * getHeight() / 2.0F)
                coordinateSystem.getOrd().setShift((-1.0F) * getHeight() / 2.0F);
        }
        if(!coordinateSystem.isSecond() && !coordinateSystem.isThird()) {
            if(coordinateSystem.getAbs().getShift() > (-1.0F) * getWidth() / 2.0F)
                coordinateSystem.getAbs().setShift((-1.0F) * getWidth() / 2.0F);
        }
        if(!coordinateSystem.isThird() && !coordinateSystem.isFourth()) {
            if(coordinateSystem.getOrd().getShift() < getHeight() / 2.0F)
                coordinateSystem.getOrd().setShift(getHeight() / 2.0F);
        }
        if(!coordinateSystem.isFirst() && !coordinateSystem.isFourth()) {
            if (coordinateSystem.getAbs().getShift() < getWidth() / 2.0F)
                coordinateSystem.getAbs().setShift(getWidth() / 2.0F);
        }

    }

    // Параметры смещения системы координат

    protected float getCSAbsTranslate() {
        return getWidth() / 2.0F + coordinateSystem.getAbs().getShift();
    }

    protected float getCSOrdTranslate() {
        return getHeight() / 2.0F + coordinateSystem.getAbs().getShift();
    }

    // Масштаб системы координат

    protected float getCSAbsScale() {
        return coordinateSystem.getZoom().getValue() * coordinateSystem.getAbs().getExpansion() / coordinateSystem.getAbs().getMeasure();
    }

    protected float getCSOrdScale() {
        return (- 1.0F) * coordinateSystem.getZoom().getValue() * coordinateSystem.getOrd().getExpansion() / coordinateSystem.getOrd().getMeasure();
    }

    // private

    private boolean isDrowAbsDash(int dashStep, int absStep) {
        return dashStep < absStep + coordinateSystem.getAbs().getExpansion() * coordinateSystem.getZoom().getValue() * calcZoomCoef();
    }

    private boolean isDrowOrdDash(int dashStep, int ordStep) {
        return dashStep < ordStep + coordinateSystem.getOrd().getExpansion() * coordinateSystem.getZoom().getValue() * calcZoomCoef();
    }

    private float calcZoomCoef() {
        return  coordinateSystem.getZoom().getValue() < coordinateSystem.getZoom().getStep() ? 100 :
                    coordinateSystem.getZoom().getValue() < Math.pow(coordinateSystem.getZoom().getStep(), 3) ? 10 : 1;
    }

    private int calcDashSize(int dashValue) {
        return dashValue % 10 == 0 ? 6 : dashValue % 5 == 0 ? 4 : 2;
    }
}
