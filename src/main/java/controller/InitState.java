package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Plan initial de l'application. Actif au lancement de la fenetre
 */
public class InitState extends DefaultState{

    @Override
    public void loadPlan(Window window, CommandList commandList, File file) throws Exception {
        commandList.addCommand(new LoadPlanCommand(file));
        Controller.setCurrentState(Controller.planState);
    }
}
