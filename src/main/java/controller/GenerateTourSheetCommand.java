package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.Section;
import view.Window;

/**
 * Commande liée à la génération d'une feuille de route
 */
public class GenerateTourSheetCommand implements Command {

    public GenerateTourSheetCommand() {
    }

    @Override
    public boolean doCommand() {
    	String tourMessage = "";
    	Map<Integer, Section> sections = Window.tour.getSections();
    	tourMessage += "Prenez la rue "+ sections.get(0).getStreet() + "\r\n";
    	for(int i = 1; i < sections.size(); i++)
    	{
    	    int x1 = sections.get(i-1).getDestination().getX() - sections.get(i-1).getOrigin().getX();
    	    int y1 = sections.get(i-1).getDestination().getY() - sections.get(i-1).getOrigin().getY();
    	    int x2 = sections.get(i).getDestination().getX() - sections.get(i).getOrigin().getX();
    	    int y2 = sections.get(i).getDestination().getY() - sections.get(i).getOrigin().getY();
    	    
    	    double angle1 = Math.atan2(x1, y1);
    	    double angle2 = Math.atan2(x2, y2);
    	    
    	    int dotProduct = x1*x2 + y1*y2;
    	    double deltaA = Math.toDegrees(angle1 - angle2);
    	    
    	    tourMessage += "Degre : "+deltaA+" / ProduitScalaire : "+dotProduct+"\r\n" ;
    	    if(deltaA < -45)
    	    {
    	    	tourMessage += "Tournez a gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	    
    	    else if(deltaA < -20)
    	    {
    	    	tourMessage += "Tournez legerement à gauche vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	    
    	    else if(deltaA == 180)
    	    {
    	    	tourMessage += "Faites demi tour vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	    
    	    else if(deltaA > 45)
    	    {
    	    	tourMessage += "Tournez a droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
    	    }
    	    
    	    else if(deltaA > 20)
    	    {
    	    	tourMessage += "Tournez legerement à droite vers la rue "+ sections.get(i).getStreet() + "\r\n\n";
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
}
