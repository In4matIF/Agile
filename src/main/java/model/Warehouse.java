package model;

import java.sql.Time;

/**
 * Objet repr�sentant l'entrep�t de d�part de la livraison
 */
public class Warehouse extends CrossingPoint{

    private String departureTime;

    public Warehouse() {
    }

    public long getBeginTime() { return 0; }

    public void setBeginTime(long beginTime) {}

    public long getEndTime() { return Long.MAX_VALUE; }

    public void setEndTime(long endTime) {}

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
