package model;

import model.CrossingPoint;
import model.Intersection;

import java.sql.Time;

/**
 * Objet repr�sentant un point de livraison pour le vendeur
 */
public class DeliveryPoint extends CrossingPoint {

    private long beginTime;
    private long endTime;

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
}
