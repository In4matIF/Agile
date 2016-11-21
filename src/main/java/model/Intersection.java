package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Intersection {

    private Integer id;
    private Integer x;
    private Integer y;
    private List<Section> sections;

    public Intersection() {
    }

    public Intersection(Integer id, Integer x, Integer y) {
        this.id = id;
        this.x = x;
        this.y = y;
        sections = new ArrayList<>();
    }

    public Intersection(Integer id, Integer x, Integer y, ArrayList sections) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.sections = sections;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public List getSections() {
        return sections;
    }

    public void setSections(ArrayList sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", sections=" + sections +
                '}';
    }
}
