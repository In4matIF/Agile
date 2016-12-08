package controller;

import model.Plan;
import model.Tour;
import view.Window;

/**
 * Etat d'erreur de l'application
 */
public class ErrorState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Window.plan = new Plan();
        Window.tour = new Tour();
        commandList.addCommand(new RenitializePlanCommand());
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {
        Window.tour = new Tour();
        commandList.addCommand(new RenitializeTourCommand());
        Controller.setCurrentState(Controller.planState);
    }
}
