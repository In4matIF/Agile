package test;

import model.Graph;
import model.Intersection;
import model.Path;
import model.Plan;
import model.Section;
import model.Tour;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

/**
* Classe de test de l'objet Graph
*/
public class GraphTest {

    private Graph g;


    /**
     * Création du graphe
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    	Plan plan = new Plan(new File("src/main/resources/xml/testGraphPlan.xml"));
        g = new Graph(plan, new Tour(new File("src/main/resources/xml/testGraphTour.xml"), plan));
    }

   @After
   public void tearDown() throws Exception {
   }

   /**
    * Test des arretes générées
    */
   @Test
   public void testGraph() {
       List<Path> paths = g.getPaths();
       assertEquals(g.getPaths().size(), 6);
       boolean path13 = false;
       boolean path15 = false;
       boolean path31 = false;
       boolean path35 = false;
       boolean path51 = false;
       boolean path53 = false;
       boolean wrong = false;


       for (int i = 0; i < 6; i++)
       {
            if(paths.get(i).getOrigin().getId() == 1 && paths.get(i).getDestination().getId() == 3 && paths.get(i).getLength() == 15268)
                path13 = true;
            else if(paths.get(i).getOrigin().getId() == 1 && paths.get(i).getDestination().getId() == 5 && paths.get(i).getLength() == 19318)
                path15 = true;
            else if(paths.get(i).getOrigin().getId() == 3 && paths.get(i).getDestination().getId() == 1 && paths.get(i).getLength() == 15268)
                path31 = true;
            else if(paths.get(i).getOrigin().getId() == 3 && paths.get(i).getDestination().getId() == 5 && paths.get(i).getLength() == 4050)
                path35 = true;
            else if(paths.get(i).getOrigin().getId() == 5 && paths.get(i).getDestination().getId() == 1 && paths.get(i).getLength() == 22900)
                path51 = true;
            else if(paths.get(i).getOrigin().getId() == 5 && paths.get(i).getDestination().getId() == 3 && paths.get(i).getLength() == 15203)
                path53 = true;
            else
                wrong = true;
       }
       
       assertEquals(path13, true);
       assertEquals(path15, true);
       assertEquals(path31, true);
       assertEquals(path35, true);
       assertEquals(path51, true);
       assertEquals(path53, true);
       assertEquals(wrong, false);
   }
}