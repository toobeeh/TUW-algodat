package main.java.exercise.graph;

import main.java.exercise.StudentSolutionImplementation;
import main.java.framework.graph.BasicGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.Graphs;

public class Graph extends BasicGraph<VertexImplementation, EdgeImplementation> implements Cloneable {

    boolean readonly;

    int dfvsLength;

    StudentSolutionImplementation studentSolution;

    public Graph(SimpleDirectedGraph<VertexImplementation, EdgeImplementation> graph, boolean readonly, int dfvsLength,
            StudentSolutionImplementation studentSolution) {
        super(graph);
        this.readonly = readonly;
        this.dfvsLength = dfvsLength;
        this.studentSolution = studentSolution;
    }

    public SimpleDirectedGraph<VertexImplementation, EdgeImplementation> getGraphObject() {
        return (SimpleDirectedGraph<VertexImplementation, EdgeImplementation>)this.graph;
    }

    public int inDegree(int vertex) {
        VertexImplementation mappedVertex = this.vertices.get(vertex);
        if (mappedVertex == null) {
            return -1;
        }
        return this.graph.inDegreeOf(mappedVertex);
    }

    public int outDegree(int vertex) {
        VertexImplementation mappedVertex = this.vertices.get(vertex);
        if (mappedVertex == null) {
            return -1;
        }
        return this.graph.outDegreeOf(mappedVertex);
    }

    public int[] getPredecessors(int vertex) {
        VertexImplementation mappedVertex = this.vertices.get(vertex);
        if (mappedVertex == null) {
            return null;
        }
        int[] predecessors = new int[this.graph.inDegreeOf(mappedVertex)];
        int i = 0;
        for (EdgeImplementation edge : this.graph.incomingEdgesOf(mappedVertex)) {
            predecessors[i++] = Graphs.getOppositeVertex(this.graph, edge, mappedVertex).getId();
        }
        return predecessors;
    }

    public int[] getVertices() {
        int[] vs = new int[this.graph.vertexSet().size()];
        int i = 0;
        for (VertexImplementation vertex : this.graph.vertexSet()) {
            vs[i++] = vertex.getId();
        }
        return vs;
    }

    public boolean removeVertex(int vertex) {
        if (!this.readonly) {
          VertexImplementation mappedVertex = this.vertices.get(vertex);
          if (mappedVertex == null) {
              return false;
          }
          for (int predecessor : this.getPredecessors(vertex)) {
              this.edges.remove(new Tuple<Integer, Integer>(predecessor, vertex));
          }
          for (int successor : this.getSuccessors(vertex)) {
              this.edges.remove(new Tuple<Integer, Integer>(vertex, successor));
          }
          this.vertices.remove(mappedVertex);
          return this.graph.removeVertex(mappedVertex);
      } else {
          return false;
      }
    }

    public boolean isAcyclic() {
        return studentSolution.isAcyclic(this);
    }

    @Override
    public Object clone() {
        SimpleDirectedGraph<VertexImplementation, EdgeImplementation> g = (SimpleDirectedGraph<VertexImplementation, EdgeImplementation>)
            ((SimpleDirectedGraph<VertexImplementation, EdgeImplementation>)this.graph).clone();
        return new Graph(g, this.readonly, dfvsLength, this.studentSolution);
    }

    public Graph editableCopy() {
        SimpleDirectedGraph<VertexImplementation, EdgeImplementation> g = (SimpleDirectedGraph<VertexImplementation, EdgeImplementation>)
            ((SimpleDirectedGraph<VertexImplementation, EdgeImplementation>)this.graph).clone();
        return new Graph(g, false, dfvsLength, this.studentSolution);
    }

    public int getDfvsLength() {
        return this.dfvsLength;
    }
}
