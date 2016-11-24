package controller;

import model.Tour;
import view.Window;

import java.io.File;

/**
 * Created by Olivice on 18/11/2016.
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
}
