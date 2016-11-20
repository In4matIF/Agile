package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Graph {

    private List<Path> paths;
    private Map<Integer, CrossingPoint> crossingPoints;

    public Graph() {
    }

    public Graph(List<Path> paths, Map<Integer, CrossingPoint> crossingPoints) {
        this.paths = paths;
        this.crossingPoints = crossingPoints;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Map<Integer, CrossingPoint> getCrossingPoints() {
        return crossingPoints;
    }

    public void setCrossingPoints(Map<Integer, CrossingPoint> crossingPoints) {
        this.crossingPoints = crossingPoints;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "paths=" + paths +
                ", crossingPoints=" + crossingPoints +
                '}';
    }
}
