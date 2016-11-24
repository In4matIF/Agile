package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Objet repr駸entant un chemin d'un CrossingPoint � un autre.
 * Contient la liste des sections composant le chemin
 */
public class Path {

    private List<Section> sections;
    private int length;

    public Path() {
    }

    public Path(List<Section> sections, int length) {
        this.sections = sections;
        this.length = length;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public Intersection getOrigin(){
		return sections.get(0).getOrigin();
	}
	
	public Intersection getDestination(){
		return sections.get(sections.size()-1).getDestination();
	}
	

	@Override
    public String toString() {
        return "Path{" +
                "sections=" + sections +
                '}';
    }
    
    
}
