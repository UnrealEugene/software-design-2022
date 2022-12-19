package ru.akirakozov.sd.bridge.draw;

import com.machinezoo.noexception.Exceptions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFxAPI implements DrawingAPI {
    private final List<Shape> shapes = new ArrayList<>();
    private final long width;
    private final long height;

    public JavaFxAPI(long width, long height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public long getDrawingAreaWidth() {
        return width;
    }

    @Override
    public long getDrawingAreaHeight() {
        return height;
    }

    @Override
    public void drawCircle(Point point, long radius) {
        shapes.add(new Circle(point.getX(), point.getY(), radius));
    }

    @Override
    public void drawLine(Point point1, Point point2) {
        shapes.add(new Line(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
    }

    @Override
    public void render() {
        try {
            InternalApplication app = new InternalApplication();
            app.init();
            Platform.startup(Exceptions.sneak().runnable(() -> app.start(new Stage())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class InternalApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            Group root = new Group();
            shapes.forEach(root.getChildren()::add);
            primaryStage.setScene(new Scene(root, width, height));
            primaryStage.show();
        }
    }
}
