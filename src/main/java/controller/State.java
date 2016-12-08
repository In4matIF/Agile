package controller;

import model.DeliveryPoint;
import model.Plan;
import view.Window;

import java.io.File;

/**
 * Interface pour l'impl�mentation du design pattern State
 */
public interface State {

    public void loadPlan(Window window, CommandList commandList, File file) throws Exception;
    public boolean loadTour(Window window, CommandList commandList, File file) throws Exception;
    public void renitializePlan(Window window, CommandList commandList) throws Exception;
    public void renitializeTour(Window window, CommandList commandList) throws Exception;
    public void generateTourSheet(Window window, CommandList commandList) throws Exception;
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete) throws Exception;
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd) throws Exception;
    public void undo(CommandList commandList);

}
