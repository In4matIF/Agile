package controller;

import model.Plan;
import model.Tour;
import view.Window;

import java.io.File;

/**
 * Etat ou le plan est charge mais pas la livraison
 */
public class PlanState extends DefaultState{

    @Override
    public boolean loadTour(Window window, CommandList commandList, File file) throws Exception {
        Window.tour = new Tour(file, Window.plan);
        boolean isOk = commandList.addCommand(new LoadTourCommand(file));
        if(isOk){
            Controller.setCurrentState(Controller.tourState);
        }else{
            Controller.setCurrentState(Controller.errorState);
        }
        return isOk;
    }

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Window.plan = new Plan();
        Window.tour = new Tour();
        try {
            commandList.addCommand(new RenitializePlanCommand());
        }catch (Exception e){}
        Controller.setCurrentState(Controller.initState);
    }
}
