package controller;

import view.Window;

/**
 * Etat d'erreur de l'application
 */
public class ErrorState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        commandList.addCommand(new RenitializePlanCommand());
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {
        commandList.addCommand(new RenitializeTourCommand());
        Controller.setCurrentState(Controller.planState);
    }
}
