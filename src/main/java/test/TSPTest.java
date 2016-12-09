package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import controller.LoadPlanCommand;
import model.Graph;
import model.Tour;
import util.TSP1;
import view.Window;

/**
 * Classe de test de l'algorithme TSP
 */
public class TSPTest {
	
	TSP1 tsp;
	
	/**
	 * Construction du Tour et du Graph puis application du TSP dessus
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	    LoadPlanCommand lp = new LoadPlanCommand(new File("src/main/resources/xml/testGraphPlan.xml"));
	    lp.doCommand();
	    Tour t = new Tour(new File("src/main/resources/xml/testGraphTour.xml"),Window.plan);
	    Graph g = new Graph(Window.plan,t);
	    
	    tsp = new TSP1();
		tsp.chercheSolution(10000, g);
	}
	
	/**
	 * Vérifie la solution renvoyée par le TSP
	 */
	@Test
	public void testSolutionTSP()
	{
		assertEquals(tsp.getMeilleureSolution(0),(Integer)1);
		assertEquals(tsp.getMeilleureSolution(1),(Integer)3);
		assertEquals(tsp.getMeilleureSolution(2),(Integer)5);
		
		assertEquals(tsp.getCoutMeilleureSolution(),33717);
	}

}
