package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Commande liee au chargement du Plan
 */
public class LoadPlanCommand implements Command {

    private File file;

    public LoadPlanCommand( File xmlFile) {
        file = xmlFile;
    }

    @Override
    public boolean doCommand() throws Exception{
        Window.plan = new Plan(file);
        return true;
    }
    
    public boolean undoCommand() {
    	return true;
    }
    
	public boolean isDoable() {
		return false;
	}
}
