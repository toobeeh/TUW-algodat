package main.java.exercise;

import main.java.exercise.graph.Graph;
import main.java.framework.Instance;

public class InstanceImplementation implements Instance {

    private String groupName;

    private int number;

    private String graphName;

    private Graph graph;

    private int vertex;

    private boolean isAcyclic;

    private double heuristicResult;

    public InstanceImplementation(String groupName, int number, String graphName, Graph graph,
            int vertex, boolean isAcyclic, double heuristicResult) {
        this.groupName = groupName;
        this.number = number;
        this.graphName = graphName;
        this.graph = graph;
        this.vertex = vertex;
        this.isAcyclic = isAcyclic;
        this.heuristicResult = heuristicResult;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    public String getGraphName() {
        return this.graphName;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public int getVertex() {
        return this.vertex;
    }

    public boolean isAcyclic() {
        return this.isAcyclic;
    }

    public double getHeuristicResult() {
        return this.heuristicResult;
    }
}
