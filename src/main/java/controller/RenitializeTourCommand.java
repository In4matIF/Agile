package controller;

/**
 * Etat pour reinitialiser la livraison chargee
 */
public class RenitializeTourCommand implements Command {

    public RenitializeTourCommand() {
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
