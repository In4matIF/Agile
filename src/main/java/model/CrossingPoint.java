package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite qui represente un point de passage du livreur (Warehouse ou DeliveryPoint)
 */
public abstract class CrossingPoint {

    /**
     * Intersection liee au CrossingPoint
     */
	private Intersection intersection;
	
	/**
	 * Map des paths vers d'autres CrossingPoints
	 */
    private Map<Integer, Path> paths;
    
    /**
     * Duree de livraison
     */
    private Integer duration;
    
    /**
     * Debut de la plage horaire de livraison
     */
    protected long beginTime = 0;
    
    /**
     * Fin de la plage horaire de livraison
     */
    protected long endTime = Long.MAX_VALUE;

    public CrossingPoint() {
    	paths = new HashMap<Integer,Path>();
    }

    public CrossingPoint(Intersection intersection) {
        this.intersection = intersection;
        paths = new HashMap<Integer,Path>();
    }

    public abstract long getBeginTime();

    public abstract void setBeginTime(long beginTime);

    public abstract long getEndTime();

    public abstract void setEndTime(long endTime);

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
