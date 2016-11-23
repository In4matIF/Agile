package controller;

import model.Plan;
import view.Window;

import java.io.File;

/**
 * Created by Olivice on 22/11/2016.
 */
public class LoadPlanCommand implements Command {

    private File file;

    public LoadPlanCommand( File xmlFile) {
        file = xmlFile;
    }

    @Override
    public boolean doCommand() {
        Window.plan = new Plan(file);
        return true;
    }
}
