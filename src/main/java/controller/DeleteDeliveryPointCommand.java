package controller;

import java.util.LinkedList;
import java.util.List;

import model.*;
import util.Dijkstra;
import view.Window;

public class DeleteDeliveryPointCommand implements Command {

	
	DeliveryPoint toDelete;
	List<CrossingPoint> listCP;
	List<Section> listSections;
	
	public DeleteDeliveryPointCommand(DeliveryPoint toDelete) {
		this.toDelete = toDelete;
		listCP = new LinkedList<>();
		listSections = new LinkedList<>();
	}

	@Override
	public boolean doCommand() {
		listCP.addAll(Window.tour.getOrdainedCrossingPoints());
		listSections.addAll(Window.tour.getSections());
		
		int index = Window.tour.getOrdainedCrossingPoints().indexOf(toDelete);
		
		//Remove the old path from the Tour
		int startPath = 0;
		int timeSaved = 0;
		while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(index-1).getIntersection().getId())
			startPath++; //Find the first section of the path to delete
		while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(index+1).getIntersection().getId())
		{
			timeSaved+=Window.tour.getSections().get(startPath).getDurationSeconds();
			Window.tour.getSections().remove(startPath);
		}
		
		Dijkstra dijkstra = new Dijkstra(Window.plan);
		dijkstra.execute(Window.tour.getOrdainedCrossingPoints().get(index-1).getIntersection());
		LinkedList<Intersection> intersectionsToAdd = dijkstra.getPath(Window.tour.getOrdainedCrossingPoints().get(index+1).getIntersection());
		
		int timeAdd = 0;
		for(int i=0;i<intersectionsToAdd.size()-1;i++)
		{
			timeAdd+=intersectionsToAdd.get(i).getSectionTo(intersectionsToAdd.get(i+1)).getDurationSeconds();
			Window.tour.getSections().add(startPath+i,intersectionsToAdd.get(i).getSectionTo(intersectionsToAdd.get(i+1)));
		}

		((DeliveryPoint)Window.tour.getOrdainedCrossingPoints().get(index+1)).setWaitTime(((DeliveryPoint)Window.tour.getOrdainedCrossingPoints().get(index+1)).getWaitTime()+timeSaved-timeAdd);
		((DeliveryPoint)Window.tour.getOrdainedCrossingPoints().get(index+1)).setArrival(((DeliveryPoint)Window.tour.getOrdainedCrossingPoints().get(index+1)).getArrival()-timeSaved+timeAdd);
		Window.tour.getCrossingPoints().remove(toDelete.getIntersection().getId());
		Window.tour.getOrdainedCrossingPoints().remove(index);
		
		return true;
	}
	
	@Override
	public boolean undoCommand() {
		Window.tour.setOrdainedCrossingPoints(listCP);
		Window.tour.setSections(listSections);
		return true;
	}

	public boolean isDoable() {
		return true;
	}
}
