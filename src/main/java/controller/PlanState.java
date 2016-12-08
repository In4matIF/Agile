package controller;

import model.Plan;
import model.Tour;
import view.Window;

import java.io.File;

/**
 * Etat o� le plan est charg� mais pas la livraison
 */
public class PlanState extends DefaultState{

    @Override
    public void loadTour(Window window, CommandList commandList, File file) {
        Window.tour = new Tour(file, Window.plan);
        //TODO : add TSP
        boolean isOk = commandList.addCommand(new LoadTourCommand(file));
        if(isOk){
            Controller.setCurrentState(Controller.tourState);
        }else{
            Controller.setCurrentState(Controller.errorState);
        }
    }

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Window.plan = new Plan();
        Window.tour = new Tour();
        commandList.addCommand(new RenitializePlanCommand());
        Controller.setCurrentState(Controller.initState);
    }
}
