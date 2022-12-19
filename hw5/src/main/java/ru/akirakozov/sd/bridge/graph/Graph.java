package ru.akirakozov.sd.bridge.graph;

import ru.akirakozov.sd.bridge.draw.DrawingAPI;
import ru.akirakozov.sd.bridge.draw.strategy.GraphDrawingStrategy;

import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class Graph {
    protected final DrawingAPI drawingAPI;
    protected final GraphDrawingStrategy drawingStrategy;

    public Graph(DrawingAPI drawingAPI, GraphDrawingStrategy drawingStrategy) {
        this.drawingAPI = drawingAPI;
        this.drawingStrategy = drawingStrategy;
    }

    public final void readGraph(InputStream in) {
        readGraph(in, Charset.defaultCharset());
    }

    public abstract void readGraph(InputStream in, Charset charset);

    public abstract void drawGraph();
}
