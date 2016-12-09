package model;

import java.sql.Time;

/**
 * Objet representant l'entrepot de depart de la livraison
 */
public class Warehouse extends CrossingPoint{

    private long departureTime;

    public Warehouse() {
    }

    public long getBeginTime() { return 0; }

    public void setBeginTime(long beginTime) {}

    public long getEndTime() { return Long.MAX_VALUE; }

    public void setEndTime(long endTime) {}

    public Warehouse(Intersection intersection, long departureTime) {
        super(intersection);
        this.departureTime = departureTime;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
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
