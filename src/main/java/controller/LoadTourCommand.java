package controller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.CrossingPoint;
import model.Graph;
import model.Intersection;
import model.Path;
import model.Section;
import model.Tour;
import util.TSP1;
import view.Window;

/**
 * Commande liee au chargement de la livraison
 */
public class LoadTourCommand implements Command {

	private File file;
	int k = 0;
	
    public LoadTourCommand(File file) {
    	this.file = file;
    }

    /**
     * Applique les algorithmes de calcul de tournee
     */
    @Override
    public boolean doCommand() throws Exception{
		Window.tour = new Tour(file, Window.plan);
		Graph g = new Graph(Window.plan, Window.tour);
		TSP1 tsp = new TSP1();
		
		tsp.chercheSolution(10000, g);
		
		//Si il n'y a pas de solution trouvee
		if(!tsp.getFoundSolution())
			return false;
		
		List<Path> paths = g.getPaths();
		List<Intersection> intersections = new LinkedList<Intersection>();
		List<Section> sections = new LinkedList<Section>();
		List<CrossingPoint> ordainedCrossingPoints = new LinkedList<CrossingPoint>();
		
		//Ajout du premier Path au depart de l'entrepot
		paths.forEach(
				path->{
					if(path.getOrigin().getId() == g.getIdWarehouse() && (Integer)path.getDestination().getId() == tsp.getMeilleureSolution(1)){
						for(int j = 0; j < path.getSections().size(); j++)
						{
							sections.add(path.getSections().get(j));
						}
					}
				});
		
		ordainedCrossingPoints.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(0)));
		
		//Ajout des paths pour chaque etape du tsp
		for(int i=1;i<g.getCrossingPoints().size()-1;i++)
		{
			Integer id = tsp.getMeilleureSolution(i);
			Integer id2 = tsp.getMeilleureSolution(i+1);
			paths.forEach(
					path->{
						if((Integer)path.getOrigin().getId() == id && (Integer)path.getDestination().getId() == id2){
							for(int j = 0; j < path.getSections().size(); j++)
							{
								sections.add(path.getSections().get(j));
							}
						}
					});
			intersections.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(i)).getIntersection());
			ordainedCrossingPoints.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(i)));
		}
		
		Integer id = tsp.getMeilleureSolution(g.getCrossingPoints().size()-1);
		ordainedCrossingPoints.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(g.getCrossingPoints().size()-1)));
		ordainedCrossingPoints.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(0))); //Ajout du Warehouse a la fin
		
		//Ajout de la derniere etape du tsp
		paths.forEach(
				path->{
					if((Integer)path.getOrigin().getId() == id && (Integer)path.getDestination().getId() == g.getIdWarehouse()){
						for(int j = 0; j < path.getSections().size(); j++)
						{
							sections.add(path.getSections().get(j));
						}
					}
				});
		
		intersections.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(g.getCrossingPoints().size()-1)).getIntersection());
		
		Window.tour.setIntersections(intersections);
		
		
		Window.tour.setSections(sections);
		
		Window.tour.setDuration((int)tsp.getCoutMeilleureSolution());
		
		Window.tour.setOrdainedCrossingPoints(ordainedCrossingPoints);
	
        return true;
    }
    
    /**
     * Pas encore implemente
     */
    public boolean undoCommand() {
    	return true;
    }
    
    /**
     * Pas encore implemente
     */
    public boolean isDoable() {
		return false;
	}
}