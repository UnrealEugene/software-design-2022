package ru.akirakozov.sd.bridge.draw.strategy;

import ru.akirakozov.sd.bridge.draw.DrawingAPI;
import ru.akirakozov.sd.bridge.draw.Point;

import java.util.ArrayList;
import java.util.List;

public class CircularGraphDrawingStrategy implements GraphDrawingStrategy {
    public static final double CIRCLE_RADIUS_FACTOR = .75;
    public static final double NODE_RADIUS_FACTOR = .15;
    private final List<List<Integer>> adjacencyList;

    public CircularGraphDrawingStrategy() {
        this.adjacencyList = new ArrayList<>();
    }

    @Override
    public void provideNodeCount(int nodeCount) {
        for (int i = adjacencyList.size(); i < nodeCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    @Override
    public void provideEdge(int from, int to) {
        if (from < 0 || from >= adjacencyList.size() || to < 0 || to >= adjacencyList.size()) {
            throw new IllegalArgumentException("Attempt to provide invalid edge " + from + " -> " + to);
        }
        adjacencyList.get(from).add(to);
    }

    @Override
    public void draw(DrawingAPI drawingAPI) {
        long centerX = drawingAPI.getDrawingAreaWidth() / 2;
        long centerY = drawingAPI.getDrawingAreaHeight() / 2;

        double circleRadius = CIRCLE_RADIUS_FACTOR * Math.min(centerX, centerY);
        double nodeRadius = NODE_RADIUS_FACTOR * Math.min(centerX, centerY);

        int nodeCount = adjacencyList.size();

        List<Point> nodePositionList = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI / nodeCount * i;
            long dx = Math.round(circleRadius * Math.cos(angle));
            long dy = Math.round(circleRadius * Math.sin(angle));
            Point point = new Point(centerX + dx, centerY + dy);
            nodePositionList.add(point);
            drawingAPI.drawCircle(point, Math.round(nodeRadius));
        }

        for (int from = 0; from < adjacencyList.size(); from++) {
            for (int to : adjacencyList.get(from)) {
                drawingAPI.drawLine(nodePositionList.get(from), nodePositionList.get(to));
            }
        }

        drawingAPI.render();
    }
}
