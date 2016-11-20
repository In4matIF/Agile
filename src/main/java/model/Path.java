package model;

import java.util.List;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Path {

    private List<Section> sections;

    public Path() {
    }

    public Path(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Path{" +
                "sections=" + sections +
                '}';
    }
}
