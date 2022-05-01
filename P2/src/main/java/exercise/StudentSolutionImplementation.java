package main.java.exercise;

import main.java.exercise.graph.Graph;
import main.java.exercise.Heuristic;
import main.java.framework.StudentInformation;
import main.java.framework.StudentSolution;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

import java.util.ArrayList;

public class StudentSolutionImplementation implements StudentSolution {
    @Override
    public StudentInformation provideStudentInformation() {
        return new StudentInformation(
                "Tobias", // Vorname
                "Scharsching", // Nachname
                "12123692" // Matrikelnummer
        );
    }

    // Implementieren Sie hier einen Algorithmus der ueberprueft, ob der Graph azyklisch ist.
    public boolean isAcyclic(Graph g) {


        // algorithm to detect DAG, base source: https://tuwel.tuwien.ac.at/pluginfile.php/2907751/mod_resource/content/36/AD_03_Graphen.pdf pg. 69
        int[] vertices = g.getVertices();

        // find max id for better addressing so the vertices may be unordered & uncontinuously indexed
        int maxID = vertices[0];
        for(int vertex:vertices)
            if(vertex > maxID) maxID = vertex;

        // init list that holds current sources
        ArrayList<Integer> sources = new ArrayList();

        // init array that will hold positive vertex degree
        int[] vCount = new int[maxID];

        // find initial positive vertex degree and detect sources
        for(int vertex : vertices){

            vCount[vertex-1] = g.inDegree(vertex);

            if(vCount[vertex-1] == 0) sources.add(vertex);
        }

        while(!sources.isEmpty()){

            // get and remove first source
            int source = sources.remove(0);

            // simulate vertex removal and decrement in degree of all successors
            int[] successors = g.getSuccessors(source);
            for(int i = 0; i < successors.length; i++) {

                int successor = successors[i];
                vCount[successor-1]--;

                // if vertex became a source, add to list
                if(vCount[successor-1] == 0){
                    sources.add(0, successor);
                }
            }
        }

        // if no sources left and still vertices with positive degree left, it's no DAG
        for(int pEdge : vCount)
            if(pEdge > 0) return false;

        return true;
    }

    // Implementieren Sie hier die erste Heuristik.
    public double heuristic1(Graph g, int vertex) {
        return g.inDegree(vertex) + g.outDegree(vertex);
    }

    // Implementieren Sie hier die zweite Heuristik.
    public double heuristic2(Graph g, int vertex) {
        return g.inDegree(vertex) * g.outDegree(vertex);
    }

    // Implementieren Sie hier die dritte Heuristik.
    public double heuristic3(Graph g, int vertex) {
        return g.outDegree(vertex) + g.inDegree(vertex) - 0.3 * Math.abs(g.outDegree(vertex) - g.inDegree(vertex));
    }

    // Implementieren Sie hier optional eine vierte Heuristik.
    public double heuristic4(Graph g, int vertex) {
        return 0.0;
    }

    // Implementieren Sie hier den Greedy-Algorithmus, der eine Menge von Knoten findet ohne
    // denen der Graph azyklisch ist.
    public void greedyFeedbackVertexSet(Graph g, Heuristic h, int[] feedbackVertexSet) {
        int removePointer = 0;

        while(!g.isAcyclic()){

            double maxWeighted = Integer.MIN_VALUE;
            int maxWeightedVertex = -1;

            // determine max weighted
            int[] vertices = g.getVertices();
            for(int vertex : vertices){
                double weight = h.eval(g, vertex);
                if(weight > maxWeighted) {
                    maxWeighted = weight;
                    maxWeightedVertex = vertex;
                }
            }

            // remove vertex and update vertices
            g.removeVertex(maxWeightedVertex);
            feedbackVertexSet[removePointer++] = maxWeightedVertex;
        }
        int x = 1;
    }

}
