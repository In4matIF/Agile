package model;

import java.sql.Time;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Warehouse extends CrossingPoint{

    private Time departureTime;

    public Warehouse() {
    }

    public Warehouse(Intersection intersection, Time departureTime) {
        super(intersection);
        this.departureTime = departureTime;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "intersection=" + this.getIntersection() +
                "departureTime=" + departureTime +
                '}';
    }
}
