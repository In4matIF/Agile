package controller;

/**
 * Etat pour r�initialiser la livraison charg�e
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
