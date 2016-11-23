package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Created by Olivice on 18/11/2016.
 */
public interface State {

    public void loadPlan(Window window, CommandList commandList, File file);
    public void loadTour(Window window, CommandList commandList, File file);
    public void renitializePlan(Window window, CommandList commandList);
    public void renitializeTour(Window window, CommandList commandList);
    public void generateTourSheet(Window window, CommandList commandList);

}
