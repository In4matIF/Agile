package controller;

import view.Window;

/**
 * Created by Olivice on 18/11/2016.
 */
public class TourState extends DefaultState{

    @Override
    public void renitializePlan(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.initState);
    }

    @Override
    public void renitializeDelivery(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.planState);
    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.tourState);
    }
}
