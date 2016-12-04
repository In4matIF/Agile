package controller;

import java.util.LinkedList;

import model.*;
import util.Dijkstra;
import view.Window;

public class DeleteDeliveryPointCommand implements Command {

	DeliveryPoint toDelete;
	
	public DeleteDeliveryPointCommand(DeliveryPoint toDelete) {
		this.toDelete = toDelete;
	}

	@Override
	public boolean doCommand() {
		int index = Window.tour.getOrdainedCrossingPoints().indexOf(toDelete);
		
		//Remove the old path from the Tour
		//int startPath = Window.tour.getIntersections().indexOf(Window.tour.getOrdainedCrossingPoints().get(index-1).getIntersection());
		int startPath = 0;
		while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(index-1).getIntersection().getId())
			startPath++;
		while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(index+1).getIntersection().getId())
		{
			//Window.tour.getIntersections().remove(currentIntersect);
			Window.tour.getSections().remove(startPath);
		}
		
		Dijkstra dijkstra = new Dijkstra(Window.plan);
		dijkstra.execute(Window.tour.getOrdainedCrossingPoints().get(index-1).getIntersection());
		LinkedList<Intersection> intersectionsToAdd = dijkstra.getPath(Window.tour.getOrdainedCrossingPoints().get(index+1).getIntersection());
		
		
		for(int i=1;i<intersectionsToAdd.size()-1;i++)
		{
			//Window.tour.getIntersections().add(startPath+i, intersectionsToAdd.get(i));
			Window.tour.getSections().add(startPath+i,intersectionsToAdd.get(i).getSectionTo(intersectionsToAdd.get(i+1)));
		}
		
		return true;
	}

}
