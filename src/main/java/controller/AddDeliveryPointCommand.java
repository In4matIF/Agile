package controller;

import java.util.LinkedList;
import java.util.List;

import model.CrossingPoint;
import model.DeliveryPoint;
import model.Intersection;
import model.Section;
import util.Dijkstra;
import view.Window;

/**
 * Commande ajoutant un point de livraison à la livraison
 * Ne modifie pas les heures de passage des autres livraisons
 */
public class AddDeliveryPointCommand implements Command {
	
	DeliveryPoint toAdd; //Intersection à ajouter
	List<CrossingPoint> listCP; //Liste des CrossingPoints initiale
	List<Section> listSections; //Liste des CrossingPoints initiale

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
		for(int i=1;i<Window.tour.getOrdainedCrossingPoints().size()-2;i++)
		{
			dijkstraGoing.execute(Window.tour.getOrdainedCrossingPoints().get(i).getIntersection());
			
			int goingCost=0;
			int returnCost=0;
			LinkedList<Intersection> goingIntersects = dijkstraGoing.getPath(toAdd.getIntersection());
			LinkedList<Intersection> returnIntersects = dijkstraReturn.getPath(Window.tour.getOrdainedCrossingPoints().get(i+1).getIntersection());
			
			for(int j=0;j<goingIntersects.size()-1;j++)
				goingCost+=goingIntersects.get(j).getSectionTo(goingIntersects.get(j+1)).getDurationSeconds();
			for(int j=0;j<returnIntersects.size()-1;j++)
				returnCost+=returnIntersects.get(j).getSectionTo(returnIntersects.get(j+1)).getDurationSeconds();
			
			
			DeliveryPoint nextDP = (DeliveryPoint) Window.tour.getOrdainedCrossingPoints().get(i+1);
			DeliveryPoint prevDP = (DeliveryPoint) Window.tour.getOrdainedCrossingPoints().get(i);
			
			//Si le temps d'attente au point suivant est suffisant que le temps ajouté à la tournée plus faible que le meilleur temps trouvé,
			//et que les plages horaires sont respectées, on le garde
			if(nextDP.getArrival() + nextDP.getWaitTime() - prevDP.getDeparture() > goingCost+returnCost+toAdd.getDuration()
					&& goingCost+returnCost < bestCost
					&& toAdd.getBeginTime()< prevDP.getDeparture()+goingCost
					&& toAdd.getEndTime()> prevDP.getDeparture()+goingCost)
			{
				bestCrossingPoint = i;
				bestCost = goingCost+returnCost;
			}
		}
		
		//Si on a pas trouvé de créneau adapté, on l'ajoute à la fin
		if(bestCrossingPoint == -1)
		{
			int i = Window.tour.getOrdainedCrossingPoints().size()-2;
			dijkstraGoing.execute(Window.tour.getOrdainedCrossingPoints().get(i).getIntersection());
			
			int goingCost=0;
			int returnCost=0;
			LinkedList<Intersection> goingIntersects = dijkstraGoing.getPath(toAdd.getIntersection());
			LinkedList<Intersection> returnIntersects = dijkstraReturn.getPath(Window.tour.getOrdainedCrossingPoints().get(i+1).getIntersection());
			
			for(int j=0;j<goingIntersects.size()-1;j++)
				goingCost+=goingIntersects.get(j).getSectionTo(goingIntersects.get(j+1)).getDurationSeconds();
			for(int j=0;j<returnIntersects.size()-1;j++)
				returnCost+=returnIntersects.get(j).getSectionTo(returnIntersects.get(j+1)).getDurationSeconds();
			
			DeliveryPoint prevDP = (DeliveryPoint) Window.tour.getOrdainedCrossingPoints().get(i);
			
			if(toAdd.getBeginTime()< prevDP.getDeparture()+goingCost
				&& toAdd.getEndTime()> prevDP.getDeparture()+goingCost)
			{
				bestCrossingPoint = i;
				bestCost = goingCost+returnCost;
			}
		}
		
		if(bestCrossingPoint != -1)
		{
			//Supprime l'ancien path entre les deux points
			int startPath = 0;
			while(Window.tour.getSections().get(startPath).getOrigin().getId()!=Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint).getIntersection().getId())
				startPath++; //Trouve l'index de la première intersection du path
			while(Window.tour.getSections().get(startPath).getDestination().getId()!=Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint+1).getIntersection().getId())
			{
				Window.tour.getSections().remove(startPath);
			}
			Window.tour.getSections().remove(startPath);
						
			//Execution de Dijkstra à partir du nouveau point
			dijkstraGoing.execute(Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint).getIntersection());
			
			//Récupération du chemin pour aller au nouveau point
			LinkedList<Intersection> goingIntersects = dijkstraGoing.getPath(toAdd.getIntersection());
			
			//Récupération du chemin pour revenir du nouveau point
			LinkedList<Intersection> returnIntersects = dijkstraReturn.getPath(Window.tour.getOrdainedCrossingPoints().get(bestCrossingPoint+1).getIntersection());
			
			//Ajout des sections pour aller au nouveau point
			for(int i=0;i<goingIntersects.size()-1;i++)
			{
				//Window.tour.getIntersections().add(startPath+i, intersectionsToAdd.get(i));
				Window.tour.getSections().add(startPath+i,goingIntersects.get(i).getSectionTo(goingIntersects.get(i+1)));
			}
			
			startPath+=goingIntersects.size()-1;
			
			//Ajout des sections pour revenir du nouveau point et aller au point suivant
			for(int i=0;i<returnIntersects.size()-1;i++)
			{
				//Window.tour.getIntersections().add(startPath+i, intersectionsToAdd.get(i));
				Window.tour.getSections().add(startPath+i,returnIntersects.get(i).getSectionTo(returnIntersects.get(i+1)));
			}
			
			Window.tour.getOrdainedCrossingPoints().add(bestCrossingPoint, toAdd);
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
