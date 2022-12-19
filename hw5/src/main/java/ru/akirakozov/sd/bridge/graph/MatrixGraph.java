package ru.akirakozov.sd.bridge.graph;

import ru.akirakozov.sd.bridge.draw.DrawingAPI;
import ru.akirakozov.sd.bridge.draw.strategy.GraphDrawingStrategy;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MatrixGraph extends Graph {
    private final List<List<Boolean>> matrix;

    public MatrixGraph(DrawingAPI drawingAPI, GraphDrawingStrategy drawingStrategy) {
        super(drawingAPI, drawingStrategy);
        this.matrix = new ArrayList<>();
    }

    @Override
    public void readGraph(InputStream in, Charset charset) {
        try (Scanner scanner = new Scanner(in, charset)) {
            int nodeCount = scanner.nextInt();
            for (int i = 0; i < nodeCount; i++) {
                List<Boolean> row = new ArrayList<>();
                for (int j = 0; j < nodeCount; j++) {
                    row.add(scanner.nextInt() > 0);
                }
                matrix.add(row);
            }
        }
    }

    @Override
    public void drawGraph() {
        drawingStrategy.provideNodeCount(matrix.size());
        for (int from = 0; from < matrix.size(); from++) {
            for (int to = 0; to < matrix.get(from).size(); to++) {
                if (matrix.get(from).get(to)) {
                    drawingStrategy.provideEdge(from, to);
                }
            }
        }
        drawingStrategy.draw(drawingAPI);
    }
}
