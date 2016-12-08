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

import controller.GenerateTourSheetCommand;
import controller.LoadPlanCommand;
import controller.LoadTourCommand;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Olivice on 06/12/2016.
 */
public class GenerateTourSheetCommandTest {

    private GenerateTourSheetCommand GTSC = new GenerateTourSheetCommand();


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCommand() {
    	LoadPlanCommand LPC = new LoadPlanCommand(new File("/src/main/resources/xml/testGraphPlan.xml"));
    	LPC.doCommand();
        LoadTourCommand LTC = new LoadTourCommand(new File("/src/main/resources/xml/testGraphTour.xml"));
        LTC.doCommand();
    	String bigText = "Prenez la rue v0"
    			+ "Continuez tour droit vers la rue v0"
    			+ "Prenez la 1ere a gauche vers la rue v0"
    			+ "Tournez a gauche vers la rue h0"
    			+ "Tournez a gauche vers la rue v0";
        GTSC.doCommand();
		BufferedReader br = null;
		FileReader fr = null;
		String feuilleDeRouteString = "";
		try {

			fr = new FileReader("FeuilleDeRoute.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;
			br = new BufferedReader(fr);


			while ((sCurrentLine = br.readLine()) != null) {
				feuilleDeRouteString += sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		boolean testPass = false;
		
		System.out.println(bigText);
		System.out.println(feuilleDeRouteString);
		
		if(feuilleDeRouteString.equals(bigText))
			testPass = true;
		
		assertEquals(testPass, true);
    }
    
    
}
