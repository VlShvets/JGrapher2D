package com.jgrapher2d.mouse;

import java.awt.geom.Point2D;

public class Pos {  // Последняя позиция курсора

    private Point2D click;    // Позиция курсора при нажатии
    private Point2D noClick;  // Позиция курсора без нажатия

    public Pos() {
        click = new Point2D.Float();
        noClick = new Point2D.Float();
    }

    public void setClick(Point2D click) {
        this.click = click;
    }

    public void setNoClick(Point2D noClick) {
        this.noClick = noClick;
    }

    public Point2D getClick() {
        return click;
    }

    public Point2D getNoClick() {
        return noClick;
    }
}
