package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite qui représente un point de passage du livreur (Warehouse ou DeliveryPoint)
 */
public abstract class CrossingPoint {

    private Intersection intersection;
    private Map<Integer, Path> paths;
    private Integer duration;

    public CrossingPoint() {
    	paths = new HashMap<Integer,Path>();
    }

    public CrossingPoint(Intersection intersection) {
        this.intersection = intersection;
        paths = new HashMap<Integer,Path>();
    }
    
    public void addPath(Path p, int i)
    {
    	paths.put(i, p);
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public void setIntersection(Intersection intersection) {
        this.intersection = intersection;
    }

    @Override
    public String toString() {
        return "CrossingPoint{" +
                "intersection=" + intersection +
                '}';
    }

    public Map<Integer, Path> getPaths() {
        return paths;
    }

    public void setPaths(Map<Integer, Path> paths) {
        this.paths = paths;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
