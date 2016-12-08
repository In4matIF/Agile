package test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import controller.AddDeliveryPointCommand;
import controller.DeleteDeliveryPointCommand;
import controller.LoadTourCommand;
import model.DeliveryPoint;
import model.Plan;
import model.Tour;
import view.Window;

public class DeleteDeliveryTest {
	
	Tour t;
	
	@Before
	public void setUp() throws Exception {
	    Window.plan = new Plan(new File("src/main/resources/xml/testGraphPlan.xml"));
	    LoadTourCommand lt = new LoadTourCommand(new File("src/main/resources/xml/testDeletePoint.xml"));
	    lt.doCommand();
	       
	    DeleteDeliveryPointCommand ddp = new DeleteDeliveryPointCommand((DeliveryPoint)Window.tour.getOrdainedCrossingPoints().get(2));
	    ddp.doCommand();
	    t = Window.tour;
	}
	
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
		assertEquals(t.getSections().get(3).getDestination().getId(),(Integer) 0);
		
		assertEquals(t.getSections().get(4).getOrigin().getId(),(Integer) 0);
		assertEquals(t.getSections().get(4).getDestination().getId(),(Integer) 1);
	}

}
