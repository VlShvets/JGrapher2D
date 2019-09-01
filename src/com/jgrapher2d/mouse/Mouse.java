package com.jgrapher2d.mouse;

import java.awt.*;

public class Mouse {    // Курсор мыши

    public Pos pos;     // Последняя позиция мыши
    Cursor cursor;      // Текущий вид курсора

    public Mouse() {
        pos = new Pos();
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Pos getPos() {
        return pos;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
