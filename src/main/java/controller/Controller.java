package controller;

import view.Window;

import java.io.File;

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

    public Controller(Window window) {
        this.currentState = initState;
        this.window = window;
        this.commandList = new CommandList();
    }

    protected static void setCurrentState(State state){
        currentState = state;
    }

    public void loadPlan(File file){
        currentState.loadPlan(window,commandList,file);
    }

    public void loadTour(File file){
        currentState.loadTour(window,commandList, file);
    }

    public void renitializePlan(){
        currentState.renitializePlan(window,commandList);
    }

    public void renitializeDelivery(){
        currentState.renitializeTour(window,commandList);
    }

    public void generateTourSheet(){
        currentState.generateTourSheet(window,commandList);
    }
}
