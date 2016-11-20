package model;

import java.sql.Time;

/**
 * Created by Olivice on 18/11/2016.
 */
public class DeliveryPoint extends CrossingPoint{

    private Time beginTime;
    private Time endTime;
    private Integer duration;

    public DeliveryPoint() {
    }

    public DeliveryPoint(Intersection intersection, Time beginTime, Time endTime, Integer duration) {
        super(intersection);
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.duration = duration;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DeliveryPoint{" +
                "intersection=" + this.getIntersection() +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }
}
