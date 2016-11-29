package controller;

import view.Window;

/**
 * Etat où la livraison est chargée et calculée
 */
public class TourState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.planState);
    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) {
    	commandList.addCommand(new GenerateTourSheetCommand());
        Controller.setCurrentState(Controller.tourState);
    }
}
