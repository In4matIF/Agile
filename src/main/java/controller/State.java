package controller;

import model.DeliveryPoint;
import model.Plan;
import view.Window;

import java.io.File;

/**
 * Interface pour l'impl�mentation du design pattern State
 */
public interface State {

    public void loadPlan(Window window, CommandList commandList, File file);
    public boolean loadTour(Window window, CommandList commandList, File file);
    public void renitializePlan(Window window, CommandList commandList);
    public void renitializeTour(Window window, CommandList commandList);
    public void generateTourSheet(Window window, CommandList commandList);
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete);
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd);
    public void undo(CommandList commandList);

}
