package controller;

/**
 * Etat pour réinitialiser la livraison chargée
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
