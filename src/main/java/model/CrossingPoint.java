package model;

/**
 * Created by Olivice on 18/11/2016.
 */
public abstract class CrossingPoint {

    private Intersection intersection;

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
}
