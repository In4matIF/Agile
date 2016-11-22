package controller;

import view.Window;

/**
 * Created by Olivice on 18/11/2016.
 */
public class PlanState extends DefaultState{

    @Override
    public void loadTour(Window window, CommandList commandList) {
        boolean isOk = true;
        if(isOk){
            Controller.setCurrentState(Controller.tourState);
        }else{
            Controller.setCurrentState(Controller.errorState);
        }
    }
}
