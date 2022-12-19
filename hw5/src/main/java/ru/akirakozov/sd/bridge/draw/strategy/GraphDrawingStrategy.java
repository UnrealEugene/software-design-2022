package ru.akirakozov.sd.bridge.draw.strategy;

import ru.akirakozov.sd.bridge.draw.DrawingAPI;

public interface GraphDrawingStrategy {
    void provideNodeCount(int nodeCount);
    void provideEdge(int from, int to);
    void draw(DrawingAPI drawingAPI);
}
