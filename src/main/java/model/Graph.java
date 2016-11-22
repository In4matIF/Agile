package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Dijkstra.Dijkstra;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Graph {

    private List<Path> paths;
    private Map<Integer, CrossingPoint> crossingPoints;

    public Graph() {
    	
    	List<Path> paths = new ArrayList<Path>();
		Dijkstra dijkstra = new Dijkstra(p);
		
		for(Map.Entry<Integer,CrossingPoint> origin : t.getCrossingPoints().entrySet())
		{
			dijkstra.execute(origin.getValue().getIntersection());
			
			for(Map.Entry<Integer,CrossingPoint> destination : t.getCrossingPoints().entrySet())
			{
				if(!destination.equals(origin))
				{
					LinkedList<Intersection> listinter = dijkstra.getPath(destination.getValue().getIntersection());
					List<Section> sections = new ArrayList<Section>();
					int length = 0;
					for(int i=0 ; i<listinter.size()-1; i++)
					{
						Section toAdd = listinter.get(i).getSectionTo(listinter.get(i+1));
						sections.add(toAdd);
						length += toAdd.getLength();
					}
					
					paths.add(new Path(sections,length));
				}
			}
		}
		
		this.paths=paths;
    }

    public Graph(List<Path> paths, Map<Integer, CrossingPoint> crossingPoints) {
        this.paths = paths;
        this.crossingPoints = crossingPoints;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Map<Integer, CrossingPoint> getCrossingPoints() {
        return crossingPoints;
    }

    public void setCrossingPoints(Map<Integer, CrossingPoint> crossingPoints) {
        this.crossingPoints = crossingPoints;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "paths=" + paths +
                ", crossingPoints=" + crossingPoints +
                '}';
    }
}
