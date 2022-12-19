package ru.akirakozov.sd.bridge.draw;

public interface DrawingAPI {
    long getDrawingAreaWidth();
    long getDrawingAreaHeight();
    void drawCircle(Point point, long radius);
    void drawLine(Point point1, Point point2);
    void render();
}
