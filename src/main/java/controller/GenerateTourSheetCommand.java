package controller;

/**
 * Commande liée à la génération d'une feuille de route
 */
public class GenerateTourSheetCommand implements Command {

    public GenerateTourSheetCommand() {
    }

    @Override
    public boolean doCommand() {
        return true;
    }
}
