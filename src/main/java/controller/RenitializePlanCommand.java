package controller;

/**
 * Commande pour reinitialiser le plan charge
 */
public class RenitializePlanCommand implements Command {

    public RenitializePlanCommand() {
    }

    @Override
    public boolean doCommand() {
        return true;
    }
    
    public boolean undoCommand() {
    	return true;
    }
    
	public boolean isDoable() {
		return false;
	}
}
