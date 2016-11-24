package model;


/**
 * Tronçon reliant deux intersections
 * Définis dans le xml du plan
 */
public class Section {

    private Intersection origin;
    private Intersection destination;
    private Integer length;
    private Integer speed;
    private String street;

    public Section() {
    }

    public Section(Intersection origin, Intersection destination, Integer length, Integer speed, String street) {
        this.origin = origin;
        this.destination = destination;
        this.length = length;
        this.speed = speed;
        this.street = street;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Section{" +
                "origin=" + origin.getId() +
                ", destination=" + destination.getId() +
                ", length=" + length +
                ", speed=" + speed +
                ", street='" + street + '\'' +
                '}';
    }
}
