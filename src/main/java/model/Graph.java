package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.Dijkstra;

/**
 * Objet graphe pour le TSP
 * @author Tom
 *
 */
public class Graph {

    /**
     * Les arrêtes du graphe
     */
	private List<Path> paths;
	
	/**
	 * Les noeuds du graphe (Les points de passage de la livraison)
	 */
    private Map<Integer, CrossingPoint> crossingPoints;
    
    /**
     * L'id de l'entrepôt
     */
    private int idWarehouse;

    public Graph() {
    	
    }
    
    /**
     * Constructeur de Graph ï¿½ partir d'un Plan et d'un Tour
     * Execute Dijkstra pour calculer le graphe
     * @param p l'objet Plan
     * @param t l'object Tour
     */
    public Graph(Plan p,Tour t) {
    	
    	crossingPoints = t.getCrossingPoints();
    	idWarehouse = t.getIdWarehouse();
    	crossingPoints.get(idWarehouse).setDuration(0);
    	List<Path> paths = new ArrayList<Path>();
		
    	Dijkstra dijkstra = new Dijkstra(p); //Construction du graphe à partir du Plan
		
		for(Map.Entry<Integer,CrossingPoint> origin : t.getCrossingPoints().entrySet())
		{
			dijkstra.execute(origin.getValue().getIntersection()); //Application de dijkstra à partir de chaque CrossingPoint
			
			for(Map.Entry<Integer,CrossingPoint> destination : t.getCrossingPoints().entrySet())
			{
				if(!destination.equals(origin))
				{
					//Récupération du chemin le plus court vers chaque CrossingPoint de destination
					LinkedList<Intersection> listinter = dijkstra.getPath(destination.getValue().getIntersection());
					List<Section> sections = new ArrayList<Section>();
					int length = 0;
					int duration = 0;
					
					for(int i=0 ; i<listinter.size()-1; i++) //Transformation en liste de sections
					{
						Section toAdd = listinter.get(i).getSectionTo(listinter.get(i+1));
						sections.add(toAdd);
						length += toAdd.getLength();
						duration += toAdd.getDurationSeconds();
					}
					
					Path newPath = new Path(sections,length,duration);
					paths.add(newPath);
					
					// Ajout de l'arrête générée au CrossingPoint de départ
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

    public void releaseBeginConstraint(int targetId, int secondId){
        CrossingPoint targetCrossingPoint = this.getCrossingPoints().get(targetId);
        CrossingPoint secondCrossingPoint = this.getCrossingPoints().get(secondId);
        targetCrossingPoint.setBeginTime(secondCrossingPoint.getEndTime()
                        - targetCrossingPoint.getPaths().get(secondId).getDuration()
                        - targetCrossingPoint.getDuration()
        );
    }

    public void releaseEndConstraint(int targetId, int secondId){
        CrossingPoint targetCrossingPoint = this.getCrossingPoints().get(targetId);
        CrossingPoint secondCrossingPoint = this.getCrossingPoints().get(secondId);
        targetCrossingPoint.setEndTime(secondCrossingPoint.getBeginTime()
                + secondCrossingPoint.getDuration()
                + secondCrossingPoint.getPaths().get(targetId).getDuration()
        );
    }
}
