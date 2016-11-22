package controller;

import model.Plan;

import java.io.File;

/**
 * Created by Olivice on 22/11/2016.
 */
public class LoadPlanCommand implements Command {

    private Plan plan;
    private File file;

    public LoadPlanCommand(Plan plan, File xmlFile) {
        this.plan = plan;
        file = xmlFile;
    }

    @Override
    public boolean doCommand() {
        plan = new Plan(file);
        return true;
    }
}
