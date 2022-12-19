package ru.akirakozov.sd.bridge.draw;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class JavaAwtAPI implements DrawingAPI {
    private final List<Ellipse2D> circles;
    private final List<Line2D> lines;
    private final long width;
    private final long height;
    private InternalFrame frame;

    public JavaAwtAPI(long width, long height) {
        this.circles = new ArrayList<>();
        this.lines = new ArrayList<>();
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
        circles.add(new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, 2 * radius, 2 * radius));
    }

    @Override
    public void drawLine(Point point1, Point point2) {
        lines.add(new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
    }

    @Override
    public void render() {
        if (frame == null) {
            frame = new InternalFrame(width, height);
        }
    }

    private class InternalFrame extends Frame {
        public InternalFrame(long width, long height) throws HeadlessException {
            setVisible(true);
            setSize((int) width, (int) height);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            circles.forEach(g2d::fill);
            lines.forEach(g2d::draw);
        }
    }
}
