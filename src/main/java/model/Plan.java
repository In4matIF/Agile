package model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Plan implements Observable{

    private Map<Integer, Intersection> intersections = new HashMap<Integer, Intersection>();
    private Map<Integer, Section> sections = new HashMap<Integer, Section>();

    public Plan() {
    }

    public Plan(Map<Integer, Intersection> intersections, Map<Integer, Section> sections) {
        this.intersections = intersections;
        this.sections = sections;
    }

    public Map<Integer, Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(Map<Integer, Intersection> intersections) {
        this.intersections = intersections;
    }

    public Map<Integer, Section> getSections() {
        return sections;
    }

    public void setSections(Map<Integer, Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "intersections=" + intersections +
                ", sections=" + sections +
                '}';
    }

    public void addListener(InvalidationListener listener) {

    }

    public void removeListener(InvalidationListener listener) {

    }
}
