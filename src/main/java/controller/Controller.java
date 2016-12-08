package controller;

import view.Window;

import java.io.File;

import model.DeliveryPoint;

/**
 * Classe pour la StateMachine de l'application
 */
public class Controller {

    private Window window;
    private CommandList commandList;

    private static State currentState;

    protected static final InitState initState = new InitState();
    protected static final PlanState planState = new PlanState();
    protected static final TourState tourState = new TourState();
    protected static final ErrorState errorState = new ErrorState();

    public Controller(Window window) {
        this.currentState = initState;
        this.window = window;
        this.commandList = new CommandList();
    }

    protected static void setCurrentState(State state){
        currentState = state;
    }

    public void loadPlan(File file) throws Exception{
        currentState.loadPlan(window,commandList,file);
    }

    public boolean loadTour(File file) throws Exception{
        return currentState.loadTour(window,commandList, file);
    }

    public void renitializePlan() throws Exception{
        currentState.renitializePlan(window,commandList);
    }

    public void renitializeDelivery() throws Exception{
        currentState.renitializeTour(window,commandList);
    }

    public void generateTourSheet() throws Exception{
        currentState.generateTourSheet(window,commandList);
    }
    
    public void deleteDeliveryPoint(DeliveryPoint toDelete) throws Exception{
    	currentState.deleteDeliveryPoint(window, commandList, toDelete);
    }
    
    public void addDeliveryPoint(DeliveryPoint toAdd) throws Exception{
    	currentState.addDeliveryPoint(window, commandList, toAdd);
    }
    
    public void undo(){
    	currentState.undo(commandList);
    }
}
