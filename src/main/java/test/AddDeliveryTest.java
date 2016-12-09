package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import controller.AddDeliveryPointCommand;
import controller.LoadTourCommand;
import model.DeliveryPoint;
import model.Plan;
import model.Tour;
import view.Window;

/**
 * Classe de test JUnit sur la commande d'ajout de point de livraison AddDeliveryPoint
 */
public class AddDeliveryTest {
	
	Tour t;
	
	/**
	 * Creation d'un Tour et d'un Plan
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	    Window.plan = new Plan(new File("src/main/resources/xml/testGraphPlan.xml"));
	    LoadTourCommand lt = new LoadTourCommand(new File("src/main/resources/xml/testGraphTour.xml"));
	    lt.doCommand();
	       
	    AddDeliveryPointCommand adp = new AddDeliveryPointCommand(new DeliveryPoint(Window.plan.getIntersections().get(4), 0, Long.MAX_VALUE, 900));
	    adp.doCommand();
	    t = Window.tour;
	}
	
	/**
	 * Verification du nouveau chemin apres ajout du nouveau point
	 */
	@Test
	public void testAddDelivery()
	{
		assertEquals(t.getSections().get(0).getOrigin().getId(),(Integer) 1);
		assertEquals(t.getSections().get(0).getDestination().getId(),(Integer) 2);
		
		assertEquals(t.getSections().get(1).getOrigin().getId(),(Integer) 2);
		assertEquals(t.getSections().get(1).getDestination().getId(),(Integer) 3);
		
		assertEquals(t.getSections().get(2).getOrigin().getId(),(Integer) 3);
		assertEquals(t.getSections().get(2).getDestination().getId(),(Integer) 5);
		
		assertEquals(t.getSections().get(3).getOrigin().getId(),(Integer) 5);
		assertEquals(t.getSections().get(3).getDestination().getId(),(Integer) 3);
		
		assertEquals(t.getSections().get(4).getOrigin().getId(),(Integer) 3);
		assertEquals(t.getSections().get(4).getDestination().getId(),(Integer) 4);
		
		assertEquals(t.getSections().get(5).getOrigin().getId(),(Integer) 4);
		assertEquals(t.getSections().get(5).getDestination().getId(),(Integer) 3);
		
		assertEquals(t.getSections().get(6).getOrigin().getId(),(Integer) 3);
		assertEquals(t.getSections().get(6).getDestination().getId(),(Integer) 2);
		
		assertEquals(t.getSections().get(7).getOrigin().getId(),(Integer) 2);
		assertEquals(t.getSections().get(7).getDestination().getId(),(Integer) 1);
		
		assertNotNull(t.getCrossingPoints().get(4));
		
		assertEquals(t.getOrdainedCrossingPoints().get(0).getIntersection().getId(),(Integer) 1);
		assertEquals(t.getOrdainedCrossingPoints().get(1).getIntersection().getId(),(Integer) 3);
		assertEquals(t.getOrdainedCrossingPoints().get(2).getIntersection().getId(),(Integer) 4);
		assertEquals(t.getOrdainedCrossingPoints().get(3).getIntersection().getId(),(Integer) 5);
	}
}
