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
 * Created by Olivice on 22/11/2016.
 */
public class LoadTourCommand implements Command {

	private File file;
	int k = 0;
	
    public LoadTourCommand(File file) {
    	this.file = file;
    }

    @Override
    public boolean doCommand() {
		Window.tour = new Tour(file, Window.plan);
		k=0;
		Graph g = new Graph(Window.plan, Window.tour);
		TSP1 tsp = new TSP1();
		tsp.chercheSolution(20000, g);
		List<Path> paths = g.getPaths();
		List<Intersection> intersections = new LinkedList();
		Map<Integer, Section> sections = new HashMap<>();
		for(int i=0;i<g.getCrossingPoints().size()-1;i++)
		{
			//System.out.print(tsp.getMeilleureSolution(i)+" -> ");
			Integer id = tsp.getMeilleureSolution(i);
			Integer id2 = tsp.getMeilleureSolution(i+1);
			//System.out.println(id+" : " +id2);
			paths.forEach(
					path->{
						//
						
						if(path.getOrigin().getId() == id && path.getDestination().getId() == id2) {
							System.out.println(path.getOrigin().getId() + "->" + path.getDestination().getId());
							for(int j = 0; j < path.getSections().size(); j++)
							{
								System.out.println(j+k);
								sections.put(j+k, path.getSections().get(j));
							}
							k += path.getSections().size();
						}
					});
			intersections.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(i)).getIntersection());
		}
		intersections.add(Window.tour.getCrossingPoints().get(tsp.getMeilleureSolution(g.getCrossingPoints().size()-1)).getIntersection());
		//System.out.println(tsp.getMeilleureSolution(g.getCrossingPoints().size()-1));
		
		Window.tour.setIntersections(intersections);
		
		
		Window.tour.setSections(sections);
		
		
		System.out.println("Cout total : "+tsp.getCoutMeilleureSolution());
	
        return true;
    }
}
