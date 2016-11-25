package controller;

import java.io.File;

import model.*;
import util.TSP1;

/**
 * Classe de test du calcul de la tourn�e de bout en bout (provisoire)
 */
public class Testsalgo {

	public static void main(String[] args) {
		File xmlPlan = new File("src/main/resources/xml/plan20x20.xml");
		Plan p = new Plan(xmlPlan);
		
		File xmlTour = new File("src/main/resources/xml/livraisons20x20-14.xml");
		Tour t = new Tour(xmlTour,p);
		
		Graph g = new Graph(p,t);
		
		TSP1 tsp = new TSP1();
		tsp.chercheSolution(100, g);
		for(int i=0;i<g.getCrossingPoints().size()-1;i++)
		{
			System.out.print(tsp.getMeilleureSolution(i)+" -> ");
		}
		System.out.println(tsp.getMeilleureSolution(g.getCrossingPoints().size()-1));
		
		System.out.println("Cout total : "+tsp.getCoutMeilleureSolution());
	}

}
