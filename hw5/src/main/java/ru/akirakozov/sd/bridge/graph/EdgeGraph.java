package ru.akirakozov.sd.bridge.graph;

import ru.akirakozov.sd.bridge.draw.DrawingAPI;
import ru.akirakozov.sd.bridge.draw.strategy.GraphDrawingStrategy;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

public class EdgeGraph extends Graph {
    private int nodeCount;
    private final List<Edge> edges;

    public EdgeGraph(DrawingAPI drawingAPI, GraphDrawingStrategy drawingStrategy) {
        super(drawingAPI, drawingStrategy);
        this.edges = new ArrayList<>();
    }


    @Override
    public void readGraph(InputStream in, Charset charset) {
        try (Scanner scanner = new Scanner(in, charset)) {
            nodeCount = scanner.nextInt();
            int edgeCount = scanner.nextInt();
            for (int i = 0; i < edgeCount; i++) {
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                edges.add(new Edge(from, to));
            }
        }
    }

    @Override
    public void drawGraph() {
        drawingStrategy.provideNodeCount(nodeCount);
        for (Edge edge : edges) {
            drawingStrategy.provideEdge(edge.getFrom(), edge.getTo());
        }
        drawingStrategy.draw(drawingAPI);
    }

    private static class Edge {
        private final int from;
        private final int to;

        private Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }
    }
}
