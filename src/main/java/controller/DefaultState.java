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
    public void loadPlan(Window window, CommandList commandList, File file) {

    }

    @Override
    public void loadTour(Window window, CommandList commandList, File file) {

    }

    @Override
    public void renitializePlan(Window window, CommandList commandList) {

    }

    @Override
    public void renitializeTour(Window window, CommandList commandList) {

    }

    @Override
    public void generateTourSheet(Window window, CommandList commandList) {

    }
    
    @Override
    public void deleteDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toDelete){
    }
    
    public void addDeliveryPoint(Window window, CommandList commandList, DeliveryPoint toAdd){  	
    }
    
    public void undo(CommandList commandList){
    	commandList.undoCommand();
    }
}
