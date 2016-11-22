package controller;

import view.Window;

/**
 * Created by Olivice on 18/11/2016.
 */
public class Controller {

    private Window window;
    private CommandList commandList;

    private static State currentState;

    protected static final InitState initState = new InitState();
    protected static final PlanState planState = new PlanState();
    protected static final TourState tourState = new TourState();
    protected static final ErrorState errorState = new ErrorState();

    public Controller(Window window, CommandList commandList) {
        this.window = window;
        this.commandList = commandList;
    }

    protected static void setCurrentState(State state){
        currentState = state;
    }

    public void loadPlan(){
        currentState.loadPlan(window,commandList);
    }

    public void loadTour(){
        currentState.loadTour(window,commandList);
    }

    public void renitializePlan(){
        currentState.renitializePlan(window,commandList);
    }

    public void renitializeDelivery(){
        currentState.renitializeDelivery(window,commandList);
    }

    public void generateTourSheet(){
        currentState.generateTourSheet(window,commandList);
    }
}
