package model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Tour implements Observable{

    private Map<Integer, Section> sections;
    private Map<Integer, CrossingPoint> crossingPoints;
    private Integer duration;

    public Tour() {
    }

    public Tour(Map<Integer, Section> sections, Map<Integer, CrossingPoint> crossingPoints, Integer duration) {
        this.sections = sections;
        this.crossingPoints = crossingPoints;
        this.duration = duration;
    }

    public Map<Integer, Section> getSections() {
        return sections;
    }

    public void setSections(Map<Integer, Section> sections) {
        this.sections = sections;
    }

    public Map<Integer, CrossingPoint> getCrossingPoints() {
        return crossingPoints;
    }

    public void setCrossingPoints(Map<Integer, CrossingPoint> crossingPoints) {
        this.crossingPoints = crossingPoints;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "sections=" + sections +
                ", crossingPoints=" + crossingPoints +
                ", duration=" + duration +
                '}';
    }

    public void addListener(InvalidationListener listener) {

    }

    public void removeListener(InvalidationListener listener) {

    }
}
