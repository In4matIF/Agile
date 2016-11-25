package controller;

/**
 * Commande pour reinitialiser le plan chargé
 */
public class RenitializePlanCommand implements Command {

    public RenitializePlanCommand() {
    }

    @Override
    public boolean doCommand() {
        return true;
    }
}
