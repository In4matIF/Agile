package model;

import java.sql.Time;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Warehouse extends CrossingPoint{

    private Long departureTime;

    public Warehouse() {
    }

    public Warehouse(Intersection intersection, Long departureTime) {
        super(intersection);
        this.departureTime = departureTime;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Long departureTime) {
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
