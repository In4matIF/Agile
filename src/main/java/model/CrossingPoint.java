package model;

import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public abstract class CrossingPoint {

    private Intersection intersection;
    private Map<Integer, Path> paths;
    private Integer duration;

    public CrossingPoint() {
    }

    public CrossingPoint(Intersection intersection) {
        this.intersection = intersection;
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
