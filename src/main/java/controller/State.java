package controller;

import view.Window;

/**
 * Created by Olivice on 18/11/2016.
 */
public interface State {

    public void loadPlan(Window window, CommandList commandList);
    public void loadTour(Window window, CommandList commandList);
    public void renitializePlan(Window window, CommandList commandList);
    public void renitializeDelivery(Window window, CommandList commandList);
    public void generateTourSheet(Window window, CommandList commandList);

}
