package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Plan;


/**
 * Tests unitaires de JUnit pour la classe Plan
 * @author Tom
 *
 */
public class PlanTest {

	private File xmlPlan;
	private Plan p;
	
	@Before
	public void setUp() throws Exception {
		 xmlPlan = new File("src/main/resources/xml/plan5x5.xml");
		 p = new Plan(xmlPlan);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlanNonVide() {
		assertFalse((p.getIntersections().size()==0)||(p.getSections().size()==0));
	}

	@Test
	public void testPlanNominalCase() {
		assertTrue((p.getIntersections().size()==25)&&(p.getSections().size()==56));
	}
}
