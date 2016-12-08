package controller;

import model.Plan;
import model.Tour;
import view.Window;

/**
 * Etat d'erreur de l'application
 */
public class ErrorState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList){
        Window.plan = new Plan();
        Window.tour = new Tour();
        try {
            commandList.addCommand(new RenitializePlanCommand());
        }catch (Exception e){}
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeTour(Window window, CommandList commandList){
        Window.tour = new Tour();
        try {
            commandList.addCommand(new RenitializeTourCommand());
        }catch (Exception e){}
        Controller.setCurrentState(Controller.planState);
    }
}
