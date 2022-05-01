package main.java.exercise;

import main.java.exercise.graph.Graph;

public interface Heuristic {
    public double eval(Graph g, int vertex);
}
