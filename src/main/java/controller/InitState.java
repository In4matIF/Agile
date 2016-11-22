package controller;

import view.Window;

/**
 * Created by Olivice on 18/11/2016.
 */
public class InitState extends DefaultState{

    @Override
    public void loadPlan(Window window, CommandList commandList) {
        Controller.setCurrentState(Controller.planState);
    }
}
