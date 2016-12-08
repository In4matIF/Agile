package controller;

import model.DeliveryPoint;
import model.Plan;
import view.Window;

import java.io.File;

/**
 * Objet State g�n�rique pour le design patter State
 */
public class DefaultState implements State{

    @Override
    public void loadPlan(Window window, CommandList commandList, File file) throws Exception{

    }

    @Override
    public boolean loadTour(Window window, CommandList commandList, File file) throws Exception {
        return false;
    }

    @Override
    public void renitializePlan(Window window, CommandList commandList) throws Exception{

    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) throws Exception{

    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) throws Exception {

    }
    
    @Override
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete) throws Exception{
    }
    
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd) throws Exception{
    }
    
    public void undo(CommandList commandList){
    	commandList.undoCommand();
    }
}
