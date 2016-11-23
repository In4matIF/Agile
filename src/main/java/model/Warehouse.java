package model;

import java.sql.Time;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Warehouse extends CrossingPoint{

    private String departureTime;

    public Warehouse() {
    }

    public Warehouse(Intersection intersection, String departureTime) {
        super(intersection);
        this.departureTime = departureTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
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
