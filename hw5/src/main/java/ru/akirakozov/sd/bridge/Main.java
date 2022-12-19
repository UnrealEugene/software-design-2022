package ru.akirakozov.sd.bridge;

import org.apache.commons.cli.*;
import ru.akirakozov.sd.bridge.draw.*;
import ru.akirakozov.sd.bridge.draw.strategy.*;
import ru.akirakozov.sd.bridge.graph.*;

public class Main {
    public static void main(String[] args) {
        Options options = new Options()
                .addOption(Option.builder()
                        .option("a")
                        .longOpt("api")
                        .hasArg(true)
                        .desc("API used for drawing ('awt' or 'fx')")
                        .required()
                        .build())
                .addOption(Option.builder()
                        .option("g")
                        .longOpt("graph")
                        .hasArg(true)
                        .desc("graph input format ('edge' or 'matrix')")
                        .required()
                        .build())
                .addOption(Option.builder()
                        .option("w")
                        .longOpt("width")
                        .hasArg(true)
                        .desc("drawing area width (default: 800)")
                        .build())
                .addOption(Option.builder()
                        .option("h")
                        .longOpt("height")
                        .hasArg(true)
                        .desc("drawing area height (default: 600)")
                        .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(
                    "java Main <args>...",
                    "Draws a visualisation of graph given in input using selected drawing API (JavaFX or JavaAWT).",
                    options,
                    "Example:\n java Main -a awt -g edge -w 1024 -h 768");
            System.exit(1);
        }

        long width = Long.parseLong(cmd.getOptionValue("width", "800"));
        long height = Long.parseLong(cmd.getOptionValue("height", "600"));

        DrawingAPI drawingAPI = "awt".equals(cmd.getOptionValue("api"))
                ? new JavaAwtAPI(width, height)
                : new JavaFxAPI(width, height);

        GraphDrawingStrategy drawingStrategy = new CircularGraphDrawingStrategy();

        Graph graph = "edge".equals(cmd.getOptionValue("graph"))
                ? new EdgeGraph(drawingAPI, drawingStrategy)
                : new MatrixGraph(drawingAPI, drawingStrategy);
        graph.readGraph(System.in);
        graph.drawGraph();
    }
}