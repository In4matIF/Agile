package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Dijkstra;

/**
 * Graph object
 * @author Tom
 *
 */
public class Graph {

    private List<Path> paths;
    private Map<Integer, CrossingPoint> crossingPoints;
    private int idWarehouse;

    public Graph() {
    	
    }
    
    /**
     * Graph constructor based on a Plan and a model.Tour to generate the graph
     * @param p the Plan object
     * @param t the model.Tour object
     */
    public Graph(Plan p,Tour t) {
    	
    	crossingPoints = t.getCrossingPoints();
    	idWarehouse = t.getIdWarehouse();
    	crossingPoints.get(idWarehouse).setDuration(0);
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
					
					Path newPath = new Path(sections,length);
					paths.add(newPath);
					
					// Add the generated path to the crossing point
					origin.getValue().addPath(newPath, listinter.getLast().getId());
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

    public int getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(int idWarehouse) {
        this.idWarehouse = idWarehouse;
    }
}
