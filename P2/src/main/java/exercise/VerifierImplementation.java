package main.java.exercise;

import main.java.exercise.graph.*;
import main.java.exercise.InstanceImplementation;
import main.java.exercise.StudentSolutionImplementation;
import main.java.exercise.ResultImplementation;
import main.java.framework.*;

import java.util.Arrays;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.traverse.TopologicalOrderIterator;


public class VerifierImplementation extends Verifier<InstanceImplementation, StudentSolutionImplementation, ResultImplementation> {

    @Override
    public ResultImplementation solveProblemUsingStudentSolution(InstanceImplementation instance, StudentSolutionImplementation studentSolution) {
        boolean isAcyclic = false;
        double resHeuristic = Double.NaN;
        int[] resDfvs = null;
        int goodDfvsLen = 0;
        Heuristic h = null;
        if (instance.getGroupName().contains("h1")) {
            h = (Heuristic) ((Graph g, int vertex) -> studentSolution.heuristic1(g, vertex));
        } else if (instance.getGroupName().contains("h2")) {
            h = (Heuristic) ((Graph g, int vertex) -> studentSolution.heuristic2(g, vertex));
        } else if (instance.getGroupName().contains("h3")) {
            h = (Heuristic) ((Graph g, int vertex) -> studentSolution.heuristic3(g, vertex));
        } else if (instance.getGroupName().contains("h4")) {
            h = (Heuristic) ((Graph g, int vertex) -> studentSolution.heuristic4(g, vertex));
        }

        Timer timer = new Timer();
            timer.start();
        if (instance.getGroupName().equals("isAcyclic")) {
            isAcyclic = studentSolution.isAcyclic((Graph)instance.getGraph());
        } else if (instance.getGroupName().startsWith("h")) {
            resHeuristic = h.eval(instance.getGraph(), instance.getVertex());
        } else if (instance.getGroupName().startsWith("greedy - h")) {
            int[] dfvs = new int[instance.getGraph().numberOfVertices()];
            Arrays.fill(dfvs, -1);
            studentSolution.greedyFeedbackVertexSet(instance.getGraph().editableCopy(), h, dfvs);
            int solLen = 0;
            while (solLen < dfvs.length && dfvs[solLen] != -1) {
                solLen++;
            }
            resDfvs = Arrays.copyOfRange(dfvs, 0, solLen);
            goodDfvsLen = instance.getGraph().getDfvsLength();
            Graph g = instance.getGraph().editableCopy();
            for (int v : resDfvs) {
                g.removeVertex(v);
            }
        }
        timer.stop();
        return new ResultImplementation(instance.getGraphName(), instance.getGraph().numberOfVertices(), instance.getGraph().numberOfEdges(),
            timer.getDuration(), isAcyclic, resHeuristic, h, resDfvs, goodDfvsLen);
    }

    private boolean hasCycles(Graph graph) {
        CycleDetector<VertexImplementation, EdgeImplementation> detector =
            new CycleDetector<VertexImplementation, EdgeImplementation>(graph.getGraphObject());
        return detector.detectCycles();
    }

    @Override
    public Report verifyResult(InstanceImplementation instance, ResultImplementation result) {
        if (instance.getGroupName().equals("isAcyclic")) {
            if (instance.isAcyclic() == result.getResIsAcyclic()) {
                return new Report(true, "");
            } else {
                return new Report(false, "Error in instance " + instance.getNumber() +
                    ": Expected " + instance.isAcyclic() + " but `isAcyclic(g)` returned " + result.getResIsAcyclic() + ".");
            }
        } else if (instance.getGroupName().startsWith("h")) {
            if (Math.abs(instance.getHeuristicResult() - result.getResHeuristic()) < 1e-8) {
                return new Report(true, "");
            } else {
                return new Report(false, "Error in instance " + instance.getNumber() +
                    ": Expected " + instance.getHeuristicResult() + " but `" + instance.getGroupName() + "(g)` returned " +
                    result.getResHeuristic() + ".");
            }
        } else if (instance.getGroupName().startsWith("greedy - h")) {
            Heuristic h = result.getHeuristic();
            Graph g = instance.getGraph().editableCopy();
            for (int v : result.getStudentDfvs()) {
                if (!this.hasCycles(g)) {
                    return new Report(false, "Error in instance " + instance.getNumber() +
                        ": Graph is already acyclic before removing vertex " + v + ".");
                }
                double val_v = h.eval(g, v);
                for (int w : g.getVertices()) {
                    double val_w = h.eval(g, w);
                    if (val_w > val_v + 1e-10) {
                        return new Report(false, "Error in instance " + instance.getNumber() +
                            ": Selected suboptimal vertex " + v + " with heuristic evaluation " + val_v + "." +
                            " Better is vertex " + w + " with heuristic evaluation " + val_w + ".");
                    }
                }
                g.removeVertex(v);
            }
            if (this.hasCycles(g)) {
                return new Report(false, "Error in instance " + instance.getNumber() +
                    ": Graph is not acyclic after removing the vertices that were returned by `greedyFeedbackVertexSet(g, h)`.");
            }

            return new Report(true, "");
        } else {
            return new Report(false, "Error in instance " + instance.getNumber() +
                ": Unknown group name " + instance.getGroupName() + ".");
        }
    }
}
