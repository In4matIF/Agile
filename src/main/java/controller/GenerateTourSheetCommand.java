package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import model.Intersection;
import model.Section;
import view.Window;

/**
 * Commande li鬥･ �ｿｽ la g鬧ｭ鬧ｻation d'une feuille de route
 */
public class GenerateTourSheetCommand implements Command {

    public GenerateTourSheetCommand() {
    }

    @Override
    public boolean doCommand() {
    	String tourMessage = "";
    	List<Section> sections = Window.tour.getSections();
    	tourMessage += "Prenez la rue "+ sections.get(0).getStreet() + "\r\n";
    	
    	for(int i = 1; i < sections.size(); i++)
    	{
    		List<Section> listSections = sections.get(i).getOrigin().getSections();
    		
    		Section sectionToRemove = null;
    		/*for (int j = 0; j < listSections.size(); j ++)
    		{
    			if(listSections.get(j).getDestination() == sections.get(i-1).getOrigin())
    			{
    				sectionToRemove = listSections.get(j);
    				break;
    			}
    		}*/
    		listSections.remove(sectionToRemove);
    		
    		LinkedList<Section> listG = new LinkedList<>(); 
    		LinkedList<Section> listD = new LinkedList<>(); 
    		LinkedList<Pair<Double, Section>> listA = new LinkedList<>();
    		
    		int indexG = 0;
    		int indexD = 0;
    		
    		
			listD.clear();
			listG.clear();
    		
    		for (int j = 0; j < listSections.size(); j++)
    		{


			
	    	    int x1 = sections.get(i-1).getDestination().getX() - sections.get(i-1).getOrigin().getX();
	    	    int y1 = sections.get(i-1).getDestination().getY() - sections.get(i-1).getOrigin().getY();
	    	    int x2 = listSections.get(j).getDestination().getX() - listSections.get(j).getOrigin().getX();
	    	    int y2 = listSections.get(j).getDestination().getY() - listSections.get(j).getOrigin().getY();
	    	    
	    	    double angle1 = Math.atan2(x1, y1);
	    	    double angle2 = Math.atan2(x2, y2);    	    	    
	    	    double deltaA = Math.toDegrees(angle1 - angle2);
	    	    if (deltaA < 0)
	    	    	deltaA += 360;
	    	    
	    	    listA.add(new Pair<Double, Section>(deltaA, listSections.get(j)));
			
    		}
    		
    	    Collections.sort(listA, new Comparator<Pair<Double, Section>>() {
    	    	@Override
    	    	public int compare(Pair<Double, Section> pair1, Pair<Double, Section> pair2) {
    	    		if (Math.abs(pair1.getKey()) > Math.abs(pair2.getKey()))
    	    			return 1;
    	    		else 
    	    			return -1;
    	    	}
   	     	});
    		
    	    listA.forEach(
    	    		pair-> {
    	    			if(pair.getKey() > 180)
    	    				listG.addLast(pair.getValue());
    	    			else if (pair.getKey() < 180 && pair.getKey() != 0)
    	    				listD.push(pair.getValue());   	    				
    	    			
    	    		}
    	    );
    	    
    	    int x1 = sections.get(i-1).getDestination().getX() - sections.get(i-1).getOrigin().getX();
    	    int y1 = sections.get(i-1).getDestination().getY() - sections.get(i-1).getOrigin().getY();
    	    int x2 = sections.get(i).getDestination().getX() - sections.get(i).getOrigin().getX();
    	    int y2 = sections.get(i).getDestination().getY() - sections.get(i).getOrigin().getY();
    	    
    	    double angle1 = Math.atan2(x1, y1);
    	    double angle2 = Math.atan2(x2, y2);
    	    
    	    double deltaA = Math.toDegrees(angle1 - angle2);
    	    if (deltaA < 0)
    	    	deltaA += 360;
    	    
    	    tourMessage += "Degres : "+deltaA+"\r\n";
    	    
    	    if(deltaA < 345 && deltaA > 180)
    	    {
    	    	if (listG.size() == 1)
    	    	{
    	    		if(deltaA > 290)
    	    			tourMessage += "Tournez a legerement gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		else
    	    			tourMessage += "Tournez a gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    	}    	    		
    	    	else
    	    	{
    	    		if(listG.get(0) ==  sections.get(i))
    	    		{
    	    			tourMessage += "Prenez la 1ere a gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		}
    	    		else
    	    		{
    	    			for(int k = 1; k < listG.size(); k++)
    	    				if(listG.get(k) == sections.get(i))
    	    					tourMessage += "Prenez la "+(k+1)+"eme a gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		}
    	    	}
    	    }
    	    
    	    else if(deltaA == 180)
    	    {
    	    	tourMessage += "Faites demi tour vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	    
    	    else if(deltaA > 15 && deltaA < 180 )
    	    {
    	    	if (listD.size() == 1)
    	    	{
    	    		if(deltaA < 20)
    	    			tourMessage += "Tournez a legerement droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		else
    	    			tourMessage += "Tournez a droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    	}    	    		
    	    	else
    	    	{
    	    		if(listD.get(0) ==  sections.get(i))
    	    		{
    	    			tourMessage += "Prenez la 1ere a droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		}
    	    		else
    	    		{
    	    			for(int k = 1; k < listD.size(); k++)
    	    				if(listD.get(k) == sections.get(i))
    	    					tourMessage += "Prenez la "+(k+1)+"eme a droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    		}
    	    	}
    	    }

    	    else
    	    {
    	    	tourMessage += "Continuez tour droit vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	}
    	
    	File output = new File("FeuilleDeRoute.txt");
		try {
			FileWriter fw = new FileWriter(output);
			{
				try {
					fw.write(tourMessage);
				} catch (IOException e) {
					System.out.println("Erreur lors de la lecture des intersections : " + e.getMessage());
					e.printStackTrace();
					fw.close();
					return false;
				}
			}
			fw.close();
		} catch (IOException e) {
			System.out.println("Erreur lors de la lecture : " + e.getMessage());
			return false;
		}
	
        return true;
    }
    
    public boolean undoCommand() {
    	return true;
    }
}
