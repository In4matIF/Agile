package controller;

import java.util.LinkedList;
import java.util.List;

import model.CrossingPoint;
import model.DeliveryPoint;
import model.Intersection;
import model.Section;
import util.Dijkstra;
import view.Window;

public class AddDeliveryPointCommand implements Command {
	
	DeliveryPoint toAdd;
	List<CrossingPoint> listCP;
	List<Section> listSections;

	public AddDeliveryPointCommand(DeliveryPoint toAdd) {
		this.toAdd = toAdd;
		listCP = new LinkedList<>();
		listSections = new LinkedList<>();
	}

	@Override
	public boolean doCommand() {
		
		listCP.addAll(Window.tour.getOrdainedCrossingPoints());
		listSections.addAll(Window.tour.getSections());
		
		//Recherche du meilleur endroit ou ajouter le point
		Dijkstra dijkstraReturn = new Dijkstra(Window.plan);
		Dijkstra dijkstraGoing = new Dijkstra(Window.plan);
		dijkstraReturn.execute(toAdd.getIntersection());
		
		int bestCrossingPoint = -1;
		int bestCost = Integer.MAX_VALUE;
		for(int i=1;i<Window.tour.getOrdainedCrossingPoints().size()-1;i++)
		{
			dijkstraGoing.execute(Window.tour.getOrdainedCrossingPoints().get(i).getIntersection());
			
			int sumCostPath=0;
			LinkedList<Intersection> goingIntersects = dijkstraGoing.getPath(toAdd.getIntersection());
			LinkedList<Intersection> returnIntersects = dijkstraReturn.getPath(Window.tour.getOrdainedCrossingPoints().get(i+1).getIntersection());
			
			for(int j=0;j<goingIntersects.size()-1;j++)
				sumCostPath+=goingIntersects.get(j).getSectionTo(goingIntersects.get(j+1)).getDurationSeconds();
			for(int j=0;j<returnIntersects.size()-1;j++)
				sumCostPath+=returnIntersects.get(j).getSectionTo(returnIntersects.get(j+1)).getDurationSeconds();
			
			DeliveryPoint nextDP = (DeliveryPoint) Window.tour.getOrdainedCrossingPoints().get(i+1);
			DeliveryPoint prevDP = (DeliveryPoint) Window.tour.getOrdainedCrossingPoints().get(i);
			if(/*nextDP.getArrival() + nextDP.getWaitTime() - prevDP.getDeparture() > sumCostPath
					&&*/ sumCostPath < bestCost)
			{
				bestCrossingPoint = i;
				bestCost = sumCostPath;
			}
		}
		
		if(bestCrossingPoint != -1)
		{
			//Remove the old path from the Tour
			int startPath = 0;
			while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint).getIntersection().getId())
				startPath++; //Find the first section of the path to delete
			while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint+1).getIntersection().getId())
			{
				Window.tour.getSections().remove(startPath);
			}
						
			dijkstraGoing.execute(Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint).getIntersection());
			LinkedList<Intersection> goingIntersects = dijkstraGoing.getPath(toAdd.getIntersection());
			LinkedList<Intersection> returnIntersects = dijkstraReturn.getPath(Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint+1).getIntersection());
			
			//Add sections to go to the new point
			for(int i=0;i<goingIntersects.size()-1;i++)
			{
				//Window.tour.getIntersections().add(startPath+i, intersectionsToAdd.get(i));
				Window.tour.getSections().add(startPath+i,goingIntersects.get(i).getSectionTo(goingIntersects.get(i+1)));
			}
			startPath+=goingIntersects.size()-1;
			//Add sections to go to the next point
			for(int i=0;i<returnIntersects.size()-1;i++)
			{
				//Window.tour.getIntersections().add(startPath+i, intersectionsToAdd.get(i));
				Window.tour.getSections().add(startPath+i,returnIntersects.get(i).getSectionTo(returnIntersects.get(i+1)));
			}
			
			Window.tour.getOrdainedCrossingPoints().add(bestCrossingPoint+1, toAdd);
			Window.tour.getCrossingPoints().put(toAdd.getIntersection().getId(), toAdd);
			
			return true;
		}
		else
		{
			System.out.println("Ajout de point de livraison impossible");
			return false;
		}
	}
	
	public boolean undoCommand(){
		Window.tour.setOrdainedCrossingPoints(listCP);
		Window.tour.setSections(listSections);
		return true;
	}
	
	public boolean isDoable(){
		return true;
	}

}
