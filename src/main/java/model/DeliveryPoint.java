package model;

import model.CrossingPoint;
import model.Intersection;

import java.sql.Time;

/**
 * Created by Olivice on 18/11/2016.
 */
public class DeliveryPoint extends CrossingPoint {

    private Time beginTime;
    private Time endTime;

    public DeliveryPoint() {
    }

    public DeliveryPoint(Intersection intersection, Time beginTime, Time endTime, Integer duration) {
        super(intersection);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.setDuration(duration);
    }
    
    public DeliveryPoint(Intersection intersection, Integer duration) {
        super(intersection);
        this.setDuration(duration);
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
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
