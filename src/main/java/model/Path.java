package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Objet repré§¸entant un chemin d'un CrossingPoint ï¿½ un autre.
 * Contient la liste des sections composant le chemin
 */
public class Path {

    private List<Section> sections;
    private int length; //en mètres
    private int duration; //en secondes

    public Path() {
    }

    public Path(List<Section> sections, int length, int duration) {
        this.sections = sections;
        this.length = length;
        this.duration = duration;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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
