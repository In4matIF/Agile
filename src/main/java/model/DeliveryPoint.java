package model;

import model.CrossingPoint;
import model.Intersection;

import java.sql.Time;

/**
 * Objet reprï¿½sentant un point de livraison pour le vendeur
 */
public class DeliveryPoint extends CrossingPoint {

    /**
     * Temps d'attente sur le point
     */
	private long waitTime;
	
	/**
	 * Heure d'arrivée au point (en secondes)
	 */
    private long arrival;
    
    /**
     * Heure de départ du point (en secondes)
     */
    private long departure;

    public DeliveryPoint() {
    }

    public DeliveryPoint(Intersection intersection, long beginTime, long endTime, Integer duration) {
        super(intersection);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.setDuration(duration);
    }
    
    public DeliveryPoint(Intersection intersection, Integer duration) {
        super(intersection);
        this.setDuration(duration);
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "model.DeliveryPoint{" +
                "intersection=" + this.getIntersection() +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", duration=" + this.getDuration() +
                '}';
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getArrival() {
        return arrival;
    }

    public void setArrival(long arrival) {
        this.arrival = arrival;
    }

    public long getDeparture() {
        return departure;
    }

    public void setDeparture(long departure) {
        this.departure = departure;
    }
}
