package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Created by Olivice on 18/11/2016.
 */
public class InitState extends DefaultState{

    @Override
    public void loadPlan(Window window, CommandList commandList, Plan plan, File file) {
        commandList.addCommand(new LoadPlanCommand(plan, file));
        Controller.setCurrentState(Controller.planState);
    }
}
