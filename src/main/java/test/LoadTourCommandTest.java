package test;

import model.Graph;
import model.Intersection;
import model.Path;
import model.Plan;
import model.Section;
import model.Tour;
import view.Window;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.LoadTourCommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

/**
* Classe de test de l'objet Graph
*/
public class LoadTourCommandTest {

    private LoadTourCommand LTC;


    /**
     * Creation du graphe
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    	Window.plan = new Plan(new File("src/main/resources/xml/testGraphPlan.xml"));
    }

   @After
   public void tearDown() throws Exception {
   }

   /**
    * Test des arretes generees
    */
   @Test
   public void testLoadTourCommand() {
       LTC = new LoadTourCommand(new File("src/main/resources/xml/testGraphTour.xml"));
       try {
		LTC.doCommand();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       List<Section> sectionsToTest = Window.tour.getSections();
       boolean test = false;
       
       if(sectionsToTest.get(0).getOrigin().getId() == 1 && sectionsToTest.get(0).getDestination().getId() == 2
    	&& sectionsToTest.get(1).getOrigin().getId() == 2 && sectionsToTest.get(1).getDestination().getId() == 3
    	&& sectionsToTest.get(2).getOrigin().getId() == 3 && sectionsToTest.get(2).getDestination().getId() == 5
    	&& sectionsToTest.get(3).getOrigin().getId() == 5 && sectionsToTest.get(3).getDestination().getId() == 0
    	&& sectionsToTest.get(4).getOrigin().getId() == 0 && sectionsToTest.get(4).getDestination().getId() == 1)
       {
    	   test = true;
       }
       
       assertEquals(test, true);
       
   }
}