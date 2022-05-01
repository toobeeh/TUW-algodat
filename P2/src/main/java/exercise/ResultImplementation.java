package main.java.exercise;

import main.java.framework.PersistAs;
import main.java.framework.Result;

import java.lang.StringBuilder;

public class ResultImplementation implements Result {

    @PersistAs("graphname")
    String graphname;

    @PersistAs("n_vertices")
    int n_vertices;

    @PersistAs("n_edges")
    int n_edges;

    @PersistAs("duration")
    long duration;

    boolean resIsAcyclic;

    double resHeuristic;

    Heuristic heuristic;

    int[] studentDfvs;

    @PersistAs("studentDfvsLen")
    int studentDfvsLen;

    @PersistAs("studentDfvs")
    String strStudentDfvs;

    @PersistAs("goodDfvsLen")
    int goodDfvsLen;

    public ResultImplementation(String graphname, int n_vertices, int n_edges, long duration, boolean resIsAcyclic,
            double resHeuristic, Heuristic heuristic, int[] studentDfvs, int goodDfvsLen) {
        this.graphname = graphname;
        this.n_vertices = n_vertices;
        this.n_edges = n_edges;
        this.duration = duration;
        this.resIsAcyclic = resIsAcyclic;
        this.resHeuristic = resHeuristic;
        this.heuristic = heuristic;
        this.studentDfvs = studentDfvs;
        if (studentDfvs == null) {
            this.studentDfvsLen = 0;
            this.strStudentDfvs = "";
        } else {
            this.studentDfvsLen = studentDfvs.length;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < studentDfvs.length - 1; i++) {
                builder.append("" + studentDfvs[i] + " ");
            }
            if (studentDfvs.length > 0) {
                builder.append("" + studentDfvs[studentDfvs.length - 1]);
            }
            this.strStudentDfvs = builder.toString();
        }
        this.goodDfvsLen = goodDfvsLen;
    }

    public boolean getResIsAcyclic() {
        return this.resIsAcyclic;
    }

    public double getResHeuristic() {
        return this.resHeuristic;
    }

    public Heuristic getHeuristic() {
        return this.heuristic;
    }

    public int[] getStudentDfvs() {
        return this.studentDfvs;
    }

    public int getGoodDfvsLen() {
        return this.goodDfvsLen;
    }
}
