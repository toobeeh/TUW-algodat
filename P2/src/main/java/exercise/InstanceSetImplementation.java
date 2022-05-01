package main.java.exercise;

import main.java.exercise.graph.Graph;
import main.java.exercise.graph.EdgeImplementation;
import main.java.exercise.graph.VertexImplementation;
import main.java.exercise.InstanceImplementation;
import main.java.exercise.ResultImplementation;
import main.java.framework.InstanceSet;
import main.java.framework.graph.GraphUtil;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;


public class InstanceSetImplementation extends
        InstanceSet<InstanceImplementation, StudentSolutionImplementation, ResultImplementation,
                    VerifierImplementation, Graph> {

    public InstanceSetImplementation(Path instanceSetPath, Path outputPath) {
        super(instanceSetPath, outputPath, ResultImplementation.class);
    }

    @Override
    protected InstanceImplementation instanceFromCsv(String line) {
        // number,"isAcyclic",graphName,acyclic?
        // number,"h1"|"h2"|"h3"|"h4",graphName,vertex,heur_value
        // number,"greedy - h1"|"greedy - h2"|"greedy - h3"|"greedy - h4",graphName
        String[] splitLine = line.split(",");
        int number = Integer.parseInt(splitLine[0]);
        String groupName = splitLine[1];
        String graphName = splitLine[2];
        Graph graph = this.getAdditionalInput(graphName);
        int vertex = 0;
        if (groupName.startsWith("h1") || groupName.startsWith("h2") || groupName.startsWith("h3") || groupName.startsWith("h4")) {
            vertex = Integer.parseInt(splitLine[3]);
        }
        boolean isAcyclic = false;
        if (groupName.equals("isAcyclic")) {
            isAcyclic = Boolean.parseBoolean(splitLine[3]);
        }
        double heuristicResult = 0.0;
        if (groupName.startsWith("h")) {
            heuristicResult = Double.parseDouble(splitLine[4]);
        }
        return new InstanceImplementation(groupName, number, graphName, graph, vertex, isAcyclic, heuristicResult);
    }

    @Override
    protected StudentSolutionImplementation provideStudentSolution() {
        return new StudentSolutionImplementation();
    }

    @Override
    protected VerifierImplementation provideVerifier() {
        return new VerifierImplementation();
    }

    @Override
    protected Graph parseAdditionalInput(BufferedReader reader) {
        SimpleDirectedGraph<VertexImplementation, EdgeImplementation> graph =
            new SimpleDirectedGraph<VertexImplementation, EdgeImplementation>(EdgeImplementation.class);

        VertexProvider<VertexImplementation> vertexProvider = (String id, Map<String, Attribute> attributes) -> {
            return new VertexImplementation(Integer.parseInt(id));
        };
        EdgeProvider<VertexImplementation, EdgeImplementation> edgeProvider =
                (VertexImplementation from, VertexImplementation to, String label, Map<String, Attribute> attributes) -> {
            return new EdgeImplementation(from, to);
        };

        try {
            int[] dfvs = null;
            reader.mark(100);
            String[] splitLine = reader.readLine().split(" ");
            if (splitLine[0].equals("c")) {
                dfvs = new int[splitLine.length - 1];
                for (int i = 0; i < dfvs.length; i++) {
                    dfvs[i] = Integer.parseInt(splitLine[i+1]);
                }
            } else {
                reader.reset();
            }

            DIMACSImporter<VertexImplementation, EdgeImplementation> importer =
                new DIMACSImporter<VertexImplementation, EdgeImplementation>(vertexProvider, edgeProvider);
            importer.importGraph(graph, reader);
            return new Graph(graph, true, dfvs == null ? 0 : dfvs.length, this.provideStudentSolution());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
